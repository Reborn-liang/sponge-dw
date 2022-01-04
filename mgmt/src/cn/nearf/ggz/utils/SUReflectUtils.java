package cn.nearf.ggz.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.Date;

import com.google.gson.Gson;

public class SUReflectUtils {
	
	public static Field getField(Object entity, String name) throws Exception {
		return entity.getClass().getDeclaredField(name);
	}

	public static <T> T getFieldValue(Object entity, String name) throws Exception {
		Field field = getField(entity, name);
		boolean ogAccessible = field.isAccessible();
		field.setAccessible(true);
		T idValue = (T) field.get(entity);
		field.setAccessible(ogAccessible);

		return idValue;
	}
	
	public static void setFieldValue(Object entity, String name, Object value) throws Exception {
		Field field = getField(entity, name);
		boolean ogAccessible = field.isAccessible();
		field.setAccessible(true);
		try {
			field.set(entity, value);
		} finally {
			field.setAccessible(ogAccessible);
		}
	}
	
	public static boolean isPrimitiveOrWrapClass(Class clz) {
		try {
			return CharSequence.class.isAssignableFrom(clz) || Date.class.isAssignableFrom(clz)
					|| clz.isPrimitive()
					|| ((Class) clz.getField("TYPE").get(null)).isPrimitive()
					;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	
	public static void copyValues(Object fromObj, Object toObj) {
		try {
			Field[] fields = fromObj.getClass().getDeclaredFields();
			for (Field field : fields) {
				try {
					Object value = getFieldValue(fromObj, field.getName());
					setFieldValue(toObj, field.getName(), value);
				} catch (Exception e) {
				}
				
			}
			
		} catch (Exception e) {
		}
	}
	
	
	
	public static void main(String[] args) {
		TestA a = new TestA();
		
		System.out.println(a);
		
		Field[] fs = TestA.class.getDeclaredFields();
		for (Field f : fs) {
			try {
				if (Modifier.isFinal(f.getModifiers())) {
					continue;
				}
				if (f.getType() == int.class || f.getType() == Integer.class) {
					continue;
				}
				f.setAccessible(true);
				f.set(a, ObjectUtils.getPriceYuanValue(ObjectUtils.getDoubleValue(f.get(a))));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(a);
		
		
		if (true) {
			return;
		}
		
		
		
		System.out.println(isPrimitiveOrWrapClass(int.class));
		System.out.println(isPrimitiveOrWrapClass(Integer.class));
		System.out.println(isPrimitiveOrWrapClass(String.class));
		System.out.println(isPrimitiveOrWrapClass(Timestamp.class));
		
		
		System.out.println("xxxxxxxxxxxxxxxxxxx");
		
		System.out.println(int.class.isPrimitive());
		System.out.println(Integer.class.isPrimitive());
		System.out.println(String.class.isPrimitive());
		System.out.println(String.class.isSynthetic());
		System.out.println(String.class.isAssignableFrom(CharSequence.class));
		
		System.out.println(Timestamp.class.isPrimitive());
		System.out.println(Timestamp.class.isSynthetic());
		System.out.println(Timestamp.class.isAssignableFrom(Date.class));
		System.out.println(java.sql.Date.class.isAssignableFrom(Date.class));
		
		System.out.println("xxxxxxxxxxxxxxxxxxx");
		
		System.out.println(Date.class.isAssignableFrom(Timestamp.class));
		System.out.println(Date.class.isAssignableFrom(TestClass.class));
		System.out.println(Date.class.isAssignableFrom(java.sql.Date.class));
		System.out.println(Date.class.isAssignableFrom(Date.class));
	}
	
	public static class TestA {
		public final double da = 1.5;
		public final int ia = 2;
		
		public double db = 3.5;
		public int ib = 2;
		
		@Override
		public String toString() {
			return new Gson().toJson(this);
		}
	}
	
	
	public static class TestClass extends Date {
		
	}
}
