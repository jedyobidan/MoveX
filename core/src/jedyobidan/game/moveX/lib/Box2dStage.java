package jedyobidan.game.moveX.lib;

import java.util.HashSet;
import java.util.Set;

import jedyobidan.game.moveX.Const;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

public class Box2dStage extends Stage implements ContactListener{
	protected World physics;
	protected int velocityIterations, positionIterations;
	private boolean debugDraw;
	private Box2DDebugRenderer physicsDebug;
	private int physicsCamera;
	
	private Set<ContactListener> contactListeners;
	
	private boolean physicsPaused;
	
	public Box2dStage(SpriteBatch sb, ShapeRenderer sr, int groups){
		super(sb, sr, groups);
		velocityIterations = 6;
		positionIterations = 3;
		contactListeners = new HashSet<ContactListener>();
		physics = new World(new Vector2(0, -10f), true);
		physicsDebug = new Box2DDebugRenderer();
		physicsDebug.setDrawBodies(true);
		physicsDebug.setDrawContacts(true);
		physicsDebug.setDrawJoints(true);
		physics.setContactListener(this);
		
		OrthographicCamera physicsCam = new OrthographicCamera();
		physicsCam.setToOrtho(false, Gdx.graphics.getWidth() / Const.PIXELS_PER_METER, Gdx.graphics.getHeight() / Const.PIXELS_PER_METER);
		physicsCam.position.set(0, 0, 0);
		physicsCamera = addCamera(physicsCam);
	}
	
	protected int getPhysicsCamera(){
		return physicsCamera;
	}
	
	protected int chooseCamera(int group){
		return physicsCamera;
	}
	
	
	protected void step(float timeDelta){
		super.step(timeDelta);
		if(!physicsPaused)
			physics.step(timeDelta, velocityIterations, positionIterations);
		else
			physics.clearForces();
	}
	
	protected void render(){		
		super.render();
		
		shapeRender.begin();
		if(debugDraw){
			physicsDebug.render(physics, getCamera(physicsCamera).combined);
		}
		shapeRender.end();
	}
	
	public World getPhysics(){
		return physics;
	}
	
	public void setDebug(boolean draw){
		debugDraw = draw;
	}
	
	public boolean isDebug(){
		return debugDraw;
	}
	
	public void setPaused(boolean paused){
		this.physicsPaused = paused;
	}
	
	public boolean isPaused(){
		return physicsPaused;
	}
	
	public void dispose(){
		super.dispose();
		physics.dispose();
		
	}
	
	public void addContactListener(ContactListener listener){
		contactListeners.add(listener);
	}
	
	public void removeContactListener(ContactListener listener){
		contactListeners.remove(listener);
	}


	@Override
	public void beginContact(Contact contact) {
		for(ContactListener c: contactListeners){
			c.beginContact(contact);
		}
	}


	@Override
	public void endContact(Contact contact) {
		for(ContactListener c: contactListeners){
			c.endContact(contact);
		}
	}


	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		for(ContactListener c: contactListeners){
			c.preSolve(contact, oldManifold);
		}
	}


	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		for(ContactListener c: contactListeners){
			c.postSolve(contact, impulse);
		}
	}
}
