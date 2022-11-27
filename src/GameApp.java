
public class GameApp {
	
	public static void main(String[] args) {
		//메인 화면, 이름을 입력받고 게임 프레임 생성
		MainFrame mainFrame = new MainFrame();
	}
	
	//게임 프레임 생성 함수
	public static void run(String name) {
		GameFrame gameFrame = new GameFrame(name);
	}
	
}
