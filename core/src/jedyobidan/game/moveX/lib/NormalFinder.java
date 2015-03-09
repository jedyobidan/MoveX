package jedyobidan.game.moveX.lib;

import jedyobidan.game.moveX.actors.SolidBlock;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

public class NormalFinder implements RayCastCallback {
	public Vector2 normal;
	private float fraction = 1;

	public NormalFinder() {
	}

	public float reportRayFixture(Fixture fixture, Vector2 point,
			Vector2 normal, float fraction) {
		Actor a = (Actor) fixture.getBody().getUserData();
		if (a instanceof SolidBlock) {
			if (fraction < this.fraction) {
				this.normal = new Vector2(normal);
				this.fraction = fraction;
			}
			return fraction;
		} else {
			return 1;
		}
	}
}
