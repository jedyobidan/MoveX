package jedyobidan.game.moveX.lib;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class JUtil {
	public static Animation animationFromSheet(TextureRegion spriteSheet,
			int rows, int cols, float frameTime) {
		TextureRegion[][] tmp = spriteSheet.split(spriteSheet.getRegionWidth()
				/ cols, spriteSheet.getRegionHeight() / rows);
		TextureRegion[] frames = new TextureRegion[rows * cols];
		int index = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				frames[index++] = tmp[i][j];
			}
		}
		return new Animation(frameTime, frames);
	}
}
