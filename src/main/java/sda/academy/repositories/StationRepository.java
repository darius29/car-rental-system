package sda.academy.repositories;

import sda.academy.entities.Station;

public class StationRepository extends BaseRepository<Station, Integer>{
    public StationRepository() {
        super(Station.class);
    }



}