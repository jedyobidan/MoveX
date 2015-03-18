package jedyobidan.game.moveX.editor;

import java.util.Arrays;
import java.util.List;

import jedyobidan.game.moveX.level.RectBlock;
import jedyobidan.game.moveX.level.StaticPlatform;
import jedyobidan.game.moveX.level.TriBlock;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

public class ConstructMode extends PointSelection {

	public ConstructMode(LevelEditor editor) {
		super(editor, "rect", "plat", "tri");
	}

	
	@Override
	public boolean pointSelected(List<Vector2> points) {
		if(points.size() == 2){
			construct(points.get(0), points.get(1));
			return true;
		} else {
			return false;
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
	

	@Override
	public String getName() {
		return "construct";
	}
}
