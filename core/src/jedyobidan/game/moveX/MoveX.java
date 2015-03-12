package jedyobidan.game.moveX;

import jedyobidan.game.moveX.actors.RectBlock;
import jedyobidan.game.moveX.actors.SolidBlock;
import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.actors.StaticPlatform;
import jedyobidan.game.moveX.actors.TriBlock;
import jedyobidan.game.moveX.lib.Actor;

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

	@Override
	public void create() {
		initRes();
		Level test = new Level(spriteRender, shapeRender);
		constructLevel(test);
		test.setPlayer(new Player(), new Vector2(0,15));
		test.setDebugDraw(false);
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

	private void constructLevel(Level level) {
		level.addActor(new RectBlock(0, 0, 30, 1)); 		// Ground
		level.addActor(new RectBlock(10, 6, 0.5f, 5)); 		// Left wall
		level.addActor(new RectBlock(15, 6, 0.5f, 5)); 		// Right wall
		level.addActor(new StaticPlatform(5, 5.5f, 2.5f));		// Right plat
		level.addActor(new StaticPlatform(-5, 7f, 2.5f)); 		// Left plat
		level.addActor(new RectBlock(-20, 7.25f, 1, 5));	// Low ceil
		level.addActor(new RectBlock(-16, 13.25f, 5, 1));
		level.addActor(new StaticPlatform(0, 17, 3));
		
		
		// slope
		level.addActor(new RectBlock(-5, 2, 2.5f, 1)); 
		level.addActor(new TriBlock(-9.5f, 2, 2, 1));
		level.addActor(new TriBlock(-0.5f, 2, 2, -1));
		
//		PolygonShape slopeShape = new PolygonShape();
//		slopeShape.set(new Vector2[]{new Vector2(-5, 0), new Vector2(-3, 1f), new Vector2(3, 1f), new Vector2(5,0)});
//		SolidBlock slope = new SolidBlock(new Vector2(-5, 1), slopeShape);
//		level.addActor(slope);
		
	}
	

}
