package game;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);

		Game game = new Game();
		window.add(game);

		// set this component's size to be fit with its subcompoents' preferred size
		window.pack();

		window.setLocationRelativeTo(null);
		window.setVisible(true);

		game.startGameThread();
	}
}
