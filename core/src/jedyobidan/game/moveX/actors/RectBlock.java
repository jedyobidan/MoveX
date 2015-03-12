package jedyobidan.game.moveX.actors;

import jedyobidan.game.moveX.Const;
import jedyobidan.game.moveX.Level;
import jedyobidan.game.moveX.MoveX;
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
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class RectBlock extends Actor {
	private Vector2 position;
	private float hwidth, hheight;
	private Body body;
	
	public RectBlock(Vector2 position, float hwidth, float hheight){
		this(position.x, position.y, hwidth, hheight);
	}
	
	public RectBlock(float x, float y, float hwidth, float hheight){
		this.position = new Vector2().set(x, y);
		this.hwidth = hwidth;
		this.hheight = hheight;
	}
	
	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) {
		spriteRenderer.end();
		shapeRenderer.begin(ShapeType.Filled);
		Vector2 pos = body.getPosition();
		
		shapeRenderer.setColor(Color.DARK_GRAY);
		shapeRenderer.rect(pos.x - hwidth, pos.y - hheight, 2*hwidth, 2*hheight);
		
		shapeRenderer.end();
		spriteRenderer.begin();
	}
	
	public void addToStage(Stage s){
		Level level = (Level) s;
		
		BodyDef def = new BodyDef();
		def.position.set(position);
		def.type = BodyType.StaticBody;
		body = level.getPhysics().createBody(def);
		body.setUserData(this);

		EdgeShape edge = new EdgeShape();
		FixtureDef fix = new FixtureDef();
		fix.friction = 1;
		fix.restitution = 0;
		fix.filter.categoryBits = Const.CAT_ENVIRONMENT;
		fix.shape = edge;
		
		edge.setHasVertex0(true);
		edge.setHasVertex3(true);
		
		//Top Edge
		edge.set(-hwidth, hheight, hwidth, hheight);
		edge.setVertex0(-hwidth - PlayerPhysics.WIDTH * 3, hheight);
		edge.setVertex3( hwidth + PlayerPhysics.WIDTH * 3, hheight);
		body.createFixture(fix);
		
		//Bottom Edge
		edge.set(-hwidth, -hheight, hwidth, -hheight);
		edge.setVertex0(-hwidth - PlayerPhysics.WIDTH * 3, -hheight);
		edge.setVertex3( hwidth + PlayerPhysics.WIDTH * 3, -hheight);
		body.createFixture(fix);
		
		//Left Edge
		edge.set(-hwidth, -hheight, -hwidth, hheight);
		edge.setVertex0(-hwidth, -hheight - PlayerPhysics.GROUND * 3);
		edge.setVertex3(-hwidth,  hheight + PlayerPhysics.GROUND * 3);
		body.createFixture(fix);
		
		//Right Edge
		edge.set(hwidth, -hheight, hwidth, hheight);
		edge.setVertex0(hwidth, -hheight - PlayerPhysics.GROUND * 3);
		edge.setVertex3(hwidth,  hheight + PlayerPhysics.GROUND * 3);
		body.createFixture(fix);
	}
	
	public void removeFromStage(Stage s){
		Level level = (Level) s;
		level.getPhysics().destroyBody(this.body);
	}

}
