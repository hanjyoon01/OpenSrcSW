import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class genSnippet {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		
		if(args[0].equals("-f")) {
			if(args[2].equals("-q")) {
				gS(args[1],args[3]);
			}
		}
			
	}

	@SuppressWarnings("resource")
	private static void gS(String path, String key) throws ClassNotFoundException, IOException {
		
        File file = new File(path);
        FileReader filereader = new FileReader(file);
        BufferedReader bufReader = new BufferedReader(filereader);
        String[] line = {"","","","",""};
        for(int i=0;i<5;i++) {
            while((line[i] = bufReader.readLine()) != null){
                System.out.println(line[i]);
            }
        }
		String[] l = key.split(" ");
	}

}
