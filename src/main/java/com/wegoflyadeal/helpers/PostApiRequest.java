package com.wegoflyadeal.helpers;

import java.util.List;

public class PostApiRequest {
	
	public List<WegoFlights> wegoFlights;
	
	public PostApiRequest()
	{
	}

	public PostApiRequest(List<WegoFlights> wegoFlights)
	{
		this.wegoFlights = wegoFlights;
	}

}
