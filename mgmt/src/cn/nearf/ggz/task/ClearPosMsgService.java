package cn.nearf.ggz.task;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.nearf.ggz.api.ApiDao;
import cn.nearf.ggz.config.ErpConfig;

@Service("clearPosMsgService")
public class ClearPosMsgService {
	private static Logger log = Logger.getLogger(ClearPosMsgService.class);
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
    private ApiDao apiDao;
	
	public void start() {
		if (!ErpConfig.getBooleanProperty("Run_Report_Task")) {
			return;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -2);	//由7天调整为3天
		Date date = calendar.getTime();
		
//		Object[] params = {2, date};
//		systemService.excuteUpdateDelete("delete ErpPosMessageEntity where status = ? and createDate < ?", params);
		Object[] params = {date};
		systemService.excuteUpdateDelete("delete ErpPosMessageEntity where createDate < ?", params);
		
		
	}
}
