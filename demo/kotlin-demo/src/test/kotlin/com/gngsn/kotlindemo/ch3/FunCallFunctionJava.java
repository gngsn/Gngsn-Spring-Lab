package com.gngsn.kotlindemo.ch3;

import org.junit.jupiter.api.Test;

import java.util.List;

public class FunCallFunctionJava {

    @Test
    public void ex_fun_default() {
        List<Integer> list = List.of(1, 2, 3);
        FunCallFunction kotlinFun = new FunCallFunction();


        System.out.println(kotlinFun.joinToString_usingDefault(list));
        System.out.println(kotlinFun.joinToString_usingDefault(list, ", "));
        System.out.println(kotlinFun.joinToString_usingDefault(list, ", ", ";"));
        System.out.println(kotlinFun.joinToString_usingDefault(list, ", ", "; ", "#"));


//        FunCallFunctionKt kotlinFunKt = new FunCallFunctionKt();
        FunCallFunctionKt.getOpCount();
        FunCallFunctionKt.getUNIX_LINE_SEPARATOR();
        String constVal =
            FunCallFunctionKt.CONST_UNIX_LINE_SEPARATOR;
    }
}