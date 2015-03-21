package jedyobidan.game.moveX.player.state;

import jedyobidan.game.moveX.Const;
import jedyobidan.game.moveX.Input;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.JUtil;
import jedyobidan.game.moveX.lib.ShortestRaycast;
import jedyobidan.game.moveX.player.Player;
import jedyobidan.game.moveX.player.PlayerPhysics;
import jedyobidan.game.moveX.player.PlayerState;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

public class AirDashState extends PlayerState {
	private static final double ANGLE = (Math.PI / 4);
	private Vector2 dir;

	public AirDashState(Player p) {
		super(p);
	}

	@Override
	public boolean valid(PlayerState prev){
		return profile.canAirDash();
	}
	
	@Override
	public void init(PlayerState prev) {
		profile.useAirDash();
		
		physics.setDashbox(true);
		Animation transition = JUtil.animationFromSheet(textures.get("idle-dash"), 1, 1, 1/12f);
		setAnimation(transition, 20, 10);

		Vector2 di = controller.getDI();
		dir = new Vector2();
		dir.x = physics.getFacing() ? -1 : 1;

		if (profile.can("direction_dash") && di.y != 0) {
			dir.y = (float) (di.y * Math.sin(ANGLE));
			dir.x *= Math.cos(ANGLE);
		}

		Vector2 impulse = new Vector2();
		Body body = physics.getBody();
		impulse.x = body.getMass()
				* (dir.x * profile.getStat("walk_speed") * DashState.BOOST - body
						.getLinearVelocity().x);
		impulse.y = body.getMass()
				* (dir.y * profile.getStat("walk_speed") * DashState.BOOST - body
						.getLinearVelocity().y);
		body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
		
		body.setGravityScale(0);

	}
	
	@Override
	public void destroy(PlayerState next){
		physics.setDashbox(false);
		physics.getBody().setGravityScale(1);
	}

	@Override
	public void step(float delta, float time) {
		if(isAnimationFinished()){
			Animation main = JUtil.animationFromSheet(textures.get("dash"), 1, 1, 1/12f);
			main.setPlayMode(PlayMode.LOOP);
			setAnimation(main, 20, 10);
		}
		
		Vector2 force = new Vector2();
		Body body = physics.getBody();
		Vector2 velocity = body.getLinearVelocity();
		
		force.x = body.getMass() * 10 * (dir.x * profile.getStat("walk_speed") * DashState.BOOST - velocity.x);
		force.y = body.getMass() * 10 * (dir.y * profile.getStat("walk_speed") * DashState.BOOST - velocity.y);
		body.applyForceToCenter(force, true);
		
		
		Vector2 exitImpulse = new Vector2();
		exitImpulse.x = body.getMass() * (dir.x * profile.getStat("walk_speed") - velocity.x);
		exitImpulse.y = body.getMass() * - velocity.y;
		
		if(clearHeight() < PlayerPhysics.GROUND + PlayerPhysics.HEAD){
			return; //WARNING: Possible softlock here
		}
		
		if (controller.getWaveform(Input.JUMP).posEdge()){
			if(physics.facingWall()){
				if(player.setState(new WallRiseState(player))) return;
			} else {
				if(player.setState(new RiseState(player))) return;
			}
		} 
		
		if(player.getPhysics().onGround() && dir.y <= 0){
			Vector2 waveDash = new Vector2();
			waveDash.x = body.getMass() * (dir.x * profile.getStat("walk_speed") * DashState.BOOST);
			body.applyLinearImpulse(physics.normalize(waveDash), body.getWorldCenter(), true);
			if(player.setState(new WalkState(player))) return;
		} 
		
		if (time > DashState.DEFAULT_TIME){
			body.applyLinearImpulse(exitImpulse, body.getWorldCenter(), true);
			if(player.setState(new FallState(player))) return;
		}
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
		
		float scalar = PlayerPhysics.GROUND * PlayerPhysics.HEAD + 1;
		
		world.rayCast(groundFind, l, l.cpy().add(0, -scalar));
		world.rayCast(groundFind, c, c.cpy().add(0, -scalar));
		world.rayCast(groundFind, r, r.cpy().add(0, -scalar));
		toGround = groundFind.fraction * scalar;
		
		ShortestRaycast ceilFind = new ShortestRaycast(){
			@Override
			public boolean shouldCheck(Fixture fix) {
				return Const.isCeil((Actor) fix.getBody().getUserData());
			}
		};
		world.rayCast(ceilFind, l, l.cpy().add(0, scalar));
		world.rayCast(ceilFind, c, c.cpy().add(0, scalar));
		world.rayCast(ceilFind, r, r.cpy().add(0, scalar));
		toCeil = ceilFind.fraction * scalar;
		
		return toGround + toCeil;
	}

}
