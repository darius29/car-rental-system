package sda.academy;

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

public class MainApplication {
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
    }

    private static void executeCommand(int choice, Scanner scanner) {
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
                addNewReservation(scanner, reservationRepository, customerRepository, carRepository, maintenanceRecordRepository);
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



        }
    }

    private static void displayActiveReservationsForCustomer(Scanner scanner, CustomerRepository customerRepository, ReservationRepository reservationRepository) {
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

    private static void displayHistoricalReservationsForCustomer(Scanner scanner, CustomerRepository customerRepository, ReservationRepository reservationRepository) {
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

    private static void deleteReservation(Scanner scanner, ReservationRepository reservationRepository) {
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

    private static void updateReservation(Scanner scanner, ReservationRepository reservationRepository, CarRepository carRepository) {
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

    private static void displayOneReservation(Scanner scanner, ReservationRepository reservationRepository) {
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

    private static void displayAllReservation(ReservationRepository reservationRepository) {
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

    private static void addNewReservation(Scanner scanner, ReservationRepository reservationRepository, CustomerRepository customerRepository, CarRepository carRepository, MaintenanceRecordRepository maintenanceRecordRepository) {
        System.out.println("Introdu numele clientului:");
        String lastName = scanner.nextLine();

        Customer customer = customerRepository.findSingleByLastName(lastName);

        if (customer == null) {
            System.out.println("Clientul cu numele specificat nu exista.");
            return;
        }

        Car car = null;
        LocalDate startDate;
        LocalDate endDate;

        while (true) {
            if (car == null) {
                System.out.println("Introdu numarul de inmatriculare al masinii:");
                String licensePlate = scanner.nextLine();
                car = carRepository.findByLicensePlate(licensePlate);

                if (car == null) {
                    System.out.println("Masina cu numarul de inmatriculare specificat nu exista.");
                    continue;
                }
            }


            System.out.println("Introdu data de inceput a rezervarii (YYYY-MM-DD):");
            startDate = LocalDate.parse(scanner.nextLine());

            System.out.println("Introdu data de sfarsit a rezervarii (YYYY-MM-DD):");
            endDate = LocalDate.parse(scanner.nextLine());

            // Verificam daca masina este in mentenanta in intervalul specificat
            List<MaintenanceRecord> maintenanceRecords = maintenanceRecordRepository.findMaintenanceRecordsForCarInPeriod(car.getId(), startDate, endDate);
            if (!maintenanceRecords.isEmpty()) {
                System.out.println("Masina este in mentenanta in intervalul specificat.");
                System.out.println("Doresti sa alegi o alta masina sau sa schimbi datele? (1 - Alta masina, 2 - Alte date):");
                int response = scanner.nextInt();
                scanner.nextLine();

                if (response == 1) {
                    car = null;
                    continue;
                } else if (response == 2) {

                    continue;
                }
            }

            // Verificam daca masina este deja rezervata in intervalul specificat
            List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(car.getId(), startDate, endDate);
            if (!overlappingReservations.isEmpty()) {
                System.out.println("Masina este deja rezervata in intervalul specificat.");
                System.out.println("Doresti sa alegi o alta masina sau sa schimbi datele? (1 - Alta masina, 2 - Alte date):");
                int response = scanner.nextInt();
                scanner.nextLine();

                if (response == 1) {
                    car = null; // Resetam masina pentru a permite alegerea alteia
                    continue;
                } else if (response == 2) {
                    // Pastram masina curenta si repetam procesul doar pentru date
                    continue;
                }
            }

            // Daca nu exista suprapuneri, salvam rezervarea
            Reservation reservation = new Reservation();
            reservation.setReservationDate(LocalDate.now());
            reservation.setStartDate(startDate);
            reservation.setEndDate(endDate);
            reservation.setCustomer(customer);
            reservation.setCar(car);

            reservationRepository.save(reservation);
            System.out.println("Rezervarea a fost adaugata cu succes!");
            break; // Iesim din bucla
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
        LocalDate startDate = LocalDate.parse(scanner.nextLine());
        reservation.setStartDate(startDate);

        System.out.println("Introdu ultima zi: (yyyy-MM-dd)");
        LocalDate endDate =  LocalDate.parse(scanner.nextLine());
        reservation.setEndDate(endDate);

        reservationRepository.save(reservation);
    }

    private static void addMaintenanceRecordForCar(Scanner scanner, CarRepository carRepository, MaintenanceRecordRepository maintenanceRecordRepository) {


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

    private static void deleteCustomer(Scanner scanner, CustomerRepository customerRepository) {
        String lastName;
       Customer customer;
        System.out.println("Introdu numele clientului: ");
        lastName = scanner.nextLine();
        customer = customerRepository.findSingleByLastName(lastName);

        if (customer != null) {
            customerRepository.delete(customer);
        } else {
            System.out.println("Clientul cu numele specificat nu există.");
        }
    }

    private static void editCustomerV1(Scanner scanner, CustomerRepository customerRepository){
        String lastName;
        Customer customer;
        System.out.println("Introdu numele clientului: ");
        lastName = scanner.nextLine();
        customer = customerRepository.findSingleByLastName(lastName);

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


    private static void deleteCustomerV1(Scanner scanner, CustomerRepository customerRepository) {
        System.out.println("Introdu numele clientului: ");
        String lastName = scanner.nextLine();
        List<Customer> customers = customerRepository.findListByLastName(lastName);

        if (customers.isEmpty()) {
            System.out.println("Nu există clienți cu acest nume.");
            return;
        }

        Customer selectedCustomer = null;

        if (customers.size() == 1) {
            selectedCustomer = customers.get(0);
        } else {
            System.out.println("Clienții găsiți: ");
            for (int i = 0; i < customers.size(); i++) {
                Customer customer = customers.get(i);
                System.out.println((i + 1) + ". ID: " + customer.getId() + ", Nume: " + customer.getLastName() +
                        ", Prenume: " + customer.getFirstName() +
                        ", Numar permis de conducere: " + customer.getDriverLicenseNumber());
            }

            System.out.println("Introdu numărul corespunzător clientului pe care dorești să-l ștergi: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice < 1 || choice > customers.size()) {
                System.out.println("Selecție invalidă.");
                return;
            }

            selectedCustomer = customers.get(choice - 1);
        }

        if (selectedCustomer != null) {
            customerRepository.delete(selectedCustomer);
            System.out.println("Clientul a fost șters cu succes.");
        } else {
            System.out.println("Clientul cu numele specificat nu există.");
        }
    }

    private static void editCustomerV2(Scanner scanner, CustomerRepository customerRepository) {
        System.out.println("Introdu numele clientului: ");
        String lastName = scanner.nextLine();
        List<Customer> customers = customerRepository.findListByLastName(lastName);

        if (customers.isEmpty()) {
            System.out.println("Nu există clienți cu acest nume.");
            return;
        }

        Customer selectedCustomer = null;

        if (customers.size() == 1) {
            selectedCustomer = customers.get(0);
        } else {

            System.out.println("Clienții găsiți: ");
            for (int i = 0; i < customers.size(); i++) {
                Customer customer = customers.get(i);
                System.out.println((i + 1) + "Nume: " + customer.getLastName() +
                        ", Prenume: " + customer.getFirstName() +
                        ", Numar permis de conducere: " + customer.getDriverLicenseNumber());
            }

            System.out.println("Introdu numărul corespunzător clientului pe care dorești să-l editezi: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice < 1 || choice > customers.size()) {
                System.out.println("Selecție invalidă.");
                return;
            }

            selectedCustomer = customers.get(choice - 1);
        }


        if (selectedCustomer != null) {
            System.out.println("Introdu noul nume de familie al clientului: ");
            lastName = scanner.nextLine();
            selectedCustomer.setLastName(lastName);

            System.out.println("Introdu noul prenume al clientului: ");
            String firstName = scanner.nextLine();
            selectedCustomer.setFirstName(firstName);

            System.out.println("Introdu noul număr al permisului de conducere al clientului: ");
            String driverLicenseNumber = scanner.nextLine();
            selectedCustomer.setDriverLicenseNumber(driverLicenseNumber);

            customerRepository.update(selectedCustomer);
            System.out.println("Clientul a fost actualizat cu succes.");
        }
    }

    private static void displayOneCustomer(Scanner scanner, CustomerRepository customerRepository) {
        String lastName;
        Customer customer;
        System.out.println("Introdu numele clientului: ");
        lastName = scanner.nextLine();
        customer = customerRepository.findSingleByLastName(lastName);
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