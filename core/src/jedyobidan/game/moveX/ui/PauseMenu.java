package jedyobidan.game.moveX.ui;

import jedyobidan.game.moveX.Level;
import jedyobidan.game.moveX.lib.Stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class PauseMenu extends Menu {
	private Level level;
	private ControlsMenu controlMenu;
	public PauseMenu(Level l){
		super("Paused", 0, Gdx.graphics.getHeight()/2 - 50);
		this.level = l;
		this.controlMenu = new ControlsMenu(l.getPlayer().getController());
		Button checkpoint = new Button("Last Checkpoint", 0, 0);
		checkpoint.addListener(new ButtonListener(){
			public void onClick(Button b) {
				level.getPlayer().moveToCheckpoint();
				level.removeUIActor(PauseMenu.this);
			}
		});
		addButton(checkpoint);
		
		Button controls = new Button("Controls", 0, 0);
		controls.addListener(new ButtonListener(){
			@Override
			public void onClick(Button b) {
				setHidden(true);
				level.addUIActor(controlMenu);
			}			
		});
		addButton(controls);
		
		controlMenu.addDoneListener(new ButtonListener(){
			@Override
			public void onClick(Button b) {
				setHidden(false);
				level.removeUIActor(controlMenu);
			}
		});
		
		Button close = new Button("Resume", 0, 0);
		close.addListener(new ButtonListener(){
			public void onClick(Button b) {
				level.removeUIActor(PauseMenu.this);
			}
		});
		addButton(close);
	}
	
	@Override
	public void onAdd(Stage s){
		super.onAdd(s);
		((Level) s).setPaused(true);
	}
	
	@Override
	public void onRemove(Stage s){
		super.onRemove(s);
		((Level) s).setPaused(false);
	}
	
	@Override
	public boolean keyDown(int key){
		if(key == Keys.ESCAPE){
			level.removeUIActor(this);
			return true;
		}
		return super.keyDown(key);
	}
}
