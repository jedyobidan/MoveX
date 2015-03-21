package jedyobidan.game.moveX;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

import static jedyobidan.game.moveX.Input.*;

public class Controller extends InputAdapter{
	public Map<Input, InputWave> graphs;
	
	public Controller(){
		
		graphs = new HashMap<Input, InputWave>();
		graphs.put(LEFT, new InputWave(Keys.A));
		graphs.put(RIGHT, new InputWave(Keys.D));
		graphs.put(UP, new InputWave(Keys.W));
		graphs.put(DOWN, new InputWave(Keys.S));
		graphs.put(JUMP, new InputWave(Keys.K));
		graphs.put(ROPE, new InputWave(Keys.J));
		graphs.put(DASH, new InputWave(Keys.L));
	}
	
	public InputWave getWaveform(Input input){
		return graphs.get(input);
	}
	
	public void step(){
		for(InputWave graph: graphs.values()){
			graph.step();
		}
	}
	
	public Vector2 getDI() {
		Vector2 di = new Vector2();
		if(graphs.get(LEFT).high()){
			di.x--;
		}
		if(graphs.get(RIGHT).high()){
			di.x++;
		}
		if(graphs.get(UP).high()){
			di.y++;
		}
		if(graphs.get(DOWN).high()){
			di.y--;
		}
		return di;
	}

	@Override
	public boolean keyDown(int keycode) {
		boolean ans = false;
		for(InputWave graph: graphs.values()){
			ans |= graph.keyDown(keycode);
		}
		return ans;
	}

	@Override
	public boolean keyUp(int keycode) {
		boolean ans = false;
		for(InputWave graph: graphs.values()){
			ans |= graph.keyUp(keycode);
		}
		return ans;
	}
	
	public void configure(Input input, int keycode){
		graphs.get(input).setKey(keycode);
	}
	
	public int getKey(Input input){
		return graphs.get(input).getKey();
	}
}
