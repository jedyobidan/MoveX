package jedyobidan.game.moveX.player;

import jedyobidan.game.moveX.MoveX;
import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.actors.SolidBlock;
import jedyobidan.game.moveX.lib.Actor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class PlayerPhysics implements ContactListener {
	private Body body;
	private Fixture stand, dash, foot, left, right;
	
	private int onGround;
	private int onWallLeft, onWallRight;
	
	private boolean facing;
	
	public PlayerPhysics(Player player, World physics){
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
		footShape.setAsBox(0.45f, 0.1f, new Vector2(0, -0.5f), 0);
		
		PolygonShape leftShape = new PolygonShape();
		leftShape.setAsBox(0.1f, 0.45f, new Vector2(-0.5f, 0), 0);		

		PolygonShape rightShape = new PolygonShape();
		rightShape.setAsBox(0.1f, 0.45f, new Vector2(0.5f, 0), 0);

		FixtureDef hitbox = new FixtureDef();
		hitbox.density = 1;
		hitbox.friction = 0.0f;
		hitbox.filter.categoryBits = MoveX.CAT_PLAYER;

		hitbox.shape = standingShape;
		stand = body.createFixture(hitbox);

		hitbox.shape = dashingShape;
		dash = body.createFixture(hitbox);
		dash.getFilterData().maskBits = 0;

		hitbox.shape = footShape;
		hitbox.isSensor = true;
		foot = body.createFixture(hitbox);
		
		hitbox.shape = leftShape;
		left = body.createFixture(hitbox);
		
		hitbox.shape = rightShape;
		right = body.createFixture(hitbox);
	}
	
	public void setDashbox(boolean dashOn){
		if (dashOn) {
			dash.getFilterData().maskBits = 0xFF;
			stand.getFilterData().maskBits = 0x00;
		} else {
			dash.getFilterData().maskBits = 0x00;
			stand.getFilterData().maskBits = 0xFF;
		}
	}
	
	public Body getBody(){
		return body;
	}
	
	public boolean onGround(){
		return onGround > 0;
	}
	
	public boolean facingWall(){
		if(facing){
			return onWallLeft > 0;
		} else {
			return onWallRight > 0;
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

	@Override
	public void beginContact(Contact contact) {
		Body contacted = null;
		Fixture hitbox = null;
		if (isContactable(contact.getFixtureA())) {
			contacted = contact.getFixtureB().getBody();
			hitbox = contact.getFixtureA();
		} else if (isContactable(contact.getFixtureB())) {
			contacted = contact.getFixtureA().getBody();
			hitbox = contact.getFixtureB();
		}
		if(contacted == null){
			return;
		}
		
		Actor a = (Actor) contacted.getUserData();
		if (a instanceof SolidBlock) {
			if(hitbox == foot){
				onGround++;
			} else if (hitbox == left){
				onWallLeft++;
			} else if (hitbox == right){
				onWallRight++;
			}
		}
		
	}

	@Override
	public void endContact(Contact contact) {
		Body contacted = null;
		Fixture hitbox = null;
		if (isContactable(contact.getFixtureA())) {
			contacted = contact.getFixtureB().getBody();
			hitbox = contact.getFixtureA();
		} else if (isContactable(contact.getFixtureB())) {
			contacted = contact.getFixtureA().getBody();
			hitbox = contact.getFixtureB();
		}
		if(contacted == null){
			return;
		}
		
		Actor a = (Actor) contacted.getUserData();
		if (a instanceof SolidBlock) {
			if(hitbox == foot){
				onGround--;
			} else if (hitbox == left){
				onWallLeft--;
			} else if (hitbox == right){
				onWallRight--;
			}
		}
		
		
	}
	
	private boolean isContactable(Fixture f){
		return f == foot || f == left || f == right;
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
}
