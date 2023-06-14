package com.gngsn.persist;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;

/**
 * Spring 없이 JPA 만으로 DB 접근 시도
 */
public class UserSave {

    public static void main(String[] args) {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpa-demo");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction transaction = entityManager.getTransaction();
        
        try {
            transaction.begin();
            // Logic Start

            User user = new User("gngsn@gmail.com", "gngsn", LocalDateTime.now());
            entityManager.persist(user);

            // Logic End
            transaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            transaction.rollback();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }
}
