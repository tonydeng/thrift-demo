package com.github.tonydeng.demo.thrift.ping.server;

import org.apache.thrift.server.TServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;


/**
 * Created by tonydeng on 15/9/24.
 */
public class WebApplicationInitalizer implements WebApplicationInitializer {
    private static final Logger log = LoggerFactory.getLogger(WebApplicationInitalizer.class);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();

        rootContext.register(SpringConfiguration.class);
        rootContext.refresh();
        servletContext.addListener(new ContextLoaderListener(rootContext));

        log.info("Spring setup done.");

        TServlet thriftServlet = rootContext.getBean("thriftServlet", TServlet.class);

        ServletRegistration.Dynamic servletRegistration = servletContext.addServlet("apiServlet", thriftServlet);

        servletRegistration.setLoadOnStartup(2);

        servletRegistration.addMapping("/api");

        log.info("Registered Thrift servlet");
    }
}
