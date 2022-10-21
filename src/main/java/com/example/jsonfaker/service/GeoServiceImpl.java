package com.example.jsonfaker.service;

import com.example.jsonfaker.model.Geo;
import com.example.jsonfaker.repository.GeoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;

@Service
public class GeoServiceImpl implements GeoService {

    private GeoRepository geoRepository;

    public GeoServiceImpl(GeoRepository geoRepository) {
        this.geoRepository = geoRepository;
    }

    @Override
    public Long saveGeo(Geo geo) {
        if (checkGeo(geo).equals(FALSE)) {
            return geoRepository.save(geo).getId();
        }
        return 0L;
    }

    @Override
    public List<Geo> getAllGeos(){
        Iterable<Geo> geosIt =  geoRepository.findAll();
        List<Geo> result = new ArrayList<>();
        geosIt.forEach(result::add);
        return result;
    }

    @Override
    public Boolean checkGeo(Geo geo){
        return geoRepository.existsByLatAndLng(geo.getLat(), geo.getLng());
    }
}
