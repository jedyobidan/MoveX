package jedyobidan.game.editor;

import java.util.Set;

import jedyobidan.game.moveX.lib.Actor;

public class DestroyMode extends Mode {

	public DestroyMode(LevelEditor editor) {
		super(editor);
	}

	@Override
	public String getName() {
		return "destroy";
	}

	@Override
	public boolean execCommand(String[] args) {
		return false;
	}

	@Override
	public void mouseClicked(float x, float y, int button) {
		//FIXME: wont actually get interior of stuff
		Set<Actor> actors = editor.getLevel().getActorsAt(x, y);
		for(Actor a: actors){
			editor.getLevel().removeGameActor(a);
		}
	}

}
