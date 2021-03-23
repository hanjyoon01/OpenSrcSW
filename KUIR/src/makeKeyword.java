import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class makeKeyword {

	public static void mK(String path) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		
		int num=0;
		
		File file = new File(path);

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
		Document doc = docBuilder.parse(file);
		doc.getDocumentElement().normalize();

		NodeList title = doc.getElementsByTagName("title");
		NodeList body = doc.getElementsByTagName("body");
		
		Document idx = docBuilder.newDocument();
		Element docs = idx.createElement("docs");
		idx.appendChild(docs);

		for(int i=0;i<title.getLength();i++) {
			
			Element doc2 = idx.createElement("doc");
			docs.appendChild(doc2);
			doc2.setAttribute("id", Integer.toString(num++));
			Element title2 = idx.createElement("title");
			doc2.appendChild(title2);
			title2.appendChild(idx.createTextNode(title.item(i).getTextContent()));
			
			org.w3c.dom.Element body2 = idx.createElement("body");
			doc2.appendChild(body2);

			KeywordExtractor ke = new KeywordExtractor();
			KeywordList kl = ke.extractKeyword(body.item(i).getTextContent(), true);
			for(int j=0;j<kl.size();j++) {
				Keyword kwrd = kl.get(j);
				body2.appendChild(idx.createTextNode(kwrd.getString()+":"+kwrd.getCnt()+ "#"));
		}
	}

	TransformerFactory transformerFactory = TransformerFactory.newInstance();
	Transformer transformer;
	 
	transformer = transformerFactory.newTransformer();
	transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	DOMSource source = new DOMSource(idx);
	StreamResult result = new StreamResult(new FileOutputStream(new File("src/index/index.xml")));
	transformer.transform(source, result);
		
	}
}

