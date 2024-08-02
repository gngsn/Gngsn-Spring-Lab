package com.gngsn.transactionTest.controller;

public record ResponseView (
        String code,
        String message,
        String type
) {}