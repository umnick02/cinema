package com.cinema;

import javafx.application.Application;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("cinema");
        Application.launch(CinemaApplication.class);
    }
}
