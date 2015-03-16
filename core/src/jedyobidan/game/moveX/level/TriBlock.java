package jedyobidan.game.moveX.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import jedyobidan.game.moveX.Const;
import jedyobidan.game.moveX.Level;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.Stage;
import jedyobidan.game.moveX.player.PlayerPhysics;

public class TriBlock extends Actor{
	private Vector2 position;
	private float hdx, hdy;
	private Body body;
	
	public TriBlock(float x, float y, float hdx, float hdy){
		this.position = new Vector2().set(x, y);
		if(hdy < 0){
			hdx *= -1;
			hdy *= -1;
		}
		this.hdx = hdx;
		this.hdy = hdy;
	}
	
	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) {
		spriteRenderer.end();
		shapeRenderer.begin(ShapeType.Filled);
		Vector2 pos = body.getPosition();
		
		shapeRenderer.setColor(Color.DARK_GRAY);
		shapeRenderer.triangle(pos.x - hdx, pos.y - hdy, pos.x + hdx, pos.y - hdy, pos.x + hdx, pos.y + hdy);
		
		shapeRenderer.end();
		spriteRenderer.begin();
		
	}
	
	public void addToStage(Stage s){
		super.addToStage(s);
		Level level = (Level) s;
		float slope = hdy/hdx;
		
		BodyDef def = new BodyDef();
		def.position.set(position);
		def.type = BodyType.StaticBody;
		body = level.getPhysics().createBody(def);
		body.setUserData(this);
		
		EdgeShape edge = new EdgeShape();
		edge.setHasVertex0(true);
		edge.setHasVertex3(true);
		
		FixtureDef fix = new FixtureDef();
		fix.friction = 1;
		fix.restitution = 0;
		fix.filter.categoryBits = Const.CAT_ENVIRONMENT;
		fix.shape = edge;
		
		// Bottom edge
		edge.set(-hdx, -hdy, hdx, -hdy);
		edge.setVertex0(-hdx - PlayerPhysics.WIDTH * 3, -hdy);
		edge.setVertex3(hdx + PlayerPhysics.WIDTH * 3, -hdy);
		body.createFixture(fix);
		
		// Side edge
		edge.set(hdx, -hdy, hdx, hdy);
		edge.setVertex0(hdx, -hdy - PlayerPhysics.GROUND * 3);
		edge.setVertex3(hdx, hdy + PlayerPhysics.GROUND * 3);
		body.createFixture(fix);
		
		// Sloped edge
		edge.set(-hdx, -hdy, hdx, hdy);
		edge.setVertex0(-hdx - PlayerPhysics.WIDTH * 3, -hdy - slope * PlayerPhysics.WIDTH * 3);
		edge.setVertex0( hdx + PlayerPhysics.WIDTH * 3,  hdy + slope * PlayerPhysics.WIDTH * 3);
		body.createFixture(fix);
		
	}
	
	public void removeFromStage(Stage s){
		super.removeFromStage(s);
		Level level = (Level) s;
		level.getPhysics().destroyBody(this.body);
	}

}
