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


//게임 패널, 게임그라운드 패널과 인풋 패널로 구성
//아바타 패널과 스코어 패널을 관리함
public class GamePanel extends JPanel {
	private JTextField input = new JTextField(40);
	private AvatarPanel avatarPanel;
	private ScorePanel scorePanel;
	protected GameGroundPanel gameGroundPanel;
	private int score; // 점수
	private int energy; // 에너지
	protected int wordSize = 15; // 글자 크기
	private int fallDelay = 500; // 낙하 딜레이score
	private int createDelay = 3000; // 생성 딜레이
	protected boolean vibrateMode = false; // 진동 모드 여부
	protected boolean blindMode = false;  // 블라인드 모드 여부
	
	// 게임 오버시 STOP->START 변경을 위해 게임 패널과 시작 버튼을 연결
	private JButton startBtn = null;
	protected void linkStartBtn(JButton startBtn) {
		this.startBtn = startBtn;
	}
	
	private UserRanking userRanking = new UserRanking(); // 유저 랭킹 벡터 생성 
	
	protected AudioSource audioSource = new AudioSource(); // 오디오 클립 생성
	
	protected TextSource textSource = new TextSource(); // 단어장 벡터 생성
	private Vector<WordLabel> wordLabelVector = new Vector<>(); // 단어 레이블 벡터
	
	private class WordLabel extends JLabel { // 단어 레이블 클래스
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
			
			if(blindMode == true) { //  블라인드 모드가 켜져있을때 블라인드 레이블 1/20 확률로 생성
				int r = (int)(Math.random()*10);
				if(r==0) {
					blindLabel = true;
					setForeground(Color.RED); // RED 색으로 구분
				}
			}
			
			thisLabel = this;
			th = new WordThread();
			th.start();
		}
		
		// 계속 낙하하며 바닥에 닿으면 에너지를 감소시키고 삭제
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
	
	// 난이도 변경
	protected void changeLevel(int fallDelay, int createDelay) {
		this.fallDelay = fallDelay;
		this.createDelay = createDelay;
	}
	
	// 텍스트 크기 변경
	protected void setWordSize(int pt) {
		this.wordSize = pt;
	}
	
	protected void setMode(boolean vibrateMode, boolean blindMode) {
		this.vibrateMode = vibrateMode;
		this.blindMode = blindMode;
	}
	
	//게임 시작
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
	
	//게임 오버
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
	
	//게임 오버시 점수 및 랭킹 출력 다이얼로그
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
			
			JButton okBtn = new JButton("확인");
			okBtn.setSize(60,20);
			okBtn.setLocation(130,320);
			add(okBtn);
			
			okBtn.addActionListener(new GameOverDialogAction());
		}
		
		//다이얼로그에서 확인 버튼을 누르면 체크 박스 선택된 값 게임 패널로 전달
		private class GameOverDialogAction implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		}
		
	}
	
	//게임 도중 STOP 버튼
	public void gameStop() {
		gameGroundPanel.end();
		gameGroundPanel.repaint();
		audioSource.playAudio("button");
	}
	
	
	
	// 떨어지는 레이블이 보이는 패널
	class GameGroundPanel extends JPanel {
		ImageIcon bgIcon = new ImageIcon("image/background/space.jpg");
		Image bgImage = bgIcon.getImage();
		
		GameThread gameThread; // 레이블 생성 스레드
		VibrateThread vibrateThread; // 진동 스레드
		BlindThread blindThread; // 블라인드 스레드
		
		public GameGroundPanel() {
			setLayout(null);
		}
		
		// 스레드들을 구동
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
		
		// 스레드들을 죽이고 게임 종료 
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
		
		// 레이블을 계속 생성함
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
		
		// 오타 시 프레임을 100회 진동시킴
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
		
		// 붉은색 레이블 삭제 시 모든 레이블을 5초간 가림
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
		
		// 배경 그리기, 게임 시작 전 START 표시 그림
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(bgImage,0,0,getWidth(),getHeight(),null);
			if(startBtn.getText().equals("START")) {
				g.setColor(Color.green);
				g.drawString("PRESS START BUTTON TO PLAY", 20, 30);
			}
		}
	}
	
	// 사용자 키보드 입력 패널
	class InputPanel extends JPanel {
		
		public InputPanel() {
			setLayout(new FlowLayout());
			setBackground(Color.BLACK);
			add(input);
			
			input.addActionListener(new InputActionListener());
		}
		
		private class InputActionListener implements ActionListener {
			// 사용자 입력이 단어 벡터에 있는지 확인, 있으면 삭제
			public boolean checkInput(String text) {
				for(int i=0;i<wordLabelVector.size();i++) {
					if(text.equals(wordLabelVector.get(i).getText())) {
						gameGroundPanel.remove(wordLabelVector.get(i));
						gameGroundPanel.repaint();
						
						if(wordLabelVector.get(i).blindLabel == true) { // 블라인드 레이블이면 모든 레이블을 가리며
							gameGroundPanel.blindThread.blind = true;   // 점수 40 추가 (총 점수 50)
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
				if(startBtn.getText().equals("STOP")) { // 게임이 시작했을때
					JTextField input = (JTextField)e.getSource();
					if(checkInput(input.getText())) { // 맞추기 성공, 점수 증가 및 에너지 회복
						score += 10;
						if(energy!=100) 
							energy+=5;
						scorePanel.changeScore(score, energy);
						if(energy>=50) 
							avatarPanel.changeExpression("normal"); // 체력이 높으면 정상 표정
						avatarPanel.changeExpression("correct"); // 성공 시 표정
						
						audioSource.playAudio("correct");
					}
					else { // 실패 시 체력 -10
						energy-=10;
						scorePanel.changeScore(score, energy);
						if(energy<=0) {
							gameOver();
							return;
						}
						if(energy<50) 
							avatarPanel.changeExpression("danger"); // 체력이 낮으면 위험 표정
						avatarPanel.changeExpression("wrong"); // 실패 시 표정
						
						if(vibrateMode == true)  // 진동 모드가 추가됐을 경우, 오타 시 진동 발생
							gameGroundPanel.vibrateThread.vibrate=true;
						
						audioSource.playAudio("wrong");
					}
					input.setText(""); // 텍스트필드 초기화
				}
			}
		}
	}
}
