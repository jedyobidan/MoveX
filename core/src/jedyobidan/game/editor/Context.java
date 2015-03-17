package jedyobidan.game.editor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Context {
	protected LevelEditor editor;
	
	public Context(LevelEditor editor){
		this.editor = editor;
	}
	
	public abstract String getName();
	public abstract boolean execCommand(String[] args);
	
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) { }
	public void mouseClicked(float x, float y){ }
	
	public static Context construct(LevelEditor editor, String name){
		if(name.equals("none")){
			return new NoneContext(editor);
		} else if (name.equals("construct")){
			return new ConstructContext(editor);
		}
		throw new IllegalArgumentException("No such context: " + name);
	}
}
