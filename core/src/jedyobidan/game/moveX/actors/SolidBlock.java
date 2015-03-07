package jedyobidan.game.moveX.actors;

import jedyobidan.game.moveX.MoveX;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.Box2dStage;
import jedyobidan.game.moveX.lib.Stage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class SolidBlock extends Actor{
	private Body body;
	private Vector2 position;
	private PolygonShape shape;
	
	public SolidBlock(Vector2 position, PolygonShape shape){
		this.position = position;
		this.shape = shape;
	}

	@Override
	public void render(SpriteBatch render, ShapeRenderer shapeRender) {
		render.end();
		shapeRender.begin();
		float[] coords = new float[shape.getVertexCount() * 2];
		Vector2 vertex = new Vector2();
		for(int i = 0; i < shape.getVertexCount(); i++){
			shape.getVertex(i, vertex);
			coords[2*i] = vertex.x;
			coords[2*i + 1] = vertex.y;
		}
		Matrix4 prev = new Matrix4(shapeRender.getTransformMatrix());
		
		shapeRender.set(ShapeType.Line);
		shapeRender.setColor(Color.DARK_GRAY);
		shapeRender.translate(body.getPosition().x, body.getPosition().y, getZIndex());
		shapeRender.polygon(coords);
		shapeRender.setTransformMatrix(prev);
		shapeRender.end();
		render.begin();
	}

	@Override
	public void addToStage(Stage s) {
		Box2dStage stage = (Box2dStage) s;
				
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(position);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.friction = 0.3f;
		fixture.density = 0;
		fixture.filter.categoryBits = MoveX.CAT_ENVIRONMENT;
		
		body = stage.getPhysics().createBody(bodyDef);
		body.createFixture(fixture);
		body.setUserData(this);
	}

	@Override
	public void removeFromStage(Stage s) {
		Box2dStage stage = (Box2dStage) s;
		stage.getPhysics().destroyBody(body);
	}

	@Override
	public float getZIndex() {
		return 0;
	}
	
}
