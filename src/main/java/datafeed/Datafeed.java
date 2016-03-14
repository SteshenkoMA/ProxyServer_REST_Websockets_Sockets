package datafeed;

import java.util.TreeMap;
import java.util.Map;

public class Datafeed{
	
	private  Map<String, String> tickets = new TreeMap<>();
	
	public Datafeed() {
		populateMap();
	}
		
 	public String getStatus(String input) {
		 return tickets.get(input);
	}

 	private void populateMap(){
		 tickets.put("25612617", "Error 5");
		 tickets.put("999999", "Forbidden");
		 tickets.put("111", "OK");
		  
	}
}