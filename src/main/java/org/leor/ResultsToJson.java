package org.leor.myfirstapp;

import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.GeoPoint;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;

public class ResultsToJson {

	private Results<ScoredDocument> m_results;

	public ResultsToJson(Results<ScoredDocument> results) {
		m_results = results;
	}

	public void toJson(OutputStream os)
			throws IOException {
		JsonFactory jsonF = new JsonFactory();
		JsonGenerator jg = jsonF.createGenerator(os);

		jg.writeStartObject();
		jg.writeFieldName("results");
		jg.writeStartArray();

		for (ScoredDocument document : m_results) {
			jg.writeStartObject();
			for (Field f : document.getFields()) {
				String name = f.getName();
				switch (f.getType()) {
				case TEXT:
					jg.writeStringField(name, f.getText());
					break;
				case GEO_POINT:
					GeoPoint p = f.getGeoPoint();
					jg.writeNumberField("lat", p.getLatitude());
					jg.writeNumberField("lon", p.getLongitude());
					break;
				case ATOM:
				case DATE:
				case HTML:
				case NUMBER:
				default:
					break;
				}
			}
			jg.writeEndObject();

		}
		jg.writeEndArray();
		jg.writeEndObject();
		jg.close();
	}
}
