package com.cinema.ui;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class ApplicationUI extends Application {

    protected ConfigurableApplicationContext context;

    @Override
    public void init() {
        context = SpringApplication.run(getClass());
        context.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        context.close();
    }
}
