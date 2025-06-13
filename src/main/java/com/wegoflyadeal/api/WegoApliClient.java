package com.wegoflyadeal.api;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wegoflyadeal.constants.Constants;
import com.wegoflyadeal.helpers.WegoFlights;

public class WegoApliClient {

	public static CloseableHttpResponse get(String uri) throws ClientProtocolException, IOException
	{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet  httpget = new HttpGet(uri);
		CloseableHttpResponse closeablehttpresponse = httpClient.execute(httpget);
		return closeablehttpresponse;
	}
	
	public static void postCall(List<WegoFlights> wegoFlightsList) {
	    CloseableHttpClient client = null;
	    try {
	        if (wegoFlightsList == null || wegoFlightsList.isEmpty()) {
	            System.out.println("No flights to post.");
	            return;
	        }

	        WegoFlights.FlightRequest requestObj = convertToGroupedRequest(wegoFlightsList); // We'll define this below

	        ObjectMapper om = new ObjectMapper();
	        String finalJson = om.writeValueAsString(requestObj);

	        System.out.println(finalJson);

	        client = HttpClients.createDefault();
	        HttpPost httpPost = new HttpPost(Constants.Post_API_PATH);
	        httpPost.setEntity(new StringEntity(finalJson));
	        httpPost.setHeader("Content-Type", "application/json");

	        CloseableHttpResponse response = client.execute(httpPost);
	        System.out.println("The status code received: " + response.getStatusLine().getStatusCode());

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (client != null) client.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	private static WegoFlights.FlightRequest convertToGroupedRequest(List<WegoFlights> list) {
	    WegoFlights result = list.get(0);

	    List<WegoFlights.Fare> fares = new ArrayList<>();
	    for (WegoFlights wf : list) {
	        fares.add(new WegoFlights.Fare(wf.ps, wf.prv, String.valueOf(wf.APIPrice)));
	    }

	    String DepatDate = formatDate(result.depdt);

	    WegoFlights.Flight flight = new WegoFlights.Flight("Economy",result.fn,"",result.stym,"",DepatDate,result.frm,result.to,"", "", "","",fares);

	    List<WegoFlights.Flight> flights = new ArrayList<>();
	    flights.add(flight);

	    return new WegoFlights.FlightRequest(
	        result.frm,
	        result.to,
	        result.cur,
	        result.arlncd,
	        DepatDate,
	        result.clt,
	        result.dm,
	        flights
	    );
	}
	private static String formatDate(String isoDate) {
	    try {
	        LocalDate date = LocalDate.parse(isoDate.substring(0, 10));
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);
	        return date.format(formatter);
	    } catch (Exception e) {
	        return isoDate;
	    }
	}
}
