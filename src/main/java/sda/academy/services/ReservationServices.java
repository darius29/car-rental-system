package sda.academy.services;

import sda.academy.entities.Car;
import sda.academy.entities.Customer;
import sda.academy.entities.MaintenanceRecord;
import sda.academy.entities.Reservation;
import sda.academy.repositories.CarRepository;
import sda.academy.repositories.CustomerRepository;
import sda.academy.repositories.MaintenanceRecordRepository;
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

    public static Car suggestCar(CarRepository carRepository, ReservationRepository reservationRepository, MaintenanceRecordRepository maintenanceRecordRepository, Car car, LocalDate startDate, LocalDate endDate, Scanner scanner) {
        int lowerBound = (int) (car.getPricePerDay() * 0.67); // 20 din 30
        int upperBound = (int) (car.getPricePerDay() * 1.33); // 40 din 30

        System.out.println("Cautam masini alternative in intervalul de pret " + lowerBound + " - " + upperBound + "...");

        // Găsim mașini disponibile cu preț în intervalul specificat
        List<Car> alternativeCars = carRepository.findAvailableCarsInPriceRange(lowerBound, upperBound, startDate, endDate, 3);

        if (alternativeCars.isEmpty()) {
            System.out.println("Nu am gasit masini alternative in intervalul de pret specificat. Cautam masini disponibile, ordonate dupa pret...");

            // Găsim 3 mașini disponibile în perioada selectată, ordonate după preț
            alternativeCars = carRepository.findAvailableCarsOrderedByPrice(startDate, endDate, 3);

            if (alternativeCars.isEmpty()) {
                System.out.println("Nu am gasit masini disponibile.");
                return null;
            }
        }

        // Afisam masinile disponibile
        System.out.println("Masini alternative disponibile:");
        for (int i = 0; i < alternativeCars.size(); i++) {
            Car alternativeCar = alternativeCars.get(i);
            System.out.println((i + 1) + ". ID: " + alternativeCar.getId() + ", Model: " + alternativeCar.getModel() + "(" + alternativeCar.getLicensePlate() + ")" + ", Pret/zi: " + alternativeCar.getPricePerDay());
        }

        System.out.println("Alege o masina alternativa introducand numarul corespunzator: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > alternativeCars.size()) {
            System.out.println("Selectie invalida.");
            return null;
        }

        // Returnăm mașina aleasa
        return alternativeCars.get(choice - 1);
    }

    public static void addNewReservation(Scanner scanner, ReservationRepository reservationRepository, CustomerRepository customerRepository, CarRepository carRepository, MaintenanceRecordRepository maintenanceRecordRepository) {
        System.out.println("Introdu numele clientului:");
        String lastName = scanner.nextLine();

        Customer customer = customerRepository.findSingleByLastName(lastName);

        if (customer == null) {
            System.out.println("Clientul cu numele specificat nu exista.");
            return;
        }

        Car car;
        LocalDate startDate;
        LocalDate endDate;

        while (true) {
            System.out.println("Introdu numarul de inmatriculare al masinii:");
            String licensePlate = scanner.nextLine();
            car = carRepository.findByLicensePlate(licensePlate);

            if (car == null) {
                System.out.println("Masina cu numarul de inmatriculare specificat nu exista.");
                continue;
            }

            // Alegerea perioadei rezervarii
            System.out.println("Introdu data de inceput a rezervarii (YYYY-MM-DD):");
            startDate = LocalDate.parse(scanner.nextLine());

            System.out.println("Introdu data de sfarsit a rezervarii (YYYY-MM-DD):");
            endDate = LocalDate.parse(scanner.nextLine());

            // Verificam daca masina este in mentenanta in intervalul specificat
            List<MaintenanceRecord> maintenanceRecords = maintenanceRecordRepository.findMaintenanceRecordsForCarInPeriod(car.getId(), startDate, endDate);
            if (!maintenanceRecords.isEmpty()) {
                System.out.println("Masina este in mentenanta in intervalul specificat.");

                Car alternativeCar = suggestCar(carRepository, reservationRepository, maintenanceRecordRepository, car, startDate, endDate, scanner);

                if (alternativeCar != null) {
                    // Utilizatorul alege o mașina alternativa, continuam cu rezervarea
                    car = alternativeCar;
                } else {
                    System.out.println("Nu s-a ales nicio masina alternativa.");
                    return;
                }
            }

            // Verificam daca masina este deja rezervata in intervalul specificat
            List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(car.getId(), startDate, endDate);
            if (!overlappingReservations.isEmpty()) {
                System.out.println("Masina este deja rezervata in intervalul specificat.");

                Car alternativeCar = suggestCar(carRepository, reservationRepository, maintenanceRecordRepository, car, startDate, endDate, scanner);

                if (alternativeCar != null) {
                    // Utilizatorul alege o masina alternativa, continuam cu rezervarea
                    car = alternativeCar;
                } else {
                    System.out.println("Nu s-a ales nicio masina alternativa.");
                    return;
                }
            }

            // Daca masina este disponibila sau s-a ales o masina alternativa, adaugam rezervarea
            Reservation reservation = new Reservation();
            reservation.setReservationDate(LocalDate.now());
            reservation.setStartDate(startDate);
            reservation.setEndDate(endDate);
            reservation.setCustomer(customer);
            reservation.setCar(car);
            reservation.setPlate(car.getLicensePlate());

            reservationRepository.save(reservation);
            System.out.println("Rezervarea a fost adaugata cu succes!");
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

        System.out.println("ID: " + reservation.getId());
        System.out.println("Nume clientului: " + reservation.getCustomer().getLastName());
        System.out.println("Prenume: " + reservation.getCustomer().getFirstName());
        System.out.println("Numarul de inmatriculare al masinii: " + reservation.getCar().getLicensePlate());
        System.out.println("Data de start: " + reservation.getStartDate());
        System.out.println("Data de sfarsit a rezervarii: " + reservation.getEndDate());
        System.out.println("---------------------------");
    }

    public static void displayAllReservation(ReservationRepository reservationRepository) {
        List<Reservation> reservations = reservationRepository.findAllReservation();

        if (reservations.isEmpty()) {
            System.out.println("Nu există clienti în baza de date.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println("Reservation ID: " + reservation.getId());
                System.out.println("Car: " + reservation.getCar().getModel() + " (" + reservation.getCar().getLicensePlate() + ")");
                System.out.println("Reservation Date: " + reservation.getReservationDate());
                System.out.println("Start Date: " + reservation.getStartDate());
                System.out.println("End Date: " + reservation.getEndDate());
                System.out.println("-----------------------------------------");
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
                System.out.println("Introdu noua data de sfarsit a rezervarii (YYYY-MM-DD): ");
                LocalDate newStartDate = LocalDate.parse(scanner.nextLine());
                if (newStartDate.isAfter(reservation.getEndDate())) {
                    System.out.println("Data de sfarsit nu poate fi ulterioara datei de sfarsit. ");
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

