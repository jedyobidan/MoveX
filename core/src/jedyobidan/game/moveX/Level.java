package jedyobidan.game.moveX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import jedyobidan.game.moveX.lib.Box2dStage;

public class Level extends Box2dStage {
	public Level(SpriteBatch sb, ShapeRenderer sr) {
		super(sb, sr);
		camera.setToOrtho(false, Gdx.graphics.getWidth() / MoveX.PIXELS_PER_METER, Gdx.graphics.getHeight() / MoveX.PIXELS_PER_METER);
		camera.position.set(0, 0, 0);
		physics.setGravity(new Vector2(0, -30f));
	}

}
