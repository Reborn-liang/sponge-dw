package cn.nearf.ggz.utils.MQ;

import java.util.HashMap;
import java.util.UUID;

import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.StompConnection;

class Publisher {

	public static void main(String[] args) throws Exception {
		StompConnection connection = new StompConnection();
		connection.open("newerp.helens.com.cn", 61613);
		connection.connect("admin", "admin");

		// send text message
		for (int i = 0; i < 100; i++) {
			HashMap<String, String> txtHeaders = new HashMap<String, String>();
			txtHeaders.put(Stomp.Headers.Send.PERSISTENT, "true");
			String text = "stomp text message "+i;
			sendMessage(connection, text, txtHeaders);
			System.out.println("send: " + text);
		}

		// disconnect
		connection.disconnect();

	}

	/**
	 * 发送消息
	 * 
	 * @param conn
	 *            jms connection
	 * @param message
	 *            message content
	 * @param headers
	 *            message headers
	 * @throws Exception
	 *             exception
	 */
	private static void sendMessage(StompConnection conn, String message,
			HashMap<String, String> headers) throws Exception {
		String tx = UUID.randomUUID().toString().replaceAll("-", "");
		conn.begin(tx);
		conn.send("/topic/ORDER", message, tx, headers);
		conn.commit(tx);
	}

}