package sda.academy.repositories;

import sda.academy.entities.Car;

public class CarRepository extends BaseRepository<Car, Integer> {

    public CarRepository() {
        super(Car.class);
    }
}
