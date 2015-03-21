package jedyobidan.game.moveX;

import jedyobidan.game.moveX.ui.Button;
import jedyobidan.game.moveX.ui.ButtonListener;
import jedyobidan.game.moveX.ui.ControlsMenu;
import jedyobidan.game.moveX.ui.Menu;
import jedyobidan.game.moveX.ui.PauseMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;

public class DevUIProcessor extends DistUIProcessor {
	private String file;
	public DevUIProcessor(Level level, String file){
		super(level);
		this.file = file;
	}
	@Override
	public boolean keyDown(int keycode) {
		if(super.keyDown(keycode)){
			return true;
		}
		
		if (keycode == Keys.TAB) {
			level.setDebug(!level.isDebug());
			return true;
		} else if (keycode == Keys.LEFT) {
			level.setSpeed(level.getSpeed() / 2);
			System.out.println("Speed = " + level.getSpeed());
			return true;
		} else if (keycode == Keys.RIGHT) {
			level.setSpeed(level.getSpeed() * 2);
			System.out.println("Speed = " + level.getSpeed());
			return true;
		} else if (keycode == Keys.NUM_1) {
			MoveX.GAME.editLevel(file);
			return true;
		} else if (keycode == Keys.ENTER){
			System.out.println(level.getPlayer().getPhysics().getBody().getPosition());
			return true;
		} else if (keycode == Keys.NUM_0){
			ControlsMenu menu = new ControlsMenu(level.getPlayer().getController());
			level.addUIActor(menu);
			return true;
		}

		else {
			return false;
		}
	}

}
