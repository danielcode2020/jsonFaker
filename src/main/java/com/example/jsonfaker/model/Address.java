package com.example.jsonfaker.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
public class Address {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String street;
    private String city;

    private String suite;
    private String zipcode;
    @OneToOne(cascade = CascadeType.ALL) // ca sa adauge in tabele "copil" la salvare
    @JoinColumn(name = "geo_id", referencedColumnName = "id")
    private Geo geo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", suite='" + suite + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", geo=" + geo +
                '}';
    }
}
