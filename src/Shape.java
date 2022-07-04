import java.awt.Point;
import javax.swing.ImageIcon;
import java.net.*;
import java.awt.image.*;
import java.awt.*;

public class Shape extends ImageIcon {
	public int x;				// 모양의 위치 좌표
	public int y;				// 모양의 위치 좌표
	protected int xDirection;
	protected int yDirection;
	protected int xStart_lim;
	protected int xEnd_lim;
	protected int yStart_lim;
	protected int yEnd_lim;
	protected int steps;
	protected int size;		
	
	public Shape(URL imgURL, int x, int y, int size) {
		super (imgURL);
		this.x = x;
		this.y = y;
		this.size = size;
	}
	
	public void setBoundary(int xStart_lim, int xEnd_lim, int yStart_lim, int yEnd_lim) {
		this.xStart_lim = xStart_lim;
		this.xEnd_lim = xEnd_lim;
		this.yStart_lim = yStart_lim;
		this.yEnd_lim = yEnd_lim;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
	
	public void setSteps(int steps) {
		this.steps = steps;
	}
	
	public int getSteps() {
		return steps;
	}
	
	// 하나의 점이 이 모양과 충돌하였는지 (모양의 margin 거리안에 있는지)를 판단하는 함수
	public boolean collide (Point p2) {
		Point p = new Point(this.x + size/2, this.y + size/2);
		if (p.distance(p2) <= size/2) return true;
		return false;
	}
	
	public Point getCenterPoint () {
		return new Point(this.x + size/2, this.y + size/2);
	}
	
	// 해당 모양을 g에 출력해주는 메소드
	public void draw(Graphics g, ImageObserver io) {
		((Graphics2D)g).drawImage(this.getImage(), x, y, size, size, io);
	}

	public void move() {};
}
