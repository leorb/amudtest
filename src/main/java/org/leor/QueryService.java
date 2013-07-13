package org.leor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchException;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.SortExpression;
import com.google.appengine.api.search.SortOptions;

public class QueryService {

	private Query m_query;

	public QueryService(String queryStr) {
		m_query = Query.newBuilder().build(queryStr);
	}

	public QueryService(String queryStr, String sortStr) {
		SortExpression sortExpr = SortExpression.newBuilder()
				.setExpression(sortStr)
				.setDirection(SortExpression.SortDirection.ASCENDING)
				.setDefaultValueNumeric(4501.0)
				.build();
		
		SortOptions sortOptions = SortOptions.newBuilder()
				.addSortExpression(sortExpr)
				.build();

		m_query = Query.newBuilder()
			    .setOptions(QueryOptions.newBuilder().setSortOptions(sortOptions))
			    .build(queryStr);
	}

	public void doQuery(HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("application/json");
		ServletOutputStream os = response.getOutputStream();
		try {
			// Query the index.
			Index index = getIndex();
			Results<ScoredDocument> results = index.search(m_query);
			new ResultsToJson(results).toJson(os);
		} catch (SearchException e) {
			throw new ServletException(e.getOperationResult().toString(), e);
		}
		os.close();
	}

	public Index getIndex() {
		return SearchServiceFactory.getSearchService().getIndex(
				IndexSpec.newBuilder().setName("myindex"));
	}
}
