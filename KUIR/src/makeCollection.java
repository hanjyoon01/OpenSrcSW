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

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

public class makeCollection { 
	
	public static void mC(String path) throws ParserConfigurationException, TransformerException, IOException {
		
		File dir = new File(path);
		File []fileList = dir.listFiles();
		int num = 0;
		 
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
		Document xdoc = docBuilder.newDocument();
		org.w3c.dom.Element docs = xdoc.createElement("docs");
		xdoc.appendChild(docs);
		 
		for(File file : fileList) { 
			 
			org.jsoup.nodes.Document hdoc = Jsoup.parse(file, "UTF-8");
			Elements titles = hdoc.select("title");
			Elements bodies = hdoc.select("body");
			
			org.w3c.dom.Element doc = xdoc.createElement("doc");
			docs.appendChild(doc);
			doc.setAttribute("id", Integer.toString(num++));
			
			org.w3c.dom.Element title = xdoc.createElement("title");
			doc.appendChild(title);
			for(org.jsoup.nodes.Element e: titles) {
				title.appendChild(xdoc.createTextNode(e.text()));
			}
			 
			org.w3c.dom.Element body = xdoc.createElement("body");
			doc.appendChild(body);
			for(org.jsoup.nodes.Element e: bodies) {
				body.appendChild(xdoc.createTextNode(e.text()));
			}
		}
		 
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		 
		transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		DOMSource source = new DOMSource(xdoc);
		StreamResult result = new StreamResult(new FileOutputStream(new File("src/collection/collection.xml")));
		transformer.transform(source, result);
	}
}

