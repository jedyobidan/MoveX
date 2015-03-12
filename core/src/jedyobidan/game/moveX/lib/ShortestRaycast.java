package jedyobidan.game.moveX.lib;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

public abstract class ShortestRaycast implements RayCastCallback {
	public Vector2 normal;
	public float fraction = 1;

	public float reportRayFixture(Fixture fixture, Vector2 point,
			Vector2 normal, float fraction) {
		if(shouldCheck(fixture)) {
			if (fraction < this.fraction) {
				this.normal = new Vector2(normal);
				this.fraction = fraction;
			}
			return 1;
		} else {
			return -1;
		}
	}
	
	public abstract boolean shouldCheck(Fixture fix);
}
