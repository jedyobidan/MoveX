package jedyobidan.game.moveX.actors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import jedyobidan.game.moveX.lib.Actor;

public class FlyingText extends Actor {
	private static final float TIME = 0.5f;
	
	private float time;
	@Override
	public void step(float delta){
		time += delta;
		if(time > TIME){
			stage.removeActor(this);
		}
	}
	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) {
		// TODO Auto-generated method stub

	}

}
