package jedyobidan.game.editor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Mode {
	protected LevelEditor editor;
	
	public Mode(LevelEditor editor){
		this.editor = editor;
	}
	
	public abstract String getName();
	public abstract boolean execCommand(String... args);
	
	public String getInfo(){ return ""; }
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) { }
	public void mouseClicked(float x, float y, int button){ }
	
	public static Mode construct(LevelEditor editor, String name){
		if(name.equals("none")){
			return new NoneMode(editor);
		} else if (name.equals("construct") || name.equals("c")){
			return new ConstructMode(editor);
		} else if (name.equals("destroy") || name.equals("d")){
			return new DestroyMode(editor);
		} else if (name.equals("tile") || name.equals("t")){
			return new TileMode(editor);
		}
		throw new IllegalArgumentException("No such context: " + name);
	}
}
