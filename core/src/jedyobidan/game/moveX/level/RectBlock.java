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

public class RectBlock extends LevelObject{
	private Vector2 position;
	private float hwidth, hheight;
	private Body body;

	private String type;
	private int[][] tiles;

	public RectBlock(String type, Vector2 position, float hwidth, float hheight) {
		this(type, position.x, position.y, hwidth, hheight);
	}

	public RectBlock(String type, float x, float y, float hwidth, float hheight) {
		this.position = new Vector2().set(x, y);
		this.hwidth = hwidth;
		this.hheight = hheight;
		this.type = type;
		this.tiles = new int[(int) (hheight * 2)][(int) (hwidth * 2)];
		tileDefaults();
	}
	
	public RectBlock(){
		this("", 0, 0, 0, 0);
	}
	
	public void tileDefaults(){
		if(tiles.length == 0) return;
		for(int y = 1; y < tiles.length - 1; y++){
			tiles[y][0] = Const.Tiles.SQ_L;
			tiles[y][tiles[y].length - 1] = Const.Tiles.SQ_R;
		}
		for(int x = 1; x < tiles.length - 1; x++){
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
			tiles[tiles.length][0] = Const.Tiles.SQ_BL;
			tiles[tiles.length - 1][tiles[0].length - 1] = Const.Tiles.SQ_BR;
		}
	}


	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) {
		TextureManager textures = ((Level) stage).textures;
		SpriteTransform transform = new SpriteTransform();
		transform.scale.set(1/Const.PIXELS_PER_METER, 1/Const.PIXELS_PER_METER);
		Vector2 pos = body.getPosition();
		for(int y = 0; y < tiles.length; y++){
			for(int x = 0; x < tiles[y].length; x++){
				transform.position.set(pos.x - hwidth + x, pos.y + hheight - y - 1);
				transform.texture = textures.get(type + "/square" + tiles[y][x]);
				transform.render(spriteRenderer);
			}
		}
	}
	
	public void setTiles(int[][] tiles){
		this.tiles = tiles;
	}

	public void addToStage(Stage s) {
		super.addToStage(s);
		Level level = (Level) s;

		BodyDef def = new BodyDef();
		def.position.set(position);
		def.type = BodyType.StaticBody;
		body = level.getPhysics().createBody(def);
		body.setUserData(this);

		ChainShape shape = new ChainShape();
		FixtureDef fix = new FixtureDef();
		fix.friction = 1;
		fix.restitution = 0;
		fix.filter.categoryBits = Const.CAT_ENVIRONMENT;
		fix.shape = shape;

		Vector2[] vertices = new Vector2[4];
		vertices[0] = new Vector2(-hwidth, hheight);
		vertices[1] = new Vector2(hwidth, hheight);
		vertices[2] = new Vector2(hwidth, -hheight);
		vertices[3] = new Vector2(-hwidth, -hheight);
		shape.createLoop(vertices);

//		// Top Edge
//		edge.set(-hwidth, hheight, hwidth, hheight);
//		edge.setVertex0(-hwidth - PlayerPhysics.WIDTH * 3, hheight);
//		edge.setVertex3(hwidth + PlayerPhysics.WIDTH * 3, hheight);
//		body.createFixture(fix);
//
//		// Bottom Edge
//		edge.set(-hwidth, -hheight, hwidth, -hheight);
//		edge.setVertex0(-hwidth - PlayerPhysics.WIDTH * 3, -hheight);
//		edge.setVertex3(hwidth + PlayerPhysics.WIDTH * 3, -hheight);
//		body.createFixture(fix);
//
//		// Left Edge
//		edge.set(-hwidth, -hheight, -hwidth, hheight);
//		edge.setVertex0(-hwidth, -hheight - PlayerPhysics.GROUND * 3);
//		edge.setVertex3(-hwidth, hheight + PlayerPhysics.GROUND * 3);
//		body.createFixture(fix);
//
//		// Right Edge
//		edge.set(hwidth, -hheight, hwidth, hheight);
//		edge.setVertex0(hwidth, -hheight - PlayerPhysics.GROUND * 3);
//		edge.setVertex3(hwidth, hheight + PlayerPhysics.GROUND * 3);
		body.createFixture(fix);
	}

	public void removeFromStage(Stage s) {
		super.removeFromStage(s);
		Level level = (Level) s;
		level.getPhysics().destroyBody(this.body);
	}

	@Override
	public String writeString() {
		StringBuilder ans = new StringBuilder("Rect;\n");
		ans.append("    " + type + " " + position.x + " " + position.y + " " + hwidth + " " + hheight + ";\n");
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
		type = params[0];
		position.set(Float.parseFloat(params[1]), Float.parseFloat(params[2]));
		hwidth = Float.parseFloat(params[3]);
		hheight = Float.parseFloat(params[4]);
		tiles = new int[(int) (hheight * 2)][(int) (hwidth * 2)];
		String[] grid = lines[2].trim().split("\\s+");
		
		int i = 0;
		for(int y = 0; y < tiles.length; y++){
			for(int x = 0; x < tiles[y].length; x++){
				tiles[y][x] = Integer.parseInt(grid[i++]);
			}
		}
	}

}
