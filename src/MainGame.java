import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.*;
import javax.swing.event.*;

import java.util.ArrayList;

public class MainGame extends JFrame {
	private final int WIN_WIDTH = 800;
	private final int WIN_HEIGHT = 1000;
	private final int STEPS = 10;	//객체 움직임 단위
	private final int SPEED = 30;	//타이머 주기
	private final int SIZE = 100;
	
	private MenuDialog dialog;
	int middle_x = WIN_WIDTH/2;		
	int middle_y = WIN_HEIGHT/2;
	double start_x = WIN_WIDTH * 0.5;	//coverPanel의 startBtn 위치
	double start_y = WIN_HEIGHT * 0.75;
	int heart_x = 50;
	int heart_y = 40;
	
	//attacker 움직이는 범위
	private final int SECTION = 8;
	int attacker_xStart = 0;		
	int attacker_xEnd = WIN_WIDTH - 50;
	int attacker_yStart = WIN_HEIGHT / SECTION;
	int attacker_yEnd = (WIN_HEIGHT / SECTION) * 3;
	int attacker_x;	//attacker x좌표(위치)
	int attacker_y;	//attacker y좌표(위치)
	int attacker_spawn_counter=0;
	
	int bullet_x;
	int bullet_y;
	
	int score=0;
	int player_chance = 3;
	
	JFrame frame = new JFrame();
	CoverPanel coverPanel = new CoverPanel();
	GamePanel gamePanel = new GamePanel();
	JButton btnStart = new JButton("GAME START");
	JLabel lblScore = new JLabel("SCORE : " + Integer.toString(score));
	JLabel lblWin = new JLabel();
	JLabel lblLose = new JLabel();
	
	ArrayList<Attacker> attackerList;
	ArrayList<Bullet> playerBulletList;
	ArrayList<Bullet> attackerBulletList;
	ArrayList<Shape> heartList;
	Player player;
	Timer timer;
	

	public static void main(String[] args) {
		new MainGame().go();
	}

	// 게임 동작 구현
	public void go() {
		// GUI Setting
		btnStart.setBounds((int)start_x, (int)start_y, 350, 80); // btn 위치, 크기
		lblScore.setBounds(WIN_WIDTH/3 * 2, 20, 400, 100); // Label 위치, 크기
		lblScore.setFont(new Font("Dialog", Font.BOLD, 45));
		lblScore.setForeground(Color.yellow);
		
		coverPanel.add(btnStart);
		coverPanel.setLayout(null);
		coverPanel.setVisible(true);
		
		ImageIcon st_img_icon = new ImageIcon(getClass().getResource("/image/game_start.png"));
		Image img = st_img_icon.getImage();
		img = img.getScaledInstance(350, 80, Image.SCALE_AREA_AVERAGING);
		st_img_icon = new ImageIcon(img);
		btnStart.setIcon(st_img_icon);
		btnStart.setBorderPainted(false);
		btnStart.setContentAreaFilled(false);
				
		gamePanel.setLayout(null);
		gamePanel.add(lblScore);
		gamePanel.setVisible(false);

		frame.add(coverPanel);
		frame.add(gamePanel);

		frame.setContentPane(coverPanel);
		frame.setSize(WIN_WIDTH, WIN_HEIGHT);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 리스너 부착
		btnStart.addActionListener(new GameStartListener());
		gamePanel.addKeyListener(new DirKeyListener());
		gamePanel.setFocusable(false);
		
		attackerList = new ArrayList<Attacker>();
		playerBulletList = new ArrayList<Bullet>();
		attackerBulletList = new ArrayList<Bullet>();
		heartList = new ArrayList<Shape>();

		// 메뉴 다이얼로그 생성
		dialog = new MenuDialog(this, "MENU");

		makePlayer();
		makeAttacker();
		makeHeart();
		
		timer = new Timer(SPEED, new TimerListener());
		dialog.setTimerInfo(timer);
		
	}

	//player 객체 생성해줌
	public void makePlayer() {
		player = new Player(getClass().getResource("/image/player.png"), middle_x, middle_y, SIZE);
		player.setBoundary(0, WIN_WIDTH, 0, WIN_HEIGHT);
		player.setSteps(STEPS);
	}
	
	//attacker 객체 생성해줌
	public void makeAttacker() {
		makeRandomLocation();
		Attacker attacker = new Attacker(getClass().getResource("/image/attacker.png"), attacker_x, attacker_y, SIZE);
		attacker.setBoundary(attacker_xStart, attacker_xEnd, attacker_yStart, attacker_yEnd);
		attacker.xDirection = 1;
		attacker.yDirection = -1;
		attacker.setSteps(STEPS);
	
		attackerList.add(attacker);
	}
	
	//bullet 객체 생성해줌
	public void makePlayerBullet() {
		
		bullet_x = player.getX();
		bullet_y = player.getY();
		
		Bullet bullet = new Bullet(getClass().getResource("/image/player_bullet.png"), bullet_x, bullet_y, SIZE);
		bullet.yDirection = -1;
		bullet.setSteps(STEPS * 2);
		
		playerBulletList.add(bullet);
	}
	
	public void makeAttackerBullet(Attacker att) {	
		bullet_x = att.getX();
		bullet_y = att.getY();
		
		Bullet bullet = new Bullet(getClass().getResource("/image/attacker_bullet.png"), bullet_x, bullet_y, SIZE/2);
		bullet.yDirection = 1;
		bullet.setSteps(STEPS * 2);
		
		attackerBulletList.add(bullet);
	}
	
	public void makeHeart() {
		
		for (int i = 0; i < 3; i++) {
			Shape heart = new Shape(getClass().getResource("/image/heart.png"), heart_x, heart_y, SIZE / 2);
			heartList.add(heart);
			heart_x += SIZE/2 + 20;
		}
		
	}
	
	public void makeRandomLocation() {
		attacker_x = (int)(Math.random() * WIN_WIDTH - 100);
		attacker_y = (int)(Math.random() * (WIN_HEIGHT / SECTION * 2)) + SECTION;
	}
	
	public void gameWin() {
		timer.stop();
		attackerList.clear();
		attackerBulletList.clear();
		playerBulletList.clear();
		
		lblScore.setVisible(false);
		//lblLose.setVisible(false);
		
		lblWin.setBounds(WIN_WIDTH/5 - 100, WIN_HEIGHT/5 - 50, 700, 500); // Label 위치, 크기
		lblWin.setFont(new Font("Dialog", Font.BOLD, 60));
		lblWin.setForeground(Color.CYAN);
		lblWin.setHorizontalAlignment(JLabel.CENTER);
		lblWin.setText("<html>YOU WIN!<br/>SCORE: " + Integer.toString(score) + "</html>");
		lblWin.setVisible(true);
		gamePanel.add(lblWin);
	}
	
	public void gameLose() {
		ImageIcon die_icon = new ImageIcon(getClass().getResource("/image/die.png"));
		Image dieImg = die_icon.getImage();
		player.setImage(dieImg);
		timer.stop();
		attackerList.clear();
		attackerBulletList.clear();
		playerBulletList.clear();
		
		lblScore.setVisible(false);
		lblWin.setVisible(false);
	
		lblLose.setBounds(WIN_WIDTH/5 - 100, WIN_HEIGHT/5 - 50, 700, 500); // Label 위치, 크기
		lblLose.setFont(new Font("Dialog", Font.BOLD, 60));
		lblLose.setForeground(Color.RED);
		lblLose.setHorizontalAlignment(JLabel.CENTER);
		lblLose.setText("<html>GAME OVER!<br/>SCORE: " + Integer.toString(score) + "</html>");
		lblLose.setVisible(true);						
		gamePanel.add(lblLose);		
	}
	
	
	// coverPanel의 GAME START 버튼 누르면
	// 1. coverPanel->gamePanel로 전환
	// 2. gamePanel에 포커스 주어 키 입력 받을 수 있게 설정
	class GameStartListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			coverPanel.setVisible(false);
			gamePanel.setVisible(true);
			frame.setContentPane(gamePanel); // contentpane이 gamePanel로 변경

			gamePanel.setFocusable(true); // gamePanel이 포커스 받을 수 있도록 설정
			gamePanel.requestFocus(); // gamePanel에게 포커스를 주어 키 입력 받을 수 있게 함
			timer.start();
		}
	}

	// Timer에 의해 주기적으로 실행될 내용
	// 1. 객체의 충돌 여부 확인
	// 2. 객체의 움직임 구현
	class TimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			attacker_spawn_counter +=1;
			
			//Menu Dialog의 ReStart버튼
			if (dialog.order == 1) {
				player = null;
				score = 0;
				player_chance = 3;
				attackerList.clear();
				attackerBulletList.clear();
				playerBulletList.clear();
				lblScore.setText("SCORE : " + Integer.toString(score));
				lblScore.setVisible(true);
				lblWin.setVisible(false);
				lblLose.setVisible(false);
				dialog.order = 0;
				makeAttacker();
				makePlayer();
			}
					
			//일정 주기 단위로 attacker 생성
			if (attacker_spawn_counter == 50) {	//원래 50
				makeAttacker();
				attacker_spawn_counter=0;
			}
			
			//객체 움직임 구현
			player.move();	//player move
			
			if (attackerList != null && attackerList.size()>0) {
				for(Attacker a : attackerList) {	//attacker move
					a.shot_counter += 1;
					a.move();
					
					//attacker 객체마다 shot_counter가 50이 되면 총알 발사
					if(a.shot() == true) {	
						makeAttackerBullet(a);
						a.shot_counter = 0;
						a.isShot = false;
					}
					
					for (Bullet pb: playerBulletList) {
						if (a.collide(pb.getCenterPoint())) {
							a.isHit = true;						
						}
					}
					if (a.isHit) {
						score += 100;
						lblScore.setText("SCORE : " + Integer.toString(score));
						attackerList.remove(a);	//충돌한 attacker 객체 배열에서 제거
						a = null;	//객체 해제
						break;
					}
				}
			}
			
			if (playerBulletList != null && playerBulletList.size()>0) {
				for(Bullet pb : playerBulletList) {
					pb.move();
				}
			}
			
			if (attackerBulletList != null && attackerBulletList.size()>0) {
				for(Bullet ab : attackerBulletList) {
					ab.move();
					if (player.collide(ab.getCenterPoint())) {
						player.isHit = true;
						attackerBulletList.remove(ab);
						ab = null;
						break;
					}
				}
				if (player.isHit) {				
					//GAME LOSE
					if(player_chance == 1) {
						gameLose();
					}						
					if (player!= null) {
						player_chance--;
						player.isHit = false;
					}
				}
			}
			
			//GAME WIN
			if(player_chance > 1 && attackerList.size() == 0) {
				gameWin();			
			}	
			
			// timer 주기마다 frame(화면 전체)을 다시 그려줌
			// 객체 좌표가 이동한 후 repaint-> 객체가 움직이는 것처럼 보임
			frame.repaint();
		}
	}

	//두 개의 키 동시에 입력 받기 위해 비트 연산 (대각선 이동 가능하게 하기 위함)
	class DirKeyListener implements KeyListener {
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				player.up_pressed = true;
				break;
			case KeyEvent.VK_DOWN:
				player.down_pressed = true;
				break;
			case KeyEvent.VK_LEFT:
				player.left_pressed = true;
				break;
			case KeyEvent.VK_RIGHT:
				player.right_pressed = true;
				break;
			case KeyEvent.VK_SPACE:	//공격
				makePlayerBullet();
				break;
			case KeyEvent.VK_ESCAPE:	//메뉴창 열기
				dialog.setVisible(true);
				dialog.repaint();
				
				player.up_pressed = false;
				player.down_pressed = false;
				player.left_pressed = false;
				player.right_pressed = false;
				
				timer.stop();
				break;
			}
		}

		public void keyTyped(KeyEvent event) {
		}

		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				player.up_pressed = false;
				break;
			case KeyEvent.VK_DOWN:
				player.down_pressed = false;
				break;
			case KeyEvent.VK_LEFT:
				player.left_pressed = false;
				break;
			case KeyEvent.VK_RIGHT:
				player.right_pressed = false;
				break;
			}
		}
	}

	class CoverPanel extends JPanel {
		Image coverImage = new ImageIcon("src/image/background.png").getImage();

		public void paintComponent(Graphics g) {
			g.drawImage(coverImage, 0, 0, getWidth(), getHeight(), this);
		}
	}

	class GamePanel extends JPanel {
		Image gameImage = new ImageIcon("src/image/background2.jpg").getImage();

		public void paintComponent(Graphics g) {
			g.drawImage(gameImage, 0, 0, getWidth(), getHeight(), this);	//배경화면 그려줌
			
			if (player != null) {
				player.draw(g, this);	//player 그려줌
			}

			
			if (attackerList != null && attackerList.size()>0) {
				for(Attacker a : attackerList) {	//attacker 그려줌
					a.draw(g, this);
				}
			}
			
			if (playerBulletList != null && playerBulletList.size()>0) {
				for(Bullet pb : playerBulletList) {
					pb.draw(g, this);
				}
			}
			
			if (attackerBulletList != null && attackerBulletList.size()>0) {
				for(Bullet ab : attackerBulletList) {
					ab.draw(g, this);
				}
			}
			
			
			if (heartList != null && heartList.size()>0) {
				for(int i = 0; i < player_chance; i++) {
					Shape tmp_heart = heartList.get(i);
					tmp_heart.draw(g, this);
				}
			}
		}
	}

}
