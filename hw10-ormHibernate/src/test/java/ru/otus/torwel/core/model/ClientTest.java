package ru.otus.torwel.core.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    public void equalsTest() {
        Client expectedClient = createClient();
        Client client = createClient();
        assertNotEquals(client, null);
        assertEquals(expectedClient, client);

        expectedClient.setAddressDataSet(new AddressDataSet(2,"1st street"));
        assertNotEquals(expectedClient, client);
        client.setAddressDataSet(new AddressDataSet(0,"1st street"));
        assertNotEquals(expectedClient, client);
        client.setAddressDataSet(new AddressDataSet(2,"1st road"));
        assertNotEquals(expectedClient, client);
        client.setAddressDataSet(new AddressDataSet(2,"1st street"));
        assertEquals(expectedClient, client);

        expectedClient.addPhone(new PhoneDataSet(1, "10"));
        assertNotEquals(expectedClient, client);
        PhoneDataSet testPhone = new PhoneDataSet();
        client.addPhone(testPhone);
        assertNotEquals(expectedClient, client);
        testPhone.setNumber("10");
        assertNotEquals(expectedClient, client);
        testPhone.setId(1);
        assertEquals(expectedClient, client);
        testPhone.setClient(client);
        assertEquals(expectedClient, client);

        expectedClient.addPhone(new PhoneDataSet(2, "20"));
        assertNotEquals(expectedClient, client);
        client.addPhone(new PhoneDataSet(2, "20"));
        assertEquals(expectedClient, client);

        expectedClient.addPhone(new PhoneDataSet(3, "30"));
        assertNotEquals(expectedClient, client);
        client.addPhone(new PhoneDataSet(3, "30"));
        assertEquals(expectedClient, client);
    }

    private Client createClient() {
        return new Client(10,"Elusive Joe", new ArrayList<>());
    }
}
