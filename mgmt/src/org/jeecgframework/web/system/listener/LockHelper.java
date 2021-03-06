package org.jeecgframework.web.system.listener;

import org.jeecgframework.web.system.pojo.base.TSUser;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Daniel
 * @Description
 **/
public class LockHelper {
    public static Map<String, HttpSession> map=new HashMap<String, HttpSession>();
    public static void putSession(HttpSession session){
        TSUser user=(TSUser)session.getAttribute("userSession");
        map.put(user.getId(), session);
    }
    public static void moveSession(HttpSession session){
        TSUser user=(TSUser)session.getAttribute("userSession");
        map.remove(user.getId());
    }
    public static void destroyedSession(TSUser user){
        HttpSession session=map.get(user.getId());
        session.invalidate();
    }
}
