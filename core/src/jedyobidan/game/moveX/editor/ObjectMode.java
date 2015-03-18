package jedyobidan.game.moveX.editor;

import java.util.List;

import jedyobidan.game.moveX.level.Checkpoint;

import com.badlogic.gdx.math.Vector2;

public class ObjectMode extends PointSelection {

	public ObjectMode(LevelEditor editor) {
		super(editor, "checkpoint");
	}

	@Override
	public boolean pointSelected(List<Vector2> points) {
		if(type.equals("checkpoint")){
			Vector2 p = points.get(0);
			Checkpoint check = new Checkpoint(p.add(0, 0.75f));
			editor.getLevel().addGameActor(check);
			editor.log(String.format("check %.2f %.2f", p.x, p.y + 0.75f));
			return true;
		}
		
		return false;
	}

	@Override
	public String getName() {
		return "object";
	}

}
