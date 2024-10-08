package sda.academy.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String licensePlate;

    private String model;

    private int pricePerDay;

    @OneToMany(mappedBy = "car")
    private List<MaintenanceRecord> maintenanceRecords;



}
