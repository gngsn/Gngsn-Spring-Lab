package com.gngsn.demo.cleanCodeWithEnum;

public class AppEnum {

    public static void main(String[] args) {
        Account account = new Account(10_000);
        System.out.println("Initial Balance : " + account.getBalance());

//        executeWithEnum(account);
        executeWithIfElse(account);
    }

    public static void executeWithEnum(Account account) {

        TransactionType.DEPOSIT.doTransaction(account, 1_000);
        System.out.println("Balance after DEPOSIT : " + account.getBalance());

        TransactionType.WITHDRAWAL.doTransaction(account, 1_500);
        System.out.println("Balance after WITHDRAWAL : " + account.getBalance());
    }

    public static void executeWithIfElse(Account account) {

        Transaction transaction = new Transaction();

        transaction.doTransaction(account, 1_000, "DEPOSIT");
        System.out.println("Balance after DEPOSIT : " + account.getBalance());

        transaction.doTransaction(account, 1_500, "WITHDRAWAL");
        System.out.println("Balance after WITHDRAWAL : " + account.getBalance());
    }


    /*
    Initial Balance : 10000
    Balance after DEPOSIT : 11000
    Balance after WITHDRAWAL : 9497
     */

}
