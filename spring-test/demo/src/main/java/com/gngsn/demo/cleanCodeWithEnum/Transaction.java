package com.gngsn.demo.cleanCodeWithEnum;

import java.math.BigDecimal;


/**
 * equalsIgnoreCase
 * : 대소문자를 비교하지 않는 경우에 자주 사용.
 *   내부 코드 -> '==' 로 일치 비교 + 길이 비교 + regionMatches 호출 시 ignoreCase를 True로 지정(대소문자 무시)
 *
 * BigDecimal
 * : Float과 Double은 숫자를 저장할 때 이진수의 근사치로 지정하기 때문에 사칙연산 시,
 *   소수점의 정밀도가 완벽하지 않아 정확한 값을 출력하지 않을 수도 있음.
 *   때문에 속도는 느리지만 숫자의 오차를 미연에 방지할 수 있음.
 */
public class Transaction {

    public void doTransaction(Account account, long cash, String operation) {
        if ("BUY".equalsIgnoreCase(operation)) {
            long tax = Math.round(cash * 0.15 / 100);
            cash = cash + tax;
            account.withdraw(cash);
        } else if ("SELL".equalsIgnoreCase(operation)) {
            long tax = Math.round(cash * 0.1 / 100);
            cash = cash + tax;
            account.deposit(cash);
        } else if ("DEPOSIT".equalsIgnoreCase(operation)) {
            long tax = Math.round(cash * 0.05 / 100);
            cash = cash + tax;
            account.deposit(cash);
        } else if ("WITHDRAWAL".equalsIgnoreCase(operation)) {
            long tax = Math.round(cash * 0.20 / 100);
            cash = cash + tax;
            account.withdraw(cash);
        }
    }
}

