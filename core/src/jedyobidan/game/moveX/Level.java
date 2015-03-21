package jedyobidan.game.moveX;

import jedyobidan.game.moveX.level.Checkpoint;
import jedyobidan.game.moveX.level.LevelObject;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.Box2dStage;
import jedyobidan.game.moveX.player.Player;
import jedyobidan.game.moveX.ui.Dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Level extends Box2dStage implements Stringable {
	private Player player;
	private Controller controller;
	private Dialog dialog;
	private String tileset;
	
	private float lockLeft, lockRight, lockTop, lockBottom;

	public Level(SpriteBatch sb, ShapeRenderer sr) {
		super(sb, sr, 2, MoveX.ATLAS);
		physics.setGravity(new Vector2(0, Const.GRAVITY));
		dialog = new Dialog(Gdx.graphics.getWidth());
		setTileset("cave");
		addUIActor(dialog);
		processNewActors();
		lockLeft = lockBottom = 1000;
		lockRight = lockTop = -1000;
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
			OrthographicCamera cam = getCamera(getPhysicsCamera());
			cam.position.x = Math.max(lockLeft + cam.viewportWidth/2,
							 Math.min(lockRight - cam.viewportWidth/2,
							 body.getPosition().x));
			cam.position.y = Math.max(lockBottom + cam.viewportHeight/2,
					 Math.min(lockTop - cam.viewportHeight/2,
					 body.getPosition().y));
		}
		super.render();
		if(isDebug()){
			useCamera(getPhysicsCamera());
			Gdx.gl.glEnable(GL20.GL_BLEND);
		    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			shapeRender.begin(ShapeType.Line);
			shapeRender.setColor(1, 0, 1, 0.5f);
			shapeRender.rect(lockLeft, lockBottom, lockRight - lockLeft, lockTop - lockBottom);
			shapeRender.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
		}
	}

	public void showDialog(String... text) {
		dialog.show(text);
	}

	public void setController(Controller c) {
		if(controller != null)
			removeInput(controller);
		addInput(controller = c, false);
	}

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
	
	private String writeMeta(){
		StringBuilder ans = new StringBuilder("$META;\n");
		ans.append("tileset = " + tileset + ";\n");
		ans.append(String.format("camlock = %.2f %.2f %.2f %.2f;\n", lockTop, lockLeft, lockRight, lockBottom));
		return ans.toString();
	}
	
	private void readMetaData(String meta){
		String[] lines = meta.split(";\\s*");
		for(int i = 1; i < lines.length; i++){
			String[] arg = lines[i].split("\\s*=\\s*");
			if(arg[0].equals("tileset")){
				setTileset(arg[1]);
			} else if (arg[0].equals("camlock")){
				String[] params = arg[1].split("\\s+");
				lockTop = Float.parseFloat(params[0]);
				lockLeft = Float.parseFloat(params[1]);
				lockRight = Float.parseFloat(params[2]);
				lockBottom = Float.parseFloat(params[3]);
			}
		}
	}
	
	public void setCamlock(float lockTop, float lockLeft, float lockRight, float lockBottom){
		this.lockLeft = lockLeft;
		this.lockRight = lockRight;
		this.lockTop = lockTop;
		this.lockBottom = lockBottom;
	}
	

	public void lockLeft(float lockLeft) {
		this.lockLeft = lockLeft;
	}

	public void lockRight(float lockRight) {
		this.lockRight = lockRight;
	}

	public void lockTop(float lockTop) {
		this.lockTop = lockTop;
	}

	public void lockBottom(float lockBottom) {
		this.lockBottom = lockBottom;
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
