import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class kuir {

	public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException, SAXException {
		
		// TODO Auto-generated method stub
//		makeCollection.mC("C:\\Users\\������\\Desktop\\�Ǳ����б� ����\\���¼ҽ�SW�Թ�\\SimpleIR\\KUIR\\src\\data");
//		makeKeyword.mK("C:\\Users\\������\\Desktop\\�Ǳ����б� ����\\���¼ҽ�SW�Թ�\\SimpleIR\\KUIR\\src\\collection\\collection.xml");
		
		System.out.println(args[0]);
		System.out.println(args[1]);
		if(args[0].equals("-c")) 
			makeCollection.mC(args[1]);
		if(args[0].equals("-k")) 
			makeKeyword.mK(args[1]);
	}
}