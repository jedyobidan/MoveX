package jedyobidan.game.moveX;

import jedyobidan.game.moveX.actors.Checkpoint;
import jedyobidan.game.moveX.actors.RectBlock;
import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.actors.StaticPlatform;
import jedyobidan.game.moveX.actors.TriBlock;
import jedyobidan.game.moveX.lib.Actor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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
		test.setDebug(true);
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
		level.addGameActor(new RectBlock(0, 0, 30, 1)); 		// Ground
		level.addGameActor(new RectBlock(10, 12, 0.5f, 9)); 		// Left wall
		level.addGameActor(new RectBlock(15, 9.5f, 0.5f, 8.5f)); 		// Right wall
		level.addGameActor(new RectBlock(20, 11, 0.5f, 10));
		level.addGameActor(new RectBlock(15, 21.5f, 5.5f, 0.5f));
		level.addGameActor(new StaticPlatform(5, 5.5f, 2.5f));		// Right plat
		level.addGameActor(new StaticPlatform(-5, 7f, 2.5f)); 		// Left plat
		level.addGameActor(new RectBlock(-20, 7.25f, 1, 5));	// Low ceil
		level.addGameActor(new RectBlock(-19, 13.25f, 2, 1));
		level.addGameActor(new StaticPlatform(-5, 17, 3));
		level.addGameActor(new Checkpoint(-5f, 3.75f));
		level.addGameActor(new Checkpoint(17.5f, 1.75f));
		level.addGameActor(new Checkpoint(-5, 17.75f));
		level.addGameActor(new Checkpoint(-25, 1.75f));
		
		// slope
		level.addGameActor(new RectBlock(-5, 2, 2.5f, 1)); 
		level.addGameActor(new TriBlock(-9.5f, 2, 2, 1));
		level.addGameActor(new TriBlock(-0.5f, 2, 2, -1));
		
//		PolygonShape slopeShape = new PolygonShape();
//		slopeShape.set(new Vector2[]{new Vector2(-5, 0), new Vector2(-3, 1f), new Vector2(3, 1f), new Vector2(5,0)});
//		SolidBlock slope = new SolidBlock(new Vector2(-5, 1), slopeShape);
//		level.addActor(slope);
		
	}
	

}
