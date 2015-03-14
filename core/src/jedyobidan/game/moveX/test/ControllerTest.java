package jedyobidan.game.moveX.test;

import static org.junit.Assert.*;
import jedyobidan.game.moveX.Controller;

import org.junit.Test;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

public class ControllerTest {

	@Test
	public void testDI() {
		Controller test = new Controller();
		assertEquals(test.getDI(), new Vector2(0,0));
		test.keyDown(Keys.A);
		assertEquals(test.getDI(), new Vector2(-1, 0));
		test.keyDown(Keys.S);
		assertEquals(test.getDI(), new Vector2(-1, -1));
		test.keyDown(Keys.D);
		assertEquals(test.getDI(), new Vector2(0, -1));
		test.keyUp(Keys.A);
		assertEquals(test.getDI(), new Vector2(1, -1));
		test.keyUp(Keys.D);
		assertEquals(test.getDI(), new Vector2(0, -1));
		test.keyUp(Keys.S);
		assertEquals(test.getDI(), new Vector2(0, 0));
	}
	

}
