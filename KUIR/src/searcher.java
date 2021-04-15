import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class searcher {

	@SuppressWarnings({ "rawtypes" })
	public static void CalcSim(String path, String query) throws IOException, ClassNotFoundException, SAXException, ParserConfigurationException {
		
		float[] Sim = new float[5];
		int[] Rank = {0,1,2,3,4};
		for(int i=0;i<Sim.length;i++)
			Sim[i] = 0;
	
		FileInputStream filestream = new FileInputStream(path);
		ObjectInputStream objectInputStream = new ObjectInputStream(filestream);	
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		
		HashMap hashMap = (HashMap)object;
		
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(query, true);
		
		for(int i=0;i<Sim.length;i++) {
			for(int j=0;j<kl.size();j++) {
				Keyword kwrd = kl.get(j);
				if(hashMap.containsKey(kwrd.getString())) {
					float[] a = (float[]) hashMap.get(kwrd.getString());
					for(int k=0;k<a.length;k+=2) {
						if(a[k]==i) {
							Sim[i]+=a[k+1];
						}
					}
				}
			}
		}

		for(int i=0;i<Sim.length;i++) {
			for(int j=i+1;j<Sim.length;j++) {
				if (Sim[i] < Sim[j]) {
					float tmp = Sim[i];
					Sim[i] = Sim[j];
					Sim[j] = tmp;		
					
					int tmp2 = Rank[i];
					Rank[i] = Rank[j];
					Rank[j] = tmp2;
				}              
			}
		}
		
		
		File col = new File("C:\\Users\\한재윤\\Desktop\\건국대학교 과제\\오픈소스SW입문\\SimpleIR\\KUIR\\src\\collection\\collection.xml");
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		Document doc = docBuilder.parse(col);
		doc.getDocumentElement().normalize();

		NodeList title = doc.getElementsByTagName("title");
		
		for(int i=0;i<3;i++) {
			if(Sim[i] != 0)
				System.out.println("Rank " + (i+1) + ". " + title.item(Rank[i]).getTextContent() + " / 유사도: " + Math.round(Sim[i]*100.0)/100.0);
		}
	}
}