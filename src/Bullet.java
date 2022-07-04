import java.net.URL;

public class Bullet extends Shape {
	public Bullet(URL imgURL, int x, int y, int size) {
		super (imgURL, x, y, size);
	}
	
	public void move() {
		y += (yDirection * steps);
	}
	
}
