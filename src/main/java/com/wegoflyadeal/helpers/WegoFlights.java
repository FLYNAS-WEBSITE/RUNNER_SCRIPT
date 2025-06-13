package com.wegoflyadeal.helpers;

import java.util.List;

public class WegoFlights {

	
	public String to;
	public String frm;
	
	public String arlncd;
	public String depdt;
	public String dm;
	public String stym;
	public String cur;
	
	public int APIPrice;
	public String clt;
	public String prv;
	
	public String fn;
	public int ps;
	

	public WegoFlights() {}

	public WegoFlights( String to, String frm, String arlncd,
					   String depdt, String stym, String cur, int APIPrice,
					   String clt,String dm, String prv, String fn, String ps)
	{
		
		this.to = to;
		this.frm = frm;
		this.arlncd = arlncd;
		this.depdt = depdt;
		this.dm = dm;
		this.stym = stym;
		this.cur = cur;
		this.APIPrice = APIPrice;
		this.clt = clt;
		this.prv = prv;
		this.fn = fn;
		this.ps = Integer.parseInt(ps);
	
	}
	public static class Fare {
        public int ps;
        public String prv;
        public String fare;

        public Fare() {}

        public Fare(int ps, String prv, String fare) {
            this.ps = ps;
            this.prv = prv;
            this.fare = fare;
        }
    }

    public static class Flight {
        public String ftyp;
        public String fn;
        public String jrnytm;
        public String stym;
        public String edtym;
        public String stdt;
        public String starpt;
        public String edarpt;
        public String adtbp;
        public String cldbp;
        public String infbp;
        public String dychg;
        public List<Fare> fares;

        public Flight() {}

        public Flight(String ftyp, String fn, String jrnytm, String stym, String edtym, String stdt,
                      String starpt, String edarpt, String adtbp, String cldbp, String infbp,
                      String dychg, List<Fare> fares) {
            this.ftyp = ftyp;
            this.fn = fn;
            this.jrnytm = jrnytm;
            this.stym = stym;
            this.edtym = edtym;
            this.stdt = stdt;
            this.starpt = starpt;
            this.edarpt = edarpt;
            this.adtbp = adtbp;
            this.cldbp = cldbp;
            this.infbp = infbp;
            this.dychg = dychg;
            this.fares = fares;
        }
    }

    public static class FlightRequest {
        public String frm;
        public String to;
        public String cur;
        public String arlncd;
        public String depdt;
        public String clt;
        public String dm;
        public List<Flight> flt; 

        public FlightRequest() {}

        public FlightRequest(String frm, String to, String cur, String arlncd,
                             String depdt, String clt, String dm, List<Flight> flt) {
            this.frm = frm;
            this.to = to;
            this.cur = cur;
            this.arlncd = arlncd;
            this.depdt = depdt;
            this.clt = clt;
            this.dm = dm;
            this.flt = flt;
        }
    }
	
	
}
