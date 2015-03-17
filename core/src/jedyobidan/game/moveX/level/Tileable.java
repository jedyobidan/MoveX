package jedyobidan.game.moveX.level;

public interface Tileable {
	int getTile(float x, float y);
	int setTile(int tile, float x, float y);
	void tileDefaults();
}
