package excercise;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class CandidateCodingExcercise {


    public static void main(String[] args) throws SQLException, IOException {
        DBCrudOperations dbCrudOperations = new DBCrudOperations();
        CandidateCodingExcercise candidateCodingExcercise = new CandidateCodingExcercise();
        dbCrudOperations.createDB();
        dbCrudOperations.fillDatabase();
        candidateCodingExcercise.runUserInteraction();

        Scanner sc = new Scanner(System.in);

        String input = sc.nextLine();
        boolean exit = false;

        do {
            // remove duplicate case by converting the input to lower letters
            switch (input) {
                case "1":
                    System.out.println(dbCrudOperations.listPersons());
                    candidateCodingExcercise.runUserInteraction();

                    input = sc.nextLine();

                    break;
                case "2":
                    System.out.println("Person(firstName, lastName)");
                    System.out.println("enter a pesons's First Name");
                    String fName = sc.nextLine();
                    System.out.println("enter a person");
                    String lName = sc.nextLine();
                    dbCrudOperations.insertPerson(fName, lName);
                    candidateCodingExcercise.runUserInteraction();

                    input = sc.nextLine();
                    break;
                case "3":
                    System.out.println("Please choose a table to count (person, address)");
                    String userEntry = sc.nextLine();
                    System.out.printf("There are %d entries in the %s table%n", dbCrudOperations.countTable(userEntry), userEntry);
                    candidateCodingExcercise.runUserInteraction();

                    input = sc.nextLine();
                    break;
                case "4":
                    System.out.println(dbCrudOperations.listAddress());
                    candidateCodingExcercise.runUserInteraction();

                    input = sc.nextLine();
                    break;
                case "5":
                    System.out.println("address(String street, String city, String state, String posta)");
                    System.out.println("enter a person");
                    String street;
                    String city;
                    String state;
                    String postal;
                    System.out.println("enter a street address");
                    street = sc.nextLine();
                    System.out.println("enter a city address");
                    city = sc.nextLine();
                    System.out.println("enter a state address");
                    state = sc.nextLine();
                    System.out.println("enter a postal address");
                    postal = sc.nextLine();
                    dbCrudOperations.insertAddress(street, city, state, postal);
                    candidateCodingExcercise.runUserInteraction();

                    input = sc.nextLine();
                    break;

                case "6":
                    System.out.println(dbCrudOperations.listPersons());
                    System.out.println(dbCrudOperations.listAddress());
                    System.out.println("Please enter a valid userId");
                    input = sc.nextLine();
                    String userId = input;
                    System.out.println("Please enter a valid addressId");
                    input = sc.nextLine();
                    dbCrudOperations.updateUsersAddress(userId, input);
                    candidateCodingExcercise.runUserInteraction();

                    input = sc.nextLine();
                    break;
                case "7":
                    System.out.println("enter a pesons's id to delete");
                    dbCrudOperations.deletePerson(sc.nextInt());
                    candidateCodingExcercise.runUserInteraction();

                    input = sc.nextLine();
                    break;
                case "8":
                    System.out.println("enter a address's id to delete");
                    dbCrudOperations.deleteAddress(sc.nextInt());
                    candidateCodingExcercise.runUserInteraction();

                    input = sc.nextLine();
                    break;
                case "9":
                    System.out.println("enter a local files JSON path to insert");
                    dbCrudOperations.insertFromJson(sc.nextLine());
                    candidateCodingExcercise.runUserInteraction();

                    input = sc.nextLine();
                    break;
                case "10": {
                    System.out.println("Goodbye");
                    dbCrudOperations.closeDB();
                    exit = true;
                    break;
                }
            }


        } while (!exit);
        sc.close();

    }

    public void runUserInteraction() {
        System.out.println("please choose an option\n" +
                "1). List Current Person entries in a database\n" +
                "2). insert a new entry to our Person Table\n" +
                "3). Count entries in either table (Person, Address)\n" +
                "4). List Current Address entries in a database\n" +
                "5). insert a new entry to our Address table\n" +
                "6). Update a person's address\n" +
                "7). Delete a person\n" +
                "8). Delete an address\n" +
                "9). Insert into persons table from JSON file\n" +
                "10). Exit\n" +
                "");
    }


}
