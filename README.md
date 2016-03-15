#ProxyServer: REST Websockets Sockets
Три реализации ProxyServer с технологиями: **REST**, **Websockets**, **Sockets**    

Все варианты работают следующим образом:    
    
<table>
  <tbody>
    <tr>
    <td>1</td>
      <td>ProxyServer получает сообщение, зашифрованное при помощи AES, от Client</td>
      <td>LXDpd</td>
    </tr>
    <tr>
     <td>2</td>
      <td>Расшифровывает это сообщение и считывает номер</td>
      <td>LXDpd -> RNS|256126 -> 256126</td>
    </tr>
     <td>3</td>
      <td>Отсылает полученный номер на Datafeed</td>
      <td>256126</td>
    </tr>
     <td>4</td>
      <td>Обратно получает статус, который соотвествует данному номеру</td>
      <td>256126 = status 4</td>
    </tr>
     <td>5</td>
      <td>Зашифровывает данный статус при помощие AES и посылает Client</td>
      <td>status 4 -> iCujo9p</td>
    </tr>
  </tbody>
</table>

Другое:    
     
1) Проект собран при помощи IntelliJ IDEA и Maven    
2) Поддерживаетcя обработка нескольких одновременных сообщений от Client    
3) Класс SocketsServer необходимо запускать отдельно        
![proxyserver](https://cloud.githubusercontent.com/assets/13558216/13759176/7c8d0646-ea46-11e5-95c0-89551efbdfaa.JPG)   
Three variants of ProxyServer with different technologies: **REST**, **Websockets**, **Sockets**    

All variants work this way:   

<table>
<tbody>
the <tr>
<td>1</td>
<td>ProxyServer receives encrypted AES message from the Client</td>
<td>LXDpd</td>
</tr>
the <tr>
<td>2</td>
<td>Then decrypts the message and parse the number</td>
<td>LXDpd -> RNS|256126 -> 256126</td>
</tr>
<td>3</td>
<td>Sends the parsed number to Datafeed</td>
<td>256126</td>
</tr>
<td>4</td>
<td>Gets the status that is equak to this number</td>
<td>256126 = status 4</td>
</tr>
<td>5</td>
 <td>Encrypts this status with the help of AES and sends to the Client</td>
<td>status 4 -> iCujo9p</td>
</tr>
</tbody>
</table>

Other :
     
1) The project is built with IntelliJ IDEA  and Maven    
2) ProxyServer can recieve multiple simultaneous messages from Client     
3) Class SocketsServer must be run separately     
