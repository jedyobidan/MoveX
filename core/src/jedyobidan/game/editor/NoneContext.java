package jedyobidan.game.editor;

public class NoneContext extends Context {

	public NoneContext(LevelEditor editor) {
		super(editor);
	}

	@Override
	public String getName() {
		return "none";
	}

	@Override
	public boolean execCommand(String[] args) {
		return false;
	}

}
