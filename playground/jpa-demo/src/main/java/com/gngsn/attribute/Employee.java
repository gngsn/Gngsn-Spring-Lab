package com.gngsn.attribute;

import com.gngsn.secondaryTable.attributeOverride.Address;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Employee {
    @Id
    private String id;

    @Embedded
    private Address homeAddress;

    @AttributeOverrides({
            @AttributeOverride(name = "address1", column = @Column(name = "waddr1")),
            @AttributeOverride(name = "address2", column = @Column(name = "waddr2")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "wzipcode"))
    })
    @Embedded
    private Address workAddress;

    protected Employee() {
    }

    public Employee(String id, Address homeAddress, Address workAddress) {
        this.id = id;
        this.homeAddress = homeAddress;
        this.workAddress = workAddress;
    }

    public String getId() {
        return id;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public Address getWorkAddress() {
        return workAddress;
    }
}