package sda.academy.services;

import sda.academy.dao.ReservationDAO;
import sda.academy.entities.Car;
import sda.academy.entities.Customer;
import sda.academy.entities.Reservation;
import java.util.Date;

public class ReservationService {
    private ReservationDAO reservationDAO;

    public ReservationService() {
        this.reservationDAO = new ReservationDAO();
    }

    public void makeReservation(Car car, Customer customer, Date startDate, Date endDate) {
        Reservation reservation = new Reservation();
        reservation.setCar(car);
        reservation.setCustomer(customer);
        reservation.setReservationDate(new Date());
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);

        reservationDAO.saveReservation(reservation);
    }

    public void modifyReservationDates(Long reservationId, Date newStartDate, Date newEndDate) {
        Reservation reservation = reservationDAO.getReservation(reservationId);
        if (reservation != null) {
            reservation.setStartDate(newStartDate);
            reservation.setEndDate(newEndDate);
            reservationDAO.updateReservation(reservation);
        } else {
            System.out.println("Reservation not found.");
        }
    }

    public void changeReservationCar(Long reservationId, Car newCar) {
        Reservation reservation = reservationDAO.getReservation(reservationId);
        if (reservation != null) {
            reservation.setCar(newCar);
            reservationDAO.updateReservation(reservation);
        } else {
            System.out.println("Reservation not found.");
        }
    }

    public void extendReservation(Long reservationId, Date newEndDate) {
        Reservation reservation = reservationDAO.getReservation(reservationId);
        if (reservation != null) {
            reservation.setEndDate(newEndDate);
            reservationDAO.updateReservation(reservation);
        } else {
            System.out.println("Reservation not found.");
        }
    }
}
