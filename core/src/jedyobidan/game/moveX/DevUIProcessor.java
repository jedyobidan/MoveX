package jedyobidan.game.moveX;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;

public class DevUIProcessor extends InputAdapter {
	private Level level;
	private String file;
	public DevUIProcessor(Level level, String file){
		this.level = level;
		this.file = file;
	}
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ESCAPE) {
			// Go to menu
			// XXX: Temporary
			level.getPlayer().moveToCheckpoint();
			return true;
		} else if (keycode == Keys.TAB) {
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
		}

		else {
			return false;
		}
	}

}
