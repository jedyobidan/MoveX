package jedyobidan.game.moveX;

import jedyobidan.game.moveX.actors.RectBlock;
import jedyobidan.game.moveX.actors.StaticPlatform;
import jedyobidan.game.moveX.actors.TriBlock;
import jedyobidan.game.moveX.lib.Actor;

public class Const {
	public static final float PIXELS_PER_METER = 20;

	public static final short CAT_ENVIRONMENT = 0x0001;
	public static final short CAT_PLAYER = 0x0002;
	public static final short CAT_ENEMY = 0x0004;
	

	
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
}
