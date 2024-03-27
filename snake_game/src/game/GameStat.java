package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import game.Game.GameStatus;

public class GameStat {
	final int x, y;
	final int MID_X, MID_Y;

	private static class GameFont {
		static final String FAMILY = Font.MONOSPACED;
		static final int STYLE = Font.BOLD;
		
		static class Default {
			static final int SIZE = 20;

			public static Font get() {
				return new Font(FAMILY, STYLE, SIZE);
			}
		}

		static class Result {
			static final int SIZE = 40;

			public static Font get() {
				return new Font(FAMILY, STYLE, SIZE);
			}
		}
	}

	private static class Text {
		static final String SCORE = "내 점수: ";
		static final String SCORE_TOP = "최고 점수: ";
		static final String TIMER = "플레이 시간: ";

		static final String GAME_OVER = "게임 오버";
		static final String PAUSED = "일시 정지";
		static final String RESTART = "스페이바 눌러 재시작";
		static final String RESUME = "방향키 눌러 게임 진행";
		static final String INSTRUCTION_STOP = "s 눌러 일시 정지";
		static final String INSTRUCTION_EXIT = "x 눌러 게임 종료";
	}
	
	public GameStat(int windowWidth, int windowHeight, int col, int row) {
		this.x = windowWidth / col;
		this.y = windowHeight - (windowHeight / row);

		this.MID_X = windowWidth / 2;
		this.MID_Y = windowHeight / 2;
	}
	
	public void draw(Graphics2D g2, GameStatus status, int score, int topScore, String currentTime) {
		switch (status) {
			case GAME_OVER:
				drawMessage(g2, GameFont.Result.get(), Text.GAME_OVER, Color.RED, 0);
				drawMessage(g2, GameFont.Default.get(), Text.RESTART, Color.PINK, GameFont.Result.SIZE);
				drawMessage(g2, GameFont.Default.get(), Text.SCORE_TOP + topScore, Color.PINK, GameFont.Result.SIZE * 2);
				drawMessage(g2, GameFont.Default.get(), Text.SCORE + score, Color.PINK, GameFont.Result.SIZE * 3);
				return;
			case READY:
				String[] texts = { Text.RESUME, Text.RESTART, Text.INSTRUCTION_STOP, Text.INSTRUCTION_EXIT };
				drawMultipleMessages(g2, GameFont.Default.get(), texts, Color.LIGHT_GRAY, GameFont.Default.SIZE * 2);
				break;
			case PAUSED:
				drawMessage(g2, GameFont.Result.get(), Text.PAUSED, Color.PINK, 0);
				drawMessage(g2, GameFont.Default.get(), Text.RESUME, Color.PINK, GameFont.Result.SIZE);
				break;
			default:
				break;
		}

		drawGameStat(g2, score, currentTime);
	}
	
	private void drawGameStat(Graphics2D g2, int score, String currentTime) {
		int scoreY = y - GameFont.Default.SIZE - GameFont.Default.SIZE / 2;

		g2.setFont(GameFont.Default.get());
		g2.setColor(Color.YELLOW);

		g2.drawString(Text.SCORE + score, x, scoreY);
		g2.drawString(Text.TIMER + currentTime, x, y);
	}
	
	private void drawMessage(Graphics2D g2, Font font, String text, Color textColor, int offsetY) {
		g2.setFont(font);
		g2.setColor(textColor);

		int width = g2.getFontMetrics().stringWidth(text);
		int height = g2.getFontMetrics().getHeight();

		int x = MID_X - (width / 2);
		int y = MID_Y - (height / 2);
		
		g2.drawString(text, x, y + offsetY);
	}

	private void drawMultipleMessages(Graphics2D g2, Font font, String[] texts, Color textColor, int offsetY) {
		int startOffsetY = offsetY * texts.length;

		if (texts.length > 1)
			startOffsetY /= 2;
		
		startOffsetY *= -1;

		for (int i = 0; i < texts.length; i++) {
			drawMessage(g2, font, texts[i], textColor, startOffsetY);
			
			startOffsetY += offsetY;
		}
	}
	
}