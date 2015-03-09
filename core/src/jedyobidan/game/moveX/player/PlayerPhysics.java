package jedyobidan.game.moveX.player;

import java.util.HashSet;
import java.util.Vector;

import jedyobidan.game.moveX.Level;
import jedyobidan.game.moveX.MoveX;
import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.actors.SolidBlock;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.NormalFinder;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class PlayerPhysics implements ContactListener {
	private Body body;
	private Fixture stand, dash, foot, left, right;

	private HashSet<Actor> groundContact, leftContact, rightContact;
	private Vector2 groundNormal;

	private boolean facing;

	public static final int STICK_STRENGTH = 10;

	public PlayerPhysics(Player player, World physics) {
		groundContact = new HashSet<Actor>();
		leftContact = new HashSet<Actor>();
		rightContact = new HashSet<Actor>();
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(0, 10);
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.fixedRotation = true;
		body = physics.createBody(bodyDef);
		body.setUserData(player);

		PolygonShape standingShape = new PolygonShape();
		standingShape.setAsBox(0.5f, 0.75f, new Vector2(0, 0.25f), 0);

		PolygonShape dashingShape = new PolygonShape();
		dashingShape.setAsBox(0.5f, 0.5f);

		PolygonShape footShape = new PolygonShape();
		footShape.setAsBox(0.45f, 0.2f, new Vector2(0, -0.5f), 0);

		PolygonShape leftShape = new PolygonShape();
		leftShape.setAsBox(0.1f, 0.45f, new Vector2(-0.5f, 0), 0);

		PolygonShape rightShape = new PolygonShape();
		rightShape.setAsBox(0.1f, 0.45f, new Vector2(0.5f, 0), 0);

		FixtureDef hitbox = new FixtureDef();
		hitbox.density = 1;
		hitbox.friction = 0.0f;
		hitbox.restitution = 0.0f;
		hitbox.filter.categoryBits = MoveX.CAT_PLAYER;

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
		f.maskBits = (short) (dashOn ? 0xFF : 0x00);
		dash.setFilterData(f);

		f = dash.getFilterData();
		f.maskBits = (short) (dashOn ? 0x00 : 0xFF);
		stand.setFilterData(f);

	}

	public Body getBody() {
		return body;
	}

	public boolean onGround() {
		return groundContact.size() > 0;
	}

	public boolean facingWall() {
		if (facing) {
			return leftContact.size() > 0;
		} else {
			return rightContact.size() > 0;
		}
	}

	public boolean getFacing() {
		return facing;
	}

	public void setFacing(boolean facingLeft) {
		this.facing = facingLeft;
	}

	public void move(float x, float y) {
		body.getPosition().set(x, y);
	}

	public void requestGroundNormal() {
		NormalFinder finder = new NormalFinder();
		Vector2 center = body.getWorldCenter();
		Vector2 left = new Vector2(center).add(-0.5f, 0);
		Vector2 right = new Vector2(center).add(0.5f, 0);

		body.getWorld().rayCast(finder, center,
				new Vector2(center.x, center.y - 2f));
		body.getWorld().rayCast(finder, left, new Vector2(left.x, left.y - 2f));
		body.getWorld().rayCast(finder, right,
				new Vector2(right.x, right.y - 2f));

		if (finder.normal != null)
			groundNormal = finder.normal;

	}

	public void slopeGuard() {
		Vector2 weight = new Vector2(0, Level.GRAVITY * body.getMass()
				* body.getGravityScale());
		Vector2 normal = new Vector2(groundNormal).scl(-groundNormal
				.dot(weight));
		Vector2 cForce = weight.add(normal).scl(-1);
		body.applyForceToCenter(cForce, true);
	}

	public Vector2 normalize(Vector2 v) {
		Vector2 parallel = new Vector2(groundNormal).rotate(-90);
		return parallel.scl(parallel.dot(v));
	}

	public void stickToGround() {
		Vector2 impulse = new Vector2(groundNormal);
		body.applyLinearImpulse(impulse.scl(-STICK_STRENGTH),
				body.getWorldCenter(), true);
	}

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
		if (a instanceof SolidBlock) {
			if (hitbox == foot) {
				groundContact.add(a);
			} else if (hitbox == left) {
				leftContact.add(a);
			} else if (hitbox == right) {
				rightContact.add(a);
			}
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
		if (a instanceof SolidBlock) {
			if (hitbox == foot) {
				groundContact.remove(a);
			} else if (hitbox == left) {
				leftContact.remove(a);
			} else if (hitbox == right) {
				rightContact.remove(a);
			}
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
