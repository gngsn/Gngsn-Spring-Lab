package com.gngsn.secondaryTable.attributeOverride;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    @Column(name = "addr1")
    private String address1;

    @Column(name = "addr2")
    private String address2;

    @Column(name = "zipcode")
    private String zipcode;

    public Address() {
    }

    public Address(final String address1, final String address2, final String zipcode) {
        this.address1 = address1;
        this.address2 = address2;
        this.zipcode = zipcode;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getZipcode() {
        return zipcode;
    }
}
