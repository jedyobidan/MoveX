package jedyobidan.game.moveX.level;

import jedyobidan.game.moveX.Const;
import jedyobidan.game.moveX.Level;
import jedyobidan.game.moveX.lib.TextureTransform;
import jedyobidan.game.moveX.lib.Stage;
import jedyobidan.game.moveX.player.Player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Sign extends LevelObject implements DownInteract {
	private String[] text;
	private Vector2 position;
	private Body body;
	
	public Sign(){
		text = new String[]{"//TODO add message"};
		position = new Vector2();
	}
	
	public Sign(float x, float y, String... text){
		this.text = text;
		this.position = new Vector2(x, y);
	}
	
	@Override
	public void onAdd(Stage s){
		super.onAdd(s);
		Level level = (Level) s;
		
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(position);
		body = level.getPhysics().createBody(def);
		body.setUserData(this);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.5f, 0.5f, new Vector2(0, 0.5f), 0);
		
		FixtureDef fix = new FixtureDef();
		fix.filter.categoryBits = Const.CAT_NODE;
		fix.filter.maskBits = Const.CAT_PLAYER;
		fix.isSensor = true;
		fix.shape = shape;
		body.createFixture(fix);
		
	}
	
	@Override
	public void onRemove(Stage s){
		((Level) s).getPhysics().destroyBody(body);
	}

	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) {
		TextureTransform transform = new TextureTransform();
		TextureRegion texture = stage.textures.get("sign");
		transform.scale.set(1 / Const.PIXELS_PER_METER, 1/ Const.PIXELS_PER_METER);
		transform.origin.set(texture.getRegionWidth()/2, 0);
		transform.position.set(position);
		transform.texture = texture;
		
		transform.render(spriteRenderer);
		
	}

	@Override
	public void onInteract(Player player) {
		((Level) stage).showDialog(text);
	}

	@Override
	public String writeString() {
		StringBuilder ans = new StringBuilder("Sign;\n");
		ans.append(String.format("    %.2f %.2f;\n", position.x, position.y));
		for(String s : text){
			ans.append("    " + s + "\n");
		}
		ans.insert(ans.length() - 1, ";");
		return ans.toString();
	}

	@Override
	public void readString(String str) {
		String[] lines = str.split(";\\s*");
		String[] params = lines[1].trim().split("\\s+");
		position.set(Float.parseFloat(params[0]), Float.parseFloat(params[1]));
		text = lines[2].split("\\n");
	}

}
