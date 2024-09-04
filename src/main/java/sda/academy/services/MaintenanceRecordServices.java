package sda.academy.services;

import sda.academy.entities.Car;
import sda.academy.entities.Customer;
import sda.academy.entities.MaintenanceRecord;
import sda.academy.entities.Reservation;
import sda.academy.repositories.CarRepository;
import sda.academy.repositories.CustomerRepository;
import sda.academy.repositories.MaintenanceRecordRepository;
import sda.academy.repositories.ReservationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MaintenanceRecordServices {
    public static void addMaintenanceRecordForCar(Scanner scanner, CarRepository carRepository, MaintenanceRecordRepository maintenanceRecordRepository) {
        Car car = new Car();
        MaintenanceRecord maintenanceRecord = new MaintenanceRecord();

        System.out.println("Introdu numarul de inmatriculare al masinii: ");
        String licensePlate = scanner.nextLine();
//        car.setLicensePlate(licensePlate);
        car = carRepository.findByLicensePlate(licensePlate);
        if ( car == null ) {
            System.out.println("Masina cu numarul de inmatriculare specificat nu exista!");
            return;
        }

        maintenanceRecord.setCar(car);
        System.out.println("Adaugati o inregistrare de intretinere: ");
        String details  = scanner.nextLine();
        maintenanceRecord.setDetails(details);

        System.out.print("Introduceti data intretinerii : ");
        LocalDate maintenanceDate = LocalDate.parse(scanner.nextLine());
        maintenanceRecord.setMaintenanceDate(maintenanceDate);

        maintenanceRecordRepository.save(maintenanceRecord);
    }

    public static void displayHistoricalReservationsForCustomer(Scanner scanner, CustomerRepository customerRepository, ReservationRepository reservationRepository) {
        System.out.println("Introdu numele clientului: ");
        String lastName = scanner.nextLine();
        Customer customer = customerRepository.findSingleByLastName(lastName);

        if (customer == null) {
            System.out.println("Clientul cu numele specificat nu exista.");
            return;
        }

        List<Reservation> reservations = reservationRepository.findHistoricalReservationsByCustomer(customer.getId());

        if (reservations.isEmpty()) {
            System.out.println("Clientul nu are rezervari istorice.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println("ID: " + reservation.getId() + ", Numarul de inmatriculare al masinii:  " + reservation.getCar().getLicensePlate() +
                        ", Data de start: " + reservation.getStartDate() + ", Data de sfarsit a rezervarii: " + reservation.getEndDate());
            }
        }
    }

    public static void displayActiveReservationsForCustomer(Scanner scanner, CustomerRepository customerRepository, ReservationRepository reservationRepository) {
        System.out.println("Introdu numele clientului: ");
        String lastName = scanner.nextLine();
        Customer customer = customerRepository.findSingleByLastName(lastName);

        if (customer == null) {
            System.out.println("Clientul cu numele specificat nu exista.");
            return;
        }

        List<Reservation> reservations = reservationRepository.findActiveReservationsByCustomer(customer.getId());

        if (reservations.isEmpty()) {
            System.out.println("Clientul nu are rezervari active.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println("ID: " + reservation.getId() + ", Nume: " + reservation.getCustomer().getLastName() + ", Numarul de inmatriculare al masinii:  " + reservation.getCar().getLicensePlate() +
                        ", Data de start: " + reservation.getStartDate() + ", Data de sfarsit a rezervarii: " + reservation.getEndDate());
            }
        }
    }

    public static void displayAllMaintenaceRecord(MaintenanceRecordRepository maintenanceRecordRepository) {
        System.out.println("Istoricul de întreținere al fiecărei mașini:");
        List<MaintenanceRecord> maintenanceRecords = MaintenanceRecordRepository.findAll();
        if (maintenanceRecords.isEmpty()) {
            System.out.println("Nu există note de mentenanță în baza de date.");
        } else {
            for (MaintenanceRecord m : maintenanceRecords) {
                System.out.println("ID: " + m.getId() + ", Data: " + m.getMaintenanceDate() +
                        ", Descriere: " + m.getDetails());
            }
        }
    }


}
