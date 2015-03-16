package jedyobidan.game.moveX;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import jedyobidan.game.moveX.level.Checkpoint;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.Box2dStage;
import jedyobidan.game.moveX.lib.TextureManager;
import jedyobidan.game.moveX.player.Player;
import jedyobidan.game.moveX.ui.Dialog;

public class Level extends Box2dStage {
	private Player player;
	private InputMultiplexer input;
	private Dialog dialog;
	public final TextureManager textures;

	public Level(SpriteBatch sb, ShapeRenderer sr) {
		super(sb, sr, 2);
		physics.setGravity(new Vector2(0, Const.GRAVITY));
		input = new InputMultiplexer();
		input.addProcessor(new UIController());
		dialog = new Dialog(Gdx.graphics.getWidth());
		textures = new TextureManager(MoveX.ATLAS, "");
		addUIActor(dialog);
	}

	public void setPlayer(Player p, Vector2 start) {
		this.player = p;
		addGameActor(p);
		processNewActors();
		p.setCheckpoint(new Checkpoint(start.x, start.y));
		p.moveToCheckpoint();
	}
	
	public void setBackground(String texture){
		background = textures.getLiteral(texture + "/bg.png");
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
				dialog.keyPressed();
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
						+ "consequat congue. Sed. ", 
						"Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis "
						+ "egestas. Phasellus pulvinar rutrum nisl. Proin euismod erat justo, nec pellentesque "
						+ "nulla tempus vel. Nam tristique nunc lacus, ut ornare sem fermentum eu. Ut vel accumsan "
						+ "odio, scelerisque convallis sapien. Nulla id mi turpis. Pellentesque ullamcorper sollicitudin "
						+ "enim, id iaculis augue elementum nec. Pellentesque in purus finibus, consectetur nisl id.",
						"Hello.");
						return true;
			} else if (keycode == Keys.ENTER){
				System.out.println(player.getPhysics().getBody().getPosition());
				return true;
			}

			else {
				return false;
			}
		}

	}

}
