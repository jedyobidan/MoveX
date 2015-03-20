package jedyobidan.game.moveX.editor;

import java.util.Arrays;
import java.util.List;

import jedyobidan.game.moveX.level.Blastzone;
import jedyobidan.game.moveX.level.RectBlock;

import com.badlogic.gdx.math.Vector2;

public class HazardMode extends PointSelection{
	public HazardMode(LevelEditor editor) {
		super(editor, "blast");
	}

	@Override
	public boolean pointSelected(List<Vector2> points) {
		if(type.equals("blast") && points.size() == 2){
			Vector2 p1 = points.get(0);
			Vector2 p2 = points.get(1);
			if(p1.x == p2.x || p1.y == p2.y){
				editor.log("Error: Blastzone must have positive area");
			} else {
				float cx = (p1.x + p2.x)/2;
				float cy = (p1.y + p2.y)/2;
				float w = Math.abs(p1.x - cx);
				float h = Math.abs(p1.y - cy);
				Blastzone blast = new Blastzone(cx, cy, Math.abs(p1.x - cx), Math.abs(p1.y - cy));
				editor.log(String.format("blast %.1f, %.1f, %.1f, %.1f", cx, cy, w, h));
				editor.getLevel().addGameActor(blast);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean execCommand(String... args) {
		if(super.execCommand(args)) {
			return true;
		} else {
			return false;	
		}
	}
	


	@Override
	public String getName() {
		return "hazard";
	}


}
