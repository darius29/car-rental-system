package sda.academy.services;

import sda.academy.entities.Customer;
import sda.academy.repositories.CustomerRepository;

import java.util.List;
import java.util.Scanner;

public class CustomerServices {
    public static void addNewCustomer(Scanner scanner, Customer customer, CustomerRepository customerRepository) {
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

    public static void displayAllCustomers(CustomerRepository customerRepository) {
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

    public static void displayOneCustomer(Scanner scanner, CustomerRepository customerRepository) {
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

    public static void deleteCustomer(Scanner scanner, CustomerRepository customerRepository) {
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

    public static void editCustomerV1(Scanner scanner, CustomerRepository customerRepository){
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
}
