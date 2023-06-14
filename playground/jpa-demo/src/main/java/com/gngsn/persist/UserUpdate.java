package com.gngsn.persist;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

/**
 * Spring 없이 JPA 만으로 DB 접근 시도
 */
public class UserUpdate {

    public static void main(String[] args) {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpa-demo");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction transaction = entityManager.getTransaction();
        
        try {
            transaction.begin();
            // Logic Start

            User user = entityManager.find(User.class, "gngsn@gmail.com");

            if (user == null) {
                System.out.println("User 없음");
            } else {
                System.out.printf("User 존재: email:%s, name=%s, createDate=%s\n", user.getEmail(), user.getName(), user.getCreateDate());
                user.setName("Je m'appelle");   // update user set create_date=?, name=? where email=?
            }

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
