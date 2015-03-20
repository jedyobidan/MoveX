package jedyobidan.game.moveX.ui;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import jedyobidan.game.moveX.Const;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.Stage;
import jedyobidan.game.moveX.lib.TextureManager;

public class Button extends Actor {
	private Set<ButtonListener> listeners;
	private int state;
	private int x, y;
	private String text;
	TextureManager textures;
	
	private static final int NONE = 0, HOVER = 1, ACTIVE = 2;
	
	public Button(String text, int x, int y){
		listeners = new HashSet<ButtonListener>();
		this.x = x;
		this.y = y;
		this.text = text;
		
	}
	
	public Button(String text){
		this(text, 0, 0);
	}
	
	@Override
	public void onAdd(Stage s){
		textures = s.textures;
	}
	
	public void addListener(ButtonListener b){
		listeners.add(b);
	}
	
	public void removeListener(ButtonListener b){
		listeners.remove(b);
	}
	
	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) {
		TextureRegion texture;
		if(state == NONE){
			texture = textures.get("ui/button");
		} else if (state == HOVER){
			texture = textures.get("ui/button-hover"); 
		} else {
			texture = textures.get("ui/button-active");
		}
		spriteRenderer.draw(texture, x - texture.getRegionWidth() / 2, y - texture.getRegionHeight() / 2);
		spriteRenderer.setColor(Color.WHITE);
		BitmapFont font = Const.Fonts.UI_LARGE;
		font.setScale(1);
		font.setColor(1, 1, 1, 1);
		TextBounds bounds = font.getBounds(text);
		font.draw(spriteRenderer, text, x - bounds.width / 2, y + (bounds.height + font.getAscent()) / 2);
	}
	
	public void mouseEnter(){
		if(state == NONE){
			state = HOVER;
		}
	}
	
	public void mouseExit(){
		state = NONE;
	}
	
	public void mouseClick(){
		state = ACTIVE;
	}
	
	public void mouseRelease(){
		if(state == ACTIVE){
			state = NONE;
			for(ButtonListener b: listeners){
				b.onClick(this);
			}
		}
	}
	
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public String getText(){
		return text;
	}
	
	public void setText(String text){
		this.text = text;
	}
}
