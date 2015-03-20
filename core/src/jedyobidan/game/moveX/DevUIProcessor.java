package jedyobidan.game.moveX;

import jedyobidan.game.moveX.ui.Button;
import jedyobidan.game.moveX.ui.ButtonListener;
import jedyobidan.game.moveX.ui.Menu;
import jedyobidan.game.moveX.ui.PauseMenu;

import com.badlogic.gdx.Gdx;
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
			level.addUIActor(new PauseMenu(level));
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
		} else if (keycode == Keys.NUM_0){
			final Menu menu = new Menu("Menu", 0, Gdx.graphics.getHeight()/2 - 64);
			ButtonListener l = new ButtonListener(){
				public void onClick(Button b) {
					System.out.println("Clicked " + b.getText());
					level.removeUIActor(menu);
				}
			};
			for(int i =0; i < 5; i++){
				Button b = new Button("Button " + i, 0, 0);
				b.addListener(l);
				menu.addButton(b);
			}
			level.addUIActor(menu);
			return true;
		}

		else {
			return false;
		}
	}

}
