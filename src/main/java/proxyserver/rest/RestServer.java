package proxyserver.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import cryptographer.Cryptographer;
import datafeed.Datafeed;

@Path("/Ticket")
public class RestServer {

	@Produces({"application/json"})
    @Path("{inputBase64 : (.+)?}")
    @GET
    public String getTicket(@PathParam("inputBase64") String inputBase64) {

		StringBuffer response = new StringBuffer();
        try {                             
            
            Datafeed a = new Datafeed();
     		String ticketNumber = decode(inputBase64);
    		String msgFromDatafeed = a.getStatus(ticketNumber);
    	    String msgFromDatafeedEncrypted = encode(msgFromDatafeed);
           
            response.append(inputBase64 +" "+ ticketNumber +" "+ msgFromDatafeed +" "+ msgFromDatafeedEncrypted);
        
        }
        catch (Exception e) {
        }

        String ticketInfo = response.toString();

        return ticketInfo;
    }

	private String encode(String msgFromDatafeed) {
	    	
		    String key1 = "i-okvd-8auds-1id";
	        String key2 = "0a-gsm-v2k-s6s-x";
			String multiple16 = msgFromDatafeed;
			
	        byte [] bts = multiple16.getBytes();
	                
	        for (int i=0; i<32-bts.length;i++){
	        	multiple16 += " ";
	        }
	           	        	        
	        Cryptographer encryptor = new Cryptographer();
			String enc = encryptor.encrypt(key1, key2, multiple16);
	       	       	        
		    return enc;
		}
	
	private String decode(String inputBase64) {
		
		String key1 = "i-okvd-8auds-1id";
        String key2 = "0a-gsm-v2k-s6s-x";
		Cryptographer encryptor = new Cryptographer();
		String dec = encryptor.decrypt(key1, key2, inputBase64);
		String decTrim = dec.trim();
	    String number = decTrim.substring(decTrim.indexOf('|')+1);
	        
	    return number;
	}
}
