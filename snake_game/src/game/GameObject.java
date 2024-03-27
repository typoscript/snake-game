package game;

public class GameObject extends Position {
	protected Image image;
	
	public void setImageSize(int size) {
		image = new Image(size, size);
	}
}