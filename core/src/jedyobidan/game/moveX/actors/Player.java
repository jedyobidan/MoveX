package jedyobidan.game.moveX.actors;

import java.util.HashMap;

import jedyobidan.game.moveX.Controller;
import jedyobidan.game.moveX.Level;
import jedyobidan.game.moveX.MoveX;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.Stage;
import jedyobidan.game.moveX.lib.TextureManager;
import jedyobidan.game.moveX.player.IdleState;
import jedyobidan.game.moveX.player.PlayerState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

public class Player extends Actor implements ContactListener {
	private PlayerState state;
	private PlayerState nextState;

	// Player Data
	public final Controller controller;
	public final TextureManager textures;
	public final HashMap<String, Float> stats;
	private int jumps, dashes;

	// Player Physics
	private Body body;
	private Fixture standBox, dashBox, footbox;
	private int onGround;
	private boolean facingLeft;

	public Player() {
		textures = new TextureManager(MoveX.ATLAS, "player");
		state = new IdleState(this);
		stats = new HashMap<String, Float>();
		stats.put("walk_speed", 7f);
		stats.put("walk_accel", 7f);
		stats.put("jump_speed", 13.4f);
		stats.put("air_speed", 5f);
		stats.put("air_accel", 5f);
		stats.put("skid_force", 15f);
		
		// Can do?
		stats.put("can_jump", 1f);
		stats.put("can_dash", 1f);
		stats.put("can_direction_dash", 1f);
		stats.put("air_jumps", 1f);
		stats.put("air_dashes", 1f);
		
		controller = new Controller();
	}

	@Override
	public void step(float timeDelta) {
		if (nextState != null) {
			state = nextState;
		}
		if (onGround()){
			refresh();
		}
		controller.step();
		state.step(timeDelta);
	}

	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRender) {
		state.render(spriteRenderer);
	}

	public void setState(PlayerState next) {
		if(next.init(state)){
			state.destroy(next);
			nextState = next;
		}
	}

	public boolean getFacing() {
		return facingLeft;
	}

	public void setFacing(boolean facingLeft) {
		this.facingLeft = facingLeft;
	}

	public void move(float x, float y) {
		body.getPosition().set(x, y);
	}

	public boolean onGround() {
		return onGround > 0;
	}
	
	public boolean canAirJump(){
		return jumps > 0;
	}
	
	public void useAirJump(){
		jumps--;
	}
	
	public boolean canAirDash(){
		return dashes > 0;
	}
	
	public void useAirDash(){
		dashes--;
	}
		
	public void refresh(){
		jumps = stats.get("air_jumps").intValue();
		dashes = stats.get("air_dashes").intValue();
	}

	public Body getBody() {
		return body;
	}

	public void setDashbox(boolean dashOn) {
		if (dashOn) {
			dashBox.getFilterData().maskBits = 0xFF;
			standBox.getFilterData().maskBits = 0x00;
		} else {
			dashBox.getFilterData().maskBits = 0x00;
			standBox.getFilterData().maskBits = 0xFF;
		}
	}

	@Override
	public float getZIndex() {
		return -1;
	}

	@Override
	public void addToStage(Stage s) {
		Level level = (Level) s;
		Gdx.input.setInputProcessor(controller);
		level.addContactListener(this);
		
		World physics = level.getPhysics();
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(0, 10);
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.fixedRotation = true;
		body = physics.createBody(bodyDef);
		body.setUserData(this);

		PolygonShape standingShape = new PolygonShape();
		standingShape.setAsBox(0.5f, 0.75f, new Vector2(0, 0.25f), 0);

		PolygonShape dashingShape = new PolygonShape();
		dashingShape.setAsBox(0.5f, 0.5f);

		PolygonShape footShape = new PolygonShape();
		footShape.setAsBox(0.45f, 0.1f, new Vector2(0, -0.5f), 0);

		FixtureDef hitbox = new FixtureDef();
		hitbox.density = 1;
		hitbox.friction = 0.0f;
		hitbox.filter.categoryBits = MoveX.CAT_PLAYER;

		hitbox.shape = standingShape;
		standBox = body.createFixture(hitbox);

		hitbox.shape = dashingShape;
		dashBox = body.createFixture(hitbox);
		dashBox.getFilterData().maskBits = 0;

		hitbox.shape = footShape;
		hitbox.isSensor = true;
		footbox = body.createFixture(hitbox);
	}

	@Override
	public void removeFromStage(Stage s) {
		Level level = (Level) s;
		Gdx.input.setInputProcessor(null);
		World physics = level.getPhysics();
		physics.destroyBody(body);
		level.removeContactListener(this);
	}

	@Override
	public void beginContact(Contact contact) {
		// Ground contacts
		Body groundContact = null;
		if (contact.getFixtureA() == footbox) {
			groundContact = contact.getFixtureB().getBody();
		} else if (contact.getFixtureB() == footbox) {
			groundContact = contact.getFixtureA().getBody();
		}
		if (groundContact != null && groundContact.getUserData() instanceof SolidBlock) {
			onGround++;
		}
	}

	@Override
	public void endContact(Contact contact) {
		// Ground contacts
		Body groundContact = null;
		if (contact.getFixtureA() == footbox) {
			groundContact = contact.getFixtureB().getBody();
		} else if (contact.getFixtureB() == footbox) {
			groundContact = contact.getFixtureA().getBody();
		}
		if (groundContact != null && groundContact.getUserData() instanceof SolidBlock) {
			onGround--;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

}
