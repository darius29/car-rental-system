package sda.academy;

import sda.academy.entities.Car;
import sda.academy.entities.Customer;
import sda.academy.entities.Reservation;
import sda.academy.repositories.CarRepository;
import sda.academy.repositories.CustomerRepository;
import sda.academy.repositories.ReservationRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MainApplication {

    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            showMainMenu();
            int choice = Integer.parseInt(scanner.nextLine());
            executeCommand(choice, scanner);
        }

    }

    private static void showMainMenu() {
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
    }

    private static void executeCommand(int choice, Scanner scanner) {
        Car car = new Car();
        Customer customer = new Customer();
        CarRepository carRepository = new CarRepository();
        CustomerRepository customerRepository = new CustomerRepository();
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
                editCustomer(scanner, customerRepository);
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
                try {
                    makeReservation(scanner, customerRepository, carRepository, reservationRepository);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                break;

            case 12:
                showCustomerReservations(scanner);
                break;

            case 13:
                try {
                    modifyReservationDates(scanner, reservationRepository);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                break;

            case 14:
//                changeReservationCar(scanner);
                break;

            case 15:
                try {
                    extendReservation(scanner);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                break;

            case 16:

                break;

            case 17:

                break;
            case 18:

                break;

            case 19:

                break;

            case 20:
                try {
                    deleteReservation(scanner);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
                break;

            case 21:

                break;

            case 22:

                break;

            case 23:

                break;

            case 24:

                break;

            case 25:

                break;

            case 26:

                break;

            case 27:

                break;

            case 28:

                break;

            case 29:

                break;

            case 30:

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
            String rawEndDate = scanner.nextLine();
            Date newEndDate = formatter.parse(rawEndDate);
            reservation.setEndDate(newEndDate);
            ReservationRepository.updateReservation(reservation);
        } else {
            System.out.println("Reservation not found.");
        }
    }

//    public static void changeReservationCar(Scanner scanner) {
//        int reservationId;
//        System.out.println("Introdu ID-ul rezervarii");
//        reservationId = scanner.nextInt();
//        Reservation reservation = ReservationRepository.getReservation(reservationId);
//
//        if (reservation != null) {
//            System.out.println("Introdu noua masina");
//            String newCar = scanner.nextLine();
//            Reservation.setCar(newCar);
//            ReservationRepository.updateReservation(reservation);
//        } else {
//            System.out.println("Reservation not found.");
//        }
//    }

    private static void deleteReservation(Scanner scanner) {
        int reservationId;
        System.out.println("Introdu ID-ul rezervarii");
        reservationId = scanner.nextInt();
        ReservationRepository.deleteReservation(reservationId);
    }

    private static void modifyReservationDates(Scanner scanner, ReservationRepository reservationRepository) throws ParseException {
        int reservationId;
        System.out.println("Introdu ID-ul rezervarii");
        reservationId = scanner.nextInt();
        Reservation reservation = ReservationRepository.getReservation(reservationId);

        if (reservation != null) {
            System.out.println("Introdu prima zi: (yyyy-MM-dd)");
            String rawStartDate = scanner.nextLine();
            Date startDate = formatter.parse(rawStartDate);
            reservation.setStartDate(startDate);

            System.out.println("Introdu ultima zi: (yyyy-MM-dd)");
            String rawEndDate = scanner.nextLine();
            Date endDate = formatter.parse(rawEndDate);
            reservation.setEndDate(endDate);

            ReservationRepository.updateReservation(reservation);
        } else {
            System.out.println("Reservation not found.");
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

    private static void makeReservation(Scanner scanner, CustomerRepository customerRepository, CarRepository carRepository, ReservationRepository reservationRepository) throws ParseException, ParseException {
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
        String rawStartDate = scanner.nextLine();
        Date startDate = formatter.parse(rawStartDate);
        reservation.setStartDate(startDate);

        System.out.println("Introdu ultima zi: (yyyy-MM-dd)");
        String rawEndDate = scanner.nextLine();
        Date endDate = formatter.parse(rawEndDate);
        reservation.setEndDate(endDate);

        reservationRepository.save(reservation);
    }

    private static void deleteCustomer(Scanner scanner, CustomerRepository customerRepository) {
        String lastName;
       Customer customer;
        System.out.println("Introdu numele clientului: ");
        lastName = scanner.nextLine();
        customer = customerRepository.findByLastName(lastName);

        if (customer != null) {
            customerRepository.delete(customer);
        } else {
            System.out.println("Clientul cu numele specificat nu există.");
        }
    }

    private static void editCustomer(Scanner scanner, CustomerRepository customerRepository){
        String lastName;
        Customer customer;
        System.out.println("Introdu numele clientului: ");
        lastName = scanner.nextLine();
        customer = customerRepository.findByLastName(lastName);

        if (customer != null) {
            System.out.println("Introdu numele clientului: ");
            lastName = scanner.nextLine();
            customer.setLastName(lastName);

            System.out.println("Introdu prenumele clientului: ");
            String firstName = scanner.nextLine();
            customer.setFirstName(firstName);

            System.out.println("Introdu numarul permisului de conducere al clientului: ");
            String driverLicenseNumber = scanner.nextLine();
            customer.setDriverLicenseNumber(driverLicenseNumber);

            customerRepository.update(customer);
        } else {
            System.out.println("Clientul cu numele specificat nu există.");
        }

    }

    private static void displayOneCustomer(Scanner scanner, CustomerRepository customerRepository) {
        String lastName;
        Customer customer;
        System.out.println("Introdu numele clientului: ");
        lastName = scanner.nextLine();
        customer = customerRepository.findByLastName(lastName);
        if (customer != null) {
            System.out.println("ID: " + customer.getId() + ", Nume: " + customer.getLastName() +
                    ", Prenume: " + customer.getFirstName() + ", Numar permis: " + customer.getDriverLicenseNumber());
        } else {
            System.out.println("Clientul cu id-ul specificat nu există.");
        }
    }

    private static void displayAllCustomers(CustomerRepository customerRepository) {
        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty()) {
            System.out.println("Nu există clienti în baza de date.");
        } else {
            for (Customer c : customers) {
                System.out.println("ID: " + c.getId() + ", Nume: " + c.getLastName() +
                        ", Prenume: " + c.getFirstName() + ", Numar permis: " + c.getDriverLicenseNumber());
            }
        }
    }

    private static void addNewCustomer(Scanner scanner, Customer customer, CustomerRepository customerRepository) {
        System.out.println("Introduce numele clientului: ");
        String lastName = scanner.nextLine();
        customer.setLastName(lastName);

        System.out.println("Introduce prenumele clientului: ");
        String firstName = scanner.nextLine();
        customer.setFirstName(firstName);

        System.out.println("Introdu numarul permisului: ");
        String driverLicenseNumber = scanner.nextLine();

        customer.setDriverLicenseNumber(driverLicenseNumber);


        customerRepository.save(customer);
    }

    private static void deleteCar(Scanner scanner, CarRepository carRepository) {
        String licensePlate;
        Car car;
        System.out.println("Introdu numarul de inmatriculare al masinii: ");
        licensePlate = scanner.nextLine();
        car = carRepository.findByLicensePlate(licensePlate);

        if (car != null) {
            carRepository.delete(car);
        } else {
            System.out.println("Mașina cu numarul de inmatriculare specificat nu există.");
        }
    }

    private static void displayOneCar(Scanner scanner, CarRepository carRepository) {
        String licensePlate;
        Car car;
        System.out.println("Introdu numarul de inmatriculare al masinii: ");
        licensePlate = scanner.nextLine();
        car = carRepository.findByLicensePlate(licensePlate);
        if (car != null) {
            System.out.println("ID: " + car.getId() + ", Numar: " + car.getLicensePlate() +
                    ", Model: " + car.getModel() + ", Pret pe zi: " + car.getPricePerDay());
        } else {
            System.out.println("Mașina cu id-ul specificat nu există.");
        }
    }

    private static void displayAllCars(CarRepository carRepository) {
        List<Car> cars = carRepository.findAll();
        if (cars.isEmpty()) {
            System.out.println("Nu există mașini în baza de date.");
        } else {
            for (Car c : cars) {
                System.out.println("ID: " + c.getId() + ", Numar: " + c.getLicensePlate() +
                        ", Model: " + c.getModel() + ", Pret pe zi: " + c.getPricePerDay());
            }
        }
    }

    private static void editCar(Scanner scanner, CarRepository carRepository) {
        String licensePlate;
        Car car;
        String model;
        int pricePerDay;
        System.out.println("Introdu numarul de inmatriculare al masinii: ");
        licensePlate = scanner.nextLine();
        car = carRepository.findByLicensePlate(licensePlate);
        if (car != null) {
            System.out.println("Introdu noul numarul al masinii: ");
            licensePlate = scanner.nextLine();
            car.setLicensePlate(licensePlate);

            System.out.println("Introdu noul modelul al masinii: ");
            model = scanner.nextLine();
            car.setModel(model);

            System.out.println("Introduce pretul pe zi: ");
            pricePerDay = scanner.nextInt();
            scanner.nextLine();
            car.setPricePerDay(pricePerDay);

            carRepository.update(car);
        } else {
            System.out.println("Mașina cu id-ul specificat nu există.");
        }
    }

    private static void addCar(Scanner scanner, Car car, CarRepository carRepository) {
        System.out.println("Introdu numarul masinii: ");
        String licensePlate = scanner.nextLine();
        car.setLicensePlate(licensePlate);

        System.out.println("Introdu modelul masinii: ");
        String model = scanner.nextLine();
        car.setModel(model);

        System.out.println("Introduce pretul pe zi: ");
        int pricePerDay = scanner.nextInt();
        scanner.nextLine();
        car.setPricePerDay(pricePerDay);

        carRepository.save(car);
    }
}