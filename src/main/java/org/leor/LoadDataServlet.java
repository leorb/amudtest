package org.leor;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.GeoPoint;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.OperationResult;
import com.google.appengine.api.search.PutResponse;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;

/**
 * Servlet implementation class LoadDataServlet
 */
public class LoadDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoadDataServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		InputStream is = this.getClass().getResourceAsStream("forGoogle.xml");
		org.w3c.dom.Document doc = parseXmlDom(is);

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter pw = response.getWriter();
		Node root = doc.getChildNodes().item(0);
		
		Node placemark = root.getFirstChild();
		List<Document> putLsit = new ArrayList<Document>();
		while (placemark != null) {
			NodeList data = placemark.getChildNodes();
			if(data.getLength() == 0) {
				placemark = placemark.getNextSibling();
				continue;
			}
			
			String id = null;
			String name = null;
			String description = null;
			String lon = null;
			String lat = null;
						
			for(int j = 0; j < data.getLength(); j++) {
				Node f = data.item(j);
		
				if(f.getNodeName().equals("name")) {
					name = f.getTextContent();
				}
				if(f.getNodeName().equals("id")) {
					id = f.getTextContent();
				}
				if(f.getNodeName().equals("description")) {
					description = f.getTextContent();
				}
				if(f.getNodeName().equals("lon")) {
					lon = f.getTextContent();
				}
				if(f.getNodeName().equals("lat")) {
					lat = f.getTextContent();
				}
			}
			GeoPoint g = new GeoPoint(Double.parseDouble(lat), Double.parseDouble(lon));
			
			Document c = Document.newBuilder()
					.setId(id)
				    .addField(Field.newBuilder().setName("name").setText(name))
				    .addField(Field.newBuilder().setName("desc").setText(description))
			        .addField(Field.newBuilder().setName("loc").setGeoPoint(g))
				    .build();
			putLsit.add(c);
			
			placemark = placemark.getNextSibling();
		}
		pw.println("Entries found: " + putLsit.size());
		int countOK = createIndex(putLsit, pw);		
		pw.println("OK: " + countOK);
		
		pw.close();
	}

	public int createIndex(List<Document> putLsit, PrintWriter pw) {
	    IndexSpec indexSpec = IndexSpec.newBuilder().setName("myindex").build();
	    Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);

	    List<Document> temp = new ArrayList<Document>();
	    int countOK = 0;
	    for(int i = 0; i < putLsit.size(); i++ ) {
	    	temp.add(putLsit.get(i));
	    	if((i + 1)%200 == 0) {
	    		countOK += addList(index, temp, pw);
	    		temp.clear();
	    	}
	    }
	    countOK += addList(index, temp, pw);
	    return countOK;
	}
	
	private int addList(Index index, List<Document> temp, PrintWriter pw) {
    	PutResponse resp = index.put(temp);
    	int countOK = 0;
		for(OperationResult or : resp) {
			if(StatusCode.OK.equals(or.getCode())) {
				countOK ++;
			}
			else System.out.println("Error encountered.");
		}
		pw.println("Added: " + countOK + "<br>");
		pw.flush();
		return countOK;
	}

	public static org.w3c.dom.Document parseXmlDom(InputStream is) throws IOException {

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(true);
			dbf.setIgnoringElementContentWhitespace(true);
			DocumentBuilder builder = dbf.newDocumentBuilder();

			return builder.parse(is);

		} catch (SAXException | ParserConfigurationException e) {
			throw new IOException("Couldn't parse XML", e);
		}
	}
}
