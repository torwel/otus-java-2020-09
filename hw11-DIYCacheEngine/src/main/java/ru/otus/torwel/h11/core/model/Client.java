package ru.otus.torwel.h11.core.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="address_id")
    private AddressDataSet addressDataSet;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client", fetch = FetchType.EAGER)
    private List<PhoneDataSet> phones;

    public Client() {
    }

    public Client(long id, String name, List<PhoneDataSet> phones) {
        if (phones == null) {
            throw new IllegalArgumentException("phones is null");
        }
        this.id = id;
        this.name = name;
        this.phones = phones;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDataSet getAddressDataSet() {
        return addressDataSet;
    }

    public void setAddressDataSet(AddressDataSet addressDataSet) {
        this.addressDataSet = addressDataSet;
    }

    public List<PhoneDataSet> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDataSet> phones) {
        this.phones = phones;
    }

    public void addPhone(PhoneDataSet phoneDataSet) {
        phoneDataSet.setClient(this);
        phones.add(phoneDataSet);
    }

    public void removePhone(PhoneDataSet phoneDataSet) {
        phoneDataSet.setClient(null);
        phones.remove(phoneDataSet);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", addressDataSet=" + addressDataSet +
                ", phones=" + phones +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id
                && Objects.equals(name, client.name)
                && Objects.equals(addressDataSet, client.addressDataSet)
                && equalsPhones(client.phones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, addressDataSet, phones);
    }

    private boolean equalsPhones(List<PhoneDataSet> otherPhones) {
        if (phones == otherPhones) {
            return true;
        }
        if(phones == null || otherPhones == null || (phones.size() != otherPhones.size()) ) {
            return false;
        }
        List<PhoneDataSet> thisCopy = new ArrayList<>(phones);
        List<PhoneDataSet> otherCopy = new ArrayList<>(otherPhones);
        thisCopy.sort(Comparator.comparing(PhoneDataSet::getNumber));
        otherCopy.sort(Comparator.comparing(PhoneDataSet::getNumber));
        return thisCopy.equals(otherCopy);
    }
}
