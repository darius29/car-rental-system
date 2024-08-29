package sda.academy.repositories;

import sda.academy.entities.Reservation;

public class ReservationRepository extends BaseRepository<Reservation, Integer>{
    public ReservationRepository() {
        super(Reservation.class);
    }
}
