package proxyserver.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import cryptographer.Cryptographer;
import datafeed.Datafeed;




public class SocketsServer {

    ServerSocket myServerSocket;
    boolean ServerOn = true;

    public SocketsServer() 
    { 
        try 
        { 
            myServerSocket = new ServerSocket(11111); 
        } 
        catch(IOException ioe) 
        { 
            System.out.println("Невозможно создать сокет на порту 11111"); 
            System.exit(-1); 
        } 

        // Сервер создан. Ожидаем подключений клиентов 
        while(ServerOn) 
        {                        
            try 
            { 
                 
                Socket clientSocket = myServerSocket.accept(); 
               
                // Для каждого клиента будет создан отдельный поток ClientServiceThread
                // для обработки запросов
                
                ClientServiceThread cliThread = new ClientServiceThread(clientSocket);
                cliThread.start(); 

            } 
            catch(IOException ioe) 
            { 
                System.out.println("Ошибка при подключении клиента. Stack Trace :"); 
                ioe.printStackTrace(); 
            } 

        }

        try 
        { 
            myServerSocket.close(); 
            System.out.println("Сервер остановлен"); 
        } 
        catch(Exception ioe) 
        { 
            System.out.println("Ошибка при остановке сервера"); 
            System.exit(-1); 
        } 
    } 

    public static void main (String[] args) 
    { 
        new SocketsServer();        
    } 
    
    //Метод расшифорвывающий номер билета
	private String decode(String inputBase64) {
		
		String key1 = "i-okvd-8auds-1id";
        String key2 = "0a-gsm-v2k-s6s-x";
		Cryptographer encryptor = new Cryptographer();
		String dec = encryptor.decrypt(key1, key2, inputBase64);
		String decTrim = dec.trim();
	    String number = decTrim.substring(decTrim.indexOf('|')+1);
	        
	    return number;
	}
	
	//Метод зашифровывающий сообщение от DataFeed
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

    class ClientServiceThread extends Thread 
    { 
        Socket myClientSocket;
        boolean m_bRunThread = true; 

        public ClientServiceThread() 
        { 
            super(); 
        } 

        ClientServiceThread(Socket s) 
        { 
            myClientSocket = s; 

        } 

        public void run() 
        {            
            // Обьявляем входящий и исходящий потоки для чтения/отправки данных
            
            BufferedReader in = null; 
            PrintWriter out = null; 
               
            try 
            {                                
                in = new BufferedReader(new InputStreamReader(myClientSocket.getInputStream())); 
                out = new PrintWriter(new OutputStreamWriter(myClientSocket.getOutputStream())); 

                while(m_bRunThread) 
                {                    
                    // Считываем сообщение клиента
                    String inputBase64 = in.readLine(); 
                                     
                    String ticketNumber = decode(inputBase64);
                    Datafeed a = new Datafeed();
                    String msgFromDatafeed = a.getStatus(ticketNumber);
                    String msgFromDatafeedEncrypted = encode(msgFromDatafeed);
            		
                    // Ответ сервера 
                    out.println(inputBase64 +" "+ ticketNumber +" "+ msgFromDatafeed +" "+ msgFromDatafeedEncrypted); 
                    out.flush(); 
                            
                           
                            
                            // Закрывает m_bRunThread
                           m_bRunThread = false;   
                    
                } 
            } 
            catch(Exception e) 
            { 
                e.printStackTrace(); 
            } 
            finally 
            { 
                try 
                {                    
                    in.close(); 
                    out.close(); 
                    myClientSocket.close(); 
                } 
                catch(IOException ioe) 
                { 
                    ioe.printStackTrace(); 
                } 
            } 
        } 


    } 
}
