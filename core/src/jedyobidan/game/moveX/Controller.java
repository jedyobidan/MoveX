package jedyobidan.game.moveX;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

import static jedyobidan.game.moveX.Input.*;

public class Controller extends InputAdapter{
	public Map<Input, InputGraph> graphs;
	
	public Controller(){
		
		graphs = new HashMap<Input, InputGraph>();
		graphs.put(LEFT, new InputGraph(Keys.A));
		graphs.put(RIGHT, new InputGraph(Keys.D));
		graphs.put(UP, new InputGraph(Keys.W));
		graphs.put(DOWN, new InputGraph(Keys.S));
		graphs.put(JUMP, new InputGraph(Keys.K));
		graphs.put(ROPE, new InputGraph(Keys.J));
		graphs.put(DASH, new InputGraph(Keys.L));
	}
	
	public InputGraph getInputGraph(Input input){
		return graphs.get(input);
	}
	
	public void step(){
		for(InputGraph graph: graphs.values()){
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
		for(InputGraph graph: graphs.values()){
			ans |= graph.keyDown(keycode);
		}
		return ans;
	}

	@Override
	public boolean keyUp(int keycode) {
		boolean ans = false;
		for(InputGraph graph: graphs.values()){
			ans |= graph.keyUp(keycode);
		}
		return ans;
	}
}
