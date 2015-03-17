package jedyobidan.game.editor;

import jedyobidan.game.moveX.level.RectBlock;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class ConstructContext extends Context {
	private String type;
	private Vector2 point1;

	public ConstructContext(LevelEditor editor) {
		super(editor);
		type = "rect";
	}

	@Override
	public String getName() {
		return "construct " + type;
	}

	@Override
	public boolean execCommand(String[] args) {
		if(args[0].equals("type")){
			if(isValidType(type)){
				type = args[1];
				return true;
			} else {
				return false;
			}
		}
		
		return false;
	}
	
	public void mouseClicked(float x, float y){
		if(point1 == null) {
			point1 = new Vector2(Math.round(x), Math.round(y));
		} else {
			construct(point1, new Vector2(Math.round(x), Math.round(y)));
			point1 = null;
		}
	}
	
	private void construct(Vector2 p1, Vector2 p2){
		if(type.equals("rect")){
			float cx = (p1.x + p2.x)/2;
			float cy = (p1.y + p2.y)/2;
			RectBlock rect = new RectBlock(cx, cy, Math.abs(p1.x - cx), Math.abs(p1.y - cy));
			editor.getLevel().addGameActor(rect);
		}
	}
	
	public boolean isValidType(String type){
		return type.equals("rect") || type.equals("tri") || type.equals("plat");
	}
	
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer){
		if(point1 == null) return;
		spriteRenderer.end();
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.circle(point1.x, point1.y, 0.2f, 10);
		shapeRenderer.end();
		spriteRenderer.begin();
	}

}
