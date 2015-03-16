package jedyobidan.game.moveX.lib;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Actor {
	protected Stage stage;
	public void step(float timeDelta) { }
	/**
	 * If you plan on using the shapeRenderer, you must call spriteRenderer.end and shapeRenderer.begin,
	 * then restore the state of the renderers by the end of the method.
	 * @param shapeRenderer
	 * @param spriteRenderer
	 */
	public abstract void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer);
	public float getZIndex() { return 0; }
	public void onAdd(Stage s) {
		stage = s;
	}	
	public void onRemove(Stage s) {
		stage = null;
	}
}
