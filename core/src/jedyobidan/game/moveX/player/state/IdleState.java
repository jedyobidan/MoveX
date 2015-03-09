package jedyobidan.game.moveX.player.state;

import jedyobidan.game.moveX.Controller;
import jedyobidan.game.moveX.Input;
import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.lib.JUtil;
import jedyobidan.game.moveX.player.PlayerPhysics;
import jedyobidan.game.moveX.player.PlayerState;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class IdleState extends PlayerState {
	private Animation main;
	public IdleState(Player p) {
		super(p);
		main = JUtil.animationFromSheet(p.getTextures().get("idle"), 1, 6, 1/6f);
		main.setPlayMode(PlayMode.LOOP);	
		setAnimation(main, 20, 18);
	}
	
	@Override
	public void init(PlayerState prev){
		if(prev instanceof FallState){
			Animation anim = JUtil.animationFromSheet(textures.get("fall-idle"), 1, 2, 1/24f);
			setAnimation(anim, 22, 16);
		} else if(prev instanceof DashState){
			Animation anim = JUtil.animationFromSheet(textures.get("dash-idle"), 1, 1, 1/12f);
			setAnimation(anim, 17, 13);
		} else {
			setAnimation(main, 20, 18);
		}

	}
	

	@Override
	public void step(float delta, float time) {		
		if(isAnimationFinished()){
			setAnimation(main, 20, 18);
		}
		
		Body body = physics.getBody();
		Vector2 velocity = body.getLinearVelocity();
		Vector2 force = new Vector2();
		force.add(new Vector2(velocity).scl(-1));
		force.scl(body.getMass() * profile.getStat("skid_force"));
		
		body.applyForceToCenter(force, true);
		
		physics.stickToGround();
		
		
		if(controller.getDI().x != 0){
			physics.setFacing(controller.getDI().x < 0);
		}
		
		if(!player.getPhysics().onGround()){
			if(player.setState(new FallState(player))) return;
		}
		if(controller.getWaveform(Input.DASH).posEdge()){
			if(player.setState(new DashState(player))) return;
		}
		if(controller.getWaveform(Input.JUMP).posEdge()){
			if(player.setState(new RiseState(player))) return;
		} 
		if(controller.getDI().x != 0){
			if(player.setState(new WalkState(player))) return;
		}
	}

}
