package com.wegoflyadeal.runners;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.wegoflyadeal.api.WegoApliClient;
import com.wegoflyadeal.constants.Constants;
import com.wegoflyadeal.helpers.WegoFlights;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Wego_COM {

	WebDriver driver;
	public static List<WegoFlights> WegoFlightsVar = new ArrayList<WegoFlights>();
	int PaymenttypesLoop = 1;

	
	String Destination = "";
	String Source = "";
	String Airline = "";
	String DepartDate = "";
	String DepartTime = "";
	String CurrencyCode = "";
	String APIPrice = "";
	String Client = "";
	String Provider = "";
	String Domain = "AE";
	String DiffPrice = "";
	String FltNum ="";

	boolean isWegoStarted = false;
	String PgFee = "";
	String Lead = "";

	boolean isAirline = false;

	String[] alphaRoutes = {"RUH-JED","JED-RUH"};

	String SystemName = "F3 - SA WEGO Scrapper - System Alpha";

	int flightRunCount = 0;
	String Wego_URL;

	public Set<String> completedRouteTime = new HashSet<>();

	public Set<String> completedFlightNumbers = new HashSet<>();

	@AfterTest
	public void quitTheSession() {
		if (driver != null) {
			driver.quit();
		}
	}

	@BeforeTest
	public void setUp() throws InterruptedException {

		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.setPageLoadStrategy(PageLoadStrategy.NONE);
		options.addArguments("start-maximized");
		options.setExperimentalOption("useAutomationExtension", false);
		options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-infobars");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--disable-browser-side-navigation");
		options.addArguments("--disable-gpu");
		options.addArguments("--disable-web-security");
		options.addArguments("--no-proxy-server");
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("credentials_enable_service", false);
		prefs.put("profile.default_content_setting_values.notifications", 2);
		// 1-Allow, 2-Block, 0-default
		options.setExperimentalOption("prefs", prefs);
		prefs.put("profile.password_manager_enabled", false);
		options.setExperimentalOption("prefs", prefs);
		//prefs.put("profile.managed_default_content_settings.images", 2);
		options.setExperimentalOption("prefs", prefs);
		options.addArguments("--incognito", "--disable-blink-features=AutomationControlled");
		options.addArguments("force-device-scale-factor=0.20");
		options.addArguments("--clear-ssl-state");
		options.addArguments("--disable-cache");
		options.addArguments("--disk-cache-size=0");
		options.addArguments("--disable-network-throttling");
		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
		//wego_Login();
	}
	
   
    @SuppressWarnings("unchecked")
    @Test(priority = 0)
    public void readAllJson() throws ClientProtocolException, IOException, ParseException {
    	
    	 LocalTime currentTime = LocalTime.now();
    	LocalTime startTime = LocalTime.of(23, 30); // 11 PM
        LocalTime endTime = LocalTime.of(5, 30); // 5 AM
        
        if ((currentTime.isAfter(startTime) && currentTime.isBefore(LocalTime.MIDNIGHT)) ||
            (currentTime.isAfter(LocalTime.MIDNIGHT) && currentTime.isBefore(endTime)) ||
            currentTime.equals(startTime) || currentTime.equals(endTime)) {
    		    
    		} else {
    			//performApiCall(Constants.Get_API_Path);
    		}
    	//performApiCall(Constants.Get_API_Path);
        performApiCall(Constants.Get_API_Path1);
        performApiCall(Constants.Get_API_Path2);
        performApiCall(Constants.Get_API_Path3);
        performApiCall(Constants.Get_API_Path4);
        performApiCall(Constants.Get_API_Path5);
        performApiCall(Constants.Get_API_Path6);
        performApiCall(Constants.Get_API_Path7);
        performApiCall(Constants.Get_API_Path8);
        performApiCall(Constants.Get_API_Path9);
        performApiCall(Constants.Get_API_Path10);
        performApiCall(Constants.Get_API_Path11);
        performApiCall(Constants.Get_API_Path12);
        performApiCall(Constants.Get_API_Path13);
        performApiCall(Constants.Get_API_Path14);
        
    }

    private void performApiCall(String apiPath) throws ClientProtocolException, IOException, ParseException {
        System.err.println("URI : " + apiPath);
        CloseableHttpResponse closeableHttpResponse = WegoApliClient.get(apiPath);
		String response = EntityUtils.toString(closeableHttpResponse.getEntity(), "utf-8");
		org.json.JSONArray json = new org.json.JSONArray(response);
		//System.out.println(json);

		JSONObject obj = new JSONObject();
		obj.put("Wego", json);
		Files.write(Paths.get(Constants.JSON_RESULTFILE_PATH), obj.toJSONString().getBytes());
		getDataFromApiResponse();
    }

	
	public void getDataFromApiResponse() throws IOException, ParseException {
		JSONParser jsonParser = new JSONParser();
		try {
			FileReader reader = new FileReader(Constants.JSON_RESULTFILE_PATH);
			Object obj = jsonParser.parse(reader);
			JSONObject hotelsList = (JSONObject) obj;
			org.json.simple.JSONArray responseList = (org.json.simple.JSONArray) hotelsList.get("Wego");
			//System.out.println("Size of Json - " + responseList.size());

			int totalSize = responseList.size();
			System.out.println("totalSize : " + totalSize);
			
			

			for (int i = 0; i < responseList.size(); i++) {
				try {
					JSONObject details = (JSONObject) responseList.get(i);
					String source = (String) details.get("source");
					Source = source;
					String destination = (String) details.get("destination");
					Destination = destination;
					String DepartureDate = (String) details.get("departDate").toString();
					DepartDate = DepartureDate;
					String airline = (String) details.get("airline");
					Airline = airline;

					Client = (String) details.get("client");
					
				


					//System.out.println("----------------------------------------------------");
					/*System.out.println("Record Number :" + (i + 1));
					System.out.println("Source : " + source);
					System.out.println("Destination : " + destination);
					System.out.println("DepartureDate : " + DepartureDate);
					System.out.println("TripType : " + TripType);
					System.out.println("PgFee : " + PgFee);
					System.out.println("Lead : " + Lead);
					System.out.println("isAirline : " + isAirline);*/

					String[] DateSplits = DepartureDate.split("T")[0].split("-");

					String monthOfDate = DateSplits[1].trim();
					String dayOfDate = DateSplits[2].trim();
					if (dayOfDate.startsWith("0")) { 
						dayOfDate = dayOfDate.replace("0", "");
					}
					//System.out.println("Airline : " + airline);

					String apiRoute = source.trim() + "-" + destination.trim();

					if (airline.toLowerCase().trim().contains("f3")) {
						for (String routeGamma : alphaRoutes) {  
							if (routeGamma.equalsIgnoreCase(apiRoute)) {
								System.out.println("Record Number :" + (i + 1));
								System.out.println("Source : " + source);
								System.out.println("Destination : " + destination);
								System.out.println("DepartureDate : " + DepartureDate);
								System.out.println("I value : " + i);
								search(source, destination, DepartureDate.split("T")[0], monthOfDate, dayOfDate,airline);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			/*if (isWegoStarted) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date enddate = new Date();
				String dateEnded = dateFormat.format(enddate);
				System.out.println(dateEnded);
				CommonUtility.sendMail("automation@rehlat.com","qateam@rehlat.com", SystemName+" - Completed "+dateEnded+" ", "Dear All,\n\n Please note that "+SystemName+" has successfully completed at"+dateEnded+"\n"+Arrays.asList(alphaRoutes)+"\n\n Thanks, \n Gopi");

			}*/
		} catch (Exception e) {
		}

		System.out.println("flightRunCount : " + flightRunCount);

		System.out.println("flightRunCount : " + completedFlightNumbers);
	}

	String websiteName = "kw.wego.com";

	// com wego = kw.wego.com rehlat.com
	// ae wego = www.wego.ae our site rehlat.ae
	// sa wego = sa.wego.com our site rehlat.com.sa
	// eg wego = eg.wego.com our site rehlat.com.eg
	private Set<String> visitedURLs = new HashSet<>();

	public void search(String source, String destination, String date, String DepartureMonth, String DepartureDay, String airline) throws InterruptedException {
		
	    //Wego_URL = "https://"+websiteName+"/en/flights/searches/"+source+"-"+destination+"-"+ date+"/economy/1a:0c:0i?sort=price&order=asc&airlines=F3%2CXY";

	    Wego_URL = "https://"+websiteName+"/en/flights/searches/"+source+"-"+destination+"-"+ date+"/economy/1a:0c:0i?sort=price&order=asc&airlines=XY";

	    // Check if the URL has been visited before
	    if (visitedURLs.contains(Wego_URL)) {
	        System.out.println("Duplicate URL detected - Skipping search");
	        return; // Skip the search if the URL is a duplicate
	    }

	    // Visit the URL and add it to the set of visited URLs
	    driver.get(Wego_URL);
	    //System.out.println(Wego_URL);

	    try {
	        Thread.sleep(5000);
	    } catch (InterruptedException e) {
	    }
	    resultsForWego();
	    // Add the current URL to the set of visited URLs
	    visitedURLs.add(Wego_URL);
	}
	
	public void resultsForWego() throws InterruptedException {
        int maxRetries = 3;
        boolean isPageLoaded = false;

        for (int retryCount = 1; retryCount <= maxRetries; retryCount++) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, 5); // 10 seconds timeout
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(text(),'Payment method')]")));
                System.out.println("Wego SRP Page Displayed");
                results(); // Assuming you have a method named results()
                isPageLoaded = true;
                break; // Exit the loop if the element is displayed
            } catch (Exception e) {
                System.out.println("Wego SRP Page not Displayed - Retry #" + retryCount);
                driver.get(Wego_URL);
            }
        }

        if (!isPageLoaded) {
            System.out.println("Wego SRP Page could not be loaded after " + maxRetries + " retries");
        }
    }
	
	public void results() throws InterruptedException {

		try {
			WebElement DirectFlights = driver.findElement(By.xpath("//*/text()[normalize-space(.)='Direct']/parent::*"));
			DirectFlights.click();
			Thread.sleep(2000);

		WebElement ResultsCount = driver.findElement(By.xpath("//div[contains(text(),' of ')]"));

		// Get the text from the element
		String Results_Count = ResultsCount.getText();
		String NumberResults = Results_Count.split(" ")[0];

		if ("0".equals(NumberResults)) {
			System.out.println("No Flights Available");
		} else {
            Thread.sleep(8000);
			List<WebElement> getCount = driver.findElements(By.cssSelector("div[data-pw='flightSearchResults_tripCard']"));
			//System.out.println("GetCount of Results:" + getCount.size());
			int count = 0;
			System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
			 String searchText = "websites";
			 for (WebElement flightDetails : getCount) {
		            // Get the text content of the flight details element
				    String detailsText = flightDetails.getText().replaceAll("[\r\n]+", " ").replace(",", "").replace("SAR", "").replace("Per person", "").replace("Refundable ", "").split("View Deals")[0].trim();
		            // Check if the text contains the specified search text
		            if (detailsText.contains(searchText)) {
		                // If it contains the text, print the flight details
		            	System.out.println(detailsText);
		                count++;
		            }
			 }
			 System.out.println("Total Flights: " + count);

			if (!getCount.isEmpty()) {

				WebElement From = driver.findElement(By.cssSelector("div[data-pw='leg_departureAirportCode']"));
				String FromCity = From.getText();
				WebElement To = driver.findElement(By.cssSelector("div[data-pw='leg_arrivalAirportCode']"));
				String ToCity = To.getText();

				CurrencyCode = driver.findElement(By.cssSelector("button[aria-label='Currency']")).getText();
				//System.out.println(CurrencyCode);
				Thread.sleep(1000);
				List<WebElement> elements = driver.findElements(By.cssSelector("div[data-pw='flightSearchResults_tripCard']"));
				int count1 = 0;
				for (WebElement viewDeal : elements) {
					 String detailsText = viewDeal.getText().replaceAll("[\r\n]+", " ").replace(",", "").replace("SAR", "").replace("Per person", "").replace("Refundable ", "").split("View Deals")[0].trim();
					 if (detailsText.contains(searchText)) {
					 viewDeal.click();
					 count1++;
				
					try {
						
						
						Thread.sleep(1000);
						String tab="overlay";
									
						try {
			                WebDriverWait wait = new WebDriverWait(driver, 10); 
			                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='"+tab+"']/div/div/div/div[2]/div/div[1]/div/div/div[1]/div[1]/img")));
			            } catch (Exception e) {
			                
			                
			            }
						
						List<WebElement> showDetailsButton = driver.findElements(By.xpath("//*/text()[normalize-space(.)='Show details']/parent::*"));

		                if (showDetailsButton.isEmpty()) {
		                    // If 'Show details' button is not found, skip and move to the next viewDeal
		                    System.out.println("Show details button not found. Skipping to the next viewDeal...");
		                    continue;
		                }

		                // Click on the 'Show details' button
		                showDetailsButton.get(0).click();
		                Thread.sleep(1000);
						WebElement AirlineLogo = driver.findElement(By.xpath("//*[@id='"+tab+"']/div/div/div/div[1]/div[2]/div/div/div[1]/div[3]/div[2]/div/img"));
						String LogoAirline = AirlineLogo.getAttribute("src");

						String[] Airline_Name = LogoAirline.split("airlines_rectangular/");
						Airline = Airline_Name[1].replace(".png", "").replaceAll(" ", "");
						System.out.println("Airline Name     : " +Airline);
						
						WebElement flightNumber = null;
						if (Airline.equalsIgnoreCase("F3")) {
							flightNumber = driver.findElement(By.xpath("//span[contains(text(),'F3')]"));
						} else if (Airline.equalsIgnoreCase("SV")) {
							flightNumber = driver.findElement(By.xpath("//span[contains(text(),'SV')]"));
						} else if (Airline.equalsIgnoreCase("MS")) {
							flightNumber = driver.findElement(By.xpath("//span[contains(text(),'MS')]"));
						} else if (Airline.equalsIgnoreCase("XY")) {
							flightNumber = driver.findElement(By.xpath("//span[contains(text(),'XY')]"));
						} else {
							flightNumber = driver.findElement(By.xpath("//*[@id='"+tab+"']/div/div/div/div[1]/div[2]/div/div/div[1]/div[4]/div/div[2]/div[3]/div[2]/div/div[1]/div[1]/span[1]"));

						}

						String DepartTiming = driver.findElement(By.xpath("//div[2]/div[1]/div[2]/span[2]")).getText();
						
						/*try {
							WebElement ShowMore = driver.findElement(By.xpath("//*[@id='overlay']/div/div/div[2]/div[2]/div[3]/div[11]/div"));
							ShowMore.click();
							}
							catch (Exception e) {
							}*/
						FltNum = flightNumber.getText().replaceAll("[\r\n]+", " ").replace(",", "").replace("F3", "").replace("XY", "").replace("SV", "").replace(Airline, "").replace(" ", "");
						System.out.println("Flight Number    : " + FltNum);
						System.out.println("Departure Timing : " + DepartTiming);
												
						try {
							

							List<WebElement> links = driver.findElements(By.xpath("//*[@id='"+tab+"']/div/div/div/div[2]/div/div/div/div/div[1]/div[1]/img"));
							
							
							if (links.size() >= 4) {
							    System.out.println("Competitors Count: " + links.size());

							    if (links.size() >= 10) {
							        try {
							            WebElement showMore = driver.findElement(By.xpath("//div[@class='nlltapmKPb5TLZMtHVHk']"));
							            showMore.click();
							            Thread.sleep(1000);
							        } catch (Exception e) {
							            System.out.println("No ShowMore button found.");
							        }
							    }

							    WegoFlightsVar.clear();
							    int DanatPosition = -1;
							    int DanatFare = 0;

							    for (int i = 1; i <= links.size(); i++) {
							        try {
							            WebElement logo = driver.findElement(By.xpath("//*[@id='" + tab + "']/div/div/div/div[2]/div/div[" + i + "]/div/div/div[1]/div[1]/img"));
							            WebElement fareElement = driver.findElement(By.xpath("//*[@id='" + tab + "']/div/div/div/div[2]/div/div[" + i + "]/div/div/div/div/div/div/span"));

							            String logoSrc = logo.getAttribute("src");
							            String providerName = logoSrc.contains("rectangular_logos/") ?
							                logoSrc.split("rectangular_logos/")[1].replace(".png", "") :
							                logoSrc.split("/")[logoSrc.split("/").length - 1].replace(".png", "");

							            String fareText = fareElement.getText().replaceAll("[\r\n]+", " ").replace(",", "").trim();
							            int price = Integer.parseInt(fareText);

							            System.out.println(price + "              : " + providerName);

							            if (logoSrc.toLowerCase().contains("danat")) {
							                DanatPosition = i;
							                DanatFare = price;
							                System.out.println(DanatFare + "              : Danat");
							                System.out.println(i + "                : Danat Rank");
							            }

							            WegoFlights flight = new WegoFlights(ToCity, FromCity, Airline,DepartDate, DepartTiming, CurrencyCode,price, Client, Domain, providerName, FltNum, String.valueOf(i));
							            WegoFlightsVar.add(flight);

							        } catch (Exception e) {
							            System.out.println("Error processing competitor at position " + i);
							        }
							    }

							    WegoApliClient.postCall(WegoFlightsVar); 
							}
							    else if (links.size() >= 3) {
								System.out.println("Competitors Count: " + links.size());
								if (links.size() >= 10) {
									try {
										WebElement ShowMore = driver.findElement(By.xpath("//div[@class='nlltapmKPb5TLZMtHVHk']"));
										ShowMore.click();
										}
										catch (Exception e) {
										}
								} 
								WebElement firstLogo   = driver.findElement(By.xpath("//*[@id='"+tab+"']/div/div/div/div[2]/div/div[1]/div/div/div[1]/div[1]/img"));
								WebElement secondLogo  = driver.findElement(By.xpath("//*[@id='"+tab+"']/div/div/div/div[2]/div/div[2]/div/div/div[1]/div[1]/img"));
								WebElement thirdLogo   = driver.findElement(By.xpath("//*[@id='"+tab+"']/div/div/div/div[2]/div/div[3]/div/div/div[1]/div[1]/img"));
								String logo1 = firstLogo.getAttribute("src");
								String logo2 = secondLogo.getAttribute("src");
								String logo3 = thirdLogo.getAttribute("src");

								String firstAmount  = driver.findElement(By.xpath("//*[@id='"+tab+"']/div/div/div/div[2]/div/div[1]/div/div/div/div/div/div/span")).getText().replaceAll("[\r\n]+", " ").replace(",", "");
								String secondAmount = driver.findElement(By.xpath("//*[@id='"+tab+"']/div/div/div/div[2]/div/div[2]/div/div/div/div/div/div/span")).getText().replaceAll("[\r\n]+", " ").replace(",", "");
								String thirdAmount  = driver.findElement(By.xpath("//*[@id='"+tab+"']/div/div/div/div[2]/div/div[3]/div/div/div/div/div/div/span")).getText().replaceAll("[\r\n]+", " ").replace(",", "");
								/*System.out.println("firstAmount: " + firstAmount);
								System.out.println("secondAmount: " + secondAmount);
								System.out.println("thirdAmount: " + thirdAmount);*/

								String[] parts = logo1.split("rectangular_logos/");
								String extractedPart = parts[1].replace(".png", "");
								System.out.println(""+firstAmount+"              : "+ extractedPart.replace(".png", ""));

								String[] parts2 = logo2.split("rectangular_logos/");
								String extractedPart2 = parts2[1].replace(".png", "");
								System.out.println(""+secondAmount+"              : "+ extractedPart2.replace(".png", ""));

								String[] parts3 = logo3.split("rectangular_logos/");
								String extractedPart3 = parts3[1].replace(".png", "");
								System.out.println(""+thirdAmount+"              : " + extractedPart3.replace(".png", ""));

								int DanatAmount = 0;
								int DanatPositionNumber = 0;
								List<WebElement> Listcompetitor = driver.findElements(By.xpath("//*[@id='"+tab+"']/div/div/div/div[2]/div/div/div/div/div[1]/div[1]/img"));
								 
								for (int k = 1; k <= Listcompetitor.size() - 1; k++) {

									WebElement getVal = driver.findElement(By.xpath("//*[@id='"+tab+"']/div/div/div/div[2]/div/div["+k+"]/div/div/div[1]/div[1]/img"));
                                    String src = getVal.getAttribute("src");
									if (src.contains("danat")) {
										//System.out.println("src: " + src);
										String Amount = driver.findElement(By.xpath("//*[@id='"+tab+"']/div/div/div/div[2]/div/div["+k+"]/div/div/div/div/div/div/span")).getText().replaceAll("[\r\n]+", " ").replace(",", "");
										DanatAmount = Integer.parseInt(Amount);
										DanatPositionNumber = k;
										System.out.println(""+DanatAmount+"              : Danat");
										System.out.println(""+k+"                : Danat Rank");
										break;
									}

								}

								int amount_first = Integer.parseInt(firstAmount);
								int amount_second = Integer.parseInt(secondAmount);
								int amount_third = Integer.parseInt(thirdAmount);

								int comparFirstPrice = amount_first - DanatAmount;
								int comparSecondPrice = amount_second - DanatAmount;
								int comparThirdPrice = amount_third - DanatAmount;
								
								String Flight_Data = FromCity+" "+ToCity+" "+FltNum+" "+DepartDate+" "+DepartTiming+" "+Airline+" "+amount_first+" "+extractedPart+" 1 "+amount_second+" "+extractedPart2+" 2 "+amount_third+" "+extractedPart3+" "+DanatAmount+" Danat "+DanatPositionNumber+" ";
								System.out.println(Flight_Data);

								/*System.out.println("First Diffrence Amount: " + comparFirstPrice);
								System.out.println("Second Diffrence Amount: " + comparSecondPrice);
								System.out.println("Third Diffrence Amount: " + comparThirdPrice);*/

								WegoFlights wegoFlightsObj1 = null;
								WegoFlights wegoFlightsObj2 = null;
								WegoFlights wegoFlightsObj3 = null;
								WegoFlightsVar.clear();

								wegoFlightsObj1 = new WegoFlights(ToCity, FromCity,  Airline,DepartDate, DepartTiming, CurrencyCode, amount_first, Client,Domain,extractedPart, FltNum, "1"); 

								WegoFlightsVar.add(wegoFlightsObj1); 

								wegoFlightsObj2 = new WegoFlights(ToCity, FromCity,  Airline,DepartDate, DepartTiming, CurrencyCode, amount_second, Client,Domain,extractedPart2, FltNum, "2");
								WegoFlightsVar.add(wegoFlightsObj2);

								if (DanatPositionNumber > 3) {
									
									wegoFlightsObj3 = new WegoFlights(ToCity, FromCity,  Airline,DepartDate, DepartTiming, CurrencyCode,  DanatAmount, Client,Domain,"Danat", FltNum , String.valueOf(DanatPositionNumber));
									WegoFlightsVar.add(wegoFlightsObj3);
									}
									else{
										
										wegoFlightsObj3 = new WegoFlights(ToCity, FromCity,  Airline,DepartDate, DepartTiming, CurrencyCode, amount_third, Client,Domain,extractedPart3, FltNum, String.valueOf(DanatPositionNumber));
										WegoFlightsVar.add(wegoFlightsObj3);
									}

								WegoApliClient.postCall(WegoFlightsVar);
								
								/*if (isWegoStarted == false) {
									DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
									Date date = new Date();
									String dateStarted = dateFormat.format(date);
									System.out.println(dateStarted);
                                    CommonUtility.sendMail("automation@rehlat.com","qateam@rehlat.com", SystemName+" - Started "+dateStarted+" ", "Dear All,\n\n Please note that "+SystemName+" started just now at "+dateStarted+"\n"+Arrays.asList(alphaRoutes)+"\n\n Thanks, \n Gopi");
									isWegoStarted = true;
								}*/

							} else if (links.size() == 2) {
								System.out.println("Competitors Count: " + links.size());
								WebElement firstLogo   = driver.findElement(By.xpath("//*[@id='"+tab+"']/div/div/div/div[2]/div/div[1]/div/div/div[1]/div[1]/img"));
								WebElement secondLogo  = driver.findElement(By.xpath("//*[@id='"+tab+"']/div/div/div/div[2]/div/div[2]/div/div/div[1]/div[1]/img"));
								String logo1 = firstLogo.getAttribute("src");
								String logo2 = secondLogo.getAttribute("src");

								String firstAmount  = driver.findElement(By.xpath("//*[@id='"+tab+"']/div/div/div/div[2]/div/div[1]/div/div/div/div/div/div/span")).getText().replaceAll("[\r\n]+", " ").replace(",", "");
								String secondAmount = driver.findElement(By.xpath("//*[@id='"+tab+"']/div/div/div/div[2]/div/div[2]/div/div/div/div/div/div/span")).getText().replaceAll("[\r\n]+", " ").replace(",", "");
								/*System.out.println("firstAmount: " + firstAmount);
								System.out.println("secondAmount: " + secondAmount);*/

								String[] parts = logo1.split("rectangular_logos/");
								String extractedPart = parts[1].replace(".png", "");
								System.out.println(""+firstAmount+"              : "+ extractedPart.replace(".png", ""));

								String[] parts2 = logo2.split("rectangular_logos/");
								String extractedPart2 = parts2[1].replace(".png", "");
								System.out.println(""+secondAmount+"              : "+ extractedPart2.replace(".png", ""));

								int DanatAmount = 0;
								int DanatPositionNumber = 0;
								for (int k = 1; k <= links.size() - 1; k++) {

									WebElement getVal = driver.findElement(By.xpath("//*[@id='"+tab+"']/div/div/div/div[2]/div/div["+k+"]/div/div/div[1]/div[1]/img"));
                                    String src = getVal.getAttribute("src");
									if (src.contains("rehlat")) {
										//System.out.println("src: " + src);
										String Amount = driver.findElement(By.xpath("//*[@id='"+tab+"']/div/div/div/div[2]/div/div["+k+"]/div/div/div/div/div/div/span")).getText().replaceAll("[\r\n]+", " ").replace(",", "");
										DanatAmount = Integer.parseInt(Amount);
										DanatPositionNumber = k;
										System.out.println(""+DanatAmount+"              : Danat");
										System.out.println(""+k+"                : Danat Rank");
										break;
									}

								}

								int amount_first = Integer.parseInt(firstAmount);
								int amount_second = Integer.parseInt(secondAmount);

								int comparFirstPrice = amount_first - DanatAmount;
								int comparSecondPrice = amount_second - DanatAmount;
								
								String Flight_Data = FromCity+" "+ToCity+" "+FltNum+" "+DepartDate+" "+DepartTiming+" "+Airline+" "+amount_first+" "+extractedPart+" 1 "+amount_second+" "+extractedPart2+" 2 "+DanatAmount+" Danat "+DanatPositionNumber+" ";
								System.out.println(Flight_Data);

								/*System.out.println("First Diffrence Amount: " + comparFirstPrice);
								System.out.println("Second Diffrence Amount: " + comparSecondPrice);*/

								WegoFlights wegoFlightsObj1 = null;
								WegoFlights wegoFlightsObj2 = null;
								WegoFlightsVar.clear();

								wegoFlightsObj1 = new WegoFlights(ToCity, FromCity,  Airline,DepartDate, DepartTiming, CurrencyCode, amount_first, Client,Domain,extractedPart, FltNum, "1"); 

								WegoFlightsVar.add(wegoFlightsObj1);

								wegoFlightsObj2 = new WegoFlights(ToCity, FromCity,  Airline,DepartDate, DepartTiming, CurrencyCode, amount_second, Client,Domain,extractedPart2, FltNum, "2");
								WegoFlightsVar.add(wegoFlightsObj2);
								
							   WegoApliClient.postCall(WegoFlightsVar);
								
							} else if (links.size() == 1) {
								System.out.println("Competitors Count: " + links.size());
								WebElement firstLogo   = driver.findElement(By.xpath("//*[@id='"+tab+"']/div/div/div/div[2]/div/div[1]/div/div/div[1]/div[1]/img"));
								String logo1 = firstLogo.getAttribute("src");

								String firstAmount  = driver.findElement(By.xpath("//*[@id='"+tab+"']/div/div/div/div[2]/div/div[1]/div/div/div/div/div/div/span")).getText().replaceAll("[\r\n]+", " ").replace(",", "");
								//System.out.println("firstAmount: " + firstAmount);

								String[] parts = logo1.split("rectangular_logos/");
								String extractedPart = parts[1].replace(".png", "");
								System.out.println(""+firstAmount+"              : "+ extractedPart.replace(".png", ""));

								int DanatAmount = 0;
								int DanatPositionNumber = 0;
								for (int k = 1; k <= links.size() - 1; k++) {

									WebElement getVal = driver.findElement(By.xpath("//*[@id='"+tab+"']/div/div/div/div[2]/div/div["+k+"]/div/div/div[1]/div[1]/img"));
                                    String src = getVal.getAttribute("src");
									if (src.contains("rehlat")) {
										//System.out.println("src: " + src);
										String Amount = driver.findElement(By.xpath("//*[@id='"+tab+"']/div/div/div/div[2]/div/div["+k+"]/div/div/div/div/div/div/span")).getText().replaceAll("[\r\n]+", " ").replace(",", "");
										DanatAmount = Integer.parseInt(Amount);
										DanatPositionNumber = k;
										System.out.println(""+DanatAmount+"              : Danat");
										System.out.println(""+k+"                : Danat Rank");
										break;
									}

								}

								int amount_first = Integer.parseInt(firstAmount);

								int comparFirstPrice = amount_first - DanatAmount;
								String Flight_Data = FromCity+" "+ToCity+" "+FltNum+" "+DepartDate+" "+DepartTiming+" "+Airline+" "+amount_first+" "+extractedPart+" 1 "+DanatAmount+" Danat "+DanatPositionNumber+" ";
								System.out.println(Flight_Data);

								//System.out.println("First Diffrence Amount: " + comparFirstPrice);

								WegoFlights wegoFlightsObj1 = null;
								WegoFlightsVar.clear();

								wegoFlightsObj1 = new WegoFlights(ToCity, FromCity, Airline,DepartDate, DepartTiming, CurrencyCode,  amount_first, Client,Domain,extractedPart, FltNum, String.valueOf(DanatPositionNumber));

								WegoFlightsVar.add(wegoFlightsObj1);
								WegoApliClient.postCall(WegoFlightsVar);
							} 
						} catch (Exception e) {

						}
						driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Flight Details'])[1]/following::*[name()='svg'][1]")).click();
						Thread.sleep(1000);
						/*Set<String> windows = driver.getWindowHandles();
						if (windows.size() >1) {
							for (String windowHandle : windows) {
					            if (!windowHandle.equals(parentWindowHandle)) {
					                driver.switchTo().window(windowHandle);
					                driver.close();
					            }
					        }
		                        driver.switchTo().window(parentWindowHandle);
		                        Thread.sleep(1000);
						    }
						    else {
						    	driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Flight Details'])[1]/following::*[name()='svg'][1]")).click();
								 Thread.sleep(1000);
							 }*/ 

					}

					catch (NoSuchElementException e) {
						continue;
					}
				}
			}
			} else {
				System.out.println("No Flights Available for this search");
				resultsForWego();
			}
		}
	}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("No Direct Flights Available for this search");
		}
	}


	public void wego_Login() throws InterruptedException {
	try {
	driver.get("https://sa.wego.com/en?modal=login");
	Thread.sleep(5000);
	WebElement loginemail = driver.findElement(By.xpath("//input[@id='email']"));
	loginemail.sendKeys("flyadealreservations@gmail.com");
	WebElement loginpassword = driver.findElement(By.xpath("//input[@id='password']"));
	loginpassword.sendKeys("Flyadeal@123");
	Thread.sleep(1000);
	WebElement loginbtn = driver.findElement(By.xpath("(//button[@type='submit'])[2]"));
	Thread.sleep(3000);
	loginbtn.click();
	Thread.sleep(5000);
	System.out.println("Wego SignIn");
	}
	catch (Exception e) {
	}
  }
}