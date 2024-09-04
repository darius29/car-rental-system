package sda.academy.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String locationName;

    private String address;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;
}
