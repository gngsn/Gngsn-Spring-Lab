package com.gngsn.webClient.reactor;

import java.util.List;
import java.util.Map;

public class FruitInfo {
    private final List<String> distinctFruits;
    private final Map<String, Long> countFruits;

    public FruitInfo(List<String> distinctFruits, Map<String, Long> countFruits) {
        this.distinctFruits = distinctFruits;
        this.countFruits = countFruits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FruitInfo fruitInfo = (FruitInfo) o;

        if (distinctFruits != null ? !distinctFruits.equals(fruitInfo.distinctFruits) : fruitInfo.distinctFruits != null)
            return false;
        return countFruits != null ? countFruits.equals(fruitInfo.countFruits) : fruitInfo.countFruits == null;
    }

    @Override
    public int hashCode() {
        int result = distinctFruits != null ? distinctFruits.hashCode() : 0;
        result = 31 * result + (countFruits != null ? countFruits.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FruitInfo{" +
            "distinctFruits=" + distinctFruits +
            ", countFruits=" + countFruits +
            '}';
    }
}
