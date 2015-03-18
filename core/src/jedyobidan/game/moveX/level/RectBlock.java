package jedyobidan.game.moveX.level;

import java.util.Arrays;

import jedyobidan.game.moveX.Const;
import jedyobidan.game.moveX.Level;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.SpriteTransform;
import jedyobidan.game.moveX.lib.Stage;
import jedyobidan.game.moveX.lib.TextureManager;
import jedyobidan.game.moveX.player.PlayerPhysics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class RectBlock extends LevelObject implements Tileable{
	private Vector2 position;
	private float hwidth, hheight;
	private Body body;

	private int[][] tiles;

	public RectBlock(Vector2 position, float hwidth, float hheight) {
		this(position.x, position.y, hwidth, hheight);
	}

	public RectBlock(float x, float y, float hwidth, float hheight) {
		this.position = new Vector2().set(x, y);
		this.hwidth = hwidth;
		this.hheight = hheight;
		this.tiles = new int[(int) (hheight * 2)][(int) (hwidth * 2)];
		tileDefaults();
	}
	
	public RectBlock(){
		this(0, 0, 0, 0);
	}
	
	@Override
	public void tileDefaults(){
		if(tiles.length == 0) return;
		for(int y = 1; y < tiles.length - 1; y++){
			tiles[y][0] = Const.Tiles.SQ_L;
			tiles[y][tiles[y].length - 1] = Const.Tiles.SQ_R;
		}
		for(int x = 1; x < tiles[0].length - 1; x++){
			tiles[tiles.length - 1][x] = Const.Tiles.SQ_B;
			tiles[0][x] = Const.Tiles.SQ_T;
		}
		
		if(tiles.length == 1 && tiles[0].length == 1){
			tiles[0][0] = Const.Tiles.SQ_SINGLE;
		} else if(tiles.length == 1){
			tiles[0][0] = Const.Tiles.SQ_L3;
			tiles[0][tiles[0].length - 1] = Const.Tiles.SQ_R3;
		} else if (tiles[0].length == 1){
			tiles[0][0] = Const.Tiles.SQ_T3;
			tiles[tiles.length - 1][0] = Const.Tiles.SQ_B3;
		} else {
			tiles[0][0] = Const.Tiles.SQ_TL;
			tiles[0][tiles[0].length - 1] = Const.Tiles.SQ_TR;
			tiles[tiles.length - 1][0] = Const.Tiles.SQ_BL;
			tiles[tiles.length - 1][tiles[0].length - 1] = Const.Tiles.SQ_BR;
		}
	}


	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) {
		SpriteTransform transform = new SpriteTransform();
		transform.scale.set(1/Const.PIXELS_PER_METER, 1/Const.PIXELS_PER_METER);
		Vector2 pos = body.getPosition();
		for(float y = pos.y - hheight; y < pos.y + hheight; y++){
			for(float x = pos.x - hwidth; x < pos.x + hwidth; x++){
				transform.position.set(x, y);
				int tile = getTile(x, y + 1);
				transform.texture = ((Level) stage).getTile("square" + tile);
				transform.render(spriteRenderer);
			}
		}
	}
	
	public void setTiles(int[][] tiles){
		this.tiles = tiles;
	}

	public void onAdd(Stage s) {
		super.onAdd(s);
		Level level = (Level) s;

		BodyDef def = new BodyDef();
		def.position.set(position);
		def.type = BodyType.StaticBody;
		body = level.getPhysics().createBody(def);
		body.setUserData(this);

		ChainShape outer = new ChainShape();
		Vector2[] vertices = new Vector2[4];
		vertices[0] = new Vector2(-hwidth, hheight);
		vertices[1] = new Vector2(hwidth, hheight);
		vertices[2] = new Vector2(hwidth, -hheight);
		vertices[3] = new Vector2(-hwidth, -hheight);
		outer.createLoop(vertices);
		
		PolygonShape inner = new PolygonShape();
		inner.setAsBox(hwidth, hheight);

		FixtureDef fix = new FixtureDef();
		fix.friction = 1;
		fix.restitution = 0;
		fix.filter.categoryBits = Const.CAT_ENVIRONMENT;
		fix.shape = outer;
		body.createFixture(fix);
		
		fix.shape = inner;
		fix.filter.maskBits = 0;
		body.createFixture(fix);
		
	}

	public void onRemove(Stage s) {
		super.onRemove(s);
		Level level = (Level) s;
		level.getPhysics().destroyBody(this.body);
	}

	@Override
	public String writeString() {
		StringBuilder ans = new StringBuilder("Rect;\n");
		ans.append("    " + position.x + " " + position.y + " " + hwidth + " " + hheight + ";\n");
		for(int i = 0; i < tiles.length; i++){
			ans.append("   ");
			for(int j = 0; j < tiles[i].length; j++){
				ans.append(" " + tiles[i][j]);
			}
			ans.append("\n");
		}
		ans.deleteCharAt(ans.length() - 1);
		ans.append(";\n");
		return ans.toString();
	}

	@Override
	public void readString(String str) {
		String[] lines = str.split(";\\s*");
		String[] params = lines[1].trim().split("\\s+");
		position.set(Float.parseFloat(params[0]), Float.parseFloat(params[1]));
		hwidth = Float.parseFloat(params[2]);
		hheight = Float.parseFloat(params[3]);
		tiles = new int[(int) (hheight * 2)][(int) (hwidth * 2)];
		String[] grid = lines[2].trim().split("\\s+");
		
		int i = 0;
		for(int y = 0; y < tiles.length; y++){
			for(int x = 0; x < tiles[y].length; x++){
				tiles[y][x] = Integer.parseInt(grid[i++]);
			}
		}
	}

	@Override
	public int getTile(float x, float y) {
		x -= (position.x - hwidth);
		y = (position.y + hheight) - y;
		if(y < tiles.length && x < tiles[(int) y].length){
			return tiles[(int) y][(int) x];
		} else {
			return -1;
		}
	}

	@Override
	public int setTile(int tile, float x, float y) {
		tile %= Const.Tiles.SQ_MAX;
		if(tile < Const.Tiles.SQ_MAX){
			tile += Const.Tiles.SQ_MAX;
		}
		x -= (position.x - hwidth);
		y = (position.y + hheight) - y;
		if(y < tiles.length && x < tiles[(int) y].length){
			tiles[(int) y][(int) x] = tile;
		}
		return tile;
	}

}
