package org.leor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchException;
import com.google.appengine.api.search.SearchServiceFactory;

public class QueryService {

	private String m_queryStr;

	public QueryService(String queryStr) {
		m_queryStr = queryStr;
	}

	public void doQuery(HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("application/json");
		ServletOutputStream os = response.getOutputStream();
		try {
		    // Query the index.
        Index index = getIndex();
		    Results<ScoredDocument> results = index.search(m_queryStr);
		    new ResultsToJson(results).toJson(os);
		} catch (SearchException e) {
			throw new ServletException(e.getOperationResult().toString(), e);
		}
		os.close();	
		
	}
	
	public Index getIndex() {
		return SearchServiceFactory.getSearchService()
			    .getIndex(IndexSpec.newBuilder().setName("myindex"));
	}

}
