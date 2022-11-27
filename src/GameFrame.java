import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

// ���� ��ü ������
// �ֻ�� ����, ���� ���� �г�, ���� �ƹ�Ÿ �г� + ���ھ� �гη� ����
// ���� �г��� �ƹ�Ÿ �гΰ� ���ھ� �г��� ������
public class GameFrame extends JFrame {
	private ScorePanel scorePanel;
	private AvatarPanel avatarPanel;
	private GamePanel gamePanel;
	private GameFrame gameFrame;
	
	public GameFrame(String name) {
		gameFrame = this;
		setTitle("Endless Typing");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocation(600,200);
		
		scorePanel = new ScorePanel();
		avatarPanel = new AvatarPanel(name);
		gamePanel = new GamePanel(avatarPanel, scorePanel);
		
		makeSplitPane();
		makeToolBar();
		setResizable(false);
		setVisible(true);
	}
	
	//���� �׶��� �гΰ� �ƹ�Ÿ�г�, ���ھ��г��� ����
	private void makeSplitPane() {
		JSplitPane hPane = new JSplitPane();
		getContentPane().add(hPane, BorderLayout.CENTER);
		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		hPane.setDividerLocation(600);
		hPane.setEnabled(false);
		hPane.setLeftComponent(gamePanel);
		
		JSplitPane pPane = new JSplitPane();
		pPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		pPane.setDividerLocation(200);
		pPane.setTopComponent(avatarPanel);
		pPane.setBottomComponent(scorePanel);
		hPane.setRightComponent(pPane);
	}
	
	// ��� ���ٿ��� 7���� ��ư�� ���� : ���� ����/���� , ���̵� ����, �ܾ� ���� ���� 
	// ���� ��� ����, �ƹ�Ÿ ����, �ؽ�Ʈ ũ�� ����, ������� �ѱ�/����
	private void makeToolBar() {
		JToolBar tBar = new JToolBar();
		getContentPane().add(tBar, BorderLayout.NORTH);
		tBar.setFloatable(false); // ���� ����
		
		JButton startBtn = new JButton("START");
		startBtn.setBackground(Color.red);
		tBar.add(startBtn);
		startBtn.addActionListener(new StartAction());
		// ���� ������ STOP->START ������ ���� ���� �гΰ� ���� ��ư�� ����
		gamePanel.linkStartBtn(startBtn); 
		
		JButton levelBtn = new JButton("EASY");
		levelBtn.setForeground(Color.GREEN);
		levelBtn.setBackground(Color.BLACK);
		tBar.add(levelBtn);
		levelBtn.addActionListener(new LevelAction());
		
		JButton wordBtn = new JButton("KOR");
		wordBtn.setForeground(Color.GREEN);
		wordBtn.setBackground(Color.BLACK);
		tBar.add(wordBtn);
		wordBtn.addActionListener(new WordAction());
		
		JButton modeBtn = new JButton("MODE");
		modeBtn.setForeground(Color.GREEN);
		modeBtn.setBackground(Color.BLACK);
		tBar.add(modeBtn);
		modeBtn.addActionListener(new ModeAction());
		
		JButton avatarBtn = new JButton("AVATAR");
		avatarBtn.setForeground(Color.GREEN);
		avatarBtn.setBackground(Color.BLACK);
		tBar.add(avatarBtn);
		avatarBtn.addActionListener(new AvatarAction());
		
		JButton sizeBtn = new JButton("�ؽ�Ʈ ũ��: 15pt");
		sizeBtn.setForeground(Color.GREEN);
		sizeBtn.setBackground(Color.BLACK);
		tBar.add(sizeBtn);
		sizeBtn.addActionListener(new SizeAction());
		
		JButton bgmBtn = new JButton("������� ����");
		bgmBtn.setForeground(Color.GREEN);
		bgmBtn.setBackground(Color.BLACK);
		tBar.add(bgmBtn);
		bgmBtn.addActionListener(new BgmAction());
	}
	
	//���� ����
	private class StartAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton thisBtn = (JButton)e.getSource();
			if(thisBtn.getText().equals("START")) {
				gamePanel.startGame();
				thisBtn.setText("STOP");
			}
			else {
				gamePanel.gameStop();
				thisBtn.setText("START");
			}
		}
	} 
	
	// ���̵� ����, �ܾ� ���̺��� ���� �ӵ��� ���� �ӵ��� ����
	private class LevelAction implements ActionListener {
		JButton levelBtn;
		public void actionPerformed(ActionEvent e) {
			levelBtn = (JButton)e.getSource();
			LevelDialog dialog = new LevelDialog(gameFrame, "���̵� ����");
			dialog.setVisible(true);
		}
		
		private class LevelDialog extends JDialog { 
			private LevelDialog(JFrame frame, String title) {
				super(frame, title, true); 
				setLocation(750,300);
				setSize(300,100);
				setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
				
				JButton easyBtn = new JButton("EASY");
				easyBtn.setBackground(Color.BLACK);
				easyBtn.setForeground(Color.GREEN);
				JButton normalBtn = new JButton("NORMAL");
				normalBtn.setBackground(Color.BLACK);
				normalBtn.setForeground(Color.ORANGE);
				JButton hardBtn = new JButton("HARD");
				hardBtn.setBackground(Color.BLACK);
				hardBtn.setForeground(Color.RED);
				add(easyBtn);
				add(normalBtn);
				add(hardBtn);
				
				LevelDialogAction levelDialogAction = new LevelDialogAction();
				
				easyBtn.addActionListener(levelDialogAction);
				normalBtn.addActionListener(levelDialogAction);
				hardBtn.addActionListener(levelDialogAction);
			}
			
			private class LevelDialogAction implements ActionListener {
				public void actionPerformed(ActionEvent e) {
					JButton thisBtn = (JButton)e.getSource();
					switch(thisBtn.getText()) {
					case "EASY" : 
						gamePanel.changeLevel(500, 3000); 
						levelBtn.setText("EASY");
						levelBtn.setForeground(Color.GREEN);
						break;
					case "NORMAL" : 
						gamePanel.changeLevel(350, 2000); 
						levelBtn.setText("NORMAL");
						levelBtn.setForeground(Color.ORANGE);
						break;
					case "HARD" : 
						gamePanel.changeLevel(200, 1000);
						levelBtn.setText("HARD");
						levelBtn.setForeground(Color.RED);
						break;
					}
					setVisible(false);
				}
			}
		}
	} 
	
	// �ܾ� ���� ����, ���� ���̾�α� ���
	private class WordAction implements ActionListener {

		public void actionPerformed(ActionEvent e) { // ���� ���̾�α� ���, ���� ���� ����
			JFileChooser chooser = new JFileChooser("word");
			FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt ����", "txt");
			chooser.setFileFilter(filter);
			int ret = chooser.showOpenDialog(null);
			if(ret == JFileChooser.APPROVE_OPTION) {
				String pathName = chooser.getSelectedFile().getPath(); // ���� ��θ�
				String fileName = chooser.getSelectedFile().getName();
				gamePanel.textSource.changeFile(pathName);
				JButton thisBtn = (JButton)e.getSource();
				thisBtn.setText(fileName.split("\\.")[0]); // .txt ���� �� ��ư �̸� ����
			}
		}
		
	} 
	
	// ��� ����, ���̾�α� �� üũ �ڽ� ����
	private class ModeAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			ModeDialog dialog = new ModeDialog(gameFrame, "��� ����");
			dialog.setVisible(true);
		}
		
		private class ModeDialog extends JDialog {
			JCheckBox vibrateBox = null;
			JCheckBox blindBox = null;
			private ModeDialog(JFrame frame, String title) {
				super(frame, title, true); 
				setLocation(750,300);
				setSize(250,130);
				setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
				
				vibrateBox = new JCheckBox("���� ���");
				vibrateBox.setFont(new Font("GOTHIC",Font.PLAIN,15));
				if(gamePanel.vibrateMode==true)
					vibrateBox.setSelected(true);
				blindBox = new JCheckBox("����ε� ���");
				if(gamePanel.blindMode==true)
					blindBox.setSelected(true);
				blindBox.setFont(new Font("GOTHIC",Font.PLAIN,15));
				
				add(vibrateBox);
				add(blindBox);
				JButton okBtn = new JButton("Ȯ��");
				add(okBtn);
				
				okBtn.addActionListener(new ModeDialogAction());
			}
			
			//���̾�α׿��� Ȯ�� ��ư�� ������ üũ �ڽ� ���õ� �� ���� �гη� ����
			private class ModeDialogAction implements ActionListener {
				public void actionPerformed(ActionEvent e) {
					gamePanel.setMode(vibrateBox.isSelected(), blindBox.isSelected());
					setVisible(false);
				}
			}
		}
	} 
	
	// �ƹ�Ÿ ����, ���̾�α׿� �̹��� ��ư 2�� ����
	private class AvatarAction implements ActionListener {
		private ImageIcon astronautIcon = new ImageIcon("image/avatar/astronaut_icon.png");
		private ImageIcon alienIcon = new ImageIcon("image/avatar/alien_icon.png");
		
		public void actionPerformed(ActionEvent e) {
			AvatarDialog dialog = new AvatarDialog(gameFrame, "�ƹ�Ÿ ����");
			dialog.setVisible(true);
		}
		
		private class AvatarDialog extends JDialog { 
			private JButton astronautButton = new JButton(astronautIcon); 
			private JButton alienButton = new JButton(alienIcon); 
			
			private AvatarDialog(JFrame frame, String title) {
				super(frame, title, true); 
				setLocation(750,300);
				setSize(250,170);
				setLayout(null);
				
				astronautButton.setLocation(5,5);
				astronautButton.setSize(100,100);
				alienButton.setLocation(130,5);
				alienButton.setSize(100,100);
				add(astronautButton); 
				JLabel astronautLabel = new JLabel("astronaut");
				JLabel alienLabel = new JLabel("alien");
				astronautLabel.setLocation(30,105);
				astronautLabel.setSize(60,20);
				alienLabel.setLocation(165,105);
				alienLabel.setSize(60,20);
				add(astronautLabel);
				add(alienLabel);
				
				astronautButton.addActionListener(new ActionListener() { 
					public void actionPerformed(ActionEvent e) {
						avatarPanel.setAvatar("astronaut"); 
						setVisible(false);
					}
				});
				add(alienButton); 
				alienButton.addActionListener(new ActionListener() { 
					public void actionPerformed(ActionEvent e) {
						avatarPanel.setAvatar("alien");
						setVisible(false);
					}
				});
			}
		}
	} 
	
	// ���� ũ�� ����, �����̴��� ����
	private class SizeAction implements ActionListener {
		JButton thisBtn;
		public void actionPerformed(ActionEvent e) {
			thisBtn = (JButton)e.getSource();
			SizeDialog dialog = new SizeDialog(gameFrame, "�ؽ�Ʈ ũ�� ����");
			dialog.setVisible(true);
		}
		
		private class SizeDialog extends JDialog { 
			private SizeDialog(JFrame frame, String title) {
				super(frame, title, true); 
				setLocation(750,300);
				setSize(250,150);
				setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
				
				//���� ũ�� ������ ���� �����̴� ���
				JSlider slider = new JSlider(JSlider.HORIZONTAL, 10, 25, gamePanel.wordSize);
				slider.setPaintLabels(true);
				slider.setPaintTicks(true);
				slider.setPaintTrack(true);
				slider.setMinorTickSpacing(1);
				slider.setMajorTickSpacing(5);
				add(slider);
				slider.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						int pt = slider.getValue();
						gamePanel.setWordSize(pt);
						thisBtn.setText("�ؽ�Ʈ ũ��: "+pt+"pt");
					}
				});
				JButton okBtn = new JButton("Ȯ��");
				add(okBtn);
				okBtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
			}
			
		}
	} 
	private class BgmAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton thisBtn = (JButton)e.getSource();
			if(thisBtn.getText().equals("������� ����")) {
				gamePanel.audioSource.stopAudio("bgm");
				thisBtn.setText("������� �ѱ�");
			}
			else {
				gamePanel.audioSource.playAudio("bgm");
				thisBtn.setText("������� ����");
			}
		}
	} 
}

