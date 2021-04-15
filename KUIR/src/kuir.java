import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class kuir {

	public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException, SAXException, ClassNotFoundException {
		
		// TODO Auto-generated method stub

		if(args[0].equals("-c")) 
			makeCollection.mC(args[1]);
		if(args[0].equals("-k")) 
			makeKeyword.mK(args[1]);
		if(args[0].equals("-i"))
			indexer.mI(args[1]);
		if(args[0].equals("-s")) {
			if(args[2].equals("-q"))
				searcher.CalcSim(args[1],args[3]);
		}
	}	
}