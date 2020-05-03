package com.cinema.core.dao;

import com.cinema.core.config.EntityManagerProvider;
import com.cinema.core.entity.Episode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

public enum EpisodeDAO {
    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(EpisodeDAO.class);

    private final EntityManager entityManager = EntityManagerProvider.provideEntityManager();

    public void update(Episode episode) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(episode);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            logger.error(e.getMessage(), e);
        }
    }
}
