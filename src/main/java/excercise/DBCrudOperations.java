package excercise;

import com.google.gson.Gson;
import org.sqlite.SQLiteConfig;
import excercise.pojo.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class DBCrudOperations {
    public DBCrudOperations() {
        conn = connect();

        createDB();

    }

    static Connection conn;
    static Statement stmt = null;

    public Connection connect() {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            connection = DriverManager.getConnection("jdbc:sqlite::memory:", config.toProperties());
//            connection = DriverManager.getConnection("jdbc:sqlite:mydb.db",  config.toProperties());
            connection.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return connection;
    }

    public void createDB() {
        try {
            stmt = conn.createStatement();
            String sql =
                    "create table IF NOT EXISTS address( " +
                            "addressId integer PRIMARY KEY AUTOINCREMENT," +
                            "street TEXT not null," +
                            "city TEXT not null," +
                            "state TEXT not null," +
                            "postal TEXT not null)";

            stmt.executeUpdate(sql);
            sql = "create table IF NOT EXISTS person( " +
                    "ID integer PRIMARY KEY AUTOINCREMENT," +
                    "FirstName TEXT not null," +
                    "LastName number not null," +
                    "addressId integer default 1," +
                    "FOREIGN KEY(addressId)" +
                    "references address(addressId) on delete set DEFAULT on update cascade)";
            stmt.executeUpdate(sql);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }


    }

    public void insertFromJson(String filePath) throws IOException {
        // FileReader reader = new FileReader("/home/paddy/IdeaProjects/Stuff/src/main/resources/PersonList.json");
        String jsonFIle = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);

        Gson gson = new Gson();

        List<Person> personList = Arrays.asList(gson.fromJson(jsonFIle, Person[].class));


        personList.forEach(k -> {
            try {
                insertPerson(k.getId(), k.getFirstName(), k.getLastName(), k.getAddressId());
            } catch (SQLException e) {
                System.out.println("Please enter a VALID file location");
            }
        });

    }

    @SuppressWarnings("SameParameterValue")
    public void insertAddress(int addressId, String street, String city, String state, String postal) throws SQLException {
        String sql = String.format("INSERT INTO address (addressId, street, city, state, postal) VALUES (%d , '%s' , '%s' , '%s' , '%s');", addressId, street, city, state, postal);

        stmt = conn.createStatement();

        stmt.executeUpdate(sql);
    }

    void insertAddress(String street, String city, String state, String postal) throws SQLException {
        String sql = String.format("INSERT INTO address (addressId, street, city, state, postal) VALUES ( '%s' , '%s' , '%s' , '%s');", street, city, state, postal);

        stmt = conn.createStatement();

        stmt.executeUpdate(sql);
    }

    public void insertPerson(int id, String firstName, String lastName, int addressId) throws SQLException {
        String sql = String.format("INSERT INTO person (id, firstName, lastName, addressId) VALUES (%d ,'%s', '%s', %d);", id, firstName, lastName, addressId);

        stmt = conn.createStatement();

        stmt.executeUpdate(sql);
    }

    void insertPerson(String firstName, String lastName) throws SQLException {
        String sql = String.format("INSERT INTO person ( firstName, lastName) VALUES ('%s', '%s') ;", firstName, lastName);

        stmt = conn.createStatement();

        stmt.executeUpdate(sql);
    }

    public String getSinglePerson(int id) {
        String result = "";
        StringBuilder sb;

        try {
            ResultSet resultSet = stmt.executeQuery(String.format("select * from person p inner join address a on p.addressId = a.addressId where p.id = %d", id));
            ResultSetMetaData rsmd = resultSet.getMetaData();
            sb = new StringBuilder();

            int columCount = rsmd.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i < columCount + 1; i++) {
                    sb.append(String.format("%s : '%s,' ", rsmd.getColumnName(i), resultSet.getString(i)));
                }
            }
            result = sb.toString();
        } catch (Exception e) {

            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return result;
    }


    public String listPersons() {
        StringBuilder sb = new StringBuilder();
        try {
            ResultSet resultSet = stmt.executeQuery("select * from person p inner join address a on p.addressId = a.addressId");
            ResultSetMetaData rsmd = resultSet.getMetaData();

            int columCount = rsmd.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i < columCount + 1; i++) {
                    sb.append(String.format("%s : '%s,' ", rsmd.getColumnName(i), resultSet.getString(i)));
                }
                sb.append("\n");
            }
        } catch (Exception e) {

            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return sb.toString();
    }

    public String listAddress() {
        StringBuilder sb = new StringBuilder();

        try {
            ResultSet resultSet = stmt.executeQuery("select * from address");
            ResultSetMetaData rsmd = resultSet.getMetaData();

            int columCount = rsmd.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i < columCount; i++) {
                    sb.append(String.format("%s : '%s,' ", rsmd.getColumnName(i), resultSet.getString(i)));
                }
                sb.append("\n");
            }
        } catch (Exception e) {

            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return sb.toString();
    }

    public void updatePerson(int id, String firstName, String lastName) throws SQLException {
        String sql = "update person set " +
                " firstName = ?," +
                " lastName = ?" +
                "where ID = ?;";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        // set the corresponding param
        pstmt.setString(1, firstName);
        pstmt.setString(2, lastName);
        pstmt.setInt(3, id);

        pstmt.executeUpdate();

    }



    public void updateUsersAddress(String userId, String addressID)  {

        String sql = "update person set " +
                " addressId = ?" +
                "where ID = ?;";

        PreparedStatement pstmt;

        try {
            pstmt = conn.prepareStatement(sql);
            // set the corresponding param
            pstmt.setInt(1, Integer.parseInt(addressID));
            pstmt.setInt(2, Integer.parseInt(userId));

            pstmt.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("Please enter a valid addressID");
            throwables.printStackTrace();
        }


    }

    public void updatePersonsAddress(int id, int addressId) throws SQLException {
        String sql = "update person set addressId = ?" +
                "where ID = ?;";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        // set the corresponding param
        pstmt.setInt(1, addressId);
        pstmt.setInt(2, id);

        pstmt.executeUpdate();

    }

    public void updateAddress(int id, String street, String city, String state, String postal) throws SQLException {
        String sql = "update address set " +
                "street = ?, " +
                "city = ?, " +
                "state = ?, " +
                "postal = ? " +
                "where addressId = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        // set the corresponding param
        pstmt.setString(1, street);
        pstmt.setString(2, city);
        pstmt.setString(3, state);
        pstmt.setString(4, postal);
        pstmt.setInt(5, id);

        pstmt.executeUpdate();


    }

    public void deletePerson(int id) throws SQLException {
        String sql = "delete from person " +
                "where ID = ?;";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        // set the corresponding param
        pstmt.setInt(1, id);

        pstmt.executeUpdate();

    }

    public void deleteAddress(int id) throws SQLException {
        String sql = "delete from address " +
                "where addressId = ?;";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        // set the corresponding param
        pstmt.setInt(1, id);

        pstmt.executeUpdate();

    }

    public int countTable(String table) throws SQLException {
        ResultSet resultSet = stmt.executeQuery(String.format("select count(*) from %s", table));
        return resultSet.getInt("count(*)");

    }

    public void closeDB() {
        try {
            stmt.close();
            conn.commit();
            conn.close();
        } catch (SQLException throwables) {

            throwables.printStackTrace();
        }

    }

    public void fillDatabase() throws SQLException {

        stmt = conn.createStatement();
        String sql = "INSERT INTO address (street, city, state, postal) " +
                "VALUES ('BusinessAddress','Dublin','Leinster','d24 xxx1' );";
        stmt.executeUpdate(sql);

        stmt = conn.createStatement();
        sql = "INSERT INTO address (street, city, state, postal) " +
                "VALUES ('26 Ashfield Drive','Dublin','Leinster','d24 xhe1' );";
        stmt.executeUpdate(sql);

        stmt = conn.createStatement();
        sql = "INSERT INTO address (street, city, state, postal) " +
                "VALUES ('Rathglass','Tullow','Carlow','R93 YE26' );";
        stmt.executeUpdate(sql);

        stmt = conn.createStatement();
        sql = "INSERT INTO person (firstName, lastName) " +
                "VALUES ('Pádraig', 'O Naughton');";
        stmt.executeUpdate(sql);

        stmt = conn.createStatement();
        sql = "INSERT INTO person (firstName, lastName) " +
                "VALUES ('Anna', 'Nolan');";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO person (firstName, lastName) " +
                "VALUES ('Martina', 'Browne');";
        stmt.executeUpdate(sql);
    }


}
