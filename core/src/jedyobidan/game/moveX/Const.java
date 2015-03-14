package jedyobidan.game.moveX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import jedyobidan.game.moveX.actors.RectBlock;
import jedyobidan.game.moveX.actors.StaticPlatform;
import jedyobidan.game.moveX.actors.TriBlock;
import jedyobidan.game.moveX.lib.Actor;

public class Const {
	public static final float PIXELS_PER_METER = 20;

	public static final short CAT_ENVIRONMENT = 0x0001;
	public static final short CAT_PLAYER = 0x0002;
	public static final short CAT_ENEMY = 0x0004;
	public static final int GRAVITY = -30;
	
	public static final int ACT_GROUP_GAME = 0;
	public static final int ACT_GROUP_UI = 1;
	

	
	public static boolean isGround(Actor a){
		return  a instanceof RectBlock | 
				a instanceof TriBlock |
				a instanceof StaticPlatform;
	}
	
	public static boolean isCeil(Actor a){
		return  a instanceof RectBlock |
				a instanceof TriBlock;
	}

	public static boolean isWall(Actor a) {
		return  a instanceof RectBlock |
				a instanceof TriBlock;
	}
	
	public static class Fonts {
		public static final BitmapFont PIXEL;
		public static final BitmapFont UI;
		static {
			PIXEL = new BitmapFont(Gdx.files.internal("fonts/pixel.fnt"));
			UI = new BitmapFont(Gdx.files.internal("fonts/ui.fnt"));
		}
	}
}
