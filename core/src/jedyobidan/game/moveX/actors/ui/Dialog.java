package jedyobidan.game.moveX.actors.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

import jedyobidan.game.moveX.Const;
import jedyobidan.game.moveX.Level;
import jedyobidan.game.moveX.MoveX;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.Stage;
import jedyobidan.game.moveX.lib.TextureManager;

public class Dialog extends Actor {
	private float da;
	private float alpha;
	private float width;

	private String text;
	private TextBounds bounds;

	private TextureRegion bg, horz, vert, tl, tr, bl, br;

	private static final float V_PADDING = 20;
	private static final float H_PADDING = 15;
	private static final float SPEED = 5;
	private static final BitmapFont FONT = Const.Fonts.UI;

	public Dialog(float width) {
		this.width = width - 10;

	}

	@Override
	public void addToStage(Stage s) {
		super.addToStage(s);
		Level level = (Level) s;
		bg = level.textures.get("ui/bg");
		horz = level.textures.get("ui/frame-horz");
		vert = level.textures.get("ui/frame-vert");
		tl = level.textures.get("ui/frame-topleft");
		tr = level.textures.get("ui/frame-topright");
		bl = level.textures.get("ui/frame-botleft");
		br = level.textures.get("ui/frame-botright");
	}

	@Override
	public void step(float delta) {
		alpha += da * delta;
		alpha = Math.max(0, Math.min(1, alpha));
	}

	public void show(String str) {
		this.text = str;
		FONT.setScale(1);
		bounds = FONT.getWrappedBounds(str, width - H_PADDING * 2);
		da = SPEED;
	}

	public void hide() {
		da = -SPEED;
	}

	public boolean isOpen() {
		return da > 0;
	}
	
	public void keyPressed(){
		
	}

	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) {
		if (alpha == 0)
			return;
		float height = bounds.height + V_PADDING * 2;

		Vector2 position = new Vector2(-Gdx.graphics.getWidth() / 2 + 5, 
				Gdx.graphics.getHeight() / 2 - height - 5);

		spriteRenderer.setColor(1, 1, 1, alpha);

		TiledDrawable tile = new TiledDrawable(bg);
		tile.draw(spriteRenderer, position.x, position.y, width, height);
		
		float frameSize = bl.getRegionHeight()/2;

		// Frame Sides
		TiledDrawable horztile = new TiledDrawable(horz);
		horztile.draw(spriteRenderer, position.x + frameSize, position.y - frameSize, 
				width - frameSize * 2, frameSize * 2);
		horztile.draw(spriteRenderer, position.x + frameSize, position.y + height - frameSize, 
				width - frameSize * 2,	frameSize * 2);

		TiledDrawable vertile = new TiledDrawable(vert);
		vertile.draw(spriteRenderer, position.x - frameSize, position.y + frameSize,
				frameSize * 2, height - frameSize * 2);
		vertile.draw(spriteRenderer, position.x + width - frameSize, position.y + frameSize, 
				frameSize * 2, height - frameSize * 2);
		
		spriteRenderer.draw(bl, position.x - bl.getRegionWidth()/2, position.y - bl.getRegionHeight()/2);
		spriteRenderer.draw(tl, position.x - bl.getRegionWidth()/2, position.y + height - bl.getRegionHeight()/2);
		spriteRenderer.draw(br, position.x + width - bl.getRegionWidth()/2, position.y - bl.getRegionHeight()/2);
		spriteRenderer.draw(tr, position.x + width - bl.getRegionWidth()/2, position.y + height - bl.getRegionHeight()/2);
		
		spriteRenderer.setColor(1, 1, 1, 1);

		FONT.setScale(1);
		FONT.setColor(1, 1, 1, alpha);
		FONT.drawWrapped(spriteRenderer, text, position.x + H_PADDING,
				position.y + bounds.height + V_PADDING + FONT.getAscent(),
				width - H_PADDING * 2);

	}
}
