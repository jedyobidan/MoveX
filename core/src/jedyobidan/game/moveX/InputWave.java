package jedyobidan.game.moveX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

public class InputWave extends InputAdapter {
	public static final int LATENCY = 2; // Number of ticks before positive edge no longer registers
	
	private int posEdge;
	private int negEdge;
	private int key;
	private boolean wave;
	public InputWave(int key){
		this.key = key;
	}
	
	public void step(){
		posEdge = Math.max(0, posEdge - 1);
		negEdge = Math.max(0, negEdge - 1);
	}
	
	public boolean posEdge(){
		return posEdge > 0;
	}
	
	public boolean negEdge(){
		return negEdge > 0;
	}
	
	public boolean high(){
		return wave;
	}
	public boolean low() {
		return !wave;
	}
	
	public void setKey(int key){
		this.key = key;
		posEdge = 0;
		negEdge = 0;
		wave = false;
	}
	
	public int getKey() {
		return key;
	}
	

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == key){
			posEdge = LATENCY;
			wave = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == key){
			negEdge = LATENCY;
			wave = false;
			return true;
		}
		return false;
	}



}
