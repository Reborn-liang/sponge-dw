package cn.nearf.ggz.utils.gson;

import java.lang.reflect.Type;

import com.google.gson.Gson;

public class GsonUtils {
	public static final Gson gson = new Gson();
	
	public static String toJson(Object obj) {
		return gson.toJson(obj);
	}
	
	public static <T> T fromJson(String json, Type typeOfT) {
		return gson.fromJson(json, typeOfT);
	}
	
	public static void main(String[] args) {
		System.err.println(toJson(null));
	}
}
