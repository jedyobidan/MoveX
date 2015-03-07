package jedyobidan.game.moveX;

import jedyobidan.game.moveX.actors.SolidBlock;
import jedyobidan.game.moveX.actors.Player;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class MoveX extends Game {
	private SpriteBatch spriteRender;
	private BitmapFont font;
	private ShapeRenderer shapeRender;

	public static TextureAtlas ATLAS;

	public static float PIXELS_PER_METER = 24;

	public static short CAT_ENVIRONMENT = 0x01;
	public static short CAT_PLAYER = 0x02;
	public static short CAT_ENEMY = 0x04;

	@Override
	public void create() {
		initRes();
		Level test = new Level(spriteRender, shapeRender);
		constructLevel(test);
		test.setPlayer(new Player(), new Vector2(0,10));
		//test.setDebugDraw(true);
		setScreen(test);
	}

	private void initRes() {
		spriteRender = new SpriteBatch();
		font = new BitmapFont();
		shapeRender = new ShapeRenderer();
		shapeRender.setAutoShapeType(true);
		ATLAS = new TextureAtlas("img-packed/pack.atlas");
	}

	public void dispose() {
		super.dispose();
		spriteRender.dispose();
		shapeRender.dispose();
		font.dispose();
		ATLAS.dispose();
		getScreen().dispose();
	}

	private void constructLevel(jedyobidan.game.moveX.Level level) {
		PolygonShape groundShape = new PolygonShape();
		groundShape.setAsBox(10, 1);
		SolidBlock ground = new SolidBlock(new Vector2(0, 0), groundShape);
		level.addActor(ground);
		
		SolidBlock ground2 = new SolidBlock(new Vector2(0, -10), groundShape);
		level.addActor(ground2);
		
		PolygonShape wallShape = new PolygonShape();
		wallShape.setAsBox(0.5f, 5);
		SolidBlock wall = new SolidBlock(new Vector2(10, 5), wallShape);
		level.addActor(wall);
		
		PolygonShape platShape = new PolygonShape();
		platShape.setAsBox(2.5f, 0.1f);
		SolidBlock plat = new SolidBlock(new Vector2(5, 3.5f), platShape);
		level.addActor(plat);
		
		SolidBlock plat2 = new SolidBlock(new Vector2(-5, 5), platShape);
		level.addActor(plat2);
	}

}
