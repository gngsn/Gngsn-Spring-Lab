package com.gngsn.demo.cleanCodeWithEnum;

public class AppEnum {
    public static void main(String[] args) {
        Account account = new Account(10_000);

        System.out.println("Initial Balance : " + account.getBalance());

        TransactionType.BUY.doTransaction(account, 1_000);
        System.out.println("Balance after BUY : " + account.getBalance());

        TransactionType.SELL.doTransaction(account, 1_500);
        System.out.println("Balance after SELL : " + account.getBalance());

    }
}
