package jedyobidan.game.moveX.player;

import jedyobidan.game.moveX.Controller;
import jedyobidan.game.moveX.Input;
import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.lib.JUtil;

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
			Animation anim = JUtil.animationFromSheet(player.textures.get("fall-walk"), 1, 2, 1/24f);
			setAnimation(anim, 19, 16);
		} else {
			Animation anim = JUtil.animationFromSheet(player.textures.get("idle-walk"), 1, 1, 1/12f);
			setAnimation(anim, 16, 13);
		}
		return true;
	}
	

	@Override
	public void step(float delta, float time) {
		if(isAnimationFinished()){
			Animation anim = JUtil.animationFromSheet(player.textures.get("walk"), 1, 10, 1/12f);
			anim.setPlayMode(PlayMode.LOOP);
			setAnimation(anim, 22, 14);
		}
		
		float walkSpeed = player.stats.get("walk_speed");
		float walkAccel = player.stats.get("walk_accel");
		Vector2 force = new Vector2(0, 0);
		Vector2 velocity = player.getBody().getLinearVelocity();		
		Vector2 di = player.controller.getDI();
		
		if(di.x != 0){
			player.setFacing(di.x < 0);
		}
		
		// Accelerating Force
		force.x = player.getBody().getMass() * walkAccel * (di.x * walkSpeed - velocity.x);
		
		// TODO Brake Force
		
		
		player.getBody().applyForceToCenter(force,  true);
		
		// State changes
		Controller c = player.controller;
		if(!player.onGround()){
			player.setState(new FallState(player));
		} else if(di.x == 0){
			player.setState(new IdleState(player));
		} else if(c.getInputGraph(Input.JUMP).posEdge()){
			player.setState(new RiseState(player));
		} else if(c.getInputGraph(Input.DASH).posEdge()){
			player.setState(new DashState(player));
		}
	}

}
