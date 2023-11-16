package cn.nearf.dw.indicator.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public final class CsrfTokenManager {

    private static final AtomicReference<String> token = new AtomicReference<>(null);

    // Hidden field parameter name
    public static final String CSRF_PARAM_NAME = "CSRFToken";

    // Session attribute name for CSRF token
    public static final String CSRF_TOKEN_FOR_SESSION_ATTR_NAME =
            CsrfTokenManager.class.getName() + ".tokenval";

    private CsrfTokenManager() {
        // Private constructor to prevent instantiation
    }

    // Create CSRF token in the session
    public static String createTokenForSession(HttpSession session) {
        String currentToken = token.get();

        if (Objects.isNull(currentToken)) {
            currentToken = UUID.randomUUID().toString();
            session.setAttribute(CSRF_TOKEN_FOR_SESSION_ATTR_NAME, currentToken);
            token.set(currentToken);
        }

        return currentToken;
    }

    // Get CSRF token from the request
    public static String getTokenFromRequest(HttpServletRequest request) {
        return request.getParameter(CSRF_PARAM_NAME);
    }

    public static String getToken() {
        return token.get();
    }

    public static String getUrl(String url){
        if(url != null){
            String token = CsrfTokenManager.getToken();
            String modifiedUrl = url.replaceFirst("\\.do\\?(\\w+)", ".do?$1&" + CsrfTokenManager.CSRF_PARAM_NAME + "=" + token);
            return modifiedUrl;
        } else return null;

    }
    // Other utility methods or additional logic can be added as needed

}
