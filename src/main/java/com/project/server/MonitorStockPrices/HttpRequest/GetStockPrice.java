package com.project.server.MonitorStockPrices.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.project.server.MonitorStockPrices.database.DatabaseService;
import com.project.server.MonitorStockPrices.model.Resources;
/**
 * Get the stock price for symbol from Yahoo web services
 * @author ankita
 *
 */
public class GetStockPrice {
	final static Logger logger = Logger.getLogger(GetStockPrice.class);
	/**
	 * Make HTTP request to fetch recent stock data from YAHOO web services
	 * @param symbol
	 * @return
	 */
	
	
	public ArrayList<Resources> getStockPrices(String symbol) {
		ArrayList<Resources> stockList = new ArrayList<Resources>();
			String url = "http://finance.yahoo.com/webservice/v1/symbols/" + symbol
					+ "/quote?format=json&view=detail";

			URL obj;
			HttpURLConnection con;

			try {
				obj = new URL(url);
				con = (HttpURLConnection) obj.openConnection();

				con.setRequestMethod("GET");

				// add request header
				// con.setRequestProperty("User-Agent", USER_AGENT);

				int responseCode = con.getResponseCode();
				if (responseCode == 200) {
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					stockList = parseJson(response.toString());
					in.close();

				} else {
					logger.info("some error has occured "+responseCode);
				}
			} catch (MalformedURLException e) {
				logger.error(e.getMessage());
			} catch (ProtocolException e) {
				logger.error(e.getMessage());
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		
		return stockList;
	}
	/**
	 * Parse the response
	 * @param response
	 * @return
	 */
	private ArrayList<Resources> parseJson(String response) {
		ArrayList<Resources> stockList = new ArrayList<>();
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(response).getAsJsonObject();

		JsonObject meta = obj.getAsJsonObject("list").getAsJsonObject("meta");
		int count = meta.get("count").getAsInt();

		JsonArray resources = obj.getAsJsonObject("list").getAsJsonArray("resources");

		Gson gson = new Gson();
		stockList = gson.fromJson(resources, new TypeToken<List<Resources>>() {
		}.getType());

		return stockList;
	}
}
