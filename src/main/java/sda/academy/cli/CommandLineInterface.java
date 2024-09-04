package sda.academy.cli;

import sda.academy.entities.Car;
import sda.academy.entities.Customer;
import sda.academy.entities.MaintenanceRecord;
import sda.academy.repositories.CarRepository;
import sda.academy.repositories.CustomerRepository;
import sda.academy.repositories.MaintenanceRecordRepository;
import sda.academy.repositories.ReservationRepository;

import java.text.ParseException;
import java.util.Scanner;

import static sda.academy.services.CarServices.*;
import static sda.academy.services.CustomerServices.*;
import static sda.academy.services.MaintenanceRecordServices.*;
import static sda.academy.services.MaintenanceRecordServices.displayAllMaintenaceRecord;
import static sda.academy.services.ReservationServices.*;
import static sda.academy.services.ReservationServices.deleteReservation;

public class CommandLineInterface {
    public static void showMainMenu() {
        System.out.println("1. Adauga o masina noua.");
        System.out.println("2. Editeaza o masina.");
        System.out.println("3. Afiseaza toate masinile.");
        System.out.println("4. Afiseaza o singura masina.");
        System.out.println("5. Sterge o masina.");
        System.out.println("6. Adauga un client nou.");
        System.out.println("7. Editeaza un client.");
        System.out.println("8. Afiseaza toti clienti.");
        System.out.println("9. Afiseaza un client.");
        System.out.println("10. Sterge un client.");
        System.out.println("11. Adauga o rezervare.");
        System.out.println("12. Editeaza o rezervare.");
        System.out.println("13. Afiseaza toate rezervarile.");
        System.out.println("14. Afiseaza o rezervare.");
        System.out.println("15. Sterge o rezervare.");
        System.out.println("16. Afiseaza rezervarile active ale unui client.");
        System.out.println("17. Afiseaza rezervarile istorice ale unui client.");
        System.out.println("18. Adauga o noua rezervare (Ilie).");
        System.out.println("19. Afiseaza rezervarile.");
        System.out.println("20. Modifica rezervarile.");
        System.out.println("21. Extinde o rezervare.");
        System.out.println("22. Sterge o rezervare.");
        System.out.println("23. Adauga o inregistrare de mentenanta pentru masina.");
        System.out.println("24. Istoricul de întreținere al fiecărei mașini.");
    }

    public static void executeCommand(int choice, Scanner scanner) {
        Car car = new Car();
        Customer customer = new Customer();
        CarRepository carRepository = new CarRepository();
        CustomerRepository customerRepository = new CustomerRepository();
        MaintenanceRecordRepository maintenanceRecordRepository = new MaintenanceRecordRepository();
        MaintenanceRecord maintenanceRecord = new MaintenanceRecord();
        ReservationRepository reservationRepository = new ReservationRepository();

        switch (choice) {

            case 1:
                addCar(scanner, car, carRepository);
                break;

            case 2:
                editCar(scanner, carRepository);
                break;

            case 3:
                displayAllCars(carRepository);
                break;

            case 4:
                displayOneCar(scanner, carRepository);
                break;
            case 5:
                deleteCar(scanner, carRepository);
                break;
            case 6:
                addNewCustomer(scanner, customer, customerRepository);
                break;

            case 7:
                editCustomerV1(scanner, customerRepository);
                break;

            case 8:
                displayAllCustomers(customerRepository);

                break;

            case 9:
                displayOneCustomer(scanner, customerRepository);
                break;

            case 10:
                deleteCustomer(scanner, customerRepository);
                break;

            case 11:
                addNewReservation(scanner, reservationRepository, customerRepository, carRepository);
                break;

            case 12:
                updateReservation(scanner, reservationRepository, carRepository);
                break;

            case 13:
                displayAllReservation(reservationRepository);
                break;

            case 14:
                displayOneReservation(scanner, reservationRepository);
                break;
            case 15:

                deleteReservation(scanner, reservationRepository);
                break;

            case 16:
                displayActiveReservationsForCustomer(scanner, customerRepository, reservationRepository);
                break;

            case 17:
                displayHistoricalReservationsForCustomer(scanner, customerRepository, reservationRepository);
                break;

            case 18:
                try {
                    makeReservation(scanner, customerRepository, carRepository, reservationRepository);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                break;

            case 19:
                showCustomerReservations(scanner);
                break;

            case 20:
                try {
                    modifyReservationDates(scanner, reservationRepository);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                break;


            case 21:
                try {
                    extendReservation(scanner);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                break;

            case 22:
                try {
                    deleteReservation(scanner);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
                break;

            case 23:
                addMaintenanceRecordForCar(scanner,  carRepository, maintenanceRecordRepository);
                break;

            case 24:
                displayAllMaintenaceRecord(maintenanceRecordRepository);
                break;
        }
    }
}
