package jedyobidan.game.moveX.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

import jedyobidan.game.moveX.Const;
import jedyobidan.game.moveX.Level;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.Stage;

public class Menu extends Actor implements InputProcessor {
	private int x, y, width;
	private String title;
	private boolean hidden;
	private List<Button> buttons;
	
	private static final int V_PADDING = 0, TOP_MARGIN = 32, BOTTOM_MARGIN = 16;

	public Menu(String title, int x, int y, int width) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.title = title;
		buttons = new ArrayList<Button>();
	}
	
	public Menu(String title, int x, int y){
		this(title, x, y, 320);
	}

	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) {
		if (hidden)
			return;
		
		TiledDrawable paper = new TiledDrawable(stage.textures.get("ui/bg"));
		TiledDrawable horz = new TiledDrawable(stage.textures.get("ui/dframe-horz"));
		TiledDrawable vert = new TiledDrawable(stage.textures.get("ui/dframe-vert"));
		float pipeWidth = stage.textures.get("ui/dframe-horz").getRegionHeight() / 2;
		float frameHeight = buttons.size() * (stage.textures.get("ui/button").getRegionHeight() + V_PADDING)
				+ TOP_MARGIN + BOTTOM_MARGIN;
		
		// Title Calcs
		TextureRegion leftBracket = stage.textures.get("ui/dframe-bracket");
		TextureRegion rightBracket = new TextureRegion(leftBracket);
		rightBracket.flip(true, false);
		BitmapFont titleFont = Const.Fonts.UI_LARGE;
		titleFont.setScale(1);
		titleFont.setColor(1, 1, 1, 1);
		TextBounds titleBounds = titleFont.getBounds(title);
		float bracketWidth = titleBounds.width + leftBracket.getRegionWidth() * 2;
		float bracketHeight = leftBracket.getRegionHeight();
		
		// Frame
		paper.draw(spriteRenderer, x - width/2, y - frameHeight, width, frameHeight);
		vert.draw(spriteRenderer, x - width/2 - pipeWidth, y - frameHeight + pipeWidth, 
				pipeWidth * 2, frameHeight - 2 * pipeWidth);
		vert.draw(spriteRenderer, x + width/2 - pipeWidth, y - frameHeight + pipeWidth, 
				pipeWidth * 2, frameHeight - 2 * pipeWidth);
		horz.draw(spriteRenderer, x - width/2 + pipeWidth, y - frameHeight - pipeWidth,
				width - 2 * pipeWidth, pipeWidth * 2);
		horz.draw(spriteRenderer, x - width/2 + pipeWidth, y - pipeWidth,
				width / 2 - bracketWidth/2 - pipeWidth, pipeWidth * 2);
		horz.draw(spriteRenderer, x + bracketWidth/2, y - pipeWidth,
				width / 2 - bracketWidth/2 - pipeWidth, pipeWidth * 2);
		spriteRenderer.draw(stage.textures.get("ui/dframe-topleft"),
				x - width/2 - pipeWidth, y - pipeWidth);
		spriteRenderer.draw(stage.textures.get("ui/dframe-topright"),
				x + width/2 - pipeWidth, y - pipeWidth);
		spriteRenderer.draw(stage.textures.get("ui/dframe-botleft"),
				x - width/2 - pipeWidth, y - frameHeight - pipeWidth);
		spriteRenderer.draw(stage.textures.get("ui/dframe-botright"),
				x + width/2 - pipeWidth, y - frameHeight - pipeWidth);
		
		// Title Draw
		paper.draw(spriteRenderer, x - bracketWidth/2 + pipeWidth, y-bracketHeight/2 + pipeWidth, 
				bracketWidth - pipeWidth * 2, bracketHeight - pipeWidth * 2);
		spriteRenderer.draw(leftBracket, x - bracketWidth/2, y - bracketHeight/2);
		spriteRenderer.draw(rightBracket, x + bracketWidth/2 - rightBracket.getRegionWidth(), y - bracketHeight/2);
		titleFont.draw(spriteRenderer, title, x - titleBounds.width/2, y + (titleBounds.height + titleFont.getAscent()) / 2);
		horz.draw(spriteRenderer, x - titleBounds.width/2, y + leftBracket.getRegionHeight()/2f - horz.getMinHeight(),
				titleBounds.width + 1, horz.getMinHeight());
		horz.draw(spriteRenderer, x - titleBounds.width/2, y - leftBracket.getRegionHeight()/2f,
				titleBounds.width + 1, horz.getMinHeight());
		
		// Buttons
		for(int i = 0; i < buttons.size(); i++){
			Bounds bounds = computeBounds(i);
			Button button = buttons.get(i);
			button.textures = stage.textures;
			button.setPosition(bounds.x + bounds.width/2, bounds.y + bounds.height/2);
			button.render(spriteRenderer, shapeRenderer);
		}
	}

	private Bounds computeBounds(int index) {
		Bounds ans = new Bounds();
		TextureRegion button = stage.textures.get("ui/button");
		ans.width = button.getRegionWidth();
		ans.height = button.getRegionHeight();
		ans.x = x - ans.width / 2;
		ans.y = y - TOP_MARGIN - (index + 1) * (V_PADDING + ans.height);
		
		return ans;
	}

	@Override
	public void onAdd(Stage s) {
		super.onAdd(s);
		s.addInput(this, true);
	}

	@Override
	public void onRemove(Stage s) {
		super.onRemove(s);
		s.removeInput(this);
	}
	
	public void addButton(Button b){
		buttons.add(b);
	}
	
	public void addButton(int index, Button b){
		buttons.add(index, b);
	}
	
	public void removeButton(Button b){
		buttons.remove(b);
	}
	
	public void removeButton(int index){
		buttons.remove(index);
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 position = stage.getCamera(0).unproject(new Vector3(screenX, screenY, 0));
		if(button == Input.Buttons.LEFT){
			for(int i = 0; i < buttons.size(); i++){
				if(computeBounds(i).inBounds(new Vector2(position.x, position.y))){
					buttons.get(i).mouseClick();
				}
			}
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 position = stage.getCamera(0).unproject(new Vector3(screenX, screenY, 0));
		if(button == Input.Buttons.LEFT){
			for(int i = 0; i < buttons.size(); i++){
				if(computeBounds(i).inBounds(new Vector2(position.x, position.y))){
					buttons.get(i).mouseRelease();
					return true;
				}
			}
		}
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		Vector3 position = stage.getCamera(0).unproject(new Vector3(screenX, screenY, 0));
		for(int i = 0; i < buttons.size(); i++){
			if(computeBounds(i).inBounds(new Vector2(position.x, position.y))){
				buttons.get(i).mouseEnter();
			} else {
				buttons.get(i).mouseExit();
			}
		}
		return true;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) { 
		return mouseMoved(screenX, screenY);
	}


	public void setHidden(boolean h) {
		this.hidden = h;
	}

	public boolean isHidden() {
		return hidden;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private static class Bounds {
		int x, y, width, height;

		public boolean inBounds(Vector2 point) {
			return point.x > x && point.x < x + width && point.y > y
					&& point.y < y + height;
		}
	}
	
	@Override
	public boolean keyDown(int keycode) { return true; }

	@Override
	public boolean keyUp(int keycode) {	return false;}

	@Override
	public boolean keyTyped(char character) { return false; }
	

	@Override
	public boolean scrolled(int amount) { return false; }

}
