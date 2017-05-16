package com.examples;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ITunesSearchAPITest {

	private static final String RESULT_COUNT = "resultCount";
	private String baseURL = "https://itunes.apple.com/search?";

	//Get the count of search results
	public HttpResponse getResponseObject(String keyValue){

		HttpResponse httpResp = null; 	
		try
		{
			//Create a httpClient object
			DefaultHttpClient httpClient = new DefaultHttpClient();

			//Create a httpGet object using the baseURL and keyValue search string 
			HttpGet getRequest = new HttpGet( baseURL + keyValue);

			//Execute the get call
			httpResp = httpClient.execute(getRequest);
		}
		catch(Exception e){
			//e.printStackTrace();
		}

		return httpResp;
	}

	
	@Test (dataProvider = "data")
	public void testITunesSearchAPI(String keyvaluePair, String expectedCount, String exptectedResCode){
		try
		{
			HttpResponse httpResp = getResponseObject(keyvaluePair);

			int actualResponseCode = httpResp.getStatusLine().getStatusCode();
			int exptectedResponseCode = Integer.parseInt(exptectedResCode);
			//Validate ResponseCode
			Assert.assertEquals(true, actualResponseCode == exptectedResponseCode);

			//Extract the response content from the HttpResponse object
			String respContent = httpResp.getEntity().getContent().toString();

			//Create JSONObject from the above response content
			JSONObject jsonObj = new JSONObject(respContent);

			//expectedCount from DataProvider
			int expCount = Integer.parseInt(expectedCount);
			
			//calculate the results count from the JSONObject
			int respCount = Integer.parseInt(jsonObj.get(RESULT_COUNT).toString());

			//Validate resultsCount
			if (expCount == 0)
				Assert.assertEquals(true, respCount == 0);
			else
				Assert.assertEquals(true, (respCount >= expCount));
		}
		catch(Exception e){
			//e.printStackTrace();
		}

	}


	@DataProvider
	public Object[][] data(){

		Object[][] testData = new Object[16][3];

		// test data for null key
		testData[0][0] = null;
		testData[0][1] = "0";
		testData[0][2] = "200";

		// test data for only term
		testData[1][0] = "term=";
		testData[1][1] = "0";
		testData[1][2] = "200";

		testData[2][0] = "term=:";
		testData[2][1] = "0";	
		testData[2][2] = "200";

		testData[3][0] = "term=jack+johnson";
		testData[3][1] = "1";
		testData[3][2] = "200";

		// test data for only country		
		testData[4][0] = "country=";
		testData[4][1] = "0";
		testData[4][2] = "200";

		testData[5][0] = "country=:";
		testData[5][1] = "0";
		testData[5][2] = "400";	

		testData[6][0] = "country=US";
		testData[6][1] = "1";
		testData[6][2] = "200";

		// test data for only media		
		testData[7][0] = "media=";
		testData[7][1] = "0";
		testData[7][2] = "200";

		testData[8][0] = "media=:";
		testData[8][1] = "0";
		testData[8][2] = "400";	

		testData[9][0] = "media=movie";
		testData[9][1] = "1";
		testData[9][2] = "200";

		// test data for only limit		
		testData[10][0] = "limit=";
		testData[10][1] = "0";
		testData[10][2] = "200";

		testData[11][0] = "limit=:";
		testData[11][1] = "0";	
		testData[11][2] = "200";

		testData[12][0] = "limit=5";
		testData[12][1] = "1";
		testData[12][2] = "200";

		// test data for combinations of term, country, media, and count		
		testData[13][0] = "term=&country=&media=&count=";
		testData[13][1] = "0";
		testData[13][2] = "200";

		testData[14][0] = "term=:&country=:&media=:&count=:";
		testData[14][1] = "0";
		testData[14][2] = "400";	

		testData[15][0] = "term=jack+johnson&country=US&media=movie&count=5";
		testData[15][1] = "1";
		testData[15][2] = "200";

		//Many more variations/combinations of term, country, media, and count can be tested similar to above cases.
		return testData;
	}

}
