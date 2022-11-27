import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

// 이름을 입력 받고 게임을 시작하는 메인 프레임
public class MainFrame extends JFrame {
	
	public MainFrame() {
		setTitle("Endless Typing");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocation(600,200);
		setResizable(false);
		setContentPane(new MainPanel());
		
		repaint();
		setVisible(true);
	}
	
	private class MainPanel extends JPanel {
		ImageIcon icon = new ImageIcon("image/background/main.png");
		Image image = icon.getImage();
		JTextField textField;
		
		public MainPanel() {
			setLayout(null);
		
			JLabel label = new JLabel("ID");
			label.setSize(60,40);
			label.setFont(new Font("GOTHIC",Font.BOLD,20));
			label.setLocation(280,360);
			label.setForeground(Color.green);
			add(label);
			
			textField = new JTextField("GUEST");
			textField.setSize(170,40);
			textField.setLocation(310,360);
			textField.setHorizontalAlignment(JTextField.CENTER);
			textField.setForeground(Color.green);
			textField.setBackground(Color.black);
			add(textField);
			
			JButton startBtn = new JButton("START");
			startBtn.setSize(80,20);
			startBtn.setLocation(350,420);
			startBtn.setForeground(Color.green);
			startBtn.setBackground(Color.black);
			startBtn.addActionListener(new StartAction());
			add(startBtn);
		
		}
		
		// 버튼을 누르면 새 프레임을 열고 이 프레임은 종료
		private class StartAction implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				String name = textField.getText();
				GameApp.run(name);
				dispose();
			}
		}
		public void paintComponent(Graphics g) {
			super.paintComponents(g);
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		}
	}
}
