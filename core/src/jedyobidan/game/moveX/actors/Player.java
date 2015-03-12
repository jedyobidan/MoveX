package jedyobidan.game.moveX.actors;

import jedyobidan.game.moveX.Controller;
import jedyobidan.game.moveX.Input;
import jedyobidan.game.moveX.InputWave;
import jedyobidan.game.moveX.Level;
import jedyobidan.game.moveX.MoveX;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.Stage;
import jedyobidan.game.moveX.lib.TextureManager;
import jedyobidan.game.moveX.player.PlayerPhysics;
import jedyobidan.game.moveX.player.PlayerProfile;
import jedyobidan.game.moveX.player.PlayerState;
import jedyobidan.game.moveX.player.state.FallState;
import jedyobidan.game.moveX.player.state.IdleState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Player extends Actor {
	private PlayerState state;
	private PlayerState nextState;

	// Player Data
	private Controller controller;
	private TextureManager textures;
	private PlayerProfile profile;

	// Player Physics
	private PlayerPhysics physics;

	public Player() {
		textures = new TextureManager(MoveX.ATLAS, "player");
		profile = new PlayerProfile();		
		controller = new Controller();
	}

	@Override
	public void step(float timeDelta) {
		if (nextState != null) {
			state = nextState;
		}
		physics.requestGroundNormal();
		if (physics.onGround()){
			profile.refreshStat("air_jumps");
			profile.refreshStat("air_dashes");
			physics.slopeGuard();
		}
		controller.step();
		state.step(timeDelta);
	}

	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRender) {
		state.render(spriteRenderer);
	}

	@Override
	public void addToStage(Stage s) {
		Level level = (Level) s;
		Gdx.input.setInputProcessor(controller);
		physics = new PlayerPhysics(this, level.getPhysics());
		physics.move(0, 0);
		level.addContactListener(physics);
		

		state = new FallState(this);
		state.init(null);
	}

	@Override
	public void removeFromStage(Stage s) {
		Level level = (Level) s;
		Gdx.input.setInputProcessor(null);
		level.getPhysics().destroyBody(physics.getBody());
		level.removeContactListener(physics);
	}

	@Override
	public float getZIndex() {
		return 1;
	}

	public boolean setState(PlayerState next) {
		if(next.valid(state)){
			state.destroy(next);
			next.init(state);
			nextState = next;
			return true;
		}
		return false;
	}
	
	// Physics Delegations
	public PlayerPhysics getPhysics(){
		return physics;
	}
	
	// Profile Delegations
	public PlayerProfile getProfile(){
		return profile;
	}
	
	// Controller Delegations
	public Controller getController(){
		return controller;
	}
	
	public TextureManager getTextures(){
		return textures;
	}

}
