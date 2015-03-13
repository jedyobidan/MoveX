package jedyobidan.game.moveX.actors;

import jedyobidan.game.moveX.Const;
import jedyobidan.game.moveX.Level;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.Stage;
import jedyobidan.game.moveX.player.PlayerPhysics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

public class StaticPlatform extends Actor implements ContactListener{
	private float hwidth;
	private Vector2 position;
	private Body body;
	
	public StaticPlatform(Vector2 position, float hwidth){
		this(position.x, position.y, hwidth);
	}
	
	public StaticPlatform(float x, float y, float width){
		this.position = new Vector2().set(x, y);
		this.hwidth = width;
	}
	
	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) {
		spriteRenderer.end();
		shapeRenderer.begin(ShapeType.Filled);
		Vector2 pos = body.getPosition();
		
		shapeRenderer.setColor(Color.LIGHT_GRAY);
		shapeRenderer.rect(pos.x - hwidth, pos.y, hwidth * 2, -0.2f);
		
		shapeRenderer.end();
		spriteRenderer.begin();
	}
	
	public void addToStage(Stage s){
		super.addToStage(s);
		Level level = (Level) s;
		
		level.addContactListener(this);
		
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(position);
		body = level.getPhysics().createBody(def);
		body.setUserData(this);
		
		EdgeShape shape = new EdgeShape();
		shape.setHasVertex0(true);
		shape.setHasVertex3(true);
		shape.set(- hwidth, 0, hwidth, 0);
		shape.setVertex0(-hwidth - PlayerPhysics.WIDTH * 3, 0);
		shape.setVertex3(hwidth + PlayerPhysics.WIDTH * 3, 0);
		
		
		FixtureDef fix = new FixtureDef();
		fix.shape = shape;
		fix.filter.categoryBits = Const.CAT_ENVIRONMENT;
		fix.filter.maskBits = Const.CAT_PLAYER;
		fix.friction = 1f;
		fix.restitution = 0f;
		
		body.createFixture(fix);		
	}
	
	@Override
	public void removeFromStage(Stage s) {
		super.removeFromStage(s);
		Level level = (Level) s;
		level.getPhysics().destroyBody(body);
		level.removeContactListener(this);
	}

	@Override
	public void beginContact(Contact contact) { }

	@Override
	public void endContact(Contact contact) { }

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		Body other;
		if(contact.getFixtureA().getBody() == body){
			other = contact.getFixtureB().getBody();
		} else if(contact.getFixtureB().getBody() == body){
			other = contact.getFixtureA().getBody();
		} else {
			return;
		}
		Actor a = (Actor) other.getUserData();
		if(a instanceof Player){
			float feet = other.getPosition().y - PlayerPhysics.GROUND;
			if(feet < body.getPosition().y) {
				contact.setEnabled(false);
			}
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) { }

}
