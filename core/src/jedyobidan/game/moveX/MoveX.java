package jedyobidan.game.moveX;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jedyobidan.game.moveX.editor.LevelEditor;
import jedyobidan.game.moveX.level.Blastzone;
import jedyobidan.game.moveX.level.Checkpoint;
import jedyobidan.game.moveX.level.LevelObject;
import jedyobidan.game.moveX.level.RectBlock;
import jedyobidan.game.moveX.level.Sign;
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
	private ShapeRenderer shapeRender;

	public static TextureAtlas ATLAS;
	public static MoveX GAME;
	
	private Player player;
	
	
	public MoveX(String... args){
		this.args = args;
	}

	@Override
	public void create() {
		initRes();
		GAME = this;
		player = new Player();
		if(args.length > 0){
			editLevel(args[0]);
		} else {
			// TODO replace with title
			playLevel("level/test1.dat", new Vector2(0, 2), false);
		}
	}
	
	public void playLevel(String file, Vector2 start, boolean dev){
		Level level = constructLevel(file);
		level.setPlayer(player, start.cpy());
		if(dev){
			level.addInput(new DevUIProcessor(level, file), true);
		} else {
			level.addInput(new DistUIProcessor(level), true);
		}
		setScreen(level);
		//TODO replace with "cutscene"
		level.showDialog("Welcome to Move X!", "Press w/a/s/d to move\nPress k to jump\nPress l to dash\nPress Esc to go to the menu");
	}
	
	public void editLevel(String file){
		Level level;
		if(file.equals("*new")){
			level = new Level(spriteRender, shapeRender);
		} else {
			level = constructLevel(file);
		}
		level.setDebug(true);
		level.addGameActor(new LevelEditor(file));
		setScreen(level);
	}

	private void initRes() {
		spriteRender = new SpriteBatch();
		shapeRender = new ShapeRenderer();
		shapeRender.setAutoShapeType(true);
		ATLAS = new TextureAtlas("img-packed/pack.atlas");
	}

	public void dispose() {
		super.dispose();
		spriteRender.dispose();
		shapeRender.dispose();
		Const.Fonts.dispose();
		ATLAS.dispose();
		getScreen().dispose();
	}

	private Level constructLevel(String file) {
		Level level = new Level(spriteRender, shapeRender);
		String levelCode = Gdx.files.internal(file).readString();
		level.readString(levelCode);
		return level;
	}
	

}
