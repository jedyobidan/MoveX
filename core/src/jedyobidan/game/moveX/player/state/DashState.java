package jedyobidan.game.moveX.player.state;

import jedyobidan.game.moveX.Const;
import jedyobidan.game.moveX.Input;
import jedyobidan.game.moveX.MoveX;
import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.JUtil;
import jedyobidan.game.moveX.lib.ShortestRaycast;
import jedyobidan.game.moveX.player.PlayerPhysics;
import jedyobidan.game.moveX.player.PlayerState;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

public class DashState extends PlayerState{
	public static final float BOOST = 1.5f;
	public static final float DEFAULT_TIME = 0.3f;
	
	private float timeOffset;
	private int dir;
	private Animation main;
	
	public DashState(Player p, float time){
		super(p);
		timeOffset = time;
		main = JUtil.animationFromSheet(textures.get("dash"), 1, 1, 1/12f);
		main.setPlayMode(PlayMode.LOOP);
	}
	
	public DashState(Player p) {
		this(p, 0);
	}
	
	@Override
	public boolean valid(PlayerState prev){
		return profile.can("dash");
	}
	
	@Override
	public void init(PlayerState prev){
		if(prev instanceof DashState){
			setAnimation(main, 20, 10);
		} else {
			Animation transition = JUtil.animationFromSheet(textures.get("idle-dash"), 1, 1, 1/12f);
			setAnimation(transition, 20, 10);
		}
		
		dir = physics.getFacing() ? -1 : 1;
		Vector2 impulse = new Vector2();
		Body body = physics.getBody();
		impulse.x = dir * profile.getStat("walk_speed") * BOOST;
		impulse = physics.normalize(impulse);
		impulse.add(new Vector2(body.getLinearVelocity()).scl(-1));
		impulse.scl(body.getMass());
		
		body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
		player.getPhysics().setDashbox(true);
	}
	
	@Override
	public void destroy(PlayerState next){
		player.getPhysics().setDashbox(false);
	}

	@Override
	public void step(float delta, float time) {
		if(isAnimationFinished()){
			setAnimation(main, 20, 10);
		}
		
		
		Body body = physics.getBody();
		Vector2 velocity = body.getLinearVelocity();
		Vector2 force = new Vector2(dir * profile.getStat("walk_speed") * BOOST, 0);
		force = physics.normalize(force);
		force.add(new Vector2(velocity).scl(-1));
		force.scl(body.getMass() * 10);
		
		body.applyForceToCenter(force, true);
		
		
		//State changes
		Vector2 exitImpulse = new Vector2();
		exitImpulse.x = body.getMass() * (dir * profile.getStat("walk_speed") - velocity.x);
		
		if(!player.getPhysics().onGround()){
			body.applyLinearImpulse(exitImpulse, body.getWorldCenter(), true);
			if(player.setState(new FallState(player))) return;
		} 

		if (controller.getWaveform(Input.JUMP).posEdge()){
			if(player.setState(new RiseState(player))) return;
		} 
		
		if (controller.getDI().x == -dir){
			if(clearHeight() < PlayerPhysics.GROUND + PlayerPhysics.HEAD){
				physics.setFacing(!physics.getFacing());
				if(player.setState(new DashState(player, time + timeOffset))) return;
			} else {
				body.applyLinearImpulse(exitImpulse, body.getWorldCenter(), true);
				if(player.setState(new WalkState(player))) return;
			}
		} 
		
		if(time + timeOffset > DEFAULT_TIME){
			if(clearHeight() < PlayerPhysics.GROUND + PlayerPhysics.HEAD){
				return;
			}
			body.applyLinearImpulse(exitImpulse, body.getWorldCenter(), true);
			if(controller.getDI().x == 0){
				if(player.setState(new IdleState(player))) return;
			} else {
				if(player.setState(new WalkState(player))) return;
			}
		}
		physics.stickToGround();
		
	}
	
	private float clearHeight(){
		float toGround, toCeil;
		
		Vector2 c = physics.getBody().getPosition();
		Vector2 l = c.cpy().add(-PlayerPhysics.WIDTH, 0);
		Vector2 r = c.cpy().add(PlayerPhysics.WIDTH, 0);
		World world = physics.getBody().getWorld();
				
		ShortestRaycast groundFind = new ShortestRaycast(){
			@Override
			public boolean shouldCheck(Fixture fix) {
				return Const.isGround((Actor) fix.getBody().getUserData());
			}			
		};
		
		world.rayCast(groundFind, l, l.cpy().add(0, -PlayerPhysics.GROUND * 2));
		world.rayCast(groundFind, c, c.cpy().add(0, -PlayerPhysics.GROUND * 2));
		world.rayCast(groundFind, r, r.cpy().add(0, -PlayerPhysics.GROUND * 2));
		toGround = groundFind.fraction * PlayerPhysics.GROUND * 2;
		
		ShortestRaycast ceilFind = new ShortestRaycast(){
			@Override
			public boolean shouldCheck(Fixture fix) {
				return Const.isCeil((Actor) fix.getBody().getUserData());
			}
		};
		world.rayCast(ceilFind, l, l.cpy().add(0, PlayerPhysics.HEAD * 2));
		world.rayCast(ceilFind, c, c.cpy().add(0, PlayerPhysics.HEAD * 2));
		world.rayCast(ceilFind, r, r.cpy().add(0, PlayerPhysics.HEAD * 2));
		toCeil = ceilFind.fraction * PlayerPhysics.HEAD * 2;
		
		return toGround + toCeil;
	}

}
