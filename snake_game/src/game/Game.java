package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import game.Snake.Tail;

public class Game extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;
	private final int SCALE = 4;
	private final int TILE_DEFAULT_SIZE = 16;
	private final int TILE_SIZE = TILE_DEFAULT_SIZE * SCALE;

	private final int SCREEN_MAX_COL = 10;
	private final int SCREEN_MAX_ROW = 10;
	
	private final int SCREEN_WIDTH = TILE_SIZE * SCREEN_MAX_COL;
	private final int SCREEN_HEIGHT = TILE_SIZE * SCREEN_MAX_ROW;

	private final int FPS = 10;

	private int score = 0;
	private int topScore = 0;

	private GameStatus status = GameStatus.PLAYING;
	
	private Map map;
	private Thread gameThread;
	private Timer timer;

	private KeyEventHandler keyHandler	= new KeyEventHandler();
	private GameStat gameStat			= new GameStat(SCREEN_WIDTH, SCREEN_HEIGHT, SCREEN_MAX_COL, SCREEN_MAX_ROW);

	private Apple apple					= new Apple(TILE_SIZE);
	private Field field 				= new Field(SCREEN_MAX_COL, SCREEN_MAX_ROW, TILE_SIZE);
	private Snake snake					= new Snake(TILE_SIZE, keyHandler);

	enum GameStatus {
		READY,
		PLAYING,
		PAUSED,
		GAME_OVER;
	}
	
	public Game() {
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.BLACK);
		
		// focus on this panel to accept keypress input
		this.setFocusable(true);

		this.addKeyListener(keyHandler);

		initGame();
	}
	
	public boolean hasEatApple() {
		if (snake.getX() == apple.getX() && snake.getY() == apple.getY())
			return true;
		return false;
	}

	public boolean hasSnakeHitWall() {
		int x = snake.getX();
		int y = snake.getY();

		if (x < 0 || x >= SCREEN_MAX_COL || y < 0 || y >= SCREEN_MAX_ROW)
			return true;
		
		return false;
	}
	
	public boolean hasSnakeHitTail() {
		if (snake.getTailFirst() != null && map.isSnakeAt(snake.getX(), snake.getY()))
			return true;
		return false;
	}

	public void initGame() {
		topScore = 0;
		resetGame();
	}

	public void paintComponent(Graphics g) {
		// must call paintComponent() before, calling draw()
		super.paintComponent(g); 
		
		// g2 has rich features
		Graphics2D g2 = (Graphics2D)g;
		
		field.draw(g2);
		snake.draw(g2);
		apple.draw(g2);

		if (score > topScore)
			topScore = score;

		gameStat.draw(g2, status, score, topScore, timer.getCurrentTime());

		// delete g2 from the memory after drawing
		g2.dispose();
	}
	
 	private void spawnApple() {
		int x = 0;
		int y = 0;

		while (true) {
			x = (int)(Math.random() * SCREEN_MAX_COL);
			y = (int)(Math.random() * SCREEN_MAX_ROW);
			
			if (!map.isSnakeAt(x, y))
				break;
		}

		apple.setPosition(x, y);

		map.setAppleAt(x, y);
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() {
		snake.updatePosition();

		if (hasSnakeHitWall() || hasSnakeHitTail()) {
			snake.handleDead();
			status = GameStatus.GAME_OVER;
			return;
		} 

		if (hasEatApple()) {
			snake.hasApple = true;
			spawnApple();
			score++;
		}
		
		updateMap();
	}

	public void updateMap() {
		map.setAppleAt(apple.getX(), apple.getY());
		map.setFieldAt(snake.getPrevX(), snake.getPrevY());
		map.setSnakeAt(snake.getX(), snake.getY());
		
		Tail firstTail = snake.getTailFirst();
		Tail removedTail = snake.getTailRecentRemoved();
		
		if (firstTail != null)
			map.setSnakeAt(firstTail.getX(), firstTail.getY());

		if (removedTail != null)
			map.setFieldAt(removedTail.getX(), removedTail.getY());
	}

	public void resetGame() {
		map = new Map(SCREEN_MAX_ROW, SCREEN_MAX_COL);
		timer = new Timer();

		snake.setDefaultValues(); 
		keyHandler.resetPressedKey(); 

		score = 0;
		status = GameStatus.READY;

		timer.start();
		spawnApple();
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(Timer.ONE_SECOND_IN_MILI / FPS);
			} catch (InterruptedException e) { }

			String pressedKey = keyHandler.getPressedKey();

			if (pressedKey == Keys.EXIT)
				break;
			
			if (pressedKey == Keys.RESTART) {
				resetGame();
				continue;
			}

			if (status == GameStatus.GAME_OVER)
				continue;

			if (Keys.isValidDirection(pressedKey))
				status = GameStatus.PLAYING;

			repaint();

			if (status == GameStatus.PAUSED || status == GameStatus.READY)
				continue;
			
			if (pressedKey == Keys.STOP) {
				status = GameStatus.PAUSED;
				continue;
			}

			update();
			repaint();
		}

		System.exit(0);
	}
}