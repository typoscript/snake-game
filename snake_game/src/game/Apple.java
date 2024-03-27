package game;

import java.awt.Graphics2D;

public class Apple extends GameObject {
	public Apple(int imageSize) {
		setImageSize(imageSize);
		image.setBufferedImage("apple");
	}
	
	public void draw(Graphics2D g2) {
		if (image.bufferedImage == null)
			image.bufferedImage = this.image.getDefaultImage(getX(), getY());

		g2.drawImage(image.bufferedImage,
				getX() * image.WIDTH, getY() * image.HEIGHT,
				image.WIDTH, image.HEIGHT,
				null);
	}
}