package com.example.jsonfaker.model.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;


public class UserExportDTO {
    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "name")
    private String name;
    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "username")
    private String username;
    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "email")
    private String email;

    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "street")
    private String street;
    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "suite")
    private String suite;
    @CsvBindByPosition(position = 5)
    @CsvBindByName(column = "city")
    private String city;
    @CsvBindByPosition(position = 6)
    @CsvBindByName(column = "zipcode")
    private String zipcode;
    @CsvBindByPosition(position = 7)
    @CsvBindByName(column = "lat")
    private String lat;
    @CsvBindByPosition(position = 8)
    @CsvBindByName(column = "lng")
    private String lng;
    @CsvBindByPosition(position = 9)
    @CsvBindByName(column = "phone")
    private String phone;
    @CsvBindByPosition(position = 10)
    @CsvBindByName(column = "website")
    private String website;

    public UserExportDTO(String name, String username, String email, String street, String suite, String city, String zipcode, String lat, String lng, String phone, String website) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.street = street;
        this.suite = suite;
        this.city = city;
        this.zipcode = zipcode;
        this.lat = lat;
        this.lng = lng;
        this.phone = phone;
        this.website = website;
    }

    public UserExportDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
