package cn.nearf.ggz.utils.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import cn.nearf.ggz.utils.SUReflectUtils;

public class NoSubObjectExclusionStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes fieldAtrr) {
		return !SUReflectUtils.isPrimitiveOrWrapClass(fieldAtrr.getDeclaredClass());
	}
	

}
