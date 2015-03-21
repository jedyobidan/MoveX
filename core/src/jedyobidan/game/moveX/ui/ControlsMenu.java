package jedyobidan.game.moveX.ui;

import java.util.HashMap;

import jedyobidan.game.moveX.Controller;
import jedyobidan.game.moveX.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class ControlsMenu extends Menu implements ButtonListener{
	private HashMap<Button, Input> keyMap;
	private Button done;
	private Controller controller;
	private Input current;
	
	public ControlsMenu(Controller controller) {
		super("Controls", 0, Gdx.graphics.getHeight()/2 - 50);
		this.controller = controller;
		
		keyMap = new HashMap<Button, Input>();
		for(Input input: Input.values()){
			Button b = new Button("");
			b.addListener(this);
			keyMap.put(b, input);
			addButton(b);
		}
		setButtonText();
		
		done = new Button("Done");
		addButton(done);
	}
	
	@Override
	public void onClick(Button b) {
		setButtonText();
		current = keyMap.get(b);
		b.setText("Press a key");
		b.setDisabled(true);
		stage.addInput(new PressAKey(), true);
	}
	
	private void setButtonText(){
		for(Button button: keyMap.keySet()){
			button.setDisabled(false);
			button.setText(keyMap.get(button).name() + " : " + Keys.toString(controller.getKey(keyMap.get(button))));
		}
	}
	
	public void addDoneListener(ButtonListener listener){
		done.addListener(listener);
	}
	
	private class PressAKey extends InputAdapter{
		@Override
		public boolean keyDown(int keycode) {
			if(current != null){
				controller.configure(current, keycode);
				current = null;
				stage.removeInput(this);
				setButtonText();
				return true;
			} else {
				return false;
			}
		}
		
	}

}
