package jedyobidan.game.moveX.test;

import static org.junit.Assert.assertEquals;
import jedyobidan.game.moveX.Level;
import jedyobidan.game.moveX.MoveX;
import jedyobidan.game.moveX.actors.Player;

import org.junit.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Vector2;

public class LevelTest {

	@Test
	public void test() {
		// TODO figure out some way to initialize a level.
		Level test = new Level(null, null);
		Player player = new Player();
		test.setPlayer(player, new Vector2(0,0));
		test.gameLoop(0);
		assertEquals(new Vector2(0,0), player.getPhysics().getBody().getPosition());
	}

	

}
