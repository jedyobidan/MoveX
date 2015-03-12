package jedyobidan.game.moveX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.lib.Box2dStage;

public class Level extends Box2dStage {
	public static final int GRAVITY = -30;
	private Player player;
	public Level(SpriteBatch sb, ShapeRenderer sr) {
		super(sb, sr);
		camera.setToOrtho(false, Gdx.graphics.getWidth() / Const.PIXELS_PER_METER, Gdx.graphics.getHeight() / Const.PIXELS_PER_METER);
		camera.position.set(0, 0, 0);
		physics.setGravity(new Vector2(0, GRAVITY));
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
		camera.position.set(body.getPosition().x, body.getPosition().y, 0);
		super.render();
	}
	

}
