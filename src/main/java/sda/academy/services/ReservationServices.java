package sda.academy.services;

import sda.academy.entities.Car;
import sda.academy.entities.Customer;
import sda.academy.entities.Reservation;
import sda.academy.repositories.CarRepository;
import sda.academy.repositories.CustomerRepository;
import sda.academy.repositories.ReservationRepository;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ReservationServices {
    public static void makeReservation(Scanner scanner, CustomerRepository customerRepository, CarRepository carRepository, ReservationRepository reservationRepository) throws ParseException, ParseException {
        Reservation reservation = new Reservation();

        int id;
        Customer customer;
        System.out.println("Introdu ID-ul clientului");
        id = scanner.nextInt();
        customer = customerRepository.findById(id);
        reservation.setCustomer(customer);

        String licensePlate;
        Car car;
        System.out.println("Introdu numarul de inmatriculare al masinii: ");
        licensePlate = scanner.nextLine();
        car = carRepository.findByLicensePlate(licensePlate);
        reservation.setCar(car);
        reservation.setPlate(licensePlate);

        System.out.println("Introdu prima zi: (yyyy-MM-dd)");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());
        reservation.setStartDate(startDate);

        System.out.println("Introdu ultima zi: (yyyy-MM-dd)");
        LocalDate endDate =  LocalDate.parse(scanner.nextLine());
        reservation.setEndDate(endDate);

        reservationRepository.save(reservation);
    }

    public static void addNewReservation(Scanner scanner, ReservationRepository reservationRepository, CustomerRepository customerRepository, CarRepository carRepository) {
        System.out.println("Introdu numele clientului:");
        String lastName = scanner.nextLine();

        Customer customer = customerRepository.findSingleByLastName(lastName);

        if (customer == null) {
            System.out.println("Clientul cu numele specificat nu există.");
            return;
        }

        LocalDate startDate;
        LocalDate endDate;
        Car car;

        while (true) {
            System.out.println("Introdu numarul de inmatriculare al masinii:");
            String licensePlate = scanner.nextLine();
            car = carRepository.findByLicensePlate(licensePlate);

            if (car == null) {
                System.out.println("Masina cu numarul de inmatriculare specificat nu exista.");
                continue;
            }

            System.out.println("Introdu data de inceput a rezervarii (YYYY-MM-DD):");
            startDate = LocalDate.parse(scanner.nextLine());

            System.out.println("Introdu data de sfarsit a rezervarii (YYYY-MM-DD):");
            endDate = LocalDate.parse(scanner.nextLine());
            // Verifică dacă mașina este deja rezervata în intervalul specificat
            List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(car.getId(), startDate, endDate);
            if (!overlappingReservations.isEmpty()) {
                System.out.println("Masina este deja rezervata in intervalul specificat.");
                System.out.println("Doresti să alegi o altă mașina? (da/nu):");
                String response = scanner.nextLine();

                if (response.equalsIgnoreCase("nu")) {
                    return;
                } else {
                    continue;
                }
            }

            Reservation reservation = new Reservation();
            reservation.setReservationDate(LocalDate.now());
            reservation.setStartDate(startDate);
            reservation.setEndDate(endDate);
            reservation.setCustomer(customer);
            reservation.setCar(car);

            reservationRepository.save(reservation);
            System.out.println("Rezervarea a fost adăugată cu succes!");
            break;
        }
    }

    public static void displayOneReservation(Scanner scanner, ReservationRepository reservationRepository) {
        System.out.println("Introdu id-ul rezervarii");
        int id = scanner.nextInt();
        scanner.nextLine();
        Reservation reservation = reservationRepository.findById(id);

        if (reservation == null) {
            System.out.println("Rezervarea cu ID-ul specificat nu exista.");
            return;
        }

        System.out.println("ID: " + reservation.getId() + ", Nume clientului: " + reservation.getCustomer().getLastName() +
                ", Prenume: " + reservation.getCustomer().getFirstName() + ", Numarul de inmatriculare al masinii:  " + reservation.getCar().getLicensePlate() +
                ", Data de start: " + reservation.getStartDate() + ", Data de sfarsit a rezervarii: " + reservation.getEndDate()
        );
    }

    public static void displayAllReservation(ReservationRepository reservationRepository) {
        List<Reservation> reservations = reservationRepository.findAllReservation();

        if (reservations.isEmpty()) {
            System.out.println("Nu există clienti în baza de date.");
        } else {
            for (Reservation c : reservations) {
                System.out.println("ID: " + c.getId() + ", Nume clientului: " + c.getCustomer().getLastName() +
                        ", Prenume: " + c.getCustomer().getFirstName() + ", Numarul de inmatriculare al masinii:  " + c.getCar().getLicensePlate() +
                        ", Data de start: " + c.getStartDate() + ", Data de sfarsit a rezervarii: " + c.getEndDate()
                );
            }
        }
    }

    public static void showCustomerReservations(Scanner scanner) {
        int reservationId;
        System.out.println("Introdu ID-ul rezervarii");
        reservationId = scanner.nextInt();

        List<Reservation> reservations = ReservationRepository.getReservationsByCustomer(reservationId);

        if (reservations != null && !reservations.isEmpty()) {
            System.out.println("Reservations for customer ID " + reservationId + ":");
            for (Reservation reservation : reservations) {
                System.out.println("Reservation ID: " + reservation.getId());
                System.out.println("Car: " + reservation.getCar().getModel() + " (" + reservation.getCar().getLicensePlate() + ")");
                System.out.println("Reservation Date: " + reservation.getReservationDate());
                System.out.println("Start Date: " + reservation.getStartDate());
                System.out.println("End Date: " + reservation.getEndDate());
                System.out.println("-----------------------------------------");
            }
        } else {
            System.out.println("No reservations found for customer ID " + reservationId + ".");
        }
    }

    public static void updateReservation(Scanner scanner, ReservationRepository reservationRepository, CarRepository carRepository) {
        System.out.println("Introdu id-ul rezervarii: ");
        int reservationId = scanner.nextInt();
        scanner.nextLine();

        Reservation reservation = reservationRepository.findById(reservationId);

        if (reservation == null) {
            System.out.println("Rezervarea cu ID-ul specificat nu exista.");
            return;
        }

        System.out.println("Ce doresti sa modifici?");
        System.out.println("1. Prelungeste durata rezervarii (schimba data de sfarsit)");
        System.out.println("2. Prelungeste durata rezervarii (schimba data de inceput)");
        System.out.println("3. Schimba masina");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.println("Introdu noua data de inceput a rezervarii (YYYY-MM-DD): ");
                LocalDate newStartDate = LocalDate.parse(scanner.nextLine());
                if (newStartDate.isAfter(reservation.getEndDate())) {
                    System.out.println("Data de inceput nu poate fi ulterioara datei de sfarsit. ");
                    return;
                }
                reservation.setStartDate(newStartDate);
                reservationRepository.update(reservation);
                System.out.println("Durata rezervarii a fost prelungita");
                break;

            case 2:
                System.out.println("Introdu noua data de sfarsit a rezervarii (YYYY-MM-DD): ");
                LocalDate newEndDate = LocalDate.parse(scanner.nextLine());
                if (newEndDate.isBefore(reservation.getStartDate())) {
                    System.out.println("Data de sfarsit nu poate fi anterioara datei de inceput");
                    return;
                }

                reservation.setEndDate(newEndDate);
                reservationRepository.update(reservation);
                System.out.println("Durata rezervarii a fost prelungita");
                break;

            case 3:
                System.out.println("Introdu noul numar de inmatriculare al masinii:");
                String licensePlate = scanner.nextLine();

                Car newCar = carRepository.findByLicensePlate(licensePlate);

                if (newCar == null) {
                    System.out.println("Masina cu numarul de inmatriculare specificat nu exista.");
                    return;
                }

                reservation.setCar(newCar);
                reservationRepository.update(reservation);
                System.out.println("Masina a fost schimbata");
                break;


        }
    }

    public static void extendReservation(Scanner scanner) throws ParseException {
        int reservationId;
        System.out.println("Introdu ID-ul rezervarii");
        reservationId = scanner.nextInt();
        Reservation reservation = ReservationRepository.getReservation(reservationId);
        if (reservation != null) {
            System.out.println("Introdu ultima zi: (yyyy-MM-dd)");
            LocalDate endDate = LocalDate.parse(scanner.nextLine());
            reservation.setEndDate(endDate);

            ReservationRepository.updateReservation(reservation);
        } else {
            System.out.println("Reservation not found.");
        }
    }

    public static void modifyReservationDates(Scanner scanner, ReservationRepository reservationRepository) throws ParseException {
        int reservationId;
        System.out.println("Introdu ID-ul rezervarii");
        reservationId = scanner.nextInt();
        Reservation reservation = ReservationRepository.getReservation(reservationId);

        if (reservation != null) {
            System.out.println("Introdu prima zi: (yyyy-MM-dd)");
            LocalDate startDate =  LocalDate.parse(scanner.nextLine());
            reservation.setStartDate(startDate);

            System.out.println("Introdu ultima zi: (yyyy-MM-dd)");
            LocalDate endDate =  LocalDate.parse(scanner.nextLine());
            reservation.setEndDate(endDate);

            ReservationRepository.updateReservation(reservation);
        } else {
            System.out.println("Reservation not found.");
        }
    }

    public static void deleteReservation(Scanner scanner, ReservationRepository reservationRepository) {
        System.out.println("Introdu id-ul rezervarii: ");
        int reservationId = scanner.nextInt();
        scanner.nextLine();

        Reservation reservation = reservationRepository.findById(reservationId);

        if (reservation == null) {
            System.out.println("Rezervarea cu ID-ul specificat nu exista.");
            return;
        }

        reservationRepository.delete(reservation);

    }

    public static void deleteReservation(Scanner scanner) {
        int reservationId;
        System.out.println("Introdu ID-ul rezervarii");
        reservationId = scanner.nextInt();
        ReservationRepository.deleteReservation(reservationId);
    }


}
