import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

public class TestMain {
    DBCrudOperations main;

    public TestMain() {
        main = new DBCrudOperations();
    }

    @Test
    void testAll() throws SQLException {
        main.fillDatabase();
        String sb = "ID : '1,' FirstName : 'Pádraig,' LastName : 'O Naughton,' addressId : '1,' addressId : '1,' street : 'BusinessAddress,' city : 'Dublin,' state : 'Leinster,' postal : 'd24 xxx1,' \n" +
                "ID : '2,' FirstName : 'Anna,' LastName : 'Nolan,' addressId : '1,' addressId : '1,' street : 'BusinessAddress,' city : 'Dublin,' state : 'Leinster,' postal : 'd24 xxx1,' \n" +
                "ID : '3,' FirstName : 'Martina,' LastName : 'Browne,' addressId : '1,' addressId : '1,' street : 'BusinessAddress,' city : 'Dublin,' state : 'Leinster,' postal : 'd24 xxx1,' \n";
        Assertions.assertEquals(sb, main.listPersons());
    }

    @Test
    void testGetSinglePerson() throws SQLException {
        main.fillDatabase();
        String expected = "ID : '2,' FirstName : 'Anna,' LastName : 'Nolan,' addressId : '1,' addressId : '1,' street : 'BusinessAddress,' city : 'Dublin,' state : 'Leinster,' postal : 'd24 xxx1,' ";
        Assertions.assertEquals(expected, main.getSinglePerson(2));
    }

    @Test
    void testUpdate() throws SQLException {

        String expected = "ID : '1,' FirstName : 'Paddy,' LastName : 'Norton,' addressId : '1,' addressId : '1,' street : 'BusinessAddress,' city : 'Dublin,' state : 'Leinster,' postal : 'd24 xxx1,' ";

        main.updatePerson(1, "Paddy", "Norton");
        Assertions.assertEquals(expected, main.getSinglePerson(1));

    }

    @Test
    void testUpdatePersonsAddress() throws SQLException {

        String expected = "ID : '1,' FirstName : 'Pádraig,' LastName : 'O Naughton,' addressId : '3,' addressId : '3,' street : 'Rathglass,' city : 'Tullow,' state : 'Carlow,' postal : 'R93 YE26,' ";
        main.updatePersonsAddress(1, 3);

        Assertions.assertEquals(expected, main.getSinglePerson(1));
        main.updatePersonsAddress(1, 1);

    }

    @Test
    void testUpdateAddress() throws SQLException {
        main.updateAddress(1, "example street", "Galway", "Galway", "g24 sg25");

        String expected = "ID : '2,' FirstName : 'Anna,' LastName : 'Nolan,' addressId : '1,' addressId : '1,' street : 'example street,' city : 'Galway,' state : 'Galway,' postal : 'g24 sg25,' ";

        Assertions.assertEquals(expected, main.getSinglePerson(2));
        main.updateAddress(1, "BusinessAddress", "Dublin", "Leinster", "d24 xxx1");

    }

    @Test
    void testDeletePerson() throws SQLException {
        StringBuilder sb = new StringBuilder();
        main.deletePerson(1);
        sb.append("ID : '2,' FirstName : 'Anna,' LastName : 'Nolan,' addressId : '1,' addressId : '1,' street : 'BusinessAddress,' city : 'Dublin,' state : 'Leinster,' postal : 'd24 xxx1,' \n");
        sb.append("ID : '3,' FirstName : 'Martina,' LastName : 'Browne,' addressId : '1,' addressId : '1,' street : 'BusinessAddress,' city : 'Dublin,' state : 'Leinster,' postal : 'd24 xxx1,' \n");

        Assertions.assertEquals(sb.toString(), main.listPersons());
        main.insertPerson(1, "Paddy", "Norton", 1);
    }

    @Test
    void testDeleteAddress() throws SQLException {
        StringBuilder sb = new StringBuilder();
        main.deleteAddress(2);
        sb.append("addressId : '1,' street : 'BusinessAddress,' city : 'Dublin,' state : 'Leinster,' \n");
        sb.append("addressId : '3,' street : 'Rathglass,' city : 'Tullow,' state : 'Carlow,' \n");

        Assertions.assertEquals(sb.toString(), main.listAddress());
        main.insertAddress(2, "BusinessAddress", "Dublin", "Leinster", "d24 xxx1");

    }

    @Test
    void testCountPerson() throws SQLException {
        Assertions.assertEquals(3, main.countTable("person"));

    }

    @Test
    void testCountAddress() throws SQLException {
        Assertions.assertEquals(3, main.countTable("address"));
    }

    @Test
    void testInsertFromJson() throws IOException, SQLException {

        main.insertFromJson("src/main/resources/PersonList.json");

        Assertions.assertEquals("ID : '25,' FirstName : 'tom,' LastName : 'brady,' addressId : '1,' addressId : '1,' street : 'BusinessAddress,' city : 'Dublin,' state : 'Leinster,' postal : 'd24 xxx1,' ", main.getSinglePerson(25));
        main.deletePerson(25);
    }

}
