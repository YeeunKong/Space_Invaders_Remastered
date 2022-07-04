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
	private final int STEPS = 10;	//��ü ������ ����
	private final int SPEED = 30;	//Ÿ�̸� �ֱ�
	private final int SIZE = 100;
	
	private MenuDialog dialog;
	int middle_x = WIN_WIDTH/2;		
	int middle_y = WIN_HEIGHT/2;
	double start_x = WIN_WIDTH * 0.5;	//coverPanel�� startBtn ��ġ
	double start_y = WIN_HEIGHT * 0.75;
	int heart_x = 50;
	int heart_y = 40;
	
	//attacker �����̴� ����
	private final int SECTION = 8;
	int attacker_xStart = 0;		
	int attacker_xEnd = WIN_WIDTH - 50;
	int attacker_yStart = WIN_HEIGHT / SECTION;
	int attacker_yEnd = (WIN_HEIGHT / SECTION) * 3;
	int attacker_x;	//attacker x��ǥ(��ġ)
	int attacker_y;	//attacker y��ǥ(��ġ)
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

	// ���� ���� ����
	public void go() {
		// GUI Setting
		btnStart.setBounds((int)start_x, (int)start_y, 350, 80); // btn ��ġ, ũ��
		lblScore.setBounds(WIN_WIDTH/3 * 2, 20, 400, 100); // Label ��ġ, ũ��
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

		// ������ ����
		btnStart.addActionListener(new GameStartListener());
		gamePanel.addKeyListener(new DirKeyListener());
		gamePanel.setFocusable(false);
		
		attackerList = new ArrayList<Attacker>();
		playerBulletList = new ArrayList<Bullet>();
		attackerBulletList = new ArrayList<Bullet>();
		heartList = new ArrayList<Shape>();

		// �޴� ���̾�α� ����
		dialog = new MenuDialog(this, "MENU");

		makePlayer();
		makeAttacker();
		makeHeart();
		
		timer = new Timer(SPEED, new TimerListener());
		dialog.setTimerInfo(timer);
		
	}

	//player ��ü ��������
	public void makePlayer() {
		player = new Player(getClass().getResource("/image/player.png"), middle_x, middle_y, SIZE);
		player.setBoundary(0, WIN_WIDTH, 0, WIN_HEIGHT);
		player.setSteps(STEPS);
	}
	
	//attacker ��ü ��������
	public void makeAttacker() {
		makeRandomLocation();
		Attacker attacker = new Attacker(getClass().getResource("/image/attacker.png"), attacker_x, attacker_y, SIZE);
		attacker.setBoundary(attacker_xStart, attacker_xEnd, attacker_yStart, attacker_yEnd);
		attacker.xDirection = 1;
		attacker.yDirection = -1;
		attacker.setSteps(STEPS);
	
		attackerList.add(attacker);
	}
	
	//bullet ��ü ��������
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
		
		lblWin.setBounds(WIN_WIDTH/5 - 100, WIN_HEIGHT/5 - 50, 700, 500); // Label ��ġ, ũ��
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
	
		lblLose.setBounds(WIN_WIDTH/5 - 100, WIN_HEIGHT/5 - 50, 700, 500); // Label ��ġ, ũ��
		lblLose.setFont(new Font("Dialog", Font.BOLD, 60));
		lblLose.setForeground(Color.RED);
		lblLose.setHorizontalAlignment(JLabel.CENTER);
		lblLose.setText("<html>GAME OVER!<br/>SCORE: " + Integer.toString(score) + "</html>");
		lblLose.setVisible(true);						
		gamePanel.add(lblLose);		
	}
	
	
	// coverPanel�� GAME START ��ư ������
	// 1. coverPanel->gamePanel�� ��ȯ
	// 2. gamePanel�� ��Ŀ�� �־� Ű �Է� ���� �� �ְ� ����
	class GameStartListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			coverPanel.setVisible(false);
			gamePanel.setVisible(true);
			frame.setContentPane(gamePanel); // contentpane�� gamePanel�� ����

			gamePanel.setFocusable(true); // gamePanel�� ��Ŀ�� ���� �� �ֵ��� ����
			gamePanel.requestFocus(); // gamePanel���� ��Ŀ���� �־� Ű �Է� ���� �� �ְ� ��
			timer.start();
		}
	}

	// Timer�� ���� �ֱ������� ����� ����
	// 1. ��ü�� �浹 ���� Ȯ��
	// 2. ��ü�� ������ ����
	class TimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			attacker_spawn_counter +=1;
			
			//Menu Dialog�� ReStart��ư
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
					
			//���� �ֱ� ������ attacker ����
			if (attacker_spawn_counter == 50) {	//���� 50
				makeAttacker();
				attacker_spawn_counter=0;
			}
			
			//��ü ������ ����
			player.move();	//player move
			
			if (attackerList != null && attackerList.size()>0) {
				for(Attacker a : attackerList) {	//attacker move
					a.shot_counter += 1;
					a.move();
					
					//attacker ��ü���� shot_counter�� 50�� �Ǹ� �Ѿ� �߻�
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
						attackerList.remove(a);	//�浹�� attacker ��ü �迭���� ����
						a = null;	//��ü ����
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
			
			// timer �ֱ⸶�� frame(ȭ�� ��ü)�� �ٽ� �׷���
			// ��ü ��ǥ�� �̵��� �� repaint-> ��ü�� �����̴� ��ó�� ����
			frame.repaint();
		}
	}

	//�� ���� Ű ���ÿ� �Է� �ޱ� ���� ��Ʈ ���� (�밢�� �̵� �����ϰ� �ϱ� ����)
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
			case KeyEvent.VK_SPACE:	//����
				makePlayerBullet();
				break;
			case KeyEvent.VK_ESCAPE:	//�޴�â ����
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
			g.drawImage(gameImage, 0, 0, getWidth(), getHeight(), this);	//���ȭ�� �׷���
			
			if (player != null) {
				player.draw(g, this);	//player �׷���
			}

			
			if (attackerList != null && attackerList.size()>0) {
				for(Attacker a : attackerList) {	//attacker �׷���
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
