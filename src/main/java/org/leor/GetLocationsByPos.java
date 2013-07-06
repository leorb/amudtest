package org.leor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class GetLocationsByPos extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String lat = request.getParameter("lat");
		String lon = request.getParameter("lon");
		
		String queryStr = "distance(loc, geopoint(" + lat + ", " + lon + ")) < 4500";
		
		new QueryService(queryStr).doQuery(response);
	}

}
