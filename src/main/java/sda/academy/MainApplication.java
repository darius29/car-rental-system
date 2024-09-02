package sda.academy;

import sda.academy.entities.Car;
import sda.academy.entities.Customer;
import sda.academy.repositories.CarRepository;
import sda.academy.repositories.CustomerRepository;

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
    }

    private static void executeCommand(int choice, Scanner scanner) {
        Car car = new Car();
        Customer customer = new Customer();
        CarRepository carRepository = new CarRepository();
        CustomerRepository customerRepository = new CustomerRepository();
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
        }
    }

    private static void deleteCustomer(Scanner scanner, CustomerRepository customerRepository) {
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

    private static void editCustomer(Scanner scanner, CustomerRepository customerRepository) {
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