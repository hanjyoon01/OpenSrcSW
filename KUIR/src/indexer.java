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
		
		//������ �̿� split
		for(int i=0;i<N;i++) {
			String[] l = body.item(i).getTextContent().split(":|#");
			key[i] = new String[l.length/2];
			val[i] = new int[l.length/2];
			for(int j=0;j<l.length/2;j++) {
				key[i][j] = l[2*j]; //�ܾ� ����
				val[i][j] = Integer.parseInt(l[1+2*j]); //�ܾ� ��� Ƚ�� ����
			}
		}
		
		//Ű���帮��Ʈ(�ߺ�x)
		ArrayList<String> kwrd = new ArrayList<String>();

		kwrd.add(key[0][0]);
		
		int count=0;
		
		//Ű���帮��Ʈ �����(�ߺ� ����)
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
		
		//�� �ܾ��� ���� ���� Ƚ��
		int[] c = new int[kwrd.size()];
		for(int i=0; i<kwrd.size(); i++) {
			c[i] = 0;
		}
		
		//������ ���� Ƚ�� ����
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
			
			//������ȣ�� ����ġ�� ���� arrayList
			ArrayList<Float> f = new ArrayList<Float>();
			
			for(int j=0;j<N;j++) {
				for(int k=0;k<key[j].length;k++) {
					if(kwrd.get(i).equals(key[j][k])) {
						f.add((float) j); //������ȣ ����
						f.add((float)(Math.round((val[j][k]*Math.log((float)N/(float)c[i]))*100.0)/100.0)); //����ġ ����
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
		
		
		//index.post Ȯ��
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