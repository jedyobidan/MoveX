package jedyobidan.game.moveX.editor;

public class NoneMode extends Mode {

	public NoneMode(LevelEditor editor) {
		super(editor);
	}

	@Override
	public String getName() {
		return "none";
	}

	@Override
	public boolean execCommand(String... args) {
		return false;
	}

}
