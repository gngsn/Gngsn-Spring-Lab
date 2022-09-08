package com.gngsn.apressbatch.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Customer {
    private final long id;
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final String address1;
    private final String address2;
    private final String city;
    private final String state;
    private final String postalCode;
    private final String ssn;
    private final String emailAddress;
    private final String homePhone;
    private final String cellPhone;
    private final String workPhone;
    private final int notificationPreferences;
}
