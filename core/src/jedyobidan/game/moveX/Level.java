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

import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.lib.Box2dStage;

public class Level extends Box2dStage {
	public static final int GRAVITY = -30;
	private Player player;
	private InputMultiplexer input;
	private Camera physicsCamera;
	
	
	public Level(SpriteBatch sb, ShapeRenderer sr) {
		super(sb, sr);
		physics.setGravity(new Vector2(0, GRAVITY));
		input = new InputMultiplexer();
		input.addProcessor(new UIController());
	}
	
	
	public void setPlayer(Player p, Vector2 start){
		this.player = p;
		addActor(p);
		processNewActors();
		p.getPhysics().move(start.x, start.y);
	}

	@Override
	protected void render() {
		Body body = player.getPhysics().getBody();
		getCamera(getPhysicsCamera()).position.set(body.getPosition().x, body.getPosition().y, 0);
		super.render();
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

	public void setController(Controller c){
		if(input.size() < 2){
			input.addProcessor(c);
		} else {
			input.removeProcessor(1);
			input.addProcessor(c);
		}
	}
	
	private class UIController extends InputAdapter{

		@Override
		public boolean keyDown(int keycode) {
			if(keycode == Keys.ESCAPE){
				// Go to menu
				// XXX: Temporary
				player.moveToCheckpoint();
				return true;
			} else if (keycode == Keys.TAB){
				setDebug(!isDebug());
			}
			return false;
		}
		
	}

}
