package jedyobidan.game.moveX.level;

import jedyobidan.game.moveX.Const;
import jedyobidan.game.moveX.Level;
import jedyobidan.game.moveX.lib.Stage;
import jedyobidan.game.moveX.player.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Blastzone extends LevelObject implements ContactListener {
	private Vector2 position;
	private float hwidth, hheight;
	private Body body;
	private float flashTime;
	
	private static final float FLASH = 0.2f;
	
	public Blastzone(float x, float y, float hwidth, float hheight){
		this.position = new Vector2().set(x,y);
		this.hwidth = hwidth;
		this.hheight = hheight;
	}
	
	public Blastzone() {
		this(0,0,0,0);
	}

	@Override
	public void step(float delta){
		if(flashTime > 0){
			flashTime -= delta;
		}
	}

	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) {
		if(flashTime > 0){
			stage.pushMatrix();
			stage.useCamera(0);
			spriteRenderer.end();
			Gdx.gl.glEnable(GL20.GL_BLEND);
		    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			shapeRenderer.begin(ShapeType.Filled);
			
			float alpha = (float) Math.sqrt(flashTime / FLASH);
			shapeRenderer.setColor(1, 0, 0, alpha);
			shapeRenderer.rect(-Gdx.graphics.getWidth()/2, -Gdx.graphics.getHeight()/2, 
					Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			

			shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
			spriteRenderer.begin();
			stage.popMatrix();
		}
	}
	
	@Override
	public void onAdd(Stage s){
		super.onAdd(s);
		Level level = (Level) s;
		
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(position);
		body = level.getPhysics().createBody(def);
		body.setUserData(this);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(hwidth, hheight);
		
		FixtureDef fix = new FixtureDef();
		fix.filter.categoryBits = Const.CAT_HAZARD;
		fix.filter.maskBits = Const.CAT_PLAYER;
		fix.shape = shape;
		fix.isSensor = true;
		body.createFixture(fix);
		
		level.addContactListener(this);
	}
	
	@Override
	public void onRemove(Stage s){
		Level level = (Level) s;
		level.removeContactListener(this);
		level.getPhysics().destroyBody(body);
	}
	
	public float getZIndex(){
		return 10;
	}
	
	
	@Override
	public String writeString() {
		StringBuilder ans = new StringBuilder("Blast;\n");
		ans.append("    " + position.x + " " + position.y + " " + hwidth + " " + hheight + ";\n");
		return ans.toString();
	}

	@Override
	public void readString(String str) {
		String[] lines = str.split(";\\s*");
		String[] params = lines[1].split("\\s+");
		position.set(Float.parseFloat(params[0]), Float.parseFloat(params[1]));
		hwidth = Float.parseFloat(params[2]);
		hheight = Float.parseFloat(params[3]);
	}

	@Override
	public void beginContact(Contact contact) {
		Body other = null;
		if(contact.getFixtureA().getBody() == body){
			other = contact.getFixtureB().getBody();
		} else if(contact.getFixtureB().getBody() == body){
			other = contact.getFixtureA().getBody();
		}
		if(other != null && other.getUserData() instanceof Player){
			Player p = (Player) other.getUserData();
			p.moveToCheckpoint();
			flashTime = FLASH;
		}
		
	}

	@Override
	public void endContact(Contact contact) { }

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) { }

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) { }

}
