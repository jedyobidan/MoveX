package jedyobidan.game.moveX.actors.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

import jedyobidan.game.moveX.Const;
import jedyobidan.game.moveX.lib.Actor;

public class Dialog extends Actor{
	private float da;
	private float alpha;
	private float width;
	
	private String text;
	private TextBounds bounds;
	
	private static final float V_PADDING = 20;
	private static final float H_PADDING = 10;
	private static final float SPEED = 5;
	private static final BitmapFont FONT = Const.Fonts.UI;
	
	public Dialog(float width){
		this.width = width;
		
	}
	
	@Override
	public void step(float delta){
		alpha += da * delta;
		alpha = Math.max(0, Math.min(1, alpha));
	}
	
	public void show(String str){
		this.text = str;
		FONT.setScale(1);
		bounds = FONT.getWrappedBounds(str, width - H_PADDING * 2);
		da = SPEED;
	}
	
	public void hide(){
		da = -SPEED;
	}
	
	public boolean isOpen(){
		return da > 0;
	}
	
	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) {
		if(alpha == 0) return;
		float height = bounds.height + V_PADDING * 2;
		
		Vector2 position = new Vector2(-Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2 - height);
		
		spriteRenderer.end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.begin(ShapeType.Filled);
		
		shapeRenderer.setColor(0.7f, 0.6f, 0.4f, alpha);
		shapeRenderer.rect(position.x, position.y, width, height);
		shapeRenderer.setColor(Color.WHITE);
		
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		spriteRenderer.begin();
		
		FONT.setScale(1);
		FONT.setColor(1, 1, 1, alpha);
		FONT.drawWrapped(spriteRenderer, text, position.x + H_PADDING, 
				position.y + bounds.height + V_PADDING + FONT.getAscent(), 
				width - H_PADDING * 2);
		
	}
}
