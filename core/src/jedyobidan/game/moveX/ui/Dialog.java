package jedyobidan.game.moveX.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
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
import jedyobidan.game.moveX.lib.Box2dStage;
import jedyobidan.game.moveX.lib.Stage;
import jedyobidan.game.moveX.lib.TextureManager;

public class Dialog extends Actor implements InputProcessor{
	private float da;
	private float alpha;
	private float width;
	private float height;
	private float time;

	private String[] text;
	private int currentText;
	private TextureRegion bg, horz, vert, tl, tr, bl, br, arrow, square;

	private static final float V_PADDING = 20;
	private static final float H_PADDING = 15;
	private static final float SPEED = 5;
	private static final BitmapFont FONT = Const.Fonts.UI;
	private static final float ARROW_CYCLE = 0.8f, ARROW_AMP = 8;

	public Dialog(float width) {
		this.width = width - 10;

	}

	@Override
	public void onAdd(Stage s) {
		super.onAdd(s);
		Level level = (Level) s;
		bg = level.textures.get("ui/bg");
		horz = level.textures.get("ui/frame-horz");
		vert = level.textures.get("ui/frame-vert");
		tl = level.textures.get("ui/frame-topleft");
		tr = level.textures.get("ui/frame-topright");
		bl = level.textures.get("ui/frame-botleft");
		br = level.textures.get("ui/frame-botright");
		arrow = level.textures.get("ui/arrow-down");
		square = level.textures.get("ui/square");		
	}

	@Override
	public void step(float delta) {
		alpha += da * delta;
		alpha = Math.max(0, Math.min(1, alpha));
		time = (time + delta) % ARROW_CYCLE;
	}

	public void show(String... str) {
		this.text = str;
		this.currentText = 0;
		FONT.setScale(1);
		for (int i = 0; i < str.length; i++) {
			TextBounds bounds = FONT.getWrappedBounds(str[i], width - H_PADDING
					* 2);
			height = Math.max(height, bounds.height + V_PADDING * 2);
		}
		da = SPEED;
		((Level) stage).setPaused(true);
		((Level) stage).addInput(this, true);
	}

	public void hide() {
		da = -SPEED;
		((Level) stage).removeInput(this);
	}

	public boolean isOpen() {
		return da > 0;
	}

	public boolean keyDown(int keyCode) {
		if(alpha != 1) return true;
		if (currentText >= text.length - 1) {
			hide();
			((Level) stage).setPaused(false);
		} else {
			currentText++;
		}
		return true;
	}

	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) {
		if (alpha == 0)
			return;

		Vector2 position = new Vector2(-Gdx.graphics.getWidth() / 2 + 5,
				Gdx.graphics.getHeight() / 2 - height - 5);

		spriteRenderer.setColor(1, 1, 1, alpha);

		TiledDrawable tile = new TiledDrawable(bg);
		tile.draw(spriteRenderer, position.x, position.y, width, height);

		float frameSize = bl.getRegionHeight() / 2;

		// Frame Sides
		TiledDrawable horztile = new TiledDrawable(horz);
		horztile.draw(spriteRenderer, position.x + frameSize, position.y
				- frameSize, width - frameSize * 2, frameSize * 2);
		horztile.draw(spriteRenderer, position.x + frameSize, position.y
				+ height - frameSize, width - frameSize * 2, frameSize * 2);

		TiledDrawable vertile = new TiledDrawable(vert);
		vertile.draw(spriteRenderer, position.x - frameSize, position.y
				+ frameSize, frameSize * 2, height - frameSize * 2);
		vertile.draw(spriteRenderer, position.x + width - frameSize, position.y
				+ frameSize, frameSize * 2, height - frameSize * 2);

		spriteRenderer.draw(bl, position.x - bl.getRegionWidth() / 2,
				position.y - bl.getRegionHeight() / 2);
		spriteRenderer.draw(tl, position.x - bl.getRegionWidth() / 2,
				position.y + height - bl.getRegionHeight() / 2);
		spriteRenderer.draw(br, position.x + width - bl.getRegionWidth() / 2,
				position.y - bl.getRegionHeight() / 2);
		spriteRenderer.draw(tr, position.x + width - bl.getRegionWidth() / 2,
				position.y + height - bl.getRegionHeight() / 2);


		FONT.setScale(1);
		FONT.setColor(1, 1, 1, alpha);
		float textHeight = FONT.getWrappedBounds(text[currentText], width
				- H_PADDING * 2).height;

		FONT.drawWrapped(spriteRenderer, text[currentText], position.x
				+ H_PADDING,
				position.y + (height + textHeight) / 2 + FONT.getAscent(),
				width - H_PADDING * 2);

		// QED
		Vector2 qedPos = new Vector2(position.x + width - frameSize, position.y + frameSize);  
		if (currentText < text.length - 1) {
			qedPos.add(-arrow.getRegionWidth()/2, -arrow.getRegionHeight()/2);
			qedPos.add(0, (-Math.abs(time/ARROW_CYCLE - 0.5f)) * ARROW_AMP);
			spriteRenderer.draw(arrow, qedPos.x, qedPos.y);
		} else {
			qedPos.add(-square.getRegionWidth()/2, -square.getRegionHeight()/2);
			spriteRenderer.draw(square, qedPos.x, qedPos.y);
		}
		spriteRenderer.setColor(1, 1, 1, 1);

	}

	@Override
	public boolean keyUp(int keycode) { return false; }

	@Override
	public boolean keyTyped(char character) { return false; }

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }

	@Override
	public boolean mouseMoved(int screenX, int screenY) { return false; }

	@Override
	public boolean scrolled(int amount) { return false; }
}
