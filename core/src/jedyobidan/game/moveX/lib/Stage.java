package jedyobidan.game.moveX.lib;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Stage extends ScreenAdapter{
	protected Collection<Actor> actors;
	
	private Queue<Actor> addQueue;
	private Queue<Actor> removeQueue;	

	protected OrthographicCamera camera;
	protected SpriteBatch spriteRender;
	protected ShapeRenderer shapeRender;
	
	public Stage(SpriteBatch sb, ShapeRenderer sr){
		actors = new TreeSet<Actor>(new Comparator<Actor>(){
			@Override
			public int compare(Actor o1, Actor o2) {
				float zDiff = o1.getZIndex() - o2.getZIndex();
				if(zDiff != 0) return (int) Math.signum(zDiff);
				else return o1.hashCode() - o2.hashCode();
			}
			
		});
		addQueue = new LinkedList<Actor>();
		removeQueue = new LinkedList<Actor>();
		

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		spriteRender = sb;
		shapeRender = sr;
	}
	
	public void gameLoop(float timeDelta){
		step(timeDelta);
		render();
		processNewActors();
	}
	
	protected void processNewActors(){
		while(removeQueue.size() > 0){
			Actor a = removeQueue.poll();
			a.removeFromStage(this);
			actors.remove(a);
		}
		while(addQueue.size() > 0){
			Actor a = addQueue.poll();
			a.addToStage(this);
			actors.add(a);
		}
	}
	
	protected void step(float timeDelta){
		for(Actor a: actors){
			a.step(timeDelta);
		}
	}
	
	protected void render(){
		Gdx.gl.glClearColor(1, 1, 1, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    camera.update();
		
		spriteRender.setProjectionMatrix(camera.combined);
		shapeRender.setProjectionMatrix(camera.combined);
		
		spriteRender.begin();
		for(Actor a: actors){
			a.render(spriteRender, shapeRender);
		}		
		spriteRender.end();
	}
	
	public void addActor(Actor a){
		addQueue.add(a);
	}
	
	public void removeActor(Actor a){
		removeQueue.add(a);
	}
	
	public Collection<Actor> getActors(){
		return actors;
	}

	@Override
	public void render(float delta) {
		gameLoop(delta);
	}

}
