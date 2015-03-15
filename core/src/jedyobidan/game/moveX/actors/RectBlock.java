package jedyobidan.game.moveX.actors;

import java.util.HashSet;
import java.util.Set;

import jedyobidan.game.moveX.Const;
import jedyobidan.game.moveX.Level;
import jedyobidan.game.moveX.MoveX;
import jedyobidan.game.moveX.lib.Actor;
import jedyobidan.game.moveX.lib.SpriteTransform;
import jedyobidan.game.moveX.lib.Stage;
import jedyobidan.game.moveX.player.PlayerPhysics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

public class RectBlock extends Actor {
	private Vector2 position;
	private float hwidth, hheight;
	private Body body;

	private String type;

	private Texture ground, top;

	private Set<Integer[]> topped;

	public RectBlock(String type, Vector2 position, float hwidth, float hheight) {
		this(type, position.x, position.y, hwidth, hheight);
	}

	public RectBlock(String type, float x, float y, float hwidth, float hheight) {
		this.position = new Vector2().set(x, y);
		this.hwidth = hwidth;
		this.hheight = hheight;
		this.type = type + "/";
		this.topped = new HashSet<Integer[]>();
	}

	public void setTextures(String prefix) {
		Level level = (Level) stage;
		ground = level.textures.getLiteral(prefix + "ground.png");
		top = level.textures.getLiteral(prefix + "platform.png");
	}

	public RectBlock addToppedInterval(int start, int end) {
		Integer[] interval = new Integer[] { start, end };
		topped.add(interval);
		return this;
	}

	public void removeToppedInterval(int start, int end) {
		Integer[] interval = new Integer[] { start, end };
		topped.remove(interval);
	}

	public Set<Integer[]> getToppedIntervals() {
		return topped;
	}

	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) {
		Vector2 pos = body.getPosition();
		float txWidth = ground.getWidth() / Const.PIXELS_PER_METER;
		float txHeight = ground.getHeight() / Const.PIXELS_PER_METER;
		ground.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		spriteRenderer.draw(ground, pos.x - hwidth, pos.y - hheight,
				hwidth * 2, hheight * 2, 0, 0, hwidth * 2 / txWidth, -hheight
						* 2 / txHeight); // May adjust u-v later

		if (topped.size() > 0) {
			float platWidth = top.getWidth() / Const.PIXELS_PER_METER;
			float platHeight = top.getHeight() / Const.PIXELS_PER_METER;
			top.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
			for (Integer[] interval : topped) {
				spriteRenderer.draw(top, pos.x - hwidth + interval[0], pos.y + hheight - platHeight, 
						interval[1] - interval[0], platHeight, 0, 0, 
						(interval[1] - interval[0])	/ platWidth, -1);
			}
		}

		// spriteRenderer.end();
		// shapeRenderer.begin(ShapeType.Filled);
		//
		// shapeRenderer.setColor(Color.DARK_GRAY);
		// shapeRenderer.rect(pos.x - hwidth, pos.y - hheight, 2*hwidth,
		// 2*hheight);
		//
		// shapeRenderer.end();
		// spriteRenderer.begin();
	}

	public void addToStage(Stage s) {
		super.addToStage(s);
		setTextures(type);
		Level level = (Level) s;

		BodyDef def = new BodyDef();
		def.position.set(position);
		def.type = BodyType.StaticBody;
		body = level.getPhysics().createBody(def);
		body.setUserData(this);

		EdgeShape edge = new EdgeShape();
		FixtureDef fix = new FixtureDef();
		fix.friction = 1;
		fix.restitution = 0;
		fix.filter.categoryBits = Const.CAT_ENVIRONMENT;
		fix.shape = edge;

		edge.setHasVertex0(true);
		edge.setHasVertex3(true);

		// Top Edge
		edge.set(-hwidth, hheight, hwidth, hheight);
		edge.setVertex0(-hwidth - PlayerPhysics.WIDTH * 3, hheight);
		edge.setVertex3(hwidth + PlayerPhysics.WIDTH * 3, hheight);
		body.createFixture(fix);

		// Bottom Edge
		edge.set(-hwidth, -hheight, hwidth, -hheight);
		edge.setVertex0(-hwidth - PlayerPhysics.WIDTH * 3, -hheight);
		edge.setVertex3(hwidth + PlayerPhysics.WIDTH * 3, -hheight);
		body.createFixture(fix);

		// Left Edge
		edge.set(-hwidth, -hheight, -hwidth, hheight);
		edge.setVertex0(-hwidth, -hheight - PlayerPhysics.GROUND * 3);
		edge.setVertex3(-hwidth, hheight + PlayerPhysics.GROUND * 3);
		body.createFixture(fix);

		// Right Edge
		edge.set(hwidth, -hheight, hwidth, hheight);
		edge.setVertex0(hwidth, -hheight - PlayerPhysics.GROUND * 3);
		edge.setVertex3(hwidth, hheight + PlayerPhysics.GROUND * 3);
		body.createFixture(fix);
	}

	public void removeFromStage(Stage s) {
		super.removeFromStage(s);
		Level level = (Level) s;
		level.getPhysics().destroyBody(this.body);
	}

}
