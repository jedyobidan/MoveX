package jedyobidan.game.editor;

import jedyobidan.game.moveX.level.RectBlock;
import jedyobidan.game.moveX.level.StaticPlatform;
import jedyobidan.game.moveX.level.TriBlock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class ConstructMode extends Mode {
	private String type;
	private Vector2 point1;

	public ConstructMode(LevelEditor editor) {
		super(editor);
		type = "rect";
	}

	@Override
	public String getName() {
		return "construct";
	}

	@Override
	public boolean execCommand(String[] args) {
		if(args[0].equals("type")){
			if(isValidType(type)){
				type = args[1];
				editor.log("Type set to " + type);
				return true;
			} else {
				return false;
			}
		}
		
		return false;
	}
	
	public String getInfo(){
		StringBuilder ans = new StringBuilder();
		ans.append("Type:" + type + "\n");
		return ans.toString();
	}
	
	public void mouseClicked(float x, float y, int button){
		if(button == Input.Buttons.LEFT){
			if(point1 == null) {
				point1 = new Vector2(Math.round(x), Math.round(y));
			} else {
				construct(point1, new Vector2(Math.round(x), Math.round(y)));
				point1 = null;
			}
		} else {
			point1 = null;
		}
	}
	
	private void construct(Vector2 p1, Vector2 p2){
		if(type.equals("rect")){
			if(p1.x == p2.x || p1.y == p2.y){
				editor.log("Error: Rect must have positive area");
			} else {
				float cx = (p1.x + p2.x)/2;
				float cy = (p1.y + p2.y)/2;
				float w = Math.abs(p1.x - cx);
				float h = Math.abs(p1.y - cy);
				RectBlock rect = new RectBlock(cx, cy, Math.abs(p1.x - cx), Math.abs(p1.y - cy));
				editor.log(String.format("rect %.1f, %.1f, %.1f, %.1f", cx, cy, w, h));
				editor.getLevel().addGameActor(rect);
			}
		} else if (type.equals("plat")){
			if(p1.y != p2.y){
				editor.log("Error: Platform must be straight");
			} else {
				float cx = (p1.x+p2.x)/2;
				float hw = Math.abs(p1.x - cx);
				StaticPlatform plat = new StaticPlatform(cx, p1.y, hw);
				editor.log(String.format("plat %.1f, %.1f, %.1f", cx, p1.y, hw));
				editor.getLevel().addGameActor(plat);
			}
		} else if (type.equals("tri")){
			if(p1.y != p2.y){
				editor.log("Error: Triangle must be straight");
			} else if (Math.abs(p1.x -p2.x) % 2 != 0){
				editor.log("Error: Triangle must have even width");
			} else {
				float cx = (p1.x+p2.x)/2;
				float hw = Math.abs(p1.x - cx);
				boolean asc = p1.x < p2.x;
				TriBlock tri = new TriBlock(cx, p1.y, hw, asc);
				editor.log(String.format("tri %.1f, %.1f, %.1f, %s", cx, p1.y, hw, asc));
				editor.getLevel().addGameActor(tri);
			}
		}
	}
	
	public boolean isValidType(String type){
		return type.equals("rect") || type.equals("tri") || type.equals("plat");
	}
	
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer){
		spriteRenderer.end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(1, 1, 0, 0.5f);
		if(point1 != null)
			shapeRenderer.x(point1.x, point1.y, 0.2f);
		shapeRenderer.circle(Math.round(editor.getMousePosition().x), Math.round(editor.getMousePosition().y), 0.2f, 10);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		spriteRenderer.begin();
	}

}
