package jedyobidan.game.moveX;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jedyobidan.game.editor.LevelEditor;
import jedyobidan.game.moveX.level.Blastzone;
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
	private String[] args;
	
	private SpriteBatch spriteRender;	
	private BitmapFont font;
	private ShapeRenderer shapeRender;

	public static TextureAtlas ATLAS;
	
	private Player player;
	
	public MoveX(String[] args){
		this.args = args;
	}

	@Override
	public void create() {
		initRes();
		player = new Player();
		if(args.length > 0){
			editLevel(args[0]);
		} else {
			// TODO replace with title
			playLevel("level/test.dat", new Vector2(0, 10), true);
		}
	}
	
	public void playLevel(String file, Vector2 start, boolean dev){
		Level level = constructLevel(file);
		level.setPlayer(player, start.cpy());
		if(dev){
			level.addUIInput(new DevUIProcessor(level), true);
		}
		setScreen(level);
		//TODO replace with "cutscene"
		level.showDialog("Welcome to Move X!", "Press w/a/s/d to move\nPress k to jump\nPress l to dash");
	}
	
	public void editLevel(String file){
		Level level = constructLevel(file);
		level.addGameActor(new LevelEditor());
		setScreen(level);
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
