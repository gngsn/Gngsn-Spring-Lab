package com.gngsn.secondaryTable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.function.Consumer;
import java.util.function.Function;

public class TransactionTemplate {
    static public void run(Consumer<EntityManager> consumer) {
        EntityManager em = CustomEntityManager.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            consumer.accept(em);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    static public <T> Object run(Function<EntityManager, T> supplier) {
        EntityManager em = CustomEntityManager.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            final Object o = supplier.apply(em);

            tx.commit();
            return (T) o;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
