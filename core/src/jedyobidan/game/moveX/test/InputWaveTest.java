package jedyobidan.game.moveX.test;

import static org.junit.Assert.*;
import jedyobidan.game.moveX.InputWave;

import org.junit.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class InputWaveTest {

	@Test
	public void test() {
		InputWave test = new InputWave(0);
		test.keyDown(1);
		assertTrue(test.low());
		test.keyDown(0);
		assertTrue(test.high());
		assertTrue(test.posEdge());
		for(int i = 0; i < InputWave.LATENCY; i++){
			test.step();
		}
		assertFalse(test.posEdge());
		assertTrue(test.high());
		test.keyUp(1);
		assertTrue(test.high());
		test.keyUp(0);
		assertTrue(test.low());
		assertTrue(test.negEdge());
		for(int i = 0; i < InputWave.LATENCY; i++){
			test.step();
		}
		assertFalse(test.negEdge());
		assertTrue(test.low());
	}

}
