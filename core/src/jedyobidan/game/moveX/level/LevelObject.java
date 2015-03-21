package jedyobidan.game.moveX.level;

import jedyobidan.game.moveX.Stringable;
import jedyobidan.game.moveX.lib.Actor;

public abstract class LevelObject extends Actor implements Stringable{
	public static LevelObject constructObject(String code){
		LevelObject object;
		String objType = code.substring(0, code.indexOf(";"));
		if(objType.equals("Rect")){
			object = new RectBlock();
		} else if (objType.equals("Plat")){
			object = new StaticPlatform();
		} else if (objType.equals("Checkpoint")){
			object = new Checkpoint();
		} else if (objType.equals("Tri")){
			object = new TriBlock();
		} else if (objType.equals("Blast")){
			object = new Blastzone();
		} else if (objType.equals("Sign")){
			object = new Sign();
		}
		
		else {
			throw new IllegalArgumentException("Cannot parse code for object type: " + objType);
		}
		object.readString(code);
		return object;
	}
}
