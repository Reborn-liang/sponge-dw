package cn.nearf.ggz.utils.MQ;

import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.StompConnection;
import org.apache.log4j.Logger;

import cn.nearf.ggz.config.ErpConfig;
import cn.nearf.ggz.utils.SUThread;



public class ErpMQUtils {
	private static final Logger log = Logger.getLogger(ErpMQUtils.class);
	private static final int MaxRetry = 5;
	
	private static final Object ConnLock = new Object();
	private static volatile StompConnection connection;
	
	public static synchronized void sendMessage(final String message) {
		if (!ErpConfig.getBooleanProperty("ERP_SEND_MQ")) {
			return;
		}
		
		SUThread.newThread(new SUThread.SUThreadProtocol() {
			private String domain;
			private int port;
			private String userName;
			private String password;
			
			public void prepare() {
				domain = ErpConfig.getProperty("ERP_MQ_SERVER");
				port = ErpConfig.getIntProperty("ERP_MQ_SERVER_PORT");
				userName = ErpConfig.getProperty("ERP_MQ_SERVER_USER");
				password = ErpConfig.getProperty("ERP_MQ_SERVER_PWD");
			}
			
			private void syncGetConnection() throws Exception {
				synchronized (ConnLock) {
					int count = 0;
					Exception ex = null;
					while (connection == null && count < MaxRetry) {
						count ++;
						try {
							connection = getConnection(domain, port, userName, password);
						} catch (Exception e) {
							ex = e;
						}
					}
					
					if (connection == null) {
						if (ex != null) {
							throw ex;
						} else {
							throw new Exception("不能初始化MQ");
						}
					}
				}
			}
			
			public boolean process() throws Exception {
				syncGetConnection();
				
				HashMap<String, String> txtHeaders = new HashMap<String, String>();
				txtHeaders.put(Stomp.Headers.Send.PERSISTENT, "true");
				try {
					sendMessage(connection, message, txtHeaders);
				} catch (Exception e) {
					//发送错误，重试一次
					connection = null;
					syncGetConnection();
					sendMessage(connection, message, txtHeaders);
				}
				
				return false;
			}
			public void onGetError(String error) {
				log.warn(error);
			}
			public void finish() {
			}
			public void onFinally() {
			}
		}).start();
	}

	
	private static synchronized void sendMessage(StompConnection conn, String message, HashMap<String, String> headers) throws Exception {
		String tx = UUID.randomUUID().toString().replaceAll("-", "");
		conn.begin(tx);
		conn.send("/topic/ORDER", message, tx, headers);
		conn.commit(tx);
	}
	
	private static synchronized StompConnection getConnection(String domain, int port, String userName, String password) throws Exception {
		StompConnection connection = new StompConnection();
		Socket socket = new Socket(domain, port);
		socket.setSoTimeout(30 * 1000);
		connection.open(socket);
		connection.connect(userName, password);
		return connection;
	}
	
	public static void main(String[] args) {
		sendMessage("xxxxxxxx");
		System.out.println("发送完毕");
	}
}
