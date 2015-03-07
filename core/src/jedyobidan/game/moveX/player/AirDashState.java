package jedyobidan.game.moveX.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import jedyobidan.game.moveX.Controller;
import jedyobidan.game.moveX.Input;
import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.lib.JUtil;

public class AirDashState extends PlayerState {
	private static final double ANGLE = (Math.PI / 4);
	private Vector2 dir;

	public AirDashState(Player p) {
		super(p);
	}

	@Override
	public boolean init(PlayerState prev) {
		if (player.canAirDash()) {
			player.useAirDash();
		} else {
			return false;
		}
		
		player.setDashbox(true);
		Animation transition = JUtil.animationFromSheet(player.textures.get("idle-dash"), 1, 1, 1/12f);
		setAnimation(transition, 14, 12);

		Vector2 di = player.controller.getDI();
		dir = new Vector2();
		dir.x = player.getFacing() ? -1 : 1;

		if (player.stats.get("can_direction_dash") != 0 && di.y != 0) {
			dir.y = (float) (di.y * Math.sin(ANGLE));
			dir.x *= Math.cos(ANGLE);
		}

		Vector2 impulse = new Vector2();
		Body body = player.getBody();
		impulse.x = body.getMass()
				* (dir.x * player.stats.get("walk_speed") * DashState.BOOST - body
						.getLinearVelocity().x);
		impulse.y = body.getMass()
				* (dir.y * player.stats.get("walk_speed") * DashState.BOOST - body
						.getLinearVelocity().y);
		body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
		
		body.setGravityScale(0);

		return true;
	}
	
	public void destroy(PlayerState next){
		player.setDashbox(false);
		player.getBody().setGravityScale(1);
	}

	@Override
	public void step(float delta, float time) {
		if(isAnimationFinished()){
			Animation main = JUtil.animationFromSheet(player.textures.get("dash"), 1, 4, 1/12f);
			main.setPlayMode(PlayMode.LOOP);
			setAnimation(main, 19, 12);
		}
		
		Vector2 force = new Vector2();
		Body body = player.getBody();
		Vector2 velocity = body.getLinearVelocity();
		
		force.x = body.getMass() * 10 * (dir.x * player.stats.get("walk_speed") * DashState.BOOST - velocity.x);
		force.y = body.getMass() * 10 * (dir.y * player.stats.get("walk_speed") * DashState.BOOST - velocity.y);
		body.applyForceToCenter(force, true);
		
		Controller c = player.controller;
		Vector2 exitImpulse = new Vector2();
		exitImpulse.x = body.getMass() * (dir.x * player.stats.get("walk_speed") - velocity.x);
		exitImpulse.y = body.getMass() * - velocity.y;
		
		if(player.onGround()){
			Vector2 waveDash = new Vector2();
			waveDash.x = body.getMass() * (dir.x * player.stats.get("walk_speed") * DashState.BOOST);
			body.applyLinearImpulse(waveDash, body.getWorldCenter(), true);
			player.setState(new WalkState(player));
		} else if (time > DashState.TIME){
			body.applyLinearImpulse(exitImpulse, body.getWorldCenter(), true);
			player.setState(new FallState(player));
		} else if (c.getInputGraph(Input.JUMP).posEdge()){
			player.setState(new RiseState(player));
		} 
	}

}
