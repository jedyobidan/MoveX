package jedyobidan.game.moveX.player.state;

import jedyobidan.game.moveX.Input;
import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.lib.JUtil;
import jedyobidan.game.moveX.player.PlayerState;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class RiseState extends PlayerState {
	protected float riseSpeed;
	public RiseState(Player p, float riseSpeed) {
		super(p);
		this.riseSpeed = riseSpeed;
	}
	
	public RiseState(Player p){
		this(p, p.getProfile().getStat("jump_speed"));
	}
	
	@Override
	public boolean valid(PlayerState prev){
		if(!profile.can("jump")){
			return false;
		}
		
		if(!player.getPhysics().onGround()){
			return profile.canAirJump();
		} else {
			return profile.can("jump");
		}
	}
	
	@Override
	public void init(PlayerState prev){		
		if(!player.getPhysics().onGround()){
			profile.useAirJump();
		}
		
		Animation anim = JUtil.animationFromSheet(textures.get("idle-rise"), 1, 2, 1/24f);
		setAnimation(anim, 15, 18);
		
		Body body = physics.getBody();
		Vector2 velocity = body.getLinearVelocity();
		Vector2 jumpImpulse = new Vector2();
		
		jumpImpulse.y = body.getMass() * (riseSpeed - velocity.y);
		body.applyLinearImpulse(jumpImpulse, body.getWorldCenter(), true);
	}


	@Override
	public void step(float delta, float time) {
		animationStep(time);

		Body body = physics.getBody();
		Vector2 force = new Vector2();
		Vector2 di = controller.getDI();
		Vector2 velocity = body.getLinearVelocity();
		float airSpeed = profile.getStat("air_speed");
		float airAccel = profile.getStat("air_accel");
		
		
		// Accelerate to air speed if too low
		if(Math.abs(velocity.x) < airSpeed || Math.signum(velocity.x) != di.x){
			force.x = physics.getBody().getMass() * airAccel * (di.x * airSpeed - velocity.x);
		}
		body.applyForceToCenter(force, true);
		
		// State transition		
		if(controller.getWaveform(Input.DASH).posEdge()) {
			if(player.setState(new AirDashState(player))) return;
		}
		
		if(velocity.y <= 0 || controller.getWaveform(Input.JUMP).low() && time > 0.1f){
			body.setLinearVelocity(new Vector2(velocity.x, 0));
			if(player.setState(new FallState(player))) return;
		} 
	}
	
	protected void animationStep(float time){
		if(isAnimationFinished()){
			Animation anim = JUtil.animationFromSheet(textures.get("rise"), 1, 1, 1/9f);
			anim.setPlayMode(PlayMode.LOOP);
			setAnimation(anim, 15, 18);
		}
	}

}
