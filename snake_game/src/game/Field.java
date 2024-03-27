package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Field {
	private FieldImage[][] images;

	private final int COL;
	private final int ROW;
	private final int NUM_OF_FIELD_IMAGE = 3;
	private final Image image;
	
	private class FieldImage extends Position {
		final BufferedImage image;
		
		public FieldImage(BufferedImage image, int x, int y) {
			this.image = image;
			this.setPosition(x, y);
		}
	}

	public Field(int COL, int ROW, int imageSize) {
		this.COL = COL;
		this.ROW = ROW;

		images = new FieldImage[ROW][COL];
		image = new Image(imageSize, imageSize);
		
		setFieldImage();
	}
	
	public void draw(Graphics2D g2) {
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				FieldImage image = images[i][j];

				if (image.image == null) {
					g2.setColor(Color.white);
					g2.fillRect(image.getX(), image.getY(), this.image.WIDTH, this.image.HEIGHT);
				} else
					g2.drawImage(image.image, image.getX(), image.getY(), this.image.WIDTH, this.image.HEIGHT, null);
			}
		}
	}

	public void setFieldImage() {
		int x = 0;
		int y = 0;
		String fileNamePrefix = "field_";

		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				int fileNum = (int)(Math.random() * NUM_OF_FIELD_IMAGE) + 1;
				BufferedImage image = this.image.getBufferedImage(fileNamePrefix + fileNum);
				images[i][j] = new FieldImage(image, x, y);

				x += this.image.WIDTH;
			}

			x = 0;
			y += this.image.HEIGHT;
		}
	}	
}