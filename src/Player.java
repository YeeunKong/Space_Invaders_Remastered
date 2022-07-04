import java.net.URL;

public class Player extends Shape {
	boolean up_pressed = false;
	boolean down_pressed = false;
	boolean left_pressed = false;
	boolean right_pressed = false;
	boolean isHit = false;
	int chance = 3;	//player 목숨 - 하트 개수
	
	public Player(URL imgURL, int x, int y, int size) {
		super(imgURL, x, y, size);
	}
	
	@Override
	public void move() {
		if (up_pressed) {
			if (y >= yStart_lim) {
				y-= steps;
			}
		}if (down_pressed) {
			if (y + size <= yEnd_lim) {
				y+= steps;
			}
		}if (left_pressed) {
			if (x >= xStart_lim) {
				x-= steps;
			}
		}if (right_pressed) {
			if (x + size <= xEnd_lim) {
				x+= steps;
			}
		}
	}
}
