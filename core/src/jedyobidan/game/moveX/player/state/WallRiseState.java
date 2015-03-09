package jedyobidan.game.moveX.player.state;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import jedyobidan.game.moveX.Input;
import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.lib.JUtil;
import jedyobidan.game.moveX.player.PlayerState;

public class WallRiseState extends PlayerState {

	protected float riseSpeed;
	public WallRiseState(Player p) {
		this(p, p.getProfile().getStat("jump_speed"));
	}
	
	public WallRiseState(Player p, float jump){
		super(p);
		riseSpeed = jump;
	}
	
	public boolean init(PlayerState prev){
		if(profile.can("wall_jump")){
			return false;
		}
		
		Animation anim = JUtil.animationFromSheet(textures.get("fall-wrise"), 1, 2, 1/24f);
		setAnimation(anim, 19, 13);
		
		Body body = physics.getBody();
		Vector2 velocity = body.getLinearVelocity();
		Vector2 jumpImpulse = new Vector2();
		
		int dir = physics.getFacing() ? 1: -1;		

		jumpImpulse.y = body.getMass() * (riseSpeed - velocity.y);
		jumpImpulse.x = body.getMass() * (dir * profile.getStat("air_speed") - velocity.x) * 2;
		
		body.applyLinearImpulse(jumpImpulse, body.getWorldCenter(), true);
		physics.setFacing(!physics.getFacing());
		return true;
	}
	
	public void step(float delta, float time){
		if(isAnimationFinished()){
			Animation anim = JUtil.animationFromSheet(textures.get("wrise"), 1, 3, 1/9f);
			anim.setPlayMode(PlayMode.LOOP);
			setAnimation(anim, 19, 13);
		}

		Body body = physics.getBody();
		Vector2 force = new Vector2();
		Vector2 di = controller.getDI();
		Vector2 velocity = body.getLinearVelocity();
		float airSpeed = profile.getStat("air_speed");
		float airAccel = profile.getStat("air_accel");
		
		
		force.x = physics.getBody().getMass() * airAccel * (di.x * airSpeed - velocity.x) / 3;
		
		body.applyForceToCenter(force, true);
		
		// State transition		
		if(controller.getWaveform(Input.DASH).posEdge()) {
			if(player.setState(new AirDashState(player))) return;
		}
		
		if(velocity.y <= 0 || controller.getWaveform(Input.JUMP).low() && time > 0.15f){
			body.setLinearVelocity(new Vector2(velocity.x, 0));
			if(player.setState(new FallState(player))) return;
		} 
	}

}
