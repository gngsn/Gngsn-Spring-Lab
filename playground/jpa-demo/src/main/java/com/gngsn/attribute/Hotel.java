package com.gngsn.attribute;


import com.gngsn.secondaryTable.attributeOverride.Address;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "hotel_info")
public class Hotel {
    @Id
    @Column(name = "hotel_id")
    private String id;

    @Column(name = "nm")
    private String name;

    private int year;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Embedded
    private Address address;

    private LocalDateTime created;

    @Column(name = "modified")
    private LocalDateTime lastModified;

    protected Hotel() {
    }

    public Hotel(String id, String name, int year, Grade grade, Address address) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.grade = grade;
        this.address = address;
        this.created = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public Grade getGrade() {
        return grade;
    }

    public Address getAddress() {
        return address;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", year=" + year +
                ", grade=" + grade +
                ", created=" + created +
                ", lastModified=" + lastModified +
                '}';
    }

}