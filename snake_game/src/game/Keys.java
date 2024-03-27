package game;

public class Keys implements Direction {
	static final String SPACE	= "Space";
	static final String NO_KEY	= "";
	static final String X		= "X";
	static final String S		= "S";

	static final String RESTART = SPACE;
	static final String STOP	= S;
	static final String EXIT	= X;

	public static boolean isValidDirection(String dir) {
		if (dir.equals(Keys.UP) || dir.equals(Keys.DOWN) || dir.equals(Keys.LEFT) || dir.equals(Keys.RIGHT))
			return true;
		return false;
	}
}
	
interface Direction {
	static final String UP		= "Up";
	static final String DOWN	= "Down";
	static final String LEFT	= "Left";
	static final String RIGHT	= "Right";
}