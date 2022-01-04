package cn.nearf.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.jeecgframework.poi.excel.annotation.Excel;

import cn.nearf.dw.common.TypeVO;

public class EntityToExcelUtil<E> {

	private String name;

	public EntityToExcelUtil(String name) {
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	private E get() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return (E) Class.forName(name).newInstance();
	}
	
	public List<TypeVO> getExcelExportNameByEntity() {
		List<TypeVO> dictRes = new ArrayList<TypeVO>();
		try {
			E entity = get();
			if (entity != null) {
				Field[] fields = entity.getClass().getDeclaredFields();
				for (Field f : fields) {
					// 获取字段中包含Excel的注解
					Excel meta = f.getAnnotation(Excel.class);
					if (meta != null) {
						TypeVO vo = new TypeVO();
						vo.setId(f.getName());
						vo.setName(meta.name());
						dictRes.add(vo);
					}
				}
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e ) {
			e.printStackTrace();
		}
		
		return dictRes;
	}
	
}
