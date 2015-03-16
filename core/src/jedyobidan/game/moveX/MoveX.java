package jedyobidan.game.moveX;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jedyobidan.game.moveX.level.Checkpoint;
import jedyobidan.game.moveX.level.LevelObject;
import jedyobidan.game.moveX.level.RectBlock;
import jedyobidan.game.moveX.level.StaticPlatform;
import jedyobidan.game.moveX.level.TriBlock;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.player.Player;

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
		Level test = constructLevel("level/test.dat");
		test.setPlayer(new Player(), new Vector2(0,15));
		test.setDebug(false);
		setScreen(test);
		test.showDialog("Welcome to Move X!", "Press w/a/s/d to move\nPress k to jump\nPress l to dash");
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

	private Level constructLevel(String file) {
		Level level = new Level(spriteRender, shapeRender);
		level.setBackground("cave");
		String levelCode = Gdx.files.internal(file).readString();
		levelCode = levelCode.replaceAll("\\#.*", "");
		String[] in = levelCode.split("\\s*--\\s*");
		for(String objCode: in){
			objCode = objCode.trim();
			if(objCode.isEmpty()) continue;
			try{
				level.addGameActor(LevelObject.constructObject(objCode));
			} catch (RuntimeException e){
				System.err.println("Could not parse code: \n" + objCode);
				e.printStackTrace();
			}
		}
		return level;
	}
	

}
