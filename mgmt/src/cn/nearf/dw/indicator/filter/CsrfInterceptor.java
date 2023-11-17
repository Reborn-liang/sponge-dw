package cn.nearf.dw.indicator.filter;

/**
 * @Author Daniel
 * @Date: 2023/11/15 15:16
 * @Description
 **/

import org.jeecgframework.web.system.listener.LockHelper;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;


public class CsrfInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String contentType = request.getContentType();

        if ("POST".equalsIgnoreCase(request.getMethod()) && isFormUrlEncoded(contentType)) {
            String requestCsrfToken = CsrfTokenManager.getTokenFromRequest(request);
            String sessionCsrfToken = (String) request.getSession().getAttribute(CsrfTokenManager.CSRF_TOKEN_FOR_SESSION_ATTR_NAME);
            if(sessionCsrfToken == null && LockHelper.isFlag())
                return true;

            return Objects.equals(requestCsrfToken, sessionCsrfToken);
        }

        return true;
    }

    private boolean isFormUrlEncoded(String contentType) {
        return contentType != null && contentType.startsWith("application/x-www-form-urlencoded");
    }
}
