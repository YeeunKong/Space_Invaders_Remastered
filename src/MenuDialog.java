import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

//���� �� ESC ������ �� ������ �޴�â
//�޴�â���� ����ϱ�/�ٽý����ϱ�/�����ϱ� ��ư�� ����

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
		
		//��ư�� �̹��� �ֱ�
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

		//��ư ��ġ, ũ�� ����
		btnCont.setBounds(btnLocation_x, btnLocation_y, btnSize_x, btnSize_y);
		btnRestart.setBounds(btnLocation_x, btnLocation_y+ 110, btnSize_x, btnSize_y);
		btnEnd.setBounds(btnLocation_x, btnLocation_y+ 220, btnSize_x, btnSize_y);

		//���̾�α׿� ��ư �ޱ�
		
		dialogPanel.add(btnCont);
		dialogPanel.add(btnRestart);
		dialogPanel.add(btnEnd);
		
		this.add(dialogPanel);
		this.setContentPane(dialogPanel);
		
		//��ư�� ������ �ޱ�
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
			//game �̾ ���
			setVisible(false);	//dialog ����(�Ⱥ��̰� ����)
			timer.start();
		}
	}
	class RestartListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			//timer �ʱ�ȭ, game �ٽý���
			setVisible(false);	//dialog ����(�Ⱥ��̰� ����)
			order = 1;
			
			
			timer.start();
		}
	}
	class EndListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			System.exit(0);	//���� ����(���α׷� ����)
		}
	}
	class DialogPanel extends JPanel{
		Image dialogImage = new ImageIcon("src/image/pause.png").getImage();
		
		public void paintComponent(Graphics g) {
			g.drawImage(dialogImage, 0, 0, getWidth(), getHeight(), this);
		}
	}
	
}
