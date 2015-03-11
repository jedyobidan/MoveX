package jedyobidan.game.moveX.player.state;

import jedyobidan.game.moveX.Controller;
import jedyobidan.game.moveX.Input;
import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.lib.JUtil;
import jedyobidan.game.moveX.player.PlayerState;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;

public class WalkState extends PlayerState {
	
	public WalkState(Player p) {
		super(p);
	}
	
	@Override
	public void init(PlayerState prev){
		if (prev instanceof FallState){
			Animation anim = JUtil.animationFromSheet(textures.get("fall-walk"), 1, 2, 1/24f);
			setAnimation(anim, 15, 18);
		} else {
			Animation anim = JUtil.animationFromSheet(textures.get("idle-walk"), 1, 1, 1/12f);
			setAnimation(anim, 20, 11);
		}

		physics.getBody().setGravityScale(10);
	}
	
	public void destroy(PlayerState next){
		physics.getBody().setGravityScale(1);
	}
	
	

	@Override
	public void step(float delta, float time) {
		if(isAnimationFinished()){
			Animation anim = JUtil.animationFromSheet(textures.get("walk"), 1, 10, 1/12f);
			anim.setPlayMode(PlayMode.LOOP);
			setAnimation(anim, 20, 11);
		}
		
		Vector2 di = controller.getDI();
		float walkSpeed = profile.getStat("walk_speed");
		float walkAccel = profile.getStat("walk_accel");
		Vector2 velocity = physics.getBody().getLinearVelocity();
		

		Vector2 force = new Vector2(di.x * walkSpeed, 0);
		force = physics.normalize(force);
		force.add(new Vector2(velocity).scl(-1));
		force.scl(physics.getBody().getMass() * walkAccel);	
		
		physics.getBody().applyForceToCenter(force,  true);
		

		if(di.x != 0){
			physics.setFacing(di.x < 0);
		}	
		
		// State changes
		if(!player.getPhysics().onGround()){
			if(player.setState(new FallState(player))) return;
		}
		
		if(di.x == 0){
			if(player.setState(new IdleState(player))) return;
		}
		
		if(controller.getWaveform(Input.DASH).posEdge()){
			if(player.setState(new DashState(player))) return;
		}
		
		if(controller.getWaveform(Input.JUMP).posEdge()){
			if(player.setState(new RiseState(player))) return;
		}
		physics.stickToGround();
		
	}

}
