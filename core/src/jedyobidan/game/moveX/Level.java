package jedyobidan.game.moveX;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import jedyobidan.game.moveX.actors.Checkpoint;
import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.actors.ui.Dialog;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.Box2dStage;

public class Level extends Box2dStage {
	private Player player;
	private InputMultiplexer input;
	private Dialog dialog;

	public Level(SpriteBatch sb, ShapeRenderer sr) {
		super(sb, sr, 2);
		physics.setGravity(new Vector2(0, Const.GRAVITY));
		input = new InputMultiplexer();
		input.addProcessor(new UIController());
		dialog = new Dialog(Gdx.graphics.getWidth());
		addUIActor(dialog);
	}

	public void setPlayer(Player p, Vector2 start) {
		this.player = p;
		addGameActor(p);
		processNewActors();
		p.setCheckpoint(new Checkpoint(start.x, start.y));
		p.moveToCheckpoint();
	}

	@Override
	protected void render() {
		Body body = player.getPhysics().getBody();
		getCamera(getPhysicsCamera()).position.set(body.getPosition().x,
				body.getPosition().y, 0);
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
		Gdx.input.setInputProcessor(null);
	}

	public void showDialog(String text) {
		setPaused(true);
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

	private class UIController extends InputAdapter {

		@Override
		public boolean keyDown(int keycode) {
			if (dialog.isOpen()) {
				dialog.hide();
				setPaused(false);
				return true;
			}
			if (keycode == Keys.ESCAPE) {
				// Go to menu
				// XXX: Temporary
				player.moveToCheckpoint();
				return true;
			} else if (keycode == Keys.TAB) {
				setDebug(!isDebug());
				return true;
			} else if (keycode == Keys.LEFT) {
				setSpeed(getSpeed() / 2);
				System.out.println("Speed = " + getSpeed());
				return true;
			} else if (keycode == Keys.RIGHT) {
				setSpeed(getSpeed() * 2);
				System.out.println("Speed = " + getSpeed());
				return true;
			} else if (keycode == Keys.NUM_1) {
				showDialog("Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
						+ "Nulla maximus maximus placerat. Curabitur et orci vitae arcu "
						+ "consequat congue. Sed. ");
				return true;
			}

			else {
				return false;
			}
		}

	}

}
