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
	public boolean init(PlayerState prev){
		if (prev instanceof FallState){
			Animation anim = JUtil.animationFromSheet(textures.get("fall-walk"), 1, 2, 1/24f);
			setAnimation(anim, 19, 16);
		} else {
			Animation anim = JUtil.animationFromSheet(textures.get("idle-walk"), 1, 1, 1/12f);
			setAnimation(anim, 16, 13);
		}
		return true;
	}
	

	@Override
	public void step(float delta, float time) {
		if(isAnimationFinished()){
			Animation anim = JUtil.animationFromSheet(textures.get("walk"), 1, 10, 1/12f);
			anim.setPlayMode(PlayMode.LOOP);
			setAnimation(anim, 22, 14);
		}
		
		float walkSpeed = profile.getStat("walk_speed");
		float walkAccel = profile.getStat("walk_accel");
		Vector2 force = new Vector2(0, 0);
		Vector2 velocity = physics.getBody().getLinearVelocity();		
		Vector2 di = controller.getDI();
		
		if(di.x != 0){
			physics.setFacing(di.x < 0);
		}
		
		// Accelerating Force
		force.x = physics.getBody().getMass() * walkAccel * (di.x * walkSpeed - velocity.x);
		
		// TODO Brake Force
		
		
		physics.getBody().applyForceToCenter(force,  true);
		
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
		
	}

}
