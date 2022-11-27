import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

// �� �гο����� �ƹ�Ÿ �̹����� �����
// ���丮 ���� �ƹ�Ÿ�� ǥ�� 5���� �ش��ϴ� ������ ����
// File ��ü�� �о ���Ϳ� ���� �� ��Ȳ�� �°� �����
public class AvatarPanel extends JPanel {
	Vector<Image> imageVector = new Vector<>(); // �ƹ�Ÿ �̹������� ��� ����
	String filePath = null;
	File imageDir = null;
	File[] imageFiles = null;
	Image currentImage = null;
	ExpressionThread expressionThread = null;
	protected String name = null;
	
	public AvatarPanel(String name) {
		this.name = name;
		setLayout(null);
		
		JLabel nameLabel = new JLabel(name);
		nameLabel.setOpaque(true);
		nameLabel.setBackground(Color.BLACK);
		nameLabel.setForeground(Color.GREEN);
		nameLabel.setFont(new Font("GOTHIC",Font.BOLD,15));
		nameLabel.setSize(120,20);
		nameLabel.setLocation(25,170);
		nameLabel.setHorizontalAlignment(JLabel.CENTER);
		add(nameLabel);
		
		setAvatar("astronaut");
		currentImage = imageVector.get(3);
		expressionThread = new ExpressionThread();
		expressionThread.start();
	}
	
	// �ƹ�Ÿ�� ����, �ܺο��� ȣ��
	protected void setAvatar(String text) {
		if(text.equals("astronaut")) {
			filePath = "image/avatar/astronaut";
		}
		else if(text.equals("alien")) {
			filePath = "image/avatar/alien";
		}
		imageDir = new File(filePath);
		imageFiles = imageDir.listFiles();
		
		imageVector.clear(); // ���͸� �ѹ� ���
		
		// ���丮���� �̹����� ��� �����ͼ� �̹��� ���� ����
		for(int i=0;i<imageFiles.length;i++) 
			imageVector.add(new ImageIcon(imageFiles[i].getPath()).getImage());
	}
	
	// ǥ���� ����, ���Ϳ��� ���ϸ� ���ĺ� ������ ���� ������ ǥ���� �ε����� ������ ����
	// 0:correct, 1:danger, 2:gameover, 3:normal, 4:wrong
	public void changeExpression(String text) {
		switch(text) {
			case "normal" : expressionThread.danger = false; break;
			case "danger" : expressionThread.danger = true; break;
			case "gameover" : expressionThread.gameover = true; break;
			case "reset" : expressionThread.gameover = false; break;
			case "correct" : expressionThread.correct = true; break;
			case "wrong" : expressionThread.wrong = true; break;
		}
	}
	
	//���߰ų� Ʋ�� ǥ���� ��� 0.5�ʸ� ���
	private class ExpressionThread extends Thread {
		protected boolean correct = false;
		protected boolean wrong = false;
		protected boolean danger = false;
		protected boolean gameover = false;
		
		public void run() {
			while(true) {
				if(correct==true) {
					currentImage = imageVector.get(0);
					repaint();
					try {sleep(500);} catch (InterruptedException e) {return;}
					correct=false;
				}
				else if(wrong==true) {
					currentImage = imageVector.get(4);
					repaint();
					try {sleep(500);} catch (InterruptedException e) {return;}
					wrong=false;
				}
				
				if(imageVector.size()==5) { // �̹��� �ε��� ������
					if(gameover==true)
						currentImage = imageVector.get(2);
					else if(danger==true)
						currentImage = imageVector.get(1);
					else // normal
						currentImage = imageVector.get(3);
					repaint();
				}
				
				
				try {sleep(50);} catch (InterruptedException e) {return;}
			}
		}
	}
	
	// �ƹ�Ÿ �׸���
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(currentImage, 0, 0, getWidth(), getHeight(), null);
	}
		
	
}
