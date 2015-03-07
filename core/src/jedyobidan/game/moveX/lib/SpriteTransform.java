package jedyobidan.game.moveX.lib;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class SpriteTransform {
	public Vector2 origin = new Vector2(0, 0); // origin relative to lower-left
												// corner of texture
	public Vector2 position = new Vector2(0, 0); // location to render in world space
	public Vector2 scale = new Vector2(1, 1);
	public float rotation;
	public TextureRegion texture;
	public boolean flipX, flipY;

	public void render(SpriteBatch render) {
		render.draw(texture, position.x - origin.x, position.y - origin.y, 
				origin.x, origin.y,
				texture.getRegionWidth(), texture.getRegionHeight(), 
				flipX ? -scale.x : scale.x,
				flipY ? -scale.y: scale.y, 
				(float) Math.toDegrees(rotation));

	}
}
