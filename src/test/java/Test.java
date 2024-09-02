import sda.academy.entities.Car;
import sda.academy.entities.Customer;
import sda.academy.services.ReservationService;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
    public static void main(String[] args) {
        ReservationService reservationService = new ReservationService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Creare obiecte Car și Customer
        Car car1 = new Car();
        car1.setLicensePlate("ABC123");
        car1.setModel("Toyota Corolla");
        car1.setPricePerDay(50);

        Car car2 = new Car();
        car2.setLicensePlate("XYZ789");
        car2.setModel("Honda Civic");
        car2.setPricePerDay(60);

        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setDriverLicenseNumber("DL123456");

        // Rezervare inițială
        try {
            Date startDate = sdf.parse("2024-09-01");
            Date endDate = sdf.parse("2024-09-05");
            reservationService.makeReservation(car1, customer, startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Prelungire rezervare
        try {
            Long reservationId = 1L; // ID-ul rezervării de modificat
            Date newEndDate = sdf.parse("2024-09-07");
            reservationService.extendReservation(reservationId, newEndDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Schimbare mașină pentru rezervare
        try {
            Long reservationId = 1L; // ID-ul rezervării de modificat
            reservationService.changeReservationCar(reservationId, car2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}