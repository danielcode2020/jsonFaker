package com.example.jsonfaker.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement(name = "geo")
@XmlAccessorType(XmlAccessType.FIELD)
public class Geo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @XmlElement(name = "id")
    private Long id;

    @NotNull
    @XmlElement(name = "lat")
    private Double lat;

    @NotNull
    @XmlElement(name = "lng")
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
