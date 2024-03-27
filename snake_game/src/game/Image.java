package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Image {
	static final String CLASS_LOADER_PATH = "images/";
	static final String EXTENSION = ".png";

	public final int WIDTH;
	public final int HEIGHT;

	BufferedImage bufferedImage;

	public Image(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
	}
	
	 public BufferedImage getBufferedImage(String name) {
		try {
			return ImageIO.read(getClass().getClassLoader().getResourceAsStream(CLASS_LOADER_PATH + name + Image.EXTENSION));
		} catch (Exception e) {
			return null; 
		}
	}

	public BufferedImage getDefaultImage(int x, int y) {
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();

		g2.setColor(Color.WHITE);
		g2.fillRect(x, y, WIDTH, HEIGHT);

		return image;
	}

	public void setBufferedImage(String name) {
		this.bufferedImage = getBufferedImage(name);
	}
}
