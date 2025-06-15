package com.wegoflyadeal.runners;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.wegoflyadeal.api.WegoApliClient;
import com.wegoflyadeal.constants.Constants;
import com.wegoflyadeal.helpers.WegoFlights;

public class TC_003_IncreaseBy50 {

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
	String Domain = "KW";
	String DiffPrice = "";
	String FltNum ="";
	String FlightNumber ="";

	boolean isWegoStarted = false;
	String PgFee = "";
	String Lead = "";

	boolean isAirline = false;

	String[] alphaRoutes = {"ADD-KWI", "AMM-KWI", "AMM-RUH", "ATZ-KWI", "BAH-KWI", "BEY-KWI", "CAI-KWI", "DOH-KWI", "DXB-KWI", "HBE-KWI", "HMB-KWI", "IST-KWI"};

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

		FirefoxOptions options = new FirefoxOptions();
		options.addPreference("layout.css.devPixelsPerPx", "0.3");
		options.addPreference("permissions.default.image", 2);
		options.addArguments("--headless");
		driver = new FirefoxDriver(options);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
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
    	performApiCall(Constants.Get_API_Path);
       
        
    }

    private void performApiCall(String apiPath) throws ClientProtocolException, IOException, ParseException {
        System.err.println("URI : " + apiPath);
        CloseableHttpResponse closeableHttpResponse = WegoApliClient.get(apiPath);
        String response = EntityUtils.toString(closeableHttpResponse.getEntity(), "utf-8");

        // Save original response to file
        Files.write(Paths.get(Constants.JSON_RESULTFILE_PATH), response.getBytes());
        
        // Process the response
        getDataFromApiResponse();
    }

    
    public void getDataFromApiResponse() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        Set<String> alphaRouteSet = new HashSet<>(Arrays.asList(alphaRoutes));
        try (FileReader reader = new FileReader(Constants.JSON_RESULTFILE_PATH)) {
            Object obj = jsonParser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            org.json.simple.JSONArray responseList = (org.json.simple.JSONArray) jsonObject.get("payload");

            int totalSize = responseList.size();
            System.out.println("totalSize : " + totalSize);

            //for (int i = 0; i < responseList.size(); i++) {
            for (int i = responseList.size() - 1; i >= 0; i--) {
                try {
                    JSONObject details = (JSONObject) responseList.get(i);
                    
                    String client = (String) details.get("client");
                    if (!"WEGO".equalsIgnoreCase(client)) {
                        continue; // Skip non-WEGO routes
                    }

                    String source = (String) details.get("From");
                    Source = source;

                    String destination = (String) details.get("To");
                    Destination = destination;

                    String routeKey = source + "-" + destination;

                    // Only proceed if routeKey is in alphaRoutes
                    if (!alphaRouteSet.contains(routeKey)) {
                        continue;
                    }
                    
                    String departureDateStr = (String) details.get("DepartureDate");
                    DepartDate = departureDateStr;

                    Client = (String) details.get("client");

                    // Parse "24 May 2025" to "2025-05-24"
                    LocalDate departureDate = LocalDate.parse(departureDateStr, DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH));
                    String isoDate = departureDate.toString();
                    String monthOfDate = String.valueOf(departureDate.getMonthValue());
                    String dayOfDate = String.valueOf(departureDate.getDayOfMonth());

                    // Log and call search
                    System.out.println("Record Number: " + (i + 1));
                    System.out.println("Source: " + source);
                    System.out.println("Destination: " + destination);
                    System.out.println("DepartureDate: " + departureDateStr);
                    System.out.println("I value: " + i);
                    driver.manage().deleteAllCookies();
                    // Run your scraping logic
                    search(source, destination, isoDate, monthOfDate, dayOfDate, Client);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("flightRunCount : " + flightRunCount);
        System.out.println("completedFlightNumbers : " + completedFlightNumbers);
    }



	String websiteName = "kw.wego.com";

	// com wego = kw.wego.com rehlat.com
	// ae wego = www.wego.ae our site rehlat.ae
	// sa wego = sa.wego.com our site rehlat.com.sa
	// eg wego = eg.wego.com our site rehlat.com.eg
	private Set<String> visitedURLs = new HashSet<>();

	public void search(String source, String destination, String date, String DepartureMonth, String DepartureDay, String airline) throws InterruptedException {
		
	    //Wego_URL = "https://"+websiteName+"/en/flights/searches/"+source+"-"+destination+"-"+ date+"/economy/1a:0c:0i?sort=price&order=asc&airlines=F3%2CXY";

	    Wego_URL = "https://"+websiteName+"/en/flights/searches/"+source+"-"+destination+"-"+ date+"/economy/1a:0c:0i?sort=score&order=asc&payment_methods=97%2C191%2C189&airlines=J9%2CKU%2CSV%2CGF%2CRJ%2CEK%2CQR%2CTK%2CVF%2CWY%2CME%2CFZ%2CUL%2CEY%2CPC%2CET%2CG9%2CSM";

	    // Check if the URL has been visited before
	    if (visitedURLs.contains(Wego_URL)) {
	        System.out.println("Duplicate URL detected - Skipping search");
	        return; // Skip the search if the URL is a duplicate
	    }

	    // Visit the URL and add it to the set of visited URLs
	    driver.get(Wego_URL);
	    System.out.println(Wego_URL);

	    try {
	        Thread.sleep(5000);
	    } catch (InterruptedException e) {
	    }
	    resultsForWego();
	    //Next_Dates();
	    // Add the current URL to the set of visited URLs
	    visitedURLs.add(Wego_URL);
	}
	
	public void resultsForWego() throws InterruptedException {
	    int maxRetries = 2;
	    boolean isPageLoaded = false;
	    boolean isNewSRP = false;

	    for (int retryCount = 1; retryCount <= maxRetries; retryCount++) {
	        try {
	            WebDriverWait wait = new WebDriverWait(driver, 5);
	            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(text(),'Stops')]")));
	            System.out.println("Wego SRP Page Displayed");
	            
	            // Attempt to close any unexpected new tab
	            try {
	                WebElement checkbox = driver.findElement(By.xpath("//div[contains(@class, 'HjHhQ5P5R0Q0aCiTlV7G') and contains(@class, 'Ia5JDEF_0rL4Gh4_fLO7')]"));
	                checkbox.click();
	                String originalWindow = driver.getWindowHandle();

	                Set<String> oldWindows = driver.getWindowHandles();
	                Set<String> newWindows = driver.getWindowHandles();

	                for (String windowHandle : newWindows) {
	                    if (!oldWindows.contains(windowHandle)) {
	                        driver.switchTo().window(windowHandle);
	                        System.out.println("Closing unexpected new tab: " + driver.getTitle());
	                        driver.close();
	                        driver.switchTo().window(originalWindow);
	                        break;
	                    }
	                }
	            } catch (Exception e) {}

	            results(false); // old SRP
	            isPageLoaded = true;
	            break;
	        } catch (Exception e) {
	            try {
	                WebDriverWait wait = new WebDriverWait(driver, 5);
	                WebElement filters = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(),'All Filters')]")));
	                System.out.println("Wego NEW SRP Page Displayed");
	                filters.click();
	                Thread.sleep(2000);
	                isNewSRP = true;

	                results(true); // new SRP
	                isPageLoaded = true;
	                break;
	            } catch (Exception e1) {
	                System.out.println("Wego SRP Page not Displayed - Retry #" + retryCount);
	                driver.get(Wego_URL);
	                Thread.sleep(5000);
	            }
	        }
	    }

	    if (!isPageLoaded) {
	        System.out.println("Wego SRP Page could not be loaded after " + maxRetries + " retries");
	    }
	}
	
	public void results(boolean isNewSRP) throws InterruptedException {

		try {
			WebElement DirectFlights = driver.findElement(By.xpath("//*/text()[normalize-space(.)='Direct']/parent::*"));
			DirectFlights.click();
			//Thread.sleep(2000);
			WebElement Date = driver.findElement(By.xpath("//div[@data-testid='from-input-value']"));
			String DepDate = Date.getText().replace("Mon, ", "").replace("Tue, ", "").replace("Wed, ", "").replace("Thu, ", "").replace("Fri, ", "").replace("Sat, ", "").replace("Sun, ", "");
			//System.out.println(DepDate);
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
			LocalDate parsedDate = LocalDate.parse(DepDate, inputFormatter);
			String DepartDate = parsedDate.format(inputFormatter);
            System.out.println(DepartDate);
		WebElement ResultsCount = driver.findElement(By.xpath("//div[contains(text(),' of ')]"));

		// Get the text from the element
		String Results_Count = ResultsCount.getText();
		String NumberResults = Results_Count.split(" ")[0];

		if ("0".equals(NumberResults)) {
			System.out.println("No Flights Available");
		} else {
            Thread.sleep(4000);
			List<WebElement> getCount = driver.findElements(By.cssSelector("div[data-pw='flightSearchResults_tripCard']"));
			//System.out.println("GetCount of Results:" + getCount.size());
			int count = 0;
			System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
			 String searchText = "websites";		 
		        for (WebElement flightDetails : getCount) {
		            String detailsText = flightDetails.getText().replaceAll("[\r\n]+", " ").replace(",", "").replace("SAR", "").replace("Per person", "").replace("Refundable ", "").split("View Deals")[0].trim();
		            if (isNewSRP || detailsText.contains(searchText)) {
		            	System.out.println(detailsText);
		                count++;
		            }
		        }

		        System.out.println("Total Flights: " + count);
		        
		        if (getCount.isEmpty()) {
		            System.out.println("No Flights Available for this search");
		            resultsForWego();
		            return;
		        }

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
					 if (isNewSRP || detailsText.contains(searchText)) {
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
						FltNum = flightNumber.getText().replaceAll("[\r\n]+", " ").replace(",", "").replace(Airline, "").replace(" ", "");
						//FltNum = Airline + FltNum;
						System.out.println("Flight Number    : " + FltNum);
						System.out.println("Departure Timing : " + DepartTiming);
												
						try {
							
							List<WebElement> links = driver.findElements(By.xpath("//*[@id='" + tab + "']/div/div/div/div[2]/div/div/div/div/div[1]/div[1]/img"));

							if (links.size() >= 1) {
							    
							    if (links.size() >= 10) {
							        try {
							            WebElement showMore = driver.findElement(By.xpath("//div[@class='nlltapmKPb5TLZMtHVHk']"));
							            showMore.click();
							            Thread.sleep(1000);
							        } catch (Exception e) {
							            System.out.println("No ShowMore button found.");
							        }
							    }

							    class ProviderPrice {
							        String provider;
							        int price;
							        String logo;
							        int originalIndex;

							        ProviderPrice(String provider, int price, String logo, int originalIndex) {
							            this.provider = provider;
							            this.price = price;
							            this.logo = logo;
							            this.originalIndex = originalIndex;
							        }
							    }

							    List<ProviderPrice> providerPrices = new ArrayList<>();
							    ProviderPrice danatEntry = null;
							    List<WebElement> Alllinks = driver.findElements(By.xpath("//*[@id='" + tab + "']/div/div/div/div[2]/div/div/div/div/div[1]/div[1]/img"));
							    System.out.println("Competitors Count: " + Alllinks.size());
							    for (int i = 1; i <= Alllinks.size(); i++) {
							        try {
							        	WebDriverWait wait = new WebDriverWait(driver, 8); 
							        	WebElement logo = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='" + tab + "']/div/div/div/div[2]/div/div[" + i + "]/div/div/div[1]/div[1]/img")));
							            WebElement fareElement = driver.findElement(By.xpath("//*[@id='" + tab + "']/div/div/div/div[2]/div/div[" + i + "]/div/div/div/div/div/div/span"));

							            String logoSrc = logo.getAttribute("src");
							            String providerName = logoSrc.contains("rectangular_logos/")
							                    ? logoSrc.split("rectangular_logos/")[1].replace(".png", "")
							                    : logoSrc.substring(logoSrc.lastIndexOf("/") + 1).replace(".png", "");

							            int price = Integer.parseInt(fareElement.getText().replaceAll("[\r\n]+", " ").replace(",", "").trim());

							            ProviderPrice p = new ProviderPrice(providerName, price, logoSrc, i);
							            providerPrices.add(p);

							            if (providerName.toLowerCase().contains("danat")) {
							                danatEntry = p;
							            }

							        } catch (Exception e) {
							            System.out.println("Error processing competitor at index " + i);
							            e.printStackTrace();
							        }
							    }

							    // Step 1: Sort by price ASC, then DOM index ASC
							    //providerPrices.sort(Comparator.comparingInt((ProviderPrice p) -> p.price).thenComparingInt(p -> p.originalIndex));
							    providerPrices.sort(Comparator.comparingInt(p -> p.originalIndex));

							    // Step 2: Pick top 3 excluding Danat
							    List<ProviderPrice> topProviders = new ArrayList<>();
							    for (ProviderPrice p : providerPrices) {
							        if (!p.provider.toLowerCase().contains("danat") && topProviders.size() < 3) {
							            topProviders.add(p);
							        }
							    }

							    // Step 3: Include real Danat if found
							    if (danatEntry != null && !topProviders.contains(danatEntry)) {
							        topProviders.add(danatEntry);
							    }

							    // Step 4: If Danat not found, add fallback
							    if (danatEntry == null) {
							        //topProviders.add(new ProviderPrice("danattravels.com", 0, "", 0));
							    }

							    // Step 5: Sort topProviders again to finalize correct price + DOM order
							    //topProviders.sort(Comparator.comparingInt((ProviderPrice p) -> p.price).thenComparingInt(p -> p.originalIndex));

							    // Step 5: Assign unique positions
							    WegoFlightsVar.clear();
							    for (ProviderPrice p : topProviders) {
							        String posToSend;

							        if (p.price == 0 && p.provider.toLowerCase().contains("danat")) {
							            posToSend = "0"; // fallback Danat with no price
							        } else {
							            posToSend = String.valueOf(p.originalIndex); // ✅ always DOM index
							        }

							        String fullFltNum = Airline + FltNum;

							        WegoFlights flight = new WegoFlights(
							            ToCity, FromCity, Airline,
							            DepartDate, DepartTiming, CurrencyCode,
							            p.price, Client, Domain,
							            p.provider, fullFltNum, posToSend
							        );

							        System.out.println(p.price + " : " + p.provider + " Position: " + posToSend);
							        WegoFlightsVar.add(flight);
							    }

							    // Step 6: Post to API
							    WegoApliClient.postCall(WegoFlightsVar);
							}							
							
						} catch (Exception e) {

						}
						driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Flight Details'])[1]/following::*[name()='svg'][1]")).click();
						Thread.sleep(1000);
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
			//e.printStackTrace();
			System.out.println("No Direct Flights Available for this search");
		}
	}

	public void Next_Dates() throws InterruptedException {
		
		for (int i = 1; i < 10; i++) {
		    try {
		        // Click the "from-chevron-right" element
		        
		        driver.findElement(By.xpath("//div[@data-testid='from-chevron-right']")).click();
		        Thread.sleep(1000);

		        // Click the "Search" button
		        driver.findElement(By.xpath("//button[@type='submit' and .//span[text()='Search']]")).click();
		        Thread.sleep(8000);
		        Wego_URL = driver.getCurrentUrl();
		        // Call the method to process results
		        resultsForWego();
		        System.out.println("Loop " + (i + 1) + " completed successfully.");
		    } catch (Exception e) {
		        System.err.println("Error occurred in loop " + (i + 1) + ": " + e.getMessage());
		    }
		}
	
	}

	
	@AfterMethod
	public void teardown() {
		if (driver != null) {
			driver.quit();
		}
	}
}