import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

//게임 중 ESC 눌렀을 때 나오는 메뉴창
//메뉴창에는 계속하기/다시시작하기/종료하기 버튼이 있음

public class MenuDialog extends JDialog{
	JButton btnCont = new JButton();
	JButton btnRestart = new JButton();
	JButton btnEnd = new JButton();
	int btnLocation_x = 50;
	int btnLocation_y = 300;
	int btnSize_x = 300;
	int btnSize_y = 100;
	int order = 0;
	Timer timer;
	DialogPanel dialogPanel = new DialogPanel();
	
	public MenuDialog(JFrame frame, String title) {

		super(frame, title);
		setLayout(null);	
		
		dialogPanel.setLayout(null);
		
		//버튼에 이미지 넣기
		ImageIcon cont_icon = new ImageIcon(getClass().getResource("/image/continue.png"));
		Image contImg = cont_icon.getImage();
		contImg = contImg.getScaledInstance(btnSize_x, btnSize_y, Image.SCALE_AREA_AVERAGING);
		cont_icon = new ImageIcon(contImg);
		btnCont.setIcon(cont_icon);
		btnCont.setBorderPainted(false);
		btnCont.setContentAreaFilled(false);
		
		ImageIcon restart_icon = new ImageIcon(getClass().getResource("/image/restart.png"));
		Image restartImg = restart_icon.getImage();
		restartImg = restartImg.getScaledInstance(btnSize_x, btnSize_y, Image.SCALE_AREA_AVERAGING);
		restart_icon = new ImageIcon(restartImg);
		btnRestart.setIcon(restart_icon);
		btnRestart.setBorderPainted(false);
		btnRestart.setContentAreaFilled(false);
		
		ImageIcon end_icon = new ImageIcon(getClass().getResource("/image/quit.png"));
		Image endImg = end_icon.getImage();
		endImg = endImg.getScaledInstance(btnSize_x, btnSize_y, Image.SCALE_AREA_AVERAGING);
		end_icon = new ImageIcon(endImg);
		btnEnd.setIcon(end_icon);
		btnEnd.setBorderPainted(false);
		btnEnd.setContentAreaFilled(false);

		//버튼 위치, 크기 설정
		btnCont.setBounds(btnLocation_x, btnLocation_y, btnSize_x, btnSize_y);
		btnRestart.setBounds(btnLocation_x, btnLocation_y+ 110, btnSize_x, btnSize_y);
		btnEnd.setBounds(btnLocation_x, btnLocation_y+ 220, btnSize_x, btnSize_y);

		//다이얼로그에 버튼 달기
		
		dialogPanel.add(btnCont);
		dialogPanel.add(btnRestart);
		dialogPanel.add(btnEnd);
		
		this.add(dialogPanel);
		this.setContentPane(dialogPanel);
		
		//버튼에 리스너 달기
		btnCont.addActionListener(new ContListener());
		btnRestart.addActionListener(new RestartListener());
		btnEnd.addActionListener(new EndListener());
		
		setLocation(200, 200);
		setSize(400, 700);
		setVisible(false);
	}
	
	void setTimerInfo(Timer timer) {
		this.timer = timer;
	}
	
	class ContListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			//game 이어서 계속
			setVisible(false);	//dialog 꺼짐(안보이게 설정)
			timer.start();
		}
	}
	class RestartListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			//timer 초기화, game 다시시작
			setVisible(false);	//dialog 꺼짐(안보이게 설정)
			order = 1;
			
			
			timer.start();
		}
	}
	class EndListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			System.exit(0);	//게임 종료(프로그램 종료)
		}
	}
	class DialogPanel extends JPanel{
		Image dialogImage = new ImageIcon("src/image/pause.png").getImage();
		
		public void paintComponent(Graphics g) {
			g.drawImage(dialogImage, 0, 0, getWidth(), getHeight(), this);
		}
	}
	
}
