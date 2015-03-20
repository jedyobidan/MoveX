package jedyobidan.game.moveX.lib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeSet;

import jedyobidan.game.moveX.MoveX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

public class Stage extends ScreenAdapter implements Comparator<Actor>{

	protected List<Collection<Actor>> actors;
	
	private List<Queue<Actor>> addQueue;
	private List<Queue<Actor>> removeQueue;	

	private List<OrthographicCamera> cameras;
	private Stack<Matrix4> matrixStack;
	private InputMultiplexer input;
	
	protected SpriteBatch spriteRender;
	protected ShapeRenderer shapeRender;
	protected TextureRegion background;
	

	public final TextureManager textures;
	
	private float speed;
	
	public Stage(SpriteBatch sb, ShapeRenderer sr, int groups, TextureAtlas atlas){
		actors = new ArrayList<Collection<Actor>>();
		addQueue = new ArrayList<Queue<Actor>>();
		removeQueue = new ArrayList<Queue<Actor>>();
		

		cameras = new ArrayList<OrthographicCamera>();
		cameras.add(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		matrixStack = new Stack<Matrix4>();
		input = new InputMultiplexer();
		
		spriteRender = sb;
		shapeRender = sr;
		
		for(int i = 0; i < groups; i++){
			actors.add(new TreeSet<Actor>(this));
			addQueue.add(new LinkedList<Actor>());
			removeQueue.add(new LinkedList<Actor>());
		}
		
		speed = 1;

		textures = new TextureManager(atlas, "");
	}
	
	public int getNumGroups(){
		return actors.size();
	}
	
	public void gameLoop(float timeDelta){
		step(timeDelta * speed);
		render();
		processNewActors();
	}
	
	protected void processNewActors(){
		for(int group = 0; group < getNumGroups(); group ++){
			while(removeQueue.get(group).size() > 0){
				Actor a = removeQueue.get(group).poll();
				a.onRemove(this);
				actors.get(group).remove(a);
			}
			while(addQueue.get(group).size() > 0){
				Actor a = addQueue.get(group).poll();
				a.onAdd(this);
				actors.get(group).add(a);
			}
		}
		
	}
	
	protected void step(float timeDelta){
		for(int group = 0; group < getNumGroups(); group ++){
			for(Actor a: actors.get(group)){
				a.step(timeDelta);
			}
		}
	}
	
	protected int chooseCamera(int group){
		return 0;
	}
	
	protected void render(){
		Gdx.gl.glClearColor(1, 1, 1, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    for(Camera cam: cameras){
	    	cam.update();
	    }
	    
		if(background != null){
			useCamera(0);
			spriteRender.begin();
			TiledDrawable tiled = new TiledDrawable(background);
			tiled.draw(spriteRender, -Gdx.graphics.getWidth()/2, -Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			spriteRender.end();
		}

	    for(int i = 0; i < getNumGroups(); i++){
	    	render(i);
	    }
	}
	
	protected void render(int group){
		useCamera(chooseCamera(group));
		spriteRender.begin();
		for(Actor a: actors.get(group)){
			a.render(spriteRender, shapeRender);
		}		
		spriteRender.end();
	}
	
	public void useCamera(int camID){
		spriteRender.setProjectionMatrix(cameras.get(camID).combined);
		shapeRender.setProjectionMatrix(cameras.get(camID).combined);
	}
	
	
	public int addCamera(OrthographicCamera cam){
		cameras.add(cam);
		return cameras.size() - 1;
	}
	
	public void pushMatrix(){
		matrixStack.push(spriteRender.getProjectionMatrix().cpy());
	}
	
	public void popMatrix(){
		Matrix4 last = matrixStack.pop();
		spriteRender.setProjectionMatrix(last);
		shapeRender.setProjectionMatrix(last);
	}
	
	public OrthographicCamera getCamera(int camID){
		return cameras.get(camID);
	}
	
	public void addInput(InputProcessor input, boolean priority){
		if(priority){
			this.input.addProcessor(0, input);
		} else {
			this.input.addProcessor(input);
		}
	}
	
	public void removeInput(InputProcessor input){
		this.input.removeProcessor(input);
	}
	
	public void addActor(int group, Actor a){
		addQueue.get(group).add(a);
	}
	
	public void removeActor(int group, Actor a){
		removeQueue.get(group).add(a);
	}
	
	public Collection<Actor> getActors(int group){
		return actors.get(group);
	}

	@Override
	public void render(float delta) {
		gameLoop(delta);
	}
	
	@Override
	public void show() {
		super.show();
		Gdx.input.setInputProcessor(input);
	}

	@Override
	public void hide() {
		super.hide();
		Gdx.input.setInputProcessor(null);
	}

	public float getSpeed(){
		return speed;
	}
	
	public void setSpeed(float speed){
		this.speed = speed;
	}

	@Override
	public int compare(Actor o1, Actor o2) {
		float zDiff = o1.getZIndex() - o2.getZIndex();
		if(zDiff != 0) return (int) Math.signum(zDiff);
		else return o1.hashCode() - o2.hashCode();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		cameras.get(0).viewportWidth = width;
		cameras.get(0).viewportHeight = height;
	}

}
