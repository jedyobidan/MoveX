package jedyobidan.game.moveX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import jedyobidan.game.moveX.level.RectBlock;
import jedyobidan.game.moveX.level.StaticPlatform;
import jedyobidan.game.moveX.level.TriBlock;
import jedyobidan.game.moveX.lib.Actor;

public class Const {
	public static final float PIXELS_PER_METER = 20;

	public static final short CAT_PLAYER = 0x0001;
	public static final short CAT_ENVIRONMENT = 0x0002;
	public static final short CAT_NODE = 0x0004;
	public static final short CAT_HAZARD = 0x0008;
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
		public static final BitmapFont PIXEL, UI, UI_LARGE;
		static {
			PIXEL = new BitmapFont(Gdx.files.internal("fonts/pixel.fnt"));
			UI = new BitmapFont(Gdx.files.internal("fonts/ui.fnt"));
			UI_LARGE = new BitmapFont(Gdx.files.internal("fonts/ui_large.fnt"));
		}
		
		public static void dispose(){
			PIXEL.dispose();
			UI.dispose();
			UI_LARGE.dispose();
		}
	}
	
	public static class Tiles{
		public static final int
			SQ_CENTER = 0,
			SQ_TL = 1,
			SQ_T = 2,
			SQ_TR = 3,
			SQ_L = 4,
			SQ_R = 5,
			SQ_BL = 6,
			SQ_B = 7,
			SQ_BR = 8,
			SQ_T3 = 9,
			SQ_B3 = 10,
			SQ_L3 = 11,
			SQ_R3 = 12,
			SQ_SINGLE = 13,
			SQ_ASC = 14,
			SQ_DEC = 15,
			SQ_MAX = 16;
		
		public static final int
			PL_CENTER = 0,
			PL_L = 1,
			PL_R = 2,
			PL_MAX = 3;
		
		public static final int
			TR_ASC_S = 16,
			TR_ASC_L = 17,
			TR_DEC_L = 18,
			TR_DEC_S = 19,
			TR_MAX = 20;
	}
}
