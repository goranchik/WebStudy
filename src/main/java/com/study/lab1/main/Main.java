package com.study.lab1.main;

import com.study.lab1.servlets.AllRequestsServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {
    public static void main(String[] args) throws Exception {
        AllRequestsServlet allRequestsServlet = new AllRequestsServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(allRequestsServlet), "/*");

        Server server = new Server(8085);
        server.setHandler(context);

        server.start();
    }
}
