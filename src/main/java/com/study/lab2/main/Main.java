package com.study.lab2.main;

import com.study.lab2.accounts.AccountService;
import com.study.lab2.accounts.UserProfile;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import com.study.lab2.servlets.SessionsServlet;
import com.study.lab2.servlets.UsersServlet;

public class Main {
    public static void main(String[] args) throws Exception {
        AccountService accountService = new AccountService();

        accountService.addNewUser(new UserProfile("admin"));
        accountService.addNewUser(new UserProfile("test"));

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new UsersServlet(accountService)), "/users");
        context.addServlet(new ServletHolder(new SessionsServlet(accountService)), "/sessions");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("templates/lab2/public");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, context});

        Server server = new Server(8085);
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}
