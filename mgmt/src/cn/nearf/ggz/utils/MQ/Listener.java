package cn.nearf.ggz.utils.MQ;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.activemq.transport.stomp.StompConnection;
import org.apache.activemq.transport.stomp.StompFrame;

class Listener {

    public static void main(String []args) throws Exception {
    	
    	final String topic = "/topic/ORDER";  
    	
    	StompConnection connection = new StompConnection();    	
    	connection.open("newerp.helens.com.cn", 61613);        
    	connection.connect("admin", "robin1728");
    	connection.subscribe(topic);     
    	
    	while(true){  
            try {                 
                StompFrame message = connection.receive(600*1000);//60 秒钟timeout  
                System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " get -> " + message.getBody());  
            } catch (Exception e) {              	
                System.out.println(e.toString());  
                break;
            }                          
//            Thread.sleep(10);  
        }  
          
        connection.disconnect();  
    }  
    
}