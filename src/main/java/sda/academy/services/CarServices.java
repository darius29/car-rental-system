package sda.academy.services;

import sda.academy.entities.Car;
import sda.academy.repositories.CarRepository;

import java.util.List;
import java.util.Scanner;

public class CarServices {
    public static void addCar(Scanner scanner, Car car, CarRepository carRepository) {
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

    public static void editCar(Scanner scanner, CarRepository carRepository) {
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

    public static void displayAllCars(CarRepository carRepository) {
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

    public static void displayOneCar(Scanner scanner, CarRepository carRepository) {
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

    public static void deleteCar(Scanner scanner, CarRepository carRepository) {
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
}
