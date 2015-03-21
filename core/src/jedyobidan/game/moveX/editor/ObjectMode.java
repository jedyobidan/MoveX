package jedyobidan.game.moveX.editor;

import java.util.List;

import jedyobidan.game.moveX.level.Checkpoint;
import jedyobidan.game.moveX.level.Sign;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.math.Vector2;

public class ObjectMode extends PointSelection {

	public ObjectMode(LevelEditor editor) {
		super(editor, "checkpoint", "sign");
	}

	@Override
	public boolean pointSelected(List<Vector2> points) {
		if(type.equals("checkpoint")){
			Vector2 p = points.get(0);
			Checkpoint check = new Checkpoint(p.add(0, 0.75f));
			editor.getLevel().addGameActor(check);
			editor.log(String.format("check %.2f %.2f", p.x, p.y + 0.75f));
			return true;
		} else if (type.equals("sign")){
			final Vector2 p = points.get(0);
			TextInputListener listen = new TextInputListener(){
				@Override
				public void input(String text) {
					Sign sign = new Sign(p.x, p.y, text.split("\\s*\\|\\s*"));
					editor.getLevel().addGameActor(sign);
					editor.log(String.format("sign %.2f %.2f", p.x, p.y));
				}

				@Override
				public void canceled() { }
			};
			Gdx.input.getTextInput(listen, "Create Sign", "", "Sign text");
			return true;
		}
		
		return false;
	}

	@Override
	public String getName() {
		return "object";
	}

}
