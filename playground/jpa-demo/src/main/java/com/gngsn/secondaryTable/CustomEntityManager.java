package com.gngsn.secondaryTable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class CustomEntityManager {
    public static final String PERSISTENCE_UNIT_NAME = "jpa-demo";
    private static EntityManagerFactory emf;

    public static void init() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    public static EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        emf.close();
    }
}
