package jedyobidan.game.moveX;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;

public class DevUIProcessor extends InputAdapter {
	private Level level;
	public DevUIProcessor(Level level){
		this.level = level;
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
			level.showDialog("Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
					+ "Nulla maximus maximus placerat. Curabitur et orci vitae arcu "
					+ "consequat congue. Sed. ", 
					"Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis "
					+ "egestas. Phasellus pulvinar rutrum nisl. Proin euismod erat justo, nec pellentesque "
					+ "nulla tempus vel. Nam tristique nunc lacus, ut ornare sem fermentum eu. Ut vel accumsan "
					+ "odio, scelerisque convallis sapien. Nulla id mi turpis. Pellentesque ullamcorper sollicitudin "
					+ "enim, id iaculis augue elementum nec. Pellentesque in purus finibus, consectetur nisl id.",
					"Hello.");
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
