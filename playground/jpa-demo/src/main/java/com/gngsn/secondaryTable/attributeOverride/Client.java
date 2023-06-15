package com.gngsn.secondaryTable.attributeOverride;

import com.gngsn.secondaryTable.CustomEntityManager;
import com.gngsn.secondaryTable.tableName.Intro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class Client {
    public static void main(String[] args) {
        CustomEntityManager.init();
        EntityManager em = CustomEntityManager.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Writer writer = new Writer("name",
                    new Address("address1", "address2", "12345"),
                    new Intro("text/plain", "Introduction"));
            em.persist(writer);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }

        // SAVE: Null 일 경우
//        TransactionTemplate.run(em -> {
//            Writer writer = new Writer("name",
//                    new Address("address1", "address2", "12345"),
//                    null);
//            em.persist(writer);
//        });

        CustomEntityManager.close();
    }


}
