package edu.uob;

import java.util.HashMap;
import java.util.Map;

public class LocationEntity extends GameEntity{
    Map<String,Integer> artefacts=new HashMap<>();
    Map<String,Integer> furniture=new HashMap<>();
    Map<String,Integer> characters=new HashMap<>();
    Map<String,LocationEntity> toLocations=new HashMap<>();


    public LocationEntity(String name, String description) {
        super(name, description);
    }
}
