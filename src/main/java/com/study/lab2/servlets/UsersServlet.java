package com.study.lab2.servlets;

import com.study.lab2.accounts.AccountService;
import com.study.lab2.accounts.UserProfile;
import com.study.lab2.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UsersServlet extends AbstractServlet {
    @SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
    private final AccountService accountService;

    public UsersServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    //get public user profile

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Processing get request");
        String sessionId = request.getSession().getId();
        UserProfile profile = accountService.getUserBySessionId(sessionId);

        Map<String, UserProfile> profiles = new HashMap<>();

        if (profile == null) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("U'r unauthorized");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            if (profile.getLogin().equals("admin")) {
                profiles = accountService.getAllUserProfiles();
            } else {
                profiles.put(profile.getLogin(), profile);
            }
            String user = request.getParameter("user");
            response.setContentType("text/html;charset=utf-8");
            if (user == null || user.isEmpty()) {
                response.getWriter().println(PageGenerator
                                .instance()
                                .getPage("profile.html",
                                        createSessionData(sessionId, profiles)
                                )
                );
            } else {
                String action = request.getParameter("action");
                if (action.equals("change")) {
                    response.getWriter().println(PageGenerator
                                    .instance()
                                    .getPage("change.html",
                                            createSessionData(sessionId, accountService.getUserByLogin(user))
                                    )
                    );
                } else if (action.equals("delete")) {
                    response.getWriter().println(PageGenerator
                                    .instance()
                                    .getPage("delete.html",
                                            createSessionData(sessionId, accountService.getUserByLogin(user))
                                    )
                    );

                }
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    //sign up
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> data = getSessionData(request);
        UserProfile profile = (UserProfile) data.get("profile");

        if (!isProfileValid(profile)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            accountService.addNewUser(profile);
            response.setStatus(HttpServletResponse.SC_OK);
        }

        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator
                        .instance()
                        .getPage("profile.html", data)
        );
    }

    //change profile
    public void doPut(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> data = getSessionData(request);
        UserProfile profile = (UserProfile) data.get("profile");

        if (!isProfileValid(profile)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            accountService.deleteUser(profile.getLogin());
            accountService.addNewUser(profile);
            response.setStatus(HttpServletResponse.SC_OK);
        }

        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator
                        .instance()
                        .getPage("profile.html", createSessionData(request.getSession().getId(), profile)
                        )
        );
    }

    //unregister
    public void doDelete(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> data = getSessionData(request);
        UserProfile profile = (UserProfile) data.get("profile");

        if (profile.getLogin() == null || profile.getLogin().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            accountService.deleteUser(profile.getLogin());
            response.setStatus(HttpServletResponse.SC_OK);
        }
        String sessionId = request.getSession().getId();
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator
                        .instance()
                        .getPage("profile.html", createSessionData(sessionId, accountService.getUserByLogin("admin"))
                        )
        );
    }

}
