package jedyobidan.game.moveX.player;

import jedyobidan.game.moveX.Controller;
import jedyobidan.game.moveX.Input;
import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.lib.JUtil;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class IdleState extends PlayerState {
	private Animation main;
	public IdleState(Player p) {
		super(p);
		main = JUtil.animationFromSheet(p.textures.get("idle"), 1, 6, 1/6f);
		main.setPlayMode(PlayMode.LOOP);	
		setAnimation(main, 20, 18);
	}
	
	public boolean init(PlayerState prev){
		if(prev instanceof FallState){
			Animation anim = JUtil.animationFromSheet(player.textures.get("fall-idle"), 1, 2, 1/24f);
			setAnimation(anim, 22, 16);
		} else if(prev instanceof DashState){
			Animation anim = JUtil.animationFromSheet(player.textures.get("dash-idle"), 1, 1, 1/12f);
			setAnimation(anim, 17, 13);
		} else {
			setAnimation(main, 20, 18);
		}
		return true;
	}

	@Override
	public void step(float delta, float time) {		
		if(isAnimationFinished()){
			setAnimation(main, 20, 18);
		}
		
		Body body = player.getBody();
		Vector2 velocity = body.getLinearVelocity();
		Vector2 force = new Vector2(body.getMass() * player.stats.get("skid_force") * -velocity.x, 0);
		body.applyForceToCenter(force, true);
		
		Controller c = player.controller;
		if(!player.onGround()){
			player.setState(new FallState(player));
		} else if(c.getInputGraph(Input.JUMP).posEdge()){
			player.setState(new RiseState(player));
		} else if(c.getDI().x != 0){
			player.setState(new WalkState(player));
		} else if(c.getInputGraph(Input.DASH).posEdge()){
			player.setState(new DashState(player));
		}
	}

}
