package jedyobidan.game.moveX.ui;

import jedyobidan.game.moveX.Const;
import jedyobidan.game.moveX.lib.Actor;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class FlyingText extends Actor {
	private static final float TIME = 0.5f;	
	private float time;
	private String text;
	private Vector2 position;
	
	public FlyingText(String text, float x, float y){
		this.text = text;
		position = new Vector2().set(x,y);
	}
	@Override
	public void step(float delta){
		time += delta;
		if(time > TIME){
			stage.removeActor(Const.ACT_GROUP_GAME, this);
		}
	}
	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) {
		float dy;
		float alpha;
		if(time < TIME/4){
			dy =  - 1 + time / (TIME/4);
			alpha = time / (TIME/4);
		} else if (time > TIME * 3/4){
			dy = -1 + (TIME - time) / (TIME/4);
			alpha = Math.max(0, (TIME-time) / (TIME/4));
		} else {
			dy = 0;
			alpha = 1;
		}
		
		
		
		BitmapFont sans = Const.Fonts.PIXEL;
		sans.setColor(1, 1, 1, alpha);
		sans.setScale(1/Const.PIXELS_PER_METER);
		TextBounds bounds = sans.getBounds(text);
		sans.setUseIntegerPositions(false);
		sans.draw(spriteRenderer, text, position.x - bounds.width/2, 
				position.y + bounds.height/2 + dy);
	}
	
	public float getZIndex(){
		return -1;
	}

}
