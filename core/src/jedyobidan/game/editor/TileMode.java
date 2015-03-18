package jedyobidan.game.editor;

import java.util.Set;

import jedyobidan.game.moveX.level.Tileable;
import jedyobidan.game.moveX.lib.Actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class TileMode extends Mode{

	public TileMode(LevelEditor editor) {
		super(editor);
	}

	@Override
	public String getName() {
		return "tile";
	}

	@Override
	public void render(SpriteBatch spriteRenderer, ShapeRenderer shapeRenderer) {
		Vector2 mouse = editor.getMousePosition();
		spriteRenderer.end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(1, 1, 0, 0.5f);
		shapeRenderer.rect((float) Math.floor(mouse.x), (float) Math.floor(mouse.y), 1, 1);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		spriteRenderer.begin();
	}
	
	@Override
	public void mouseClicked(float x, float y, int button) {
		Set<Actor> actors = editor.getLevel().getActorsAt(x, y);
		for(Actor a: actors){
			if(a instanceof Tileable){
				Tileable t = (Tileable) a;
				int tile = t.getTile(x, y);
				if(tile == -1) continue;
				
				int delta = 0;
				if(button == Input.Buttons.LEFT) delta = 1;
				else if (button == Input.Buttons.RIGHT) delta = -1;
				
				int newTile = tile + delta;
				newTile = t.setTile(newTile, x, y);
				editor.log("Tile change " + tile + "->" + newTile);
			}
		}
	}

	@Override
	public boolean execCommand(String... args) {
		return false;
	}

}
