package cn.nearf.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.nearf.dw.common.TypeVO;
import cn.nearf.ggz.api.ApiDao;


@Controller
@RequestMapping("/commonToolController")
public class CommonToolController extends BaseController {
	
	@Autowired
	private ApiDao apiDao;
	
	@RequestMapping(params = "getDictByCode")
	@ResponseBody
    public List<TypeVO> getDictByCode(HttpServletRequest request){
		String typeGroupCode = request.getParameter("code");
		String sql = String.format(" select typecode,typename from t_s_type where typegroupid = (select id from t_s_typegroup where typegroupcode = '%s') ",typeGroupCode);
		List<Map<String, Object>> result = apiDao.findForJdbc(sql);
		List<TypeVO> dictRes = new ArrayList<TypeVO>();
		try {
			for (Map<String, Object> obj : result) {
				TypeVO vo = new TypeVO();

				String code = (String) obj.get("typecode");
				String name = (String) obj.get("typename");

				vo.setId(code);
				vo.setName(name);
				
				dictRes.add(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return dictRes;
    }
	
	@RequestMapping(params = "getExcelNamesByEntity")
	@ResponseBody
    public List<TypeVO> getExcelNamesByEntity(HttpServletRequest request){
		String entityName = request.getParameter("entityName");
		List<TypeVO> dictRes = new EntityToExcelUtil<>(entityName).getExcelExportNameByEntity();
    	return dictRes;
    }
	
	
	@RequestMapping(params = "multiSelExport")
	public ModelAndView multiSelExport(HttpServletRequest request) {
		request.setAttribute("entityName", request.getParameter("entityName"));
		request.setAttribute("selNames", request.getParameter("selNames"));
		request.setAttribute("hurl", request.getParameter("hurl"));
		return new ModelAndView("cn/nearf/common/multiSelExcelExport");
	}
	
	@RequestMapping(params = "setShowCloumns")
	public ModelAndView setShowCloumns(HttpServletRequest request) {
		request.setAttribute("fields", request.getParameter("fields"));
		request.setAttribute("titles", request.getParameter("titles"));
		request.setAttribute("selectFields", request.getParameter("selectFields"));
		return new ModelAndView("cn/nearf/common/multiSelShowCloumns");
	}
	
}
