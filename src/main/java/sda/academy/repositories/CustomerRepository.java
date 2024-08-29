package sda.academy.repositories;

import sda.academy.entities.Customer;

public class CustomerRepository extends BaseRepository<Customer, Integer>{
    public CustomerRepository() {
        super(Customer.class);
    }
}
