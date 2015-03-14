package jedyobidan.game.moveX.actors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Manifold;

import jedyobidan.game.moveX.Const;
import jedyobidan.game.moveX.Level;
import jedyobidan.game.moveX.MoveX;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.SpriteTransform;
import jedyobidan.game.moveX.lib.Stage;

public class Checkpoint extends Actor implements ContactListener {
	private TextureRegion txOn, txOff;
	private boolean on;
	private Vector2 position;
	private Body body;

	public Checkpoint(float x, float y) {
		position = new Vector2().set(x, y);
		txOn = MoveX.ATLAS.findRegion("checkpoint-on");
		txOff = MoveX.ATLAS.findRegion("checkpoint-off");
	}

	public void addToStage(Stage s) {
		super.addToStage(s);
		Level level = (Level) s;

		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(position);
		body = level.getPhysics().createBody(def);

		CircleShape shape = new CircleShape();
		shape.setRadius(0.2f);

		FixtureDef fix = new FixtureDef();
		fix.isSensor = true;
		fix.filter.categoryBits = Const.CAT_ENVIRONMENT;
		fix.filter.maskBits = Const.CAT_PLAYER;
		fix.shape = shape;
		body.createFixture(fix);
		
		level.addContactListener(this);
	}

	public void removeFromStage(Stage s) {
		super.removeFromStage(s);
		Level level = (Level) s;
		level.getPhysics().destroyBody(body);
		level.removeContactListener(this);
	}

	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) {
		TextureRegion texture = on ? txOn : txOff;
		SpriteTransform transform = new SpriteTransform();
		transform.origin.set(4, 15);
		transform.scale.set(1 / Const.PIXELS_PER_METER,
				1 / Const.PIXELS_PER_METER);
		transform.position.set(position);
		transform.texture = texture;
		transform.render(spriteRenderer);
	}

	@Override
	public void beginContact(Contact contact) {
		Body other = null;
		if (contact.getFixtureA().getBody() == body) {
			other = contact.getFixtureB().getBody();
		} else if (contact.getFixtureB().getBody() == body) {
			other = contact.getFixtureA().getBody();
		}
		if(other == null) return;
		Actor a = (Actor) other.getUserData();
		if(a instanceof Player){
			if(!on){
				stage.addActor(Const.ACT_GROUP_GAME, new FlyingText("Checkpoint", position.x, position.y + 0.75f));
			}
			Player p = (Player) a;
			p.setCheckpoint(this);
		}
	}
	
	public Vector2 getPosition(){
		return position;
	}
	
	public void setEnabled(boolean en){
		on = en;
	}
	
	public float getZIndex(){
		return 0.5f;
	}
 
	@Override
	public void endContact(Contact contact) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

}
