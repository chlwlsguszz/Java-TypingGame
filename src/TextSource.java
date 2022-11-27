import java.io.FileReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

// 단어를 파일에서 추출해서 벡터에 입력
public class TextSource {
	private Vector<String> v = new Vector<String>();
	String filePath = null;
	
	public TextSource() { 
		filePath = "word/KOR.txt";
		readFile();
	}
	
	// 외부에서 호출, 파일의 경로를 변경 후 다시 읽음
	protected void changeFile(String filePath) {
		this.filePath = filePath;
		readFile();
	}
	
	// 파일 읽음
	private void readFile() {
		v.clear(); // 벡터를 한번 지움
		try {
			Scanner fScanner = new Scanner(new FileInputStream(filePath), "utf-8");
			while(fScanner.hasNext()) {
				String word = fScanner.nextLine();
				v.add(word.trim());
			}
			fScanner.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//벡터에서 단어 랜덤 추출
	public String get() {
		int index = (int)(Math.random()*v.size());
		return v.get(index);
	}
}
