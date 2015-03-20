package jedyobidan.game.moveX;

import jedyobidan.game.moveX.level.Checkpoint;
import jedyobidan.game.moveX.level.LevelObject;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.Box2dStage;
import jedyobidan.game.moveX.player.Player;
import jedyobidan.game.moveX.ui.Dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Level extends Box2dStage implements Stringable {
	private Player player;
//	private InputMultiplexer ui;
	private Controller controller;
	private Dialog dialog;
	private String tileset;

	public Level(SpriteBatch sb, ShapeRenderer sr) {
		super(sb, sr, 2, MoveX.ATLAS);
		physics.setGravity(new Vector2(0, Const.GRAVITY));
//		ui = new InputMultiplexer();
//		addInput(ui, true);
		dialog = new Dialog(Gdx.graphics.getWidth());
		setTileset("cave");
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
	
	

	@Override
	public void setPaused(boolean paused) {
		super.setPaused(paused);
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

	public void showDialog(String... text) {
		dialog.show(text);
	}

	public void setController(Controller c) {
		if(controller != null)
			removeInput(controller);
		addInput(controller = c, false);
	}
	
//	public void addUIInput(InputProcessor input, boolean priority){
//		if(priority){
//			ui.addProcessor(0, input);
//		} else {
//			ui.addProcessor(input);
//		}
//	}

//	public void removeUIInput(InputProcessor input){
//		ui.removeProcessor(input);
//	}

	@Override
	public String writeString() {
		StringBuilder ans = new StringBuilder(writeMeta());
		ans.append("--\n");
		for(Actor a: actors.get(Const.ACT_GROUP_GAME)){
			if(a instanceof LevelObject){
				ans.append(((LevelObject) a).writeString());
				ans.append("--\n");
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
		String[] lines = meta.split(";\\s*");
		for(int i = 1; i < lines.length; i++){
			String[] arg = lines[i].split("\\s*=\\s*");
			if(arg[0].equals("tileset")){
				setTileset(arg[1]);
			}
		}
	}
	
	public String writeMeta(){
		StringBuilder ans = new StringBuilder("$META;\n");
		ans.append("tileset = " + tileset + "\n");
		return ans.toString();
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
	protected int chooseCamera(int group) {
		if (group == Const.ACT_GROUP_GAME) {
			return getPhysicsCamera();
		} else {
			return 0;
		}
	}

	@Override
	public void hide() {
		super.hide();
		textures.release();
	}

}
