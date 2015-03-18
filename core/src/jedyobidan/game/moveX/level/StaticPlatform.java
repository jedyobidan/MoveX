package jedyobidan.game.moveX.level;

import jedyobidan.game.moveX.Const;
import jedyobidan.game.moveX.Level;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.SpriteTransform;
import jedyobidan.game.moveX.lib.Stage;
import jedyobidan.game.moveX.lib.TextureManager;
import jedyobidan.game.moveX.player.Player;
import jedyobidan.game.moveX.player.PlayerPhysics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

public class StaticPlatform extends LevelObject implements ContactListener, Tileable{
	private float hwidth;
	private Vector2 position;
	private Body body;
	
	private int[] tiles;
	
	public StaticPlatform(){
		this(0, 0, 0);
	}
	
	public StaticPlatform(Vector2 position, float hwidth){
		this(position.x, position.y, hwidth);
	}
	
	public StaticPlatform(float x, float y, float hwidth){
		this.position = new Vector2().set(x, y);
		this.hwidth = hwidth;
		this.tiles = new int[(int) (hwidth * 2)];
		tileDefaults();
	}
	
	
	
	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) {
		SpriteTransform transform = new SpriteTransform();
		transform.scale.set(1/Const.PIXELS_PER_METER, 1/Const.PIXELS_PER_METER);
		Vector2 pos = body.getPosition();
		for(float x = pos.x - hwidth; x < pos.x + hwidth; x++){
			transform.position.set(x, pos.y - 0.5f);
			transform.texture = ((Level) stage).getTile("plat" + getTile(x, pos.y));
			transform.render(spriteRenderer);			
		}
	}
	
	public void onAdd(Stage s){
		super.onAdd(s);
		Level level = (Level) s;
		
		level.addContactListener(this);
		
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(position);
		body = level.getPhysics().createBody(def);
		body.setUserData(this);
		
		EdgeShape shape = new EdgeShape();
		shape.setHasVertex0(true);
		shape.setHasVertex3(true);
		shape.set(- hwidth, 0, hwidth, 0);
//		shape.setVertex0(-hwidth - PlayerPhysics.WIDTH * 3, 0);
//		shape.setVertex3(hwidth + PlayerPhysics.WIDTH * 3, 0);
		
		
		FixtureDef fix = new FixtureDef();
		fix.shape = shape;
		fix.filter.categoryBits = Const.CAT_ENVIRONMENT;
		fix.filter.maskBits = Const.CAT_PLAYER;
		fix.friction = 1f;
		fix.restitution = 0f;
		
		body.createFixture(fix);		
	}
	
	@Override
	public void onRemove(Stage s) {
		super.onRemove(s);
		Level level = (Level) s;
		level.getPhysics().destroyBody(body);
		level.removeContactListener(this);
	}

	@Override
	public void beginContact(Contact contact) { }

	@Override
	public void endContact(Contact contact) { }

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		Body other;
		if(contact.getFixtureA().getBody() == body){
			other = contact.getFixtureB().getBody();
		} else if(contact.getFixtureB().getBody() == body){
			other = contact.getFixtureA().getBody();
		} else {
			return;
		}
		Actor a = (Actor) other.getUserData();
		if(a instanceof Player){
			float feet = other.getPosition().y - PlayerPhysics.GROUND;
			if(feet + 0.1f < body.getPosition().y) {
				contact.setEnabled(false);
			}
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) { }

	@Override
	public String writeString() {
		StringBuilder ans = new StringBuilder("Plat;\n");
		ans.append("    " + position.x + " " + position.y + " " + hwidth + ";\n");
		ans.append("   ");
		for(int tile: tiles){
			ans.append(" " + tile);
		}
		ans.append(";\n");
		return ans.toString();
	}

	@Override
	public void readString(String str) {
		String[] lines = str.split(";\\s*");
		String[] params = lines[1].trim().split("\\s+");
		position.set(Float.parseFloat(params[0]), Float.parseFloat(params[1]));
		hwidth = Float.parseFloat(params[2]);
		tiles = new int[(int) (hwidth * 2)];
		String[] tileString = lines[2].split("\\s+");
		for(int i = 0; i < tiles.length; i++){
			tiles[i] = Integer.parseInt(tileString[i]);
		}
	}
	
	@Override
	public void tileDefaults(){
		if(tiles.length == 0) return;
		tiles[0] = Const.Tiles.PL_L;
		tiles[tiles.length - 1] = Const.Tiles.PL_R;
	}

	@Override
	public int getTile(float x, float y) {
		x -= (position.x - hwidth);
		y = (position.y) - y;
		if((int) y == 0 && x < tiles.length){
			return tiles[(int) x];
		} else {
			return -1;
		}
	}

	@Override
	public int setTile(int tile, float x, float y) {
		tile %= Const.Tiles.PL_MAX;
		if(tile < Const.Tiles.PL_MAX){
			tile += Const.Tiles.PL_MAX;
		}
		x -= (position.x - hwidth);
		y = (position.y) - y;
		if((int) y == 0 && x < tiles.length){
			tiles[(int) x] = tile;
		}
		return tile;
	}
}
