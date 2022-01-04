package org.jeecgframework.poi.excel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.web.system.pojo.base.DictEntity;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.context.ApplicationContext;

public class ExcelDictUtil {
	
    private static SystemService systemService;
    
	private synchronized static SystemService getSystemService(SystemService systemService) {
		if (systemService == null) {
			ApplicationContext context = ApplicationContextUtil.getContext();
			systemService = context.getBean(SystemService.class);
		}
		return systemService;
	}
	
	public static String[] queryDict(String dicTable, String dicCode,String dicText){
		if(StringUtils.isEmpty(dicTable) && StringUtils.isEmpty(dicCode) && StringUtils.isEmpty(dicText)){
			return null;
		}
		systemService = getSystemService(systemService);
		List<String> dictReplace = new ArrayList<String>();
		List<DictEntity> dictList = null;
		//step.1 如果没有字典表则使用系统字典表
		if(StringUtils.isEmpty(dicTable)){
			dictList = systemService.queryDict(null, dicCode, null);
		}else {
			dictList = systemService.queryDict(dicTable, dicCode, dicText);
		}
		for(DictEntity t:dictList){
			dictReplace.add(t.getTypename()+"_"+t.getTypecode());
		}
		if(dictReplace!=null && dictReplace.size()!=0){
			return dictReplace.toArray(new String[dictReplace.size()]);
		}
		return null;
	}

}
