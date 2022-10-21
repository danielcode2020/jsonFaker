package com.example.jsonfaker.service;

import com.example.jsonfaker.model.Geo;
import org.springframework.stereotype.Service;

import java.util.List;


public interface GeoService {
    Long saveGeo(Geo geo );

    List<Geo> getAllGeos();

    Boolean checkGeo(Geo geo);
}
