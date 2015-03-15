package jedyobidan.game.moveX.lib;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureManager {
	private String prefix;
	private HashMap<String, TextureRegion> textures;
	private HashMap<String, Texture> literals;
	private TextureAtlas atlas;
	public TextureManager(TextureAtlas atlas, String prefix){
		this.prefix = prefix;
		this.textures = new HashMap<String, TextureRegion>();
		this.literals = new HashMap<String, Texture>();
		this.atlas = atlas;
	}
	
	public TextureRegion get(String name){
		if(!textures.containsKey(name)){
			textures.put(name, atlas.findRegion(prefix + name));
		}
		return textures.get(name);
	}
	
	public Texture getLiteral(String name){
		if(!literals.containsKey(name)){
			literals.put(name, new Texture(Gdx.files.internal("img/" + name)));
		}
		return literals.get(name);
	}
	
	public void release(){
		textures.clear();
		for(Texture t: literals.values()){
			t.dispose();
		}
		literals.clear();
	}
}
