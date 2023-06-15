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
            String newName = "gngsn" + System.currentTimeMillis() % 100;

            if (user == null) {
                System.out.println("User 없음");
            } else {

                System.out.printf("User 존재: email:%s, name=%s, createDate=%s\n", user.getEmail(), user.getName(), user.getCreateDate());
                user.setName(newName);

            }

            System.out.println("Commit 전 User의 데이터는? " + user); // 변경 후 데이터
            assert newName.equals(user.getName());

            // Logic End
            transaction.commit(); // update 문 실행
        } catch (Exception ex) {
            ex.printStackTrace();
            transaction.rollback();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }
}
