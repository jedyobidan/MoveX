package jedyobidan.game.moveX.player;

import jedyobidan.game.moveX.Const;
import jedyobidan.game.moveX.Level;
import jedyobidan.game.moveX.MoveX;
import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.lib.Accumulator;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.ShortestRaycast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class PlayerPhysics implements ContactListener {
	private Body body;
	private Fixture stand, dash, foot, left, right;

	private Accumulator<Actor> groundContact, leftContact, rightContact;
	private Vector2 groundNormal;

	private boolean facing;

	public static final int STICK_STRENGTH = 10;
	public static final float WIDTH = 0.5f, GROUND= 0.5f, HEAD = 1f, DASH = 0.5f;

	public PlayerPhysics(Player player, World physics) {
		groundContact = new Accumulator<Actor>();
		leftContact = new Accumulator<Actor>();
		rightContact = new Accumulator<Actor>();
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(0, 0);
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.fixedRotation = true;
		body = physics.createBody(bodyDef);
		body.setUserData(player);

		PolygonShape standingShape = new PolygonShape();
		standingShape.setAsBox(WIDTH, (GROUND + HEAD) / 2, new Vector2(0, (HEAD - GROUND) / 2), 0);

		PolygonShape dashingShape = new PolygonShape();
		dashingShape.setAsBox(WIDTH, (GROUND + DASH) / 2, new Vector2(0, (DASH - GROUND) / 2), 0);

		PolygonShape footShape = new PolygonShape();
		footShape.setAsBox(WIDTH - 0.05f, 0.1f, new Vector2(0, -GROUND -0.05f), 0);

		PolygonShape leftShape = new PolygonShape();
		leftShape.setAsBox(0.1f, GROUND - 0.05f, new Vector2(-WIDTH, 0.2f), 0);

		PolygonShape rightShape = new PolygonShape();
		rightShape.setAsBox(0.1f, GROUND - 0.05f, new Vector2(WIDTH, 0.2f), 0);

		FixtureDef hitbox = new FixtureDef();
		hitbox.density = 1;
		hitbox.friction = 0.0f;
		hitbox.restitution = 0.0f;
		hitbox.filter.categoryBits = Const.CAT_PLAYER;

		hitbox.shape = standingShape;
		stand = body.createFixture(hitbox);

		hitbox.filter.maskBits = 0;
		hitbox.shape = dashingShape;
		dash = body.createFixture(hitbox);

		hitbox.filter.maskBits = 0xFF;
		hitbox.shape = footShape;
		hitbox.isSensor = true;
		foot = body.createFixture(hitbox);

		hitbox.shape = leftShape;
		left = body.createFixture(hitbox);

		hitbox.shape = rightShape;
		right = body.createFixture(hitbox);
	}

	public void setDashbox(boolean dashOn) {
		Filter f;
		f = dash.getFilterData();
		f.maskBits = (short) (dashOn ? 0xFFFF : 0x0000);
		dash.setFilterData(f);

		f = dash.getFilterData();
		f.maskBits = (short) (dashOn ? 0x0000 : 0xFFFF);
		stand.setFilterData(f);

	}

	public Body getBody() {
		return body;
	}
	
	public void clearContacts(){
		groundContact.clear();
		leftContact.clear();
		rightContact.clear();
	}

	public boolean onGround() {
		return groundContact.sum() > 0;
	}

	public boolean facingWall() {
		if (facing) {
			return leftContact.sum() > 0;
		} else {
			return rightContact.sum() > 0;
		}
	}

	public boolean getFacing() {
		return facing;
	}

	public void setFacing(boolean facingLeft) {
		this.facing = facingLeft;
	}

	public void move(float x, float y) {
		body.setTransform(new Vector2(x, y), 0);
		clearContacts();
		setDashbox(false);
		setFacing(false);
		groundNormal = new Vector2(0, 1);
	}

	// BEGIN Slope code
	public void requestGroundNormal() {
		ShortestRaycast finder = new ShortestRaycast(){
			@Override
			public boolean shouldCheck(Fixture fix) {
				return Const.isGround((Actor) fix.getBody().getUserData());
			}
		};
		Vector2 center = body.getWorldCenter();
		Vector2 left = new Vector2(center).add(-WIDTH, 0);
		Vector2 right = new Vector2(center).add(WIDTH, 0);

		body.getWorld().rayCast(finder, center,
				new Vector2(center.x, center.y - GROUND * 4));
		body.getWorld().rayCast(finder, left, new Vector2(left.x, left.y - GROUND * 4));
		body.getWorld().rayCast(finder, right,
				new Vector2(right.x, right.y - 2f));

		if (finder.normal != null){
			groundNormal = finder.normal;
		}

	}

	public void slopeGuard() {
		if(groundNormal == null) return;
		Vector2 weight = new Vector2(0, Level.GRAVITY * body.getMass()
				* body.getGravityScale());
		Vector2 normal = new Vector2(groundNormal).scl(-groundNormal
				.dot(weight));
		Vector2 cForce = weight.add(normal).scl(-1);
		body.applyForceToCenter(cForce, true);
	}

	public Vector2 normalize(Vector2 v) {
		Vector2 parallel = new Vector2(groundNormal).rotate(-90);
		return v.rotate(parallel.angle());
	}

	public void stickToGround() {
		Vector2 impulse = new Vector2(groundNormal);
		body.applyLinearImpulse(impulse.scl(-STICK_STRENGTH),
				body.getWorldCenter(), true);
	}
	
	// END Slope code

	@Override
	public void beginContact(Contact contact) {
		Body contacted = null;
		Fixture hitbox = null;
		if (isSensor(contact.getFixtureA())) {
			contacted = contact.getFixtureB().getBody();
			hitbox = contact.getFixtureA();
		} else if (isSensor(contact.getFixtureB())) {
			contacted = contact.getFixtureA().getBody();
			hitbox = contact.getFixtureB();
		}
		if (contacted == null) {
			return;
		}

		Actor a = (Actor) contacted.getUserData();
		if (Const.isGround(a) && hitbox == foot) {
			groundContact.increment(a);
		} else if (Const.isWall(a) && hitbox == left) {
			leftContact.increment(a);
		} else if (Const.isWall(a) && hitbox == right) {
			rightContact.increment(a);
		}
		

	}

	@Override
	public void endContact(Contact contact) {
		Body contacted = null;
		Fixture hitbox = null;
		if (isSensor(contact.getFixtureA())) {
			contacted = contact.getFixtureB().getBody();
			hitbox = contact.getFixtureA();
		} else if (isSensor(contact.getFixtureB())) {
			contacted = contact.getFixtureA().getBody();
			hitbox = contact.getFixtureB();
		}
		if (contacted == null) {
			return;
		}

		Actor a = (Actor) contacted.getUserData();
		if (Const.isGround(a) && hitbox == foot) {
			groundContact.decrement(a);
		} else if (Const.isWall(a) && hitbox == left) {
			leftContact.decrement(a);
		} else if (Const.isWall(a) && hitbox == right) {
			rightContact.decrement(a);
		}
		

	}


	private boolean isSensor(Fixture f) {
		return f == foot || f == left || f == right;
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

}
