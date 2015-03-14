package jedyobidan.game.moveX.lib;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureManager {
	private String prefix;
	private HashMap<String, TextureRegion> textures;
	private TextureAtlas atlas;
	public TextureManager(TextureAtlas atlas, String prefix){
		this.prefix = prefix;
		this.textures = new HashMap<String, TextureRegion>();
		this.atlas = atlas;
	}
	
	public TextureRegion get(String name){
		if(!textures.containsKey(name)){
			textures.put(name, atlas.findRegion(prefix + name));
		}
		return textures.get(name);
	}
	
	public void release(){
		textures.clear();
	}
}
