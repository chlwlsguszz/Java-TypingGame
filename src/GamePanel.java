import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


//���� �г�, ���ӱ׶��� �гΰ� ��ǲ �гη� ����
//�ƹ�Ÿ �гΰ� ���ھ� �г��� ������
public class GamePanel extends JPanel {
	private JTextField input = new JTextField(40);
	private AvatarPanel avatarPanel;
	private ScorePanel scorePanel;
	protected GameGroundPanel gameGroundPanel;
	private int score; // ����
	private int energy; // ������
	protected int wordSize = 15; // ���� ũ��
	private int fallDelay = 500; // ���� ������score
	private int createDelay = 3000; // ���� ������
	protected boolean vibrateMode = false; // ���� ��� ����
	protected boolean blindMode = false;  // ����ε� ��� ����
	
	// ���� ������ STOP->START ������ ���� ���� �гΰ� ���� ��ư�� ����
	private JButton startBtn = null;
	protected void linkStartBtn(JButton startBtn) {
		this.startBtn = startBtn;
	}
	
	private UserRanking userRanking = new UserRanking(); // ���� ��ŷ ���� ���� 
	
	protected AudioSource audioSource = new AudioSource(); // ����� Ŭ�� ����
	
	protected TextSource textSource = new TextSource(); // �ܾ��� ���� ����
	private Vector<WordLabel> wordLabelVector = new Vector<>(); // �ܾ� ���̺� ����
	
	private class WordLabel extends JLabel { // �ܾ� ���̺� Ŭ����
		protected int x; 
		protected int y;
		private WordLabel thisLabel = null;
		private WordThread th = null;
		protected boolean blindLabel = false;
		
		public WordLabel(String word) {
			super(word);
			x = (int)(Math.random()*(gameGroundPanel.getWidth()-(wordSize*5+40)));
			y = 0;
			setForeground(Color.GREEN);
			setFont(new Font("GOTHIC",Font.PLAIN,wordSize));
			setSize(300,50);
			
			if(blindMode == true) { //  ����ε� ��尡 ���������� ����ε� ���̺� 1/20 Ȯ���� ����
				int r = (int)(Math.random()*10);
				if(r==0) {
					blindLabel = true;
					setForeground(Color.RED); // RED ������ ����
				}
			}
			
			thisLabel = this;
			th = new WordThread();
			th.start();
		}
		
		// ��� �����ϸ� �ٴڿ� ������ �������� ���ҽ�Ű�� ����
		private class WordThread extends Thread {
			public void run() {
				while(true) {
					y+=5;
					setLocation(x,y);
					try {sleep(fallDelay);} catch (InterruptedException e) {return;}
					if(y>=gameGroundPanel.getHeight()-20) 
						break;
				}
				energy-=10;
				scorePanel.changeScore(score, energy);
				if(energy<50) 
					avatarPanel.changeExpression("danger");
				if(energy<=0) gameOver();
				wordLabelVector.remove(thisLabel);
				gameGroundPanel.remove(thisLabel);
				gameGroundPanel.repaint();
			}
		}
	}
	
	
	public GamePanel(AvatarPanel avatarPanel, ScorePanel scorePanel) {
		this.scorePanel = scorePanel;
		this.avatarPanel = avatarPanel;
		score = 0;
		energy = 100;
		setLayout(new BorderLayout());
		gameGroundPanel = new GameGroundPanel();
		add(gameGroundPanel, BorderLayout.CENTER);
		add(new InputPanel(), BorderLayout.SOUTH);
		
		audioSource.playAudio("bgm");
	}
	
	// ���̵� ����
	protected void changeLevel(int fallDelay, int createDelay) {
		this.fallDelay = fallDelay;
		this.createDelay = createDelay;
	}
	
	// �ؽ�Ʈ ũ�� ����
	protected void setWordSize(int pt) {
		this.wordSize = pt;
	}
	
	protected void setMode(boolean vibrateMode, boolean blindMode) {
		this.vibrateMode = vibrateMode;
		this.blindMode = blindMode;
	}
	
	//���� ����
	public void startGame() {
		gameGroundPanel.repaint();
		gameGroundPanel.start();
		input.requestFocus();
		avatarPanel.changeExpression("reset");
		avatarPanel.changeExpression("normal");
		score = 0;
		energy = 100;
		scorePanel.changeScore(score, energy);
		audioSource.playAudio("button");
	}
	
	//���� ����
	public void gameOver() {
		avatarPanel.changeExpression("gameover");
		gameGroundPanel.end();
		gameGroundPanel.repaint();
		startBtn.setText("START");
		
		userRanking.saveRanking(avatarPanel.name, score);
		
		audioSource.playAudio("gameover");
		GameOverDialog dialog = new GameOverDialog((JFrame)getTopLevelAncestor(), "GAME OVER");
		dialog.getContentPane().setBackground(Color.black);
		dialog.setVisible(true);
	}
	
	//���� ������ ���� �� ��ŷ ��� ���̾�α�
	private class GameOverDialog extends JDialog {
		private GameOverDialog(JFrame frame, String title) {
			super(frame, title, false);
			setLocation(770,270);
			setSize(340,390);
			setLayout(null);
			
			JLabel label = new JLabel("GAME OVER");
			label.setForeground(Color.green);
			label.setFont(new Font("GOTHIC",Font.BOLD,40));
			label.setSize(300,40);
			label.setLocation(40,20);
			add(label);
			
			label = new JLabel(avatarPanel.name+" : "+score);
			label.setForeground(Color.green);
			label.setFont(new Font("GOTHIC",Font.BOLD,30));
			label.setSize(200,40);
			label.setLocation(70,60);
			add(label);
			
			for(int i=0;i<10;i++) {
				String name = userRanking.getUser(i).name;
				int score = userRanking.getUser(i).score;
				if(i!=9)
					label = new JLabel(i+1+".   "+name+" "+score);
				else
					label = new JLabel(i+1+". "+name+" "+score);
				label.setForeground(Color.green);
				label.setFont(new Font("GOTHIC",Font.BOLD,20));
				label.setSize(200,20);
				label.setLocation(80,100+20*i);
				add(label);
			}
			
			JButton okBtn = new JButton("Ȯ��");
			okBtn.setSize(60,20);
			okBtn.setLocation(130,320);
			add(okBtn);
			
			okBtn.addActionListener(new GameOverDialogAction());
		}
		
		//���̾�α׿��� Ȯ�� ��ư�� ������ üũ �ڽ� ���õ� �� ���� �гη� ����
		private class GameOverDialogAction implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		}
		
	}
	
	//���� ���� STOP ��ư
	public void gameStop() {
		gameGroundPanel.end();
		gameGroundPanel.repaint();
		audioSource.playAudio("button");
	}
	
	
	
	// �������� ���̺��� ���̴� �г�
	class GameGroundPanel extends JPanel {
		ImageIcon bgIcon = new ImageIcon("image/background/space.jpg");
		Image bgImage = bgIcon.getImage();
		
		GameThread gameThread; // ���̺� ���� ������
		VibrateThread vibrateThread; // ���� ������
		BlindThread blindThread; // ����ε� ������
		
		public GameGroundPanel() {
			setLayout(null);
		}
		
		// ��������� ����
		public void start() {
			gameThread = new GameThread();
			gameThread.start();
			
			if(vibrateMode==true) {
				vibrateThread = new VibrateThread();
				vibrateThread.start();
			}
			
			if(blindMode==true) {
				blindThread = new BlindThread();
				blindThread.start();
			}
		}
		
		// ��������� ���̰� ���� ���� 
		public void end() {
			for(int i=0;i<wordLabelVector.size();i++) 
				wordLabelVector.get(i).th.interrupt();
			wordLabelVector.clear();
			gameGroundPanel.removeAll();
			gameThread.interrupt();
			
			if(vibrateMode==true) {
				vibrateThread.interrupt();
			}
			
			if(blindMode==true) {
				blindThread.interrupt();
			}
		}
		
		// ���̺��� ��� ������
		private class GameThread extends Thread {
			public void run() {
				while(true) {
					WordLabel wordLabel = new WordLabel(textSource.get());
					wordLabelVector.add(wordLabel);
					add(wordLabel);
					try {sleep(createDelay);} catch (InterruptedException e) {return;}
				}
			}
		} 
		
		// ��Ÿ �� �������� 100ȸ ������Ŵ
		private class VibrateThread extends Thread {
			protected boolean vibrate = false;
			
			public void run() {
				while(true) {
					JFrame f = (JFrame)getTopLevelAncestor();
					if(vibrate == true) {
						for(int i=0;i<100;i++) {
							int x = 595+(int)(Math.random()*10);
							int y = 195+(int)(Math.random()*10);
							f.setLocation(x,y);
							try {sleep(5);} catch (InterruptedException e) {return;}
						}
						f.setLocation(600,200);
						vibrate=false;
					}
				}
			}
		}
		
		// ������ ���̺� ���� �� ��� ���̺��� 5�ʰ� ����
		private class BlindThread extends Thread {
			protected boolean blind = false;
			
			public void run() {
				while(true) {
					try {sleep(100);} catch (InterruptedException e) {return;} 
					if(blind == true) {
						for(int j=0;j<1000;j++) {
							for(int i=0;i<wordLabelVector.size();i++)  
								wordLabelVector.get(i).setForeground(Color.BLACK);
							try {sleep(5);} catch (InterruptedException e) {return;}
						}
						for(int i=0;i<wordLabelVector.size();i++) { 
							if(wordLabelVector.get(i).blindLabel==true)
								wordLabelVector.get(i).setForeground(Color.RED);
							else
								wordLabelVector.get(i).setForeground(Color.GREEN);
						}
						blind = false;
					}
				}
			}
		}
		
		// ��� �׸���, ���� ���� �� START ǥ�� �׸�
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(bgImage,0,0,getWidth(),getHeight(),null);
			if(startBtn.getText().equals("START")) {
				g.setColor(Color.green);
				g.drawString("PRESS START BUTTON TO PLAY", 20, 30);
			}
		}
	}
	
	// ����� Ű���� �Է� �г�
	class InputPanel extends JPanel {
		
		public InputPanel() {
			setLayout(new FlowLayout());
			setBackground(Color.BLACK);
			add(input);
			
			input.addActionListener(new InputActionListener());
		}
		
		private class InputActionListener implements ActionListener {
			// ����� �Է��� �ܾ� ���Ϳ� �ִ��� Ȯ��, ������ ����
			public boolean checkInput(String text) {
				for(int i=0;i<wordLabelVector.size();i++) {
					if(text.equals(wordLabelVector.get(i).getText())) {
						gameGroundPanel.remove(wordLabelVector.get(i));
						gameGroundPanel.repaint();
						
						if(wordLabelVector.get(i).blindLabel == true) { // ����ε� ���̺��̸� ��� ���̺��� ������
							gameGroundPanel.blindThread.blind = true;   // ���� 40 �߰� (�� ���� 50)
							score+=40;
						}
						wordLabelVector.get(i).th.interrupt();
						wordLabelVector.remove(i);
						return true;
					}
				}
				return false;
			}
			public void actionPerformed(ActionEvent e) {
				if(startBtn.getText().equals("STOP")) { // ������ ����������
					JTextField input = (JTextField)e.getSource();
					if(checkInput(input.getText())) { // ���߱� ����, ���� ���� �� ������ ȸ��
						score += 10;
						if(energy!=100) 
							energy+=5;
						scorePanel.changeScore(score, energy);
						if(energy>=50) 
							avatarPanel.changeExpression("normal"); // ü���� ������ ���� ǥ��
						avatarPanel.changeExpression("correct"); // ���� �� ǥ��
						
						audioSource.playAudio("correct");
					}
					else { // ���� �� ü�� -10
						energy-=10;
						scorePanel.changeScore(score, energy);
						if(energy<=0) {
							gameOver();
							return;
						}
						if(energy<50) 
							avatarPanel.changeExpression("danger"); // ü���� ������ ���� ǥ��
						avatarPanel.changeExpression("wrong"); // ���� �� ǥ��
						
						if(vibrateMode == true)  // ���� ��尡 �߰����� ���, ��Ÿ �� ���� �߻�
							gameGroundPanel.vibrateThread.vibrate=true;
						
						audioSource.playAudio("wrong");
					}
					input.setText(""); // �ؽ�Ʈ�ʵ� �ʱ�ȭ
				}
			}
		}
	}
}
