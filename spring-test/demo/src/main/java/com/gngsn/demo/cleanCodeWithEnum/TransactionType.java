package com.gngsn.demo.cleanCodeWithEnum;

public enum TransactionType {

    DEPOSIT {
        @Override
        public void doTransaction(Account account, long cash) {
            account.deposit(cash);
        }
    },
    WITHDRAWAL {
        @Override
        public void doTransaction(Account account, long cash) {
            long tax = Math.round(cash * 0.20 / 100);
            cash = cash + tax;
            account.withdraw(cash);
        }
    },
    TRANSFER {
        @Override
        public void doTransaction(Account account, long cash) {
            long tax = Math.round(cash * 0.10 / 100);
            cash = cash + tax;
            account.deposit(cash);
        }
    };

    public abstract void doTransaction(Account account, long cash);
}
