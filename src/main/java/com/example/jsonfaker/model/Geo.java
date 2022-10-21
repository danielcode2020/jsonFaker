package com.example.jsonfaker.model;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;

@Entity
public class Geo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    private Double lat;
    private Double lng;

    public Geo(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }


    public Geo() {

    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Long getId() {
        return id;
    }
}
