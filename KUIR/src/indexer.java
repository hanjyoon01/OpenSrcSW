import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class indexer {

	@SuppressWarnings({ "rawtypes","unchecked" })
	public static void mI(String path) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException {
		
		int N;
		
		File file = new File(path);

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
		Document idx = docBuilder.parse(file);
		
		NodeList body = idx.getElementsByTagName("body");
		N = body.getLength();
		
		String[][] key = new String[N][];
		int[][] val = new int[N][];
		
		//구분자 이용 split
		for(int i=0;i<N;i++) {
			String[] l = body.item(i).getTextContent().split(":|#");
			key[i] = new String[l.length/2];
			val[i] = new int[l.length/2];
			for(int j=0;j<l.length/2;j++) {
				key[i][j] = l[2*j]; //단어 저장
				val[i][j] = Integer.parseInt(l[1+2*j]); //단어 사용 횟수 저장
			}
		}
		
		//키워드리스트(중복x)
		ArrayList<String> kwrd = new ArrayList<String>();

		kwrd.add(key[0][0]);
		
		int count=0;
		
		//키워드리스트 만들기(중복 제거)
		for(int i=0;i<N;i++) {
			for(int j=0;j<key[i].length;j++) {

				for(String k : kwrd) {
		            if(k.equals(key[i][j]))
		            	count++;
		        }
				if (count==0) {
					kwrd.add(key[i][j]);
				}else {
					count=0;
				}
			}
		}
		
		//각 단어의 문서 출현 횟수
		int[] c = new int[kwrd.size()];
		for(int i=0; i<kwrd.size(); i++) {
			c[i] = 0;
		}
		
		//문서별 출현 횟수 저장
		for(int i=0;i<N;i++) {
			for(int j=0;j<key[i].length;j++) {
				for(int k=0; k<kwrd.size(); k++) {
					if(kwrd.get(k).equals(key[i][j]))
						c[k]++;
				}
			}
		}
		
		FileOutputStream filestream = new FileOutputStream("src/indexer/index.post");
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(filestream);
		HashMap<String, float[]> idxMap = new HashMap<String, float[]>();

		for(int i=0;i<kwrd.size();i++) {
			
			//문서번호별 가중치를 담을 arrayList
			ArrayList<Float> f = new ArrayList<Float>();
			
			for(int j=0;j<N;j++) {
				for(int k=0;k<key[j].length;k++) {
					if(kwrd.get(i).equals(key[j][k])) {
						f.add((float) j); //문서번호 저장
						f.add((float)(Math.round((val[j][k]*Math.log((float)N/(float)c[i]))*100.0)/100.0)); //가중치 저장
					}
				}
			}

			float[] array = new float[f.size()];
			for(int j=0;j<f.size();j++) {
				array[j] = f.get(j);
			}
			idxMap.put(kwrd.get(i), array);
		}
		objectOutputStream.writeObject(idxMap);
		objectOutputStream.close();
		
		
		//index.post 확인
		FileInputStream filestream2 = new FileInputStream("src/indexer/index.post");
		ObjectInputStream objectInputStream = new ObjectInputStream(filestream2);
		
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		
		HashMap hashMap = (HashMap)object;
		Iterator<String> it = hashMap.keySet().iterator();
		
		while(it.hasNext()) {
			String k = it.next();
			System.out.println(k + " -> " + Arrays.toString(idxMap.get(k)));
		}
	}
}