package proxyserver.websockets;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import cryptographer.Cryptographer;
import datafeed.Datafeed;


@ServerEndpoint(value = "/proxyserver")
public class WebsocketsServer {
	
	// Принимаем на вход сообщение от клиента (зашифрованный inputBase64)
	@OnMessage
	public String getTicketNubmer(String inputBase64, Session session){
				
		//Расшифровываем inputBase64
		
 		String ticketNumber = decode(inputBase64);
 		
 	    // Полученный номер билета отсылаем на Datafeed
 		// Получаем сообщение о статусе билета (ошибке)
 		// Полученный ответ зашифровываем
 		
 		Datafeed a = new Datafeed();
		String msgFromDatafeed = a.getStatus(ticketNumber);
	    String msgFromDatafeedEncrypted = encode(msgFromDatafeed);
	    
	    // Возвращаем ответ клиенту в виде :
		// зашифрованное сообщение клиента (inputBase64) + расшифрованный номер билета + статус (код ошибки) билета от Datafeed + зашифрованный статус билета (код ошибки)
	    	
		return inputBase64 +" "+ ticketNumber +" "+ msgFromDatafeed +" "+ msgFromDatafeedEncrypted;
	}
	
	    // Метод, который расшифровывает inputBase64
		// Для этого используется Crptographer.decrypt()
		// К полученной строке применяем String.trim(), чтобы отбросить пустые байты
		// И получаем строку такого вида (пример TBV|25612617)
		// затем убираем символы, идущие до знака |, всё остальное это номер билета (25612617)
	
	private String decode(String inputBase64) {
		
		String key1 = "i-okvd-8auds-1id";
        String key2 = "0a-gsm-v2k-s6s-x";
		Cryptographer encryptor = new Cryptographer();
		String dec = encryptor.decrypt(key1, key2, inputBase64);
		String decTrim = dec.trim();
	    String number = decTrim.substring(decTrim.indexOf('|')+1);
	        
	    return number;
	}
	
	    // Метод, который зашифровывает строку
		// На вход он получает строку, которую необходимо зашифровать
		// Для этого необходимо, чтобы количество байтов в строке было кратным 16
		// Поэтому мы получаем информацию о количестве байтов в строке (Например 28)
		// И прибавляем к строке пустые байты, до тех пор, пока количество байтов в строке не станет 32
		// Полученную строку зашифровываем при помощи метода Crptographerencrypt ()
	
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
    
}
