package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Iterator;

public class Snake extends GameObject implements PrevPosition {
	private KeyEventHandler keyHandler;
	
	private int prevX , prevY;

	private String direction;
	private String prevDirection;

	private ArrayDeque<Tail> tails;
	private Tail recentRemovedTail;

	private boolean isDead = false;
	public boolean hasApple = false;

	private static class BodyImage {
		static BufferedImage Vertical, Horizontal,
			TopLeftCorner, TopRightCorner, BotLeftCorner, BotRightCorner;

		static class Head {
			static BufferedImage Up, Down, Left, Right,
				UpWithTail, DownWithTail, LeftWithTail, RightWithTail;
			
			static class Dead {
				static BufferedImage Up, Down, Left, Right,
					UpWithTail, DownWithTail, LeftWithTail, RightWithTail,

					UpToLeft, UpToRight,
					DownToLeft, DownToRight,
					LeftToUp, LeftToDown,
					RightToUp, RightToDown;
			}
		}

		static class Tail {
			static BufferedImage Up, Down, Left, Right;
		}
	}

	public class Tail extends Position {
		public Tail(int x, int y)  {
			setPosition(x, y);
		}
	}

	public Snake(int imageSize, KeyEventHandler keyHandler) {
		this.keyHandler = keyHandler;
		
		this.setDefaultValues();
		this.setImageSize(imageSize);
		this.setPlayerImage();
	}

	public void draw(Graphics2D g2) {
		BufferedImage image = getImageHead();

		g2.drawImage(image, 
				getX() * this.image.WIDTH, getY() * this.image.HEIGHT,
				this.image.WIDTH, this.image.HEIGHT,
				null);

		drawBodyAndTail(g2);
	}

	private void drawBodyAndTail(Graphics2D g2) {
		// 첫 tail의 front tail은 뱀의 머리
		Tail frontTail = new Tail(getX(), getY());
		Tail backTail = null;
		Tail currentTail = null;
		BufferedImage image = null;
		
		Iterator<Tail> it = tails.iterator();

		// 첫 꼬리 인식하기
		if (currentTail == null && it.hasNext())
			currentTail = it.next();
		
		while (currentTail != null) {
			backTail = it.hasNext() ? it.next() : null;

			if (backTail == null)
				image = getImageTail(frontTail, currentTail);
			else
				image = getImageBody(frontTail, currentTail, backTail);
			
			if (image == null)
				image = this.image.getDefaultImage(getX(), getY());

			g2.drawImage(image,
					currentTail.getX() * this.image.WIDTH, currentTail.getY() * this.image.HEIGHT,
					this.image.WIDTH, this.image.HEIGHT,
					null);
			
			frontTail = currentTail;
			currentTail = backTail;
		}
	}
	
	private BufferedImage getImageBody(Tail frontTail, Tail currentTail, Tail backTail) {
		int frontTailX = frontTail.getX();
		int frontTailY = frontTail.getY();
		
		int currentTailX = currentTail.getX();
		int currentTailY = currentTail.getY();
		
		int backTailX = backTail.getX();
		int backTailY = backTail.getY();

		if (frontTailX == currentTailX && frontTailX == backTailX)
			return BodyImage.Vertical;

		if (frontTailY == currentTailY && frontTailY == backTailY)
			return BodyImage.Horizontal;
		
		// ▛
		if ((frontTailX > currentTailX && currentTailY < backTailY) || (frontTailY > currentTailY && currentTailX < backTailX))
			return BodyImage.TopLeftCorner;

		// ▜
		if ((frontTailX < currentTailX && currentTailY < backTailY) || (frontTailY > currentTailY && currentTailX > backTailX))
			return BodyImage.TopRightCorner;

		// ▙
		if ((frontTailX > currentTailX && currentTailY > backTailY) || (frontTailY < currentTailY && currentTailX < backTailX))
			return BodyImage.BotLeftCorner;

		// ▟
		if ((frontTailX < currentTailX && currentTailY > backTailY) || (frontTailY < currentTailY && currentTailX > backTailX))
			return BodyImage.BotRightCorner;
		
		return null;
	}
	
	private BufferedImage getImageHead() {
		if (tails.isEmpty()) {
			return getImageHeadWithTail();
		} else {
			switch (direction) {
				case Keys.UP:
					return isDead ? getImageHeadDead(direction) : BodyImage.Head.Up;
				case Keys.DOWN:
					return isDead ? getImageHeadDead(direction) : BodyImage.Head.Down;
				case Keys.LEFT:
					return isDead ? getImageHeadDead(direction) : BodyImage.Head.Left;
				case Keys.RIGHT:
					return isDead ? getImageHeadDead(direction) : BodyImage.Head.Right;
			}
		}
		
		return null;
	}

	private BufferedImage getImageHeadDead(String direction) {
		int x = getX();
		int y = getY();
		
		Tail firstTail = this.getTailFirst();
		int firstTailX = (firstTail == null) ? x : firstTail.getX();
		int firstTailY = (firstTail == null) ? y : firstTail.getY();

		switch (direction) {
			case Keys.UP:
				if (firstTailX == x)
					return BodyImage.Head.Dead.Up;
				if (firstTailX < x)
					return BodyImage.Head.Dead.LeftToUp;
				if (firstTailX > x)
					return BodyImage.Head.Dead.RightToUp;
			case Keys.DOWN:
				if (firstTailX == x)
					return BodyImage.Head.Dead.Down;
				if (firstTailX < x)
					return BodyImage.Head.Dead.LeftToDown;
				if (firstTailX > x)
					return BodyImage.Head.Dead.RightToDown;
			case Keys.LEFT:
				if (firstTailY == y)
					return BodyImage.Head.Dead.Left;
				if (firstTailY < y)
					return BodyImage.Head.Dead.UpToLeft;
				if (firstTailY > y)
					return BodyImage.Head.Dead.DownToLeft;
			case Keys.RIGHT:
				if (firstTailY == y)
					return BodyImage.Head.Dead.Right;
				if (firstTailY < y)
					return BodyImage.Head.Dead.UpToRight;
				if (firstTailY > y)
					return BodyImage.Head.Dead.DownToRight;
		}

		return null;
	}

	private BufferedImage getImageHeadWithTail() {
		switch (direction) {
			case Keys.UP:
				return isDead ? BodyImage.Head.Dead.UpWithTail : BodyImage.Head.UpWithTail;
			case Keys.DOWN:
				return isDead ? BodyImage.Head.Dead.DownWithTail : BodyImage.Head.DownWithTail;
			case Keys.LEFT:
				return isDead ? BodyImage.Head.Dead.LeftWithTail : BodyImage.Head.LeftWithTail;
			case Keys.RIGHT:
				return isDead ? BodyImage.Head.Dead.RightWithTail : BodyImage.Head.RightWithTail;
		}
		
		return null;
	}
	
	private BufferedImage getImageTail(Tail frontTail, Tail currentTail) {
		if (frontTail.getX() == currentTail.getX())
			return frontTail.getY() < currentTail.getY() ? BodyImage.Tail.Up : BodyImage.Tail.Down;

		if (frontTail.getY() == currentTail.getY())
			return frontTail.getX() < currentTail.getX() ? BodyImage.Tail.Left : BodyImage.Tail.Right;
		
		return null;
	}
	
	public int getPrevX() {
		return prevX;
	}

	public int getPrevY() {
		return prevY;
	}
	
	public Tail getTailFirst() {
		return this.tails.peekFirst();
	}

	public Tail getTailRecentRemoved() {
		return recentRemovedTail;
	}

	public void handleDead() {
		setToPrevPosition();
		setToPrevTails();

		isDead = true;
	}
	
	private void increaseTail() {
		Tail prevHeadAsTail = new Tail(getPrevX(), getPrevY());
		tails.addFirst(prevHeadAsTail);
	}
	
	private boolean isOppositeDirection(String newDirection) {
		switch (newDirection) {
			case Keys.UP:
				return prevDirection.equals(Keys.DOWN)	? true : false;
			case Keys.DOWN:
				return prevDirection.equals(Keys.UP)	? true : false;
			case Keys.LEFT:
				return prevDirection.equals(Keys.RIGHT)	? true : false;
			case Keys.RIGHT:
				return prevDirection.equals(Keys.LEFT)	? true : false;
			default:
				return true;
		}
	}
	
	public void setDefaultValues() {
		setPosition(0, 0);

		direction = Keys.RIGHT;
		prevDirection = "";

		tails = new ArrayDeque<>();
		recentRemovedTail = null;
		
		hasApple = false;
		isDead = false;
	}
	
	private void setPlayerImage() {
		BodyImage.Head.Up					= image.getBufferedImage("snake_head_up");
		BodyImage.Head.Down					= image.getBufferedImage("snake_head_down");
		BodyImage.Head.Left					= image.getBufferedImage("snake_head_left");
		BodyImage.Head.Right				= image.getBufferedImage("snake_head_right");

		BodyImage.Head.UpWithTail			= image.getBufferedImage("snake_head_up_with_tail");
		BodyImage.Head.DownWithTail			= image.getBufferedImage("snake_head_down_with_tail");
		BodyImage.Head.LeftWithTail			= image.getBufferedImage("snake_head_left_with_tail");
		BodyImage.Head.RightWithTail		= image.getBufferedImage("snake_head_right_with_tail");

		BodyImage.Head.Dead.Up				= image.getBufferedImage("snake_head_dead_up");
		BodyImage.Head.Dead.Down			= image.getBufferedImage("snake_head_dead_down");
		BodyImage.Head.Dead.Left			= image.getBufferedImage("snake_head_dead_left");
		BodyImage.Head.Dead.Right			= image.getBufferedImage("snake_head_dead_right");

		BodyImage.Head.Dead.UpWithTail		= image.getBufferedImage("snake_head_dead_up_with_tail");
		BodyImage.Head.Dead.DownWithTail	= image.getBufferedImage("snake_head_dead_down_with_tail");
		BodyImage.Head.Dead.LeftWithTail	= image.getBufferedImage("snake_head_dead_left_with_tail");
		BodyImage.Head.Dead.RightWithTail	= image.getBufferedImage("snake_head_dead_right_with_tail");
		
		BodyImage.Head.Dead.UpToLeft		= image.getBufferedImage("snake_head_dead_up_to_left");
		BodyImage.Head.Dead.UpToRight		= image.getBufferedImage("snake_head_dead_up_to_right");
		
		BodyImage.Head.Dead.DownToLeft		= image.getBufferedImage("snake_head_dead_down_to_left");
		BodyImage.Head.Dead.DownToRight		= image.getBufferedImage("snake_head_dead_down_to_right");

		BodyImage.Head.Dead.LeftToUp		= image.getBufferedImage("snake_head_dead_left_to_up");
		BodyImage.Head.Dead.LeftToDown		= image.getBufferedImage("snake_head_dead_left_to_down");

		BodyImage.Head.Dead.RightToUp		= image.getBufferedImage("snake_head_dead_right_to_up");
		BodyImage.Head.Dead.RightToDown		= image.getBufferedImage("snake_head_dead_right_to_down");

		BodyImage.Horizontal				= image.getBufferedImage("snake_body_horizontal");
		BodyImage.Vertical					= image.getBufferedImage("snake_body_vertical");

		BodyImage.TopLeftCorner				= image.getBufferedImage("snake_body_top_left_corner");
		BodyImage.TopRightCorner			= image.getBufferedImage("snake_body_top_right_corner");
		BodyImage.BotLeftCorner				= image.getBufferedImage("snake_body_bottom_left_corner");
		BodyImage.BotRightCorner			= image.getBufferedImage("snake_body_bottom_right_corner");;

		BodyImage.Tail.Up					= image.getBufferedImage("snake_tail_up");
		BodyImage.Tail.Down					= image.getBufferedImage("snake_tail_down");
		BodyImage.Tail.Left					= image.getBufferedImage("snake_tail_left");
		BodyImage.Tail.Right				= image.getBufferedImage("snake_tail_right");
	}

	@Override
	public void setPosition(int x, int y) {
		setPrevX();
		setPrevY();

		super.setPosition(x, y);
	}

	public void setPrevX() {
		prevX  = getX();
	}

	public void setPrevY() {
		prevY = getY();
	}

	private void setToPrevPosition() {
		setPosition(getPrevX(), getPrevY());
	}
	
	private void setToPrevTails() {
		tails.addLast(recentRemovedTail);
		tails.pollFirst();
	}
	
	private void shiftTails() {
		increaseTail();

		if (hasApple)
			hasApple = false;
		else
			recentRemovedTail = tails.pollLast();
	}

	public void updatePosition() {
		String newDirection = keyHandler.getPressedKey();
		int x = getX();
		int y = getY();
		
		if (Keys.isValidDirection(newDirection) && tails.isEmpty())
			direction = newDirection; 
		else
			if (!isOppositeDirection(newDirection))
				direction = newDirection;

		switch (direction) {
			case Keys.UP:
				setPosition(x, --y);
				break;
			case Keys.DOWN:
				setPosition(x, ++y);
				break;
			case Keys.LEFT:
				setPosition(--x, y);
				break;
			case Keys.RIGHT:
				setPosition(++x, y);
				break;
		}
		
		prevDirection = direction;
		
		shiftTails();
	}
}
