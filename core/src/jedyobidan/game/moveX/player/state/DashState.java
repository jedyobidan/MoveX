package jedyobidan.game.moveX.player.state;

import jedyobidan.game.moveX.Input;
import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.lib.JUtil;
import jedyobidan.game.moveX.player.PlayerState;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class DashState extends PlayerState{
	public static final float BOOST = 1.5f;
	public static final float TIME = 0.3f;
	
	private int dir;
	public DashState(Player p) {
		super(p);
	}
	
	@Override
	public boolean valid(PlayerState prev){
		return profile.can("dash");
	}
	
	@Override
	public void init(PlayerState prev){
		Animation transition = JUtil.animationFromSheet(textures.get("idle-dash"), 1, 1, 1/12f);
		setAnimation(transition, 14, 12);
		
		dir = physics.getFacing() ? -1 : 1;
		Vector2 impulse = new Vector2();
		Body body = physics.getBody();
		impulse.x = dir * profile.getStat("walk_speed") * BOOST;
		impulse = physics.normalize(impulse);
		impulse.add(new Vector2(body.getLinearVelocity()).scl(-1));
		impulse.scl(body.getMass());
		
		body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
		player.getPhysics().setDashbox(true);
	}
	
	@Override
	public void destroy(PlayerState next){
		player.getPhysics().setDashbox(false);
	}

	@Override
	public void step(float delta, float time) {
		if(isAnimationFinished()){
			Animation main = JUtil.animationFromSheet(textures.get("dash"), 1, 4, 1/12f);
			main.setPlayMode(PlayMode.LOOP);
			setAnimation(main, 19, 12);
		}
		
		
		Body body = physics.getBody();
		Vector2 velocity = body.getLinearVelocity();
		Vector2 force = new Vector2(dir * profile.getStat("walk_speed") * BOOST, 0);
		force = physics.normalize(force);
		force.add(new Vector2(velocity).scl(-1));
		force.scl(body.getMass() * 10);
		
		body.applyForceToCenter(force, true);
		
		
		//State changes
		Vector2 exitImpulse = new Vector2();
		exitImpulse.x = body.getMass() * (dir * profile.getStat("walk_speed") - velocity.x);
		
		if(!player.getPhysics().onGround()){
			body.applyLinearImpulse(exitImpulse, body.getWorldCenter(), true);
			if(player.setState(new FallState(player))) return;
		} 

		if (controller.getWaveform(Input.JUMP).posEdge()){
			if(player.setState(new RiseState(player))) return;
		} 
		
		if (controller.getDI().x == -dir){
			body.applyLinearImpulse(exitImpulse, body.getWorldCenter(), true);
			if(player.setState(new WalkState(player))) return;
		} 
		
		if(time > TIME){
			body.applyLinearImpulse(exitImpulse, body.getWorldCenter(), true);
			if(controller.getDI().x == 0){
				if(player.setState(new IdleState(player))) return;
			} else {
				if(player.setState(new WalkState(player))) return;
			}
		}
		physics.stickToGround();
		
	}

}
