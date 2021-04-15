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

	static float[] Sim = new float[5];
	static int[] Rank = {0,1,2,3,4};
	static float r1 = 0;
	static float[] Sim2 = new float[5];
	static float[] cosSim = {0,0,0,0,0};
	static float[] r2 = {0,0,0,0,0};

	@SuppressWarnings({ "rawtypes" })
	public static void InnerProduct(String path, String query) throws IOException, ClassNotFoundException, SAXException, ParserConfigurationException {
		

		for(int i=0;i<Sim.length;i++) {
			Sim[i] = 0;
			Sim2[i] = 0;
		}
		
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
							Sim2[i]+=a[k+1];
							r2[i] += a[k+1]*a[k+1];
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
		
		System.out.println("일반 유사도");
		for(int i=0;i<3;i++) {
			if(Sim[i] != 0)
				System.out.println("Rank " + (i+1) + ". " + title.item(Rank[i]).getTextContent() + " / 유사도: " + Math.round(Sim[i]*100.0)/100.0);
		}
		System.out.println("============================");
	}
	
	public static void CalcSim(String path, String query) throws IOException, ClassNotFoundException, SAXException, ParserConfigurationException {

		InnerProduct(path, query);
		
		Rank = new int[] {0,1,2,3,4};
		
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(query, true);
		
		r1 += kl.size();
		
		for(int i=0;i<5;i++) {
			if(Sim2[i] != 0)
				cosSim[i] += (float) (Sim2[i]/(Math.sqrt(r1)*Math.sqrt(r2[i])));
		}
		
		for(int i=0;i<5;i++) {
			for(int j=i+1;j<5;j++) {
				if (cosSim[i] < cosSim[j]) {		
					float tmp = cosSim[i];
					cosSim[i] = cosSim[j];
					cosSim[j] = tmp;		
					
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
			if(cosSim[i] != 0)
				System.out.println("Rank " + (i+1) + ". " + title.item(Rank[i]).getTextContent() + " / 유사도: " + Math.round(cosSim[i]*100.0)/100.0);
		}
	}
}

