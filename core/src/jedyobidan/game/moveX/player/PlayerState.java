package jedyobidan.game.moveX.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;

import jedyobidan.game.moveX.MoveX;
import jedyobidan.game.moveX.actors.Player;
import jedyobidan.game.moveX.lib.SpriteTransform;

public abstract class PlayerState {
	protected Player player;
	private Animation current;
	protected SpriteTransform transform;
	private float time;
	
	public PlayerState(Player p){
		this.player = p;
		transform = new SpriteTransform();
		transform.scale.set(1/MoveX.PIXELS_PER_METER, 1/MoveX.PIXELS_PER_METER);
	}

	public abstract boolean init(PlayerState prev);
	public void destroy(PlayerState next){ }
	
	public void render(SpriteBatch render){
		transform.position = new Vector2(player.getBody().getPosition());
		transform.rotation = player.getBody().getAngle();
		transform.texture = current.getKeyFrame(time);
		transform.flipX = player.getFacing();
		transform.render(render);
	}
	
	public void step(float delta){
		time = Math.max(0, time + delta);
		step(delta, time);
	}
	
	protected void setAnimation(Animation anim, int originX, int originY){
		current = anim;
		transform.origin.set(originX, originY);
		time = 0;
	}
	
	protected Animation getAnimation(){
		return current;
	}
	
	protected boolean isAnimationFinished(){
		return getAnimation().isAnimationFinished(time) && getAnimation().getPlayMode() != PlayMode.LOOP;
	}
	
	
	public abstract void step(float delta, float time);
}
