package jedyobidan.game.moveX.editor;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import jedyobidan.game.moveX.Const;
import jedyobidan.game.moveX.Level;
import jedyobidan.game.moveX.MoveX;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.Stage;

public class LevelEditor extends Actor implements InputProcessor{
	private Level level;
	private Vector2 mousePosition;
	private String file;
	private boolean gridOn;
	
	private StringBuilder console;
	private LinkedList<String> log;
	
	private Mode mode;
	
	public LevelEditor(String file){
		mousePosition = new Vector2(0,0);
		this.file = file;
		this.log = new LinkedList<String>();
		this.mode = new NoneMode(this);
		this.gridOn = true;
	}
	
	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) {
		BitmapFont font = Const.Fonts.PIXEL;
		spriteRenderer.end();
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.begin();
		if(gridOn){
			shapeRenderer.setColor(0.75f, 0.75f, 0.75f, 0.1f);
			for(int i = -1000; i <= 1000; i++){
				if(i == 0){
					shapeRenderer.setColor(1, 1, 0, 0.5f);
				} else if (i == 1){
					shapeRenderer.setColor(0.75f, 0.75f, 0.75f, 0.1f);
				}
				shapeRenderer.line(i, -1000, i, 1000);
				shapeRenderer.line(-1000, i , 1000, i);
			}
		}
		
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		spriteRenderer.begin();
		mode.render(spriteRenderer, shapeRenderer);
		
		level.pushMatrix();
		level.useCamera(0);
		
		// Info
		font.setScale(1);
		font.drawMultiLine(spriteRenderer, getInfo(), -Gdx.graphics.getWidth()/2 + 4, Gdx.graphics.getHeight()/2 - 2);
		
		// Console
		if(console != null){
			TextBounds bounds = font.getBounds(console);
			font.draw(spriteRenderer, console, -Gdx.graphics.getWidth()/2, 
					-Gdx.graphics.getHeight()/2 + bounds.height + 2);
		}
		
		// Log
		StringBuilder logString = new StringBuilder();
		for(String msg: log){
			logString.append(msg + "\n");
		}
		TextBounds bounds = font.getMultiLineBounds(logString.toString());
		font.drawMultiLine(spriteRenderer, logString.toString(),
				-Gdx.graphics.getWidth()/2, -Gdx.graphics.getHeight()/2 + bounds.height + 2 + font.getLineHeight());
		level.popMatrix();
	}
	
	public String getInfo(){
		StringBuilder ans = new StringBuilder();
		ans.append(file + "\n");
		ans.append("Mode:" + mode.getName()+ "\n");
		ans.append(mode.getInfo());
		// Mouse information
		ans.append(String.format("[%.2f:%.2f]\n", mousePosition.x, mousePosition.y));
		return ans.toString();
	}
	
	public void execCommand(String command){
		command = command.trim();
		String[] args = command.split("\\s+");
		try{
			if(args[0].equals("write") || args[0].equals("w")){
				String location;
				if(args.length == 1){
					location = this.file;
				} else {
					location = "level/" + args[1];
				}
				
				file = location;
				FileHandle file = Gdx.files.local("../core/assets/" + this.file);
				file.writeString(level.writeString(), false);
				log("Wrote level to " + file + " (" + file.length() + " bytes)");
				
			} else if (args[0].equals("open")){
				String location = "level/" + args[1];
				MoveX.GAME.editLevel(location);
			} else if (args[0].equals("new")){
				MoveX.GAME.editLevel("*new");
			} else if (args[0].equals("mode") || args[0].equals("m")){
				mode = Mode.construct(this, args[1]);
				
			}else if (!mode.execCommand(args)){
				throw new IllegalArgumentException(command + " is not a command");
			}
		} catch (Exception e){
			log("Error: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	public void log(String msg){
		log.addLast(msg);
		while(log.size() > 5){
			log.removeFirst();
		}
	}
	
	@Override
	public void onAdd(Stage s){
		level = (Level) s;
		level.addUIInput(this, true);
	}
	
	@Override
	public void onRemove(Stage s){
		level.removeUIInput(this);
	}
	
	@Override
	public float getZIndex(){
		return 1000;
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.TAB){
			gridOn = !gridOn;
			level.setDebug(!level.isDebug());
		}
		if(console != null){
			if(keycode == Keys.BACKSPACE){
				console.setLength(console.length() - 1);
				if(console.length() == 0){
					console = null;
				}
			} else if (keycode == Keys.ESCAPE){
				console = null;
			} else if (keycode == Keys.ENTER){
				execCommand(console.substring(1).trim().toLowerCase());
				console = null;
			}
		} else {
			OrthographicCamera cam = level.getCamera(level.getPhysicsCamera());
			if(keycode == Keys.A){
				cam.translate(-5, 0);
			} else if (keycode == Keys.S){
				cam.translate(0, -5);
			} else if (keycode == Keys.D){
				cam.translate(5, 0);
			} else if (keycode == Keys.W){
				cam.translate(0, 5);
			} else {
				mode.keyPressed(keycode);
			}
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if(character == ':' && console == null){
			console = new StringBuilder(":");
		} else if (console != null && !Character.isISOControl(character)) {
			console.append(character);
		}
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(button == Input.Buttons.BACK){
			execCommand("m c");
		} else if (button == Input.Buttons.FORWARD){
			execCommand("m d");
		} else if (button == Input.Buttons.MIDDLE){
			execCommand("m t");
		} else {
			OrthographicCamera cam = level.getCamera(level.getPhysicsCamera());
			Vector3 position = cam.unproject(new Vector3(screenX, screenY, 0));
			mode.mouseClicked(position.x, position.y, button);
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		OrthographicCamera cam = level.getCamera(level.getPhysicsCamera());
		Vector3 position = cam.unproject(new Vector3(screenX, screenY, 0));
		mousePosition.set(position.x, position.y);
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		if(amount > 0){
			level.getCamera(level.getPhysicsCamera()).zoom *= 2;
		} else {
			level.getCamera(level.getPhysicsCamera()).zoom /= 2;
		}
		return true;
	}
	
	public Level getLevel(){
		return level;
	}
	
	public Vector2 getMousePosition(){
		return mousePosition;
	}

}
