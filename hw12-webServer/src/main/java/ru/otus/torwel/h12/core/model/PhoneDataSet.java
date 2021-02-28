package ru.otus.torwel.h12.core.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "phones")
public class PhoneDataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "number")
    private String number;

    @ManyToOne
    private Client client;

    public PhoneDataSet() {
    }

    public PhoneDataSet(long id, String number, Client client) {
        this.id = id;
        this.number = number;
        this.client = client;
    }

    public PhoneDataSet(long id, String number) {
        this.id = id;
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneDataSet)) return false;
        PhoneDataSet that = (PhoneDataSet) o;
        return id == that.id && Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number);
    }
}
