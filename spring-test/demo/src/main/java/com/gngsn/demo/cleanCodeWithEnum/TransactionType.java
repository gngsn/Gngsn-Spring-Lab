package com.gngsn.demo.cleanCodeWithEnum;

public enum TransactionType {

    BUY {
        @Override
        public void doTransaction(Account account, long cash) {
            long tax = Math.round(cash * 0.15 / 100);
            cash = cash + tax;
            account.withdraw(cash);
        }
    },
    SELL {
        @Override
        public void doTransaction(Account account, long cash) {
            long tax = Math.round(cash * 0.1 / 100);
            cash = cash + tax;
            account.deposit(cash);
        }
    },
    DEPOSIT {
        @Override
        public void doTransaction(Account account, long cash) {
            long tax = Math.round(cash * 0.05 / 100);
            cash = cash + tax;
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
    };

    public abstract void doTransaction(Account account, long cash);


}
