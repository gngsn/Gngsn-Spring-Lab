package com.gngsn.apressbatch.domain;

import lombok.*;
import org.springframework.util.StringUtils;

@Data
public class CustomerUpdate {
    protected final long customerId;

    @Getter @Setter
    public static class Name extends CustomerUpdate {
        private final String firstName;
        private final String middleName;
        private final String lastName;

        // StringUtils.hasText: null, empty, blank check
        public Name(long customerId, String firstName, String middleName, String lastName) {
            super(customerId);
            this.firstName = StringUtils.hasText(firstName) ? firstName : null;
            this.middleName = StringUtils.hasText(middleName) ? middleName : null;
            this.lastName = StringUtils.hasText(lastName) ? lastName : null;
        }
    }

    @Getter @Setter
    public static class Address extends CustomerUpdate {
        private final String address1;
        private final String address2;
        private final String city;
        private final String state;
        private final String postalCode;

        public Address(long customerId, String address1,
                       String address2, String city, String state, String postalCode) {
            super(customerId);
            this.address1 = StringUtils.hasText(address1) ? address1 : null;
            this.address2 = StringUtils.hasText(address2) ? address2 : null;
            this.city = StringUtils.hasText(city) ? city : null;
            this.state = StringUtils.hasText(state) ? state : null;
            this.postalCode = StringUtils.hasText(postalCode) ? postalCode : null;
        }
    }

    @Getter @Setter
    public static class Contact extends CustomerUpdate {
        private final String emailAddress;
        private final String homePhone;
        private final String cellPhone;
        private final String workPhone;
        private final Integer notificationPreference;

        public Contact(long customerId, String emailAddress, String homePhone,
                       String cellPhone, String workPhone, Integer notificationPreference) {
            super(customerId);
            this.emailAddress = StringUtils.hasText(emailAddress) ? emailAddress : null;
            this.homePhone = StringUtils.hasText(homePhone) ? homePhone : null;
            this.cellPhone = StringUtils.hasText(cellPhone) ? cellPhone : null;
            this.workPhone = StringUtils.hasText(workPhone) ? workPhone : null;
            this.notificationPreference = notificationPreference;
        }
    }
}
