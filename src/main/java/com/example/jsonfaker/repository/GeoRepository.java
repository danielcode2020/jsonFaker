package com.example.jsonfaker.repository;

import com.example.jsonfaker.model.Geo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoRepository extends CrudRepository<Geo, Long> {
    Boolean existsByLatAndLng(Double lat, Double lng);

}
