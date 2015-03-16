package jedyobidan.game.moveX.level;

import jedyobidan.game.moveX.Const;
import jedyobidan.game.moveX.Level;
import jedyobidan.game.moveX.lib.Stage;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Blastzone extends LevelObject implements ContactListener {
	private Vector2 position;
	private float hwidth, hheight;
	private Body body;
	
	public Blastzone(float x, float y, float hwidth, float hheight){
		this.position = new Vector2().set(x,y);
		this.hwidth = hwidth;
		this.hheight = hheight;
	}
	
	@Override
	public void onAdd(Stage s){
		super.onAdd(s);
		Level level = (Level) s;
		
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(position);
		body = level.getPhysics().createBody(def);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(hwidth, hheight);
		
		FixtureDef fix = new FixtureDef();
		fix.filter.categoryBits = Const.CAT_HAZARD;
		fix.filter.maskBits = Const.CAT_PLAYER;
	}
	
	@Override
	public void onRemove(Stage s){
		
	}
	
	
	@Override
	public String writeString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void readString(String str) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) {
		// Invisible
	}

}
