package jedyobidan.game.moveX.player;

import jedyobidan.game.moveX.Controller;
import jedyobidan.game.moveX.Input;
import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.lib.JUtil;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class RiseState extends PlayerState {
	private float riseSpeed;
	public RiseState(Player p, float riseSpeed) {
		super(p);
		this.riseSpeed = riseSpeed;
	}
	
	public RiseState(Player p){
		this(p, p.stats.get("jump_speed"));
	}
	
	@Override
	public boolean init(PlayerState prev){
		if(player.stats.get("can_jump") == 0){
			return false;
		}
		
		if(!player.onGround()){
			if(player.canAirJump()){
				player.useAirJump();
			} else {
				return false;
			}
		}
		
		Animation anim = JUtil.animationFromSheet(player.textures.get("idle-rise"), 1, 2, 1/12f);
		setAnimation(anim, 19, 18);
		
		Body body = player.getBody();
		Vector2 velocity = body.getLinearVelocity();
		Vector2 jumpImpulse = new Vector2();
		
		jumpImpulse.y = body.getMass() * (riseSpeed - velocity.y);
		body.applyLinearImpulse(jumpImpulse, body.getWorldCenter(), true);
		return true;
	}


	@Override
	public void step(float delta, float time) {
		if(isAnimationFinished()){
			Animation anim = JUtil.animationFromSheet(player.textures.get("rise"), 1, 3, 1/9f);
			anim.setPlayMode(PlayMode.LOOP);
			setAnimation(anim, 16, 18);
		}
		

		Body body = player.getBody();
		Vector2 force = new Vector2();
		Vector2 di = player.controller.getDI();
		Vector2 velocity = body.getLinearVelocity();
		float airSpeed = player.stats.get("air_speed");
		float airAccel = player.stats.get("air_accel");
		
		
		// Accelerate to air speed if too low
		if(Math.abs(velocity.x) < airSpeed || Math.signum(velocity.x) != di.x){
			force.x = player.getBody().getMass() * airAccel * (di.x * airSpeed - velocity.x);
		}
		body.applyForceToCenter(force, true);
		
		// State transition
		Controller c = player.controller;
		
		if(velocity.y <= 0){
			player.setState(new FallState(player));
		} else if(c.getInputGraph(Input.JUMP).low() && time > 0.15f) {
			body.setLinearVelocity(new Vector2(velocity.x, 0));
			player.setState(new FallState(player));
		} else if(c.getInputGraph(Input.DASH).posEdge()) {
			player.setState(new AirDashState(player));
		}
	}

}
