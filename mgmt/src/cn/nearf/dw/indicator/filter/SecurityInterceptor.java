package cn.nearf.dw.indicator.filter;

import org.jeecgframework.core.util.PropertiesUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class SecurityInterceptor implements HandlerInterceptor {
    private static final PropertiesUtil dbPro = new PropertiesUtil("dbconfig.properties");


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }

    private String removeHtmlComments(String html) {
        // 使用正则表达式匹配并删除HTML中的注释
        return html.replaceAll("<!--(.*?)-->", "");
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) {

    }


    private Set<String> loadAllowedOriginsFromProperties() {
        Properties properties = dbPro.getProperties();
        Set<String> origins = new HashSet<>();
        String allowedOriginsStr = properties.getProperty("allowed.origin");
        if (allowedOriginsStr == null || allowedOriginsStr.equals("")) {
            origins.add("*");
        } else {
            origins.addAll(Arrays.asList(allowedOriginsStr.split(",")));

        }

        return origins;
    }

    private boolean isRefererValid(String referer) {
        String regex = "https?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?";
        return referer.matches(regex);
    }

    private boolean isRefererEmpty(String referer) {
        return referer == null || referer.isEmpty();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        Set<String> allowedOrigins = loadAllowedOriginsFromProperties();
        String origin = request.getHeader("Origin");
        String referer = request.getHeader("Referer");
        if (origin !=null){
            URI uri = new URI(origin);
            if (!allowedOrigins.contains(uri.getHost())) {
                return false;
            }
        }

        if (isRefererEmpty(referer)||isRefererValid(referer)) {
            return false;
        }

        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "SAMEORIGIN");
        response.setHeader("X-XSS-Protection", "1; mode=block");
        return true;
    }


}