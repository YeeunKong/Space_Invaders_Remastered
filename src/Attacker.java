import java.net.URL;

public class Attacker extends Shape{
	boolean isHit = false;
	int shot_counter = 0;
	boolean isShot = false;
	
	public Attacker(URL imgURL, int x, int y, int size) {
		super (imgURL, x, y, size);
	}
	
	public boolean shot() {
		if(shot_counter == 40) {
			this.isShot = true;
			shot_counter = 0;
		}
		return isShot;
	}
	
	public void move() {
		if (xDirection > 0 && x >= xEnd_lim) {
			xDirection = -1;
		}
		if (xDirection < 0 && x <= 0) {
			xDirection = 1;
		}
		x += (xDirection * steps);

		if (yDirection > 0 && y >= yEnd_lim) {
			yDirection = -1;
		}
		if (yDirection < 0 && y <= yStart_lim) {
			yDirection = 1;
		}
		y += (yDirection * steps);
	}
}
