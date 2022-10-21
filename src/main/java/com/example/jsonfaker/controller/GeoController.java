package com.example.jsonfaker.controller;

import com.example.jsonfaker.model.Geo;
import com.example.jsonfaker.service.GeoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/geo")
public class GeoController {
    private GeoService geoService;

    public GeoController(GeoService geoService) {
        this.geoService = geoService;
    }

    @PostMapping("/add")
    ResponseEntity addGeo(@RequestBody Geo geo){
        if (geoService.saveGeo(geo).equals(0L)){
            return new ResponseEntity<String>(HttpStatus.FOUND);
        }
        return ResponseEntity.ok(geo.getId());
    }

    @GetMapping("/all")
    public List<Geo> getAllGeos(){
        return geoService.getAllGeos();
    }

}
