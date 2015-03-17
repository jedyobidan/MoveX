package jedyobidan.game.editor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import jedyobidan.game.moveX.Level;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.Stage;

public class LevelEditor extends Actor{
	private Level level;
	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onAdd(Stage s){
		level = (Level) s;
	}

}
