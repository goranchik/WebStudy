package com.study.lab2.servlets;

import com.study.lab2.accounts.UserProfile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ihor on 2/17/2016.
 */
public class AbstractServlet extends HttpServlet {

    Map<String, Object> getSessionData(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();
        data.put("sessionId", request.getSession().getId());
        data.put("profile", new UserProfile(
                request.getParameter("login"),
                request.getParameter("pass"),
                request.getParameter("email")
        ));
        return data;
    }

    Map<String, Object> createSessionData(String sessionId, UserProfile profile){
        Map<String, Object> data = new HashMap<>();
        data.put("sessionId", sessionId);
        data.put("profile", profile);
        return data;
    }

    Map<String, Object> createSessionData(String sessionId, Map<String, UserProfile> profiles){
        Map<String, Object> data = new HashMap<>();
        data.put("sessionId", sessionId);
        data.put("profiles", profiles);
        return data;
    }

    boolean isProfileValid(UserProfile profile) {
        return !(profile.getLogin() == null || profile.getLogin().isEmpty()
                || profile.getPass() == null || profile.getPass().isEmpty());
    }

}
