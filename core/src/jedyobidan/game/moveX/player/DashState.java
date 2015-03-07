package jedyobidan.game.moveX.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import jedyobidan.game.moveX.Controller;
import jedyobidan.game.moveX.Input;
import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.lib.JUtil;

public class DashState extends PlayerState{
	public static final float BOOST = 1.5f;
	public static final float TIME = 0.2f;
	
	private int dir;
	public DashState(Player p) {
		super(p);
	}
	
	public boolean init(PlayerState prev){
		if(player.stats.get("can_dash") == 0){
			return false;
		}
		player.setDashbox(true);
		Animation transition = JUtil.animationFromSheet(player.textures.get("idle-dash"), 1, 1, 1/12f);
		setAnimation(transition, 14, 12);
		
		dir = player.getFacing() ? -1 : 1;
		Vector2 impulse = new Vector2();
		Body body = player.getBody();
		impulse.x = body.getMass() * (dir * player.stats.get("walk_speed") * BOOST - body.getLinearVelocity().x);
		body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
		
		return true;
	}
	
	public void destroy(PlayerState next){
		player.setDashbox(false);
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
		
		force.x = body.getMass() * 10 * (dir * player.stats.get("walk_speed") * BOOST - velocity.x);
		body.applyForceToCenter(force, true);
		
		//State changes
		Controller c = player.controller;
		Vector2 exitImpulse = new Vector2();
		exitImpulse.x = body.getMass() * (dir * player.stats.get("walk_speed") - velocity.x);
		
		if(!player.onGround()){
			body.applyLinearImpulse(exitImpulse, body.getWorldCenter(), true);
			player.setState(new FallState(player));
		} else if (c.getDI().x == -dir){
			body.applyLinearImpulse(exitImpulse, body.getWorldCenter(), true);
			player.setState(new WalkState(player));
		} else if (c.getInputGraph(Input.JUMP).posEdge()){
			player.setState(new RiseState(player));
		} else if(time > TIME){
			body.applyLinearImpulse(exitImpulse, body.getWorldCenter(), true);
			if(c.getDI().x == 0){
				player.setState(new IdleState(player));
			} else {
				player.setState(new WalkState(player));
			}
		}
		
	}

}
