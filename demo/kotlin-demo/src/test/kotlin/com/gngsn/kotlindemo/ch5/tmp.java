package com.gngsn.kotlindemo.ch5;

import com.gngsn.kotlindemo.ch5.LambdaExpression.Person;
import kotlin.collections.CollectionsKt;
import kotlin.sequences.Sequence;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

public class tmp {
    @Test
    public final void sequence() {
        Person[] var2 = new Person[]{new Person("Alice", 29), new Person("Bob", 31)};
        List people = CollectionsKt.listOf(var2);
        Sequence sequence = CollectionsKt.asSequence((Iterable)people);
        Iterator iterator = sequence.iterator();

        Object o;
        while(true) {
            if (iterator.hasNext()) {
                Object nextPerson = iterator.next();
                Person it = (Person)nextPerson;
                if (it.getName().length() <= 5) {
                    continue;
                }

                o = nextPerson;
                break;
            }

            o = null;
            break;
        }

        Person var3 = (Person)o;
        String sequenceName = var3 != null ? var3.getName() : null;
        System.out.println("sequenceName : " + sequenceName);
    }
}
