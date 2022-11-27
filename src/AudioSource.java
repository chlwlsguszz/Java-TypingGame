import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

// 오디오를 송출하는 클래스
public class AudioSource {
	private Clip bgmClip;
	private Clip correctClip;
	private Clip wrongClip;
	private Clip buttonClip;
	private Clip gameoverClip;
	
	public AudioSource() {
		bgmClip = getClip("audio/bgm.wav");
		correctClip = getClip("audio/correct.wav");
		wrongClip = getClip("audio/wrong.wav");
		buttonClip = getClip("audio/button.wav");
		gameoverClip = getClip("audio/gameover.wav");
	}
	
	// 음악 파일과 오디오 클립을 연결
	private Clip getClip(String filePath) {
		Clip clip=null;
		try {
			clip = AudioSystem.getClip();
			File audioFile = new File(filePath);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			clip.open(audioStream); 
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clip;
	}
	
	// 오디오 클립 재생
	public void playAudio(String name) {
		switch(name) {
		case "bgm": bgmClip.setFramePosition(0); bgmClip.loop(Clip.LOOP_CONTINUOUSLY); break;
		case "correct": correctClip.setFramePosition(0); correctClip.start(); break;
		case "wrong": wrongClip.setFramePosition(0); wrongClip.start(); break;
		case "button": buttonClip.setFramePosition(0); buttonClip.start(); break;
		case "gameover": gameoverClip.setFramePosition(0); gameoverClip.start(); break;
		}
	}
	
	// 오디오 클립 정지
	public void stopAudio(String name) {
		switch(name) {
		case "bgm": bgmClip.stop(); break;
		case "correct": correctClip.stop(); break;
		case "wrong": wrongClip.stop(); break;
		case "button": buttonClip.stop(); break;
		case "gameover": gameoverClip.stop(); break;
		}
	}
}

