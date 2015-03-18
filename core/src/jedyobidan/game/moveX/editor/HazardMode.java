package jedyobidan.game.moveX.editor;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class HazardMode extends PointSelection{
	public HazardMode(LevelEditor editor) {
		super(editor, "blast");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean pointSelected(List<Vector2> points) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean execCommand(String... args) {
		super.execCommand(args);
		return false;
	}
	


	@Override
	public String getName() {
		return "hazard";
	}


}
