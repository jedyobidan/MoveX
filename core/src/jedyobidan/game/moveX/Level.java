package jedyobidan.game.moveX;

import jedyobidan.game.moveX.level.Checkpoint;
import jedyobidan.game.moveX.level.LevelObject;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.Box2dStage;
import jedyobidan.game.moveX.lib.TextureManager;
import jedyobidan.game.moveX.player.Player;
import jedyobidan.game.moveX.ui.Dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Level extends Box2dStage implements Stringable {
	private Player player;
	private InputMultiplexer input;
	private InputMultiplexer ui;
	private Dialog dialog;
	private String tileset;
	public final TextureManager textures;

	public Level(SpriteBatch sb, ShapeRenderer sr) {
		super(sb, sr, 2);
		physics.setGravity(new Vector2(0, Const.GRAVITY));
		input = new InputMultiplexer();
		ui = new InputMultiplexer();
		input.addProcessor(ui);
		dialog = new Dialog(Gdx.graphics.getWidth());
		textures = new TextureManager(MoveX.ATLAS, "");
		addUIActor(dialog);
		processNewActors();
	}

	public void setPlayer(Player p, Vector2 start) {
		this.player = p;
		addGameActor(p);
		processNewActors();
		p.setCheckpoint(new Checkpoint(start.x, start.y));
		p.moveToCheckpoint();
	}
	
	
	public void setTileset(String tileset){
		this.tileset = tileset;
		background = textures.get(tileset + "/bg");
	}
	
	public TextureRegion getTile(String name){
		return textures.get(tileset + "/" + name);
	}
	
	public String getTileset(){
		return tileset;
	}

	@Override
	protected void render() {
		if(player != null){
			Body body = player.getPhysics().getBody();
			getCamera(getPhysicsCamera()).position.set(body.getPosition().x,
					body.getPosition().y, 0);
		}
		super.render();
	}

	@Override
	protected int chooseCamera(int group) {
		if (group == Const.ACT_GROUP_GAME) {
			return getPhysicsCamera();
		} else {
			return 0;
		}
	}

	@Override
	public void show() {
		super.show();
		Gdx.input.setInputProcessor(input);
	}

	@Override
	public void hide() {
		super.hide();
		textures.release();
		Gdx.input.setInputProcessor(null);
	}

	public void showDialog(String... text) {
		dialog.show(text);
	}

	public void setController(Controller c) {
		if (input.size() < 2) {
			input.addProcessor(c);
		} else {
			input.removeProcessor(1);
			input.addProcessor(c);
		}
	}
	
	public void addUIInput(InputProcessor input, boolean priority){
		if(priority){
			ui.addProcessor(0, input);
		} else {
			ui.addProcessor(input);
		}
	}


	@Override
	public String writeString() {
		StringBuilder ans = new StringBuilder(writeMeta());
		ans.append("--\n");
		for(Actor a: actors.get(Const.ACT_GROUP_GAME)){
			if(a instanceof LevelObject){
				ans.append(((LevelObject) a).writeString());
			}
		}
		return ans.toString();
	}

	@Override
	public void readString(String str) {
		str = str.replaceAll("\\#.*", "");
		String[] in = str.split("\\s*--\\s*");
		for(String objCode: in){
			objCode = objCode.trim();
			if(objCode.isEmpty()) continue;
			if(objCode.startsWith("$META;")){
				readMetaData(objCode);
			} else {
				try{
					addGameActor(LevelObject.constructObject(objCode));
				} catch (RuntimeException e){
					System.err.println("Could not parse code: \n" + objCode);
					e.printStackTrace();
				}
			}
		}
	}
	
	public void readMetaData(String meta){
		
	}
	
	public String writeMeta(){
		return "$META;\n";
	}
	
	public void removeUIInput(InputProcessor input){
		ui.removeProcessor(input);
	}

	public void addGameActor(Actor a) {
		addActor(Const.ACT_GROUP_GAME, a);
	}

	public void removeGameActor(Actor a) {
		removeActor(Const.ACT_GROUP_GAME, a);
	}

	public void addUIActor(Actor a) {
		addActor(Const.ACT_GROUP_UI, a);
	}

	public void removeUIActor(Actor a) {
		removeActor(Const.ACT_GROUP_UI, a);
	}
	
	public Player getPlayer(){
		return player;
	}
	

}
