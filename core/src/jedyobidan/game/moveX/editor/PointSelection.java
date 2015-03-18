package jedyobidan.game.moveX.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public abstract class PointSelection extends Mode {
	private List<Vector2> points;
	public final List<String> types;
	protected String type;
	
	public PointSelection(LevelEditor editor, String... types) {
		super(editor);
		this.types = Arrays.asList(types);
		points = new ArrayList<Vector2>();
		type = types[0];
	}
	
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer){
		spriteRenderer.end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(1, 1, 0, 0.5f);
		
		for(Vector2 p : points){
			shapeRenderer.x(p.x, p.y, 0.2f);
		}
		
		Vector2 mouse = editor.getMousePosition();
		shapeRenderer.circle(Math.round(mouse.x), Math.round(mouse.y), 0.2f, 10);
		
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		spriteRenderer.begin();
	}
	
	public void mouseClicked(float x, float y, int button){
		if(button == Input.Buttons.LEFT){
			points.add(new Vector2(Math.round(x), Math.round(y)));
			if(pointSelected(points)){
				points.clear();
			}
		} else if (button == Input.Buttons.RIGHT){
			points.clear();
		} 
	}
	
	@Override
	public boolean execCommand(String... args) {
		if(args[0].equals("type") || args[0].equals("t")){
			if(types.contains(args[1])){
				type = args[1];
				editor.log("Type set to " + type);
				points.clear();
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
	
	@Override
	public void keyPressed(int key) {
		int index = -1;
		if(key == Keys.E){
			index = (types.indexOf(type) + 1) % types.size();
		} else if (key == Keys.Q){
			index = (types.indexOf(type) - 1);
			if(index < 0){
				index += types.size();
			}
		}
		if(index >= 0){
			execCommand("t", types.get(index));
		}
	}
	
	public abstract boolean pointSelected(List<Vector2> points);

}
