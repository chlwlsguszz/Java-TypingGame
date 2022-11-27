import java.io.FileReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

// �ܾ ���Ͽ��� �����ؼ� ���Ϳ� �Է�
public class TextSource {
	private Vector<String> v = new Vector<String>();
	String filePath = null;
	
	public TextSource() { 
		filePath = "word/KOR.txt";
		readFile();
	}
	
	// �ܺο��� ȣ��, ������ ��θ� ���� �� �ٽ� ����
	protected void changeFile(String filePath) {
		this.filePath = filePath;
		readFile();
	}
	
	// ���� ����
	private void readFile() {
		v.clear(); // ���͸� �ѹ� ����
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
	
	//���Ϳ��� �ܾ� ���� ����
	public String get() {
		int index = (int)(Math.random()*v.size());
		return v.get(index);
	}
}
