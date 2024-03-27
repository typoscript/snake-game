package game;

public class Map {
	public int[][] map;
	
	static class Value {
		static final int FIELD = 0;
		static final int SNAKE = 1;
		static final int APPLE = 2;
	}

	public Map(int col, int row) {
		map = new int[row][col];
	}

	public int getValueAt(int x, int y) {
		return map[y][x];
	}

	public boolean isSnakeAt(int x, int y) {
		return getValueAt(x, y) == Value.SNAKE;
	}
	
	public void setAppleAt(int x, int y) {
		setValueAt(x, y, Value.APPLE);
	}

	public void setFieldAt(int x, int y) {
		setValueAt(x, y, Value.FIELD);
	}

	public void setSnakeAt(int x, int y) {
		setValueAt(x, y, Value.SNAKE);
	}

	public void setValueAt(int x, int y, int value) {
		map[y][x] = value;
	}
}
