package jedyobidan.game.moveX.player.state;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;

import jedyobidan.game.moveX.Controller;
import jedyobidan.game.moveX.Input;
import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.lib.JUtil;
import jedyobidan.game.moveX.player.PlayerState;

public class FallState extends PlayerState {

	public FallState(Player p) {
		super(p);
	}
	
	@Override
	public boolean init(PlayerState prev){
		Animation anim = JUtil.animationFromSheet(textures.get("rise-fall"), 1, 1, 1/12f);
		setAnimation(anim, 22, 18);
		return true;
	}

	@Override
	public void step(float delta, float time) {
		if(isAnimationFinished()){
			Animation anim = JUtil.animationFromSheet(textures.get("fall"), 1, 4, 1/9f);
			anim.setPlayMode(PlayMode.LOOP);
			setAnimation(anim, 19, 18);
		}
		
		Vector2 force = new Vector2();
		Vector2 di = controller.getDI();
		Vector2 velocity = physics.getBody().getLinearVelocity();
		float airSpeed = profile.getStat("air_speed");
		float airAccel = profile.getStat("air_accel");
		
		
		// Accelerate to air speed if too low
		if(Math.abs(velocity.x) < airSpeed || Math.signum(velocity.x) != di.x){
			force.x = physics.getBody().getMass() * airAccel * (di.x * airSpeed - velocity.x);
		}

		physics.getBody().applyForceToCenter(force, true);
		
		
		if(player.getPhysics().onGround()){
			if(di.x == 0){
				if(player.setState(new IdleState(player))) return;
			} else {
				if(player.setState(new WalkState(player))) return;
			}
		} 

		if(controller.getWaveform(Input.DASH).posEdge()) {
			if(player.setState(new AirDashState(player))) return;
		}
		
		if(controller.getWaveform(Input.JUMP).posEdge()){
			if(physics.facingWall()){
				if(player.setState(new WallRiseState(player))) return;
			} else {
				if(player.setState(new RiseState(player))) return;
			}
		}
	}

}
