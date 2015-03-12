package jedyobidan.game.moveX.player.state;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;

import jedyobidan.game.moveX.Controller;
import jedyobidan.game.moveX.Input;
import jedyobidan.game.moveX.Level;
import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.lib.JUtil;
import jedyobidan.game.moveX.player.PlayerState;

public class FallState extends PlayerState {
	public static final int VEL_TERM = 30;
	private Animation main;

	public FallState(Player p) {
		super(p);
		main = JUtil.animationFromSheet(textures.get("fall"), 1, 1, 1/9f);
		main.setPlayMode(PlayMode.LOOP);
	}
	
	@Override
	public void init(PlayerState prev){
		if(prev instanceof AirDashState){
			setAnimation(main, 15, 18);
		} else {
			Animation anim = JUtil.animationFromSheet(textures.get("rise-fall"), 1, 1, 1/12f);
			setAnimation(anim, 15, 18);
		}
	}

	@Override
	public void step(float delta, float time) {
		if(isAnimationFinished()){
			setAnimation(main, 15, 18);
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
		
		// Air resistance
		if(velocity.y < -VEL_TERM){
			physics.getBody().setLinearVelocity(velocity.x, -VEL_TERM);
		}
		
		
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
