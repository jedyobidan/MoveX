package jedyobidan.game.moveX.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;

import jedyobidan.game.moveX.Controller;
import jedyobidan.game.moveX.Input;
import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.lib.JUtil;

public class FallState extends PlayerState {

	public FallState(Player p) {
		super(p);
	}
	
	@Override
	public boolean init(PlayerState prev){
		Animation anim = JUtil.animationFromSheet(player.textures.get("rise-fall"), 1, 1, 1/12f);
		setAnimation(anim, 22, 18);
		return true;
	}

	@Override
	public void step(float delta, float time) {
		if(isAnimationFinished()){
			Animation anim = JUtil.animationFromSheet(player.textures.get("fall"), 1, 4, 1/9f);
			anim.setPlayMode(PlayMode.LOOP);
			setAnimation(anim, 19, 18);
		}
		
		Vector2 force = new Vector2();
		Vector2 di = player.controller.getDI();
		Vector2 velocity = player.getBody().getLinearVelocity();
		float airSpeed = player.stats.get("air_speed");
		float airAccel = player.stats.get("air_accel");
		
		
		// Accelerate to air speed if too low
		if(Math.abs(velocity.x) < airSpeed || Math.signum(velocity.x) != di.x){
			force.x = player.getBody().getMass() * airAccel * (di.x * airSpeed - velocity.x);
		}

		player.getBody().applyForceToCenter(force, true);
		
		// State transitions
		Controller c = player.controller;
		
		if(player.onGround()){
			if(di.x == 0){
				player.setState(new IdleState(player));
			} else {
				player.setState(new WalkState(player));
			}
		} else if(player.controller.getInputGraph(Input.JUMP).posEdge()){
			player.setState(new RiseState(player));
		} else if(c.getInputGraph(Input.DASH).posEdge()) {
			player.setState(new AirDashState(player));
		}
	}

}
