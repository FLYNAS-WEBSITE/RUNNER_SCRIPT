package com.wegoflyadeal.helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyFileReader {
	Properties properties = null;

	public PropertyFileReader()
	{	
		properties = new Properties();
		loadProperties();
	}

	private void loadProperties()
	{
		try
		{
			FileInputStream inputStream = new FileInputStream("src/main/resources/WegoFlightsApiServices.properties");
			properties.load(inputStream);
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public String readProperty(String property)
	{
		return properties.getProperty(property);
	}

}
