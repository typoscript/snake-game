package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyEventHandler implements KeyListener {
	private String pressedKey = Keys.NO_KEY;

	public String getPressedKey() {
		return this.pressedKey;
	}

	public void resetPressedKey() {
		this.pressedKey = Keys.NO_KEY;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		String key = KeyEvent.getKeyText(e.getKeyCode());

		switch (key) {
			case Keys.UP:
				pressedKey = Keys.UP;
				break;
			case Keys.DOWN:
				pressedKey = Keys.DOWN;
				break;
			case Keys.LEFT:
				pressedKey = Keys.LEFT;
				break;
			case Keys.RIGHT:
				pressedKey = Keys.RIGHT;
				break;
			case Keys.SPACE:
				pressedKey = Keys.RESTART;
				break;
			case Keys.S:
				pressedKey = Keys.STOP;
				break;
			case Keys.X:
				pressedKey = Keys.EXIT;
				break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void keyReleased(KeyEvent e) { }
}