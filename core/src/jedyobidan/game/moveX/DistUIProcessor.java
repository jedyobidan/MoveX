package jedyobidan.game.moveX;

import jedyobidan.game.moveX.ui.ControlsMenu;
import jedyobidan.game.moveX.ui.PauseMenu;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;

public class DistUIProcessor extends InputAdapter{
	protected Level level;
	public DistUIProcessor(Level level){
		this.level = level;
	}
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ESCAPE) {
			level.addUIActor(new PauseMenu(level));
			return true;
		}

		else {
			return false;
		}
	}
}
