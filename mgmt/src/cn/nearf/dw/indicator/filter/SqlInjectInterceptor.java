package cn.nearf.dw.indicator.filter;

import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.owasp.esapi.ESAPI;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;

public class SqlInjectInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) {

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3) {

    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        String cookie = request.getHeader("cookie");
        TSUser sessionUser = ResourceUtil.getSessionUserName();
        if (sessionUser != null) {
            if (sessionUser.getUserKey() == null || !sessionUser.getUserKey().equals("管理员")) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Your account has no access!");
            }
        }
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String[] values = request.getParameterValues(name);
            for (String value : values) {
                //sql注入直接拦截
/*                if(judgeSQLInject(value.toLowerCase())){
                    response.setContentType("text/html;charset=UTF-8");
                    response.getWriter().print("参数含有非法攻击字符,已禁止继续访问！");
                    return false;
                }*/
                //跨站xss清理
                String s = cleanXSS(value);
                if (!value.equals(s)) {
                    //                    throw new Exception("No access!");
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("No access!");
//                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("webpage/common/error.jsp");
//                    requestDispatcher.forward(request, response);
                    return false;
                }

            }
        }
        return true;
    }

    /**
     * 判断参数是否含有攻击串 * @param value * @return
     */
    public boolean judgeSQLInject(String value) {
        if (value == null || "".equals(value)) {
            return false;
        }
        String xssStr = "and|or|select|update|delete|drop|truncate|%20|=|-|--|;|'|%|#|+|,|//|/| |\\|!=|(|)";
        String[] xssArr = xssStr.split("\\|");
        for (int i = 0; i < xssArr.length; i++) {
            if (value.indexOf(xssArr[i]) > -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 处理跨站xss字符转义 * * @param value * @return
     */
    private String cleanXSS(String value) {
        if (value != null) {
            // 推荐使用ESAPI库来避免脚本攻击
            value = ESAPI.encoder().canonicalize(value);

            // 避免空字符串
            value = value.replaceAll("", "");

            // 避免script 标签
            Pattern scriptPattern = compile("<script>(.*?)</script>", CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            //避免src形式的表达式
            //scriptPattern = compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", CASE_INSENSITIVE | MULTILINE | DOTALL);
            //value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", CASE_INSENSITIVE | MULTILINE | DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            // 删除单个的 </script> 标签
            scriptPattern = compile("</script>", CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            // 删除单个的<script ...> 标签
            scriptPattern = compile("<script(.*?)>", CASE_INSENSITIVE | MULTILINE | DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            // 避免 eval(...) 形式表达式
            scriptPattern = compile("eval\\((.*?)\\)", CASE_INSENSITIVE | MULTILINE | DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            // 避免 e­xpression(...) 表达式
            scriptPattern = compile("expression\\((.*?)\\)", CASE_INSENSITIVE | MULTILINE | DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            // 避免 javascript: 表达式
            scriptPattern = compile("javascript:", CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            // 避免 vbscript: 表达式
            scriptPattern = compile("vbscript:", CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            // 避免 onload= 表达式
            scriptPattern = compile("onload(.*?)=", CASE_INSENSITIVE | MULTILINE | DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            // 避免 onXX= 表达式
            scriptPattern = compile("on.*(.*?)=", CASE_INSENSITIVE | MULTILINE | DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

        }
        return value;
    }
}