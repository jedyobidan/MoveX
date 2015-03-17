package jedyobidan.game.moveX.level;

import java.util.Arrays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import jedyobidan.game.moveX.Const;
import jedyobidan.game.moveX.Level;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.SpriteTransform;
import jedyobidan.game.moveX.lib.Stage;
import jedyobidan.game.moveX.lib.TextureManager;
import jedyobidan.game.moveX.player.PlayerPhysics;

public class TriBlock extends LevelObject{
	private Vector2 position;
	private float hwidth;
	private boolean ascending;
	private Body body;
	
	private int[][] tiles;
	
	public TriBlock(){
		this(0,0,0,true);
	}
	
	public TriBlock(float x, float y, float hwidth, boolean ascending){
		this.position = new Vector2().set(x, y);
		this.hwidth = hwidth;
		this.ascending = ascending;
		tiles = new int[(int) (hwidth)][(int) (hwidth * 2) + 1];
		tileDefaults();
	}
	
	public void tileDefaults(){
		if(tiles.length == 0) return;
		for(int y = 0; y < tiles.length; y++){
			if(ascending){
				tiles[y][0] = Const.Tiles.TR_ASC_S;
				tiles[y][1] = Const.Tiles.TR_ASC_L;
				tiles[y][2] = Const.Tiles.SQ_ASC;
				tiles[y][2 + 2 * y] = Const.Tiles.SQ_R;
			} else {
				tiles[y][0] = Const.Tiles.SQ_L;
				tiles[y][2 * y] = Const.Tiles.SQ_DEC;
				tiles[y][1 + 2 * y] = Const.Tiles.TR_DEC_L;
				tiles[y][2 + 2 * y] = Const.Tiles.TR_DEC_S;
			}
		}
		if(ascending){
			tiles[0][2] = Const.Tiles.SQ_TR;
		} else {
			tiles[0][0] = Const.Tiles.SQ_TL;
		}
	}
	
	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) {
		SpriteTransform transform = new SpriteTransform();
		transform.scale.set(1/Const.PIXELS_PER_METER, 1/Const.PIXELS_PER_METER);
		for(int y = 0; y < tiles.length; y++){
			for(int x = 0; x < 3 + y * 2; x++){
				int tile = tiles[y][x];
				if(tile < 16){ // HARD CODED
					transform.texture = ((Level) stage).getTile("square" + tile);
				} else {
					transform.texture = ((Level) stage).getTile("triangle" + (tile-16));
				}
				transform.position.y = position.y + hwidth - y - 1	;
				if(ascending){
					transform.position.x = (position.x + hwidth + 1) - (3 + y * 2) + x;
				} else {
					transform.position.x = (position.x - hwidth - 1) + x;
				}
				transform.render(spriteRenderer);
			}
		}
		
	}
	
	public void onAdd(Stage s){
		super.onAdd(s);
		Level level = (Level) s;
		
		BodyDef def = new BodyDef();
		def.position.set(position);
		def.type = BodyType.StaticBody;
		body = level.getPhysics().createBody(def);
		body.setUserData(this);
		
		EdgeShape edge = new EdgeShape();
		edge.setHasVertex0(true);
		edge.setHasVertex3(true);
		
		ChainShape chain = new ChainShape();
		Vector2[] vertices = new Vector2[4];
		if(ascending){
			vertices[0] = new Vector2(-hwidth + 0.5f, 0);
			vertices[1] = new Vector2(hwidth + 1f, 0);
			vertices[2] = new Vector2(hwidth + 1f, hwidth);
			vertices[3] = new Vector2(hwidth + 0.5f, hwidth);
		} else {
			vertices[0] = new Vector2(-hwidth - 1f, 0);
			vertices[1] = new Vector2(hwidth - 0.5f, 0);
			vertices[2] = new Vector2(-hwidth - 0.5f, hwidth);
			vertices[3] = new Vector2(-hwidth - 1f, hwidth);
		}
		chain.createLoop(vertices);
		
		FixtureDef fix = new FixtureDef();
		fix.friction = 1;
		fix.restitution = 0;
		fix.filter.categoryBits = Const.CAT_ENVIRONMENT;
		fix.shape = chain;
		body.createFixture(fix);
		
	}
	
	public void onRemove(Stage s){
		super.onRemove(s);
		Level level = (Level) s;
		level.getPhysics().destroyBody(this.body);
	}

	@Override
	public String writeString() {
		StringBuilder ans = new StringBuilder("Tri;\n");
		ans.append("    " + position.x + " " + position.y + " " + hwidth + " ");
		ans.append(ascending? 0.5f: -0.5f);
		ans.append(";\n");
		
		for(int y = 0; y < tiles.length; y++){
			ans.append("   ");
			for(int x = 0; x < 3 + y * 2; x++){
				ans.append(" " + tiles[y][x]);
			}
			ans.append("\n");
		}
		ans.insert(ans.length()-1, ";");
		return ans.toString();
	}

	@Override
	public void readString(String str) {
		String[] lines = str.split(";\\s*");
		String[] params = lines[1].split("\\s+");
		position.set(Float.parseFloat(params[0]), Float.parseFloat(params[1]));
		hwidth = Float.parseFloat(params[2]);
		ascending = Float.parseFloat(params[3]) > 0;
		tiles = new int[(int) (hwidth)][(int) (hwidth * 2) + 1];
		
		String[] grid = lines[2].split("\\s+");
		int i = 0;
		for(int y = 0; y < tiles.length; y++){
			for(int x = 0; x < 3 + 2 * y; x++){
				tiles[y][x] = Integer.parseInt(grid[i++]);
			}
		}
	}

}
