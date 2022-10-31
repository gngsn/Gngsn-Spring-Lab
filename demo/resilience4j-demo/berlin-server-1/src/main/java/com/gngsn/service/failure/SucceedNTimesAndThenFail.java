package com.gngsn.service.failure;

import com.gngsn.exception.CircuitBreakerException;

public class SucceedNTimesAndThenFail implements PotentialFailure {

    int n;
    int successCount;

    public SucceedNTimesAndThenFail(int n) {
        this.n = n;
    }

    @Override
    public void occur() {
        if (successCount < n) {
            successCount++;
            return;
        }

        throw new CircuitBreakerException("Error occurred during service executes");
    }
}
