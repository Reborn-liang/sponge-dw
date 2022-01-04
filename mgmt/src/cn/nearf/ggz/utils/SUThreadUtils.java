package cn.nearf.ggz.utils;

public class SUThreadUtils {

	
	
	public static void start(Runnable runnable) {
		new Thread(runnable).start();
	}
	
	
}
