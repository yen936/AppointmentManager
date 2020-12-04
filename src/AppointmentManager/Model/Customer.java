package AppointmentManager.Model;

import AppointmentManager.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Customer extends Noun {

    private String address;
    private String postalCode;
    private String phoneNumber;
    private String divisionName;

    /** Constructor for Customer.
     *
     * @param id int
     * @param name String
     * @param address String
     * @param postalCode String
     * @param phoneNumber String
     * @param divisionName String
     */
    public Customer(int id, String name, String address, String postalCode, String phoneNumber, String divisionName) {
        super(id, name);
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.divisionName = divisionName;

    }

    /** Getter for address.
     *
     * @return String
     */
    public String getAddress() {
        return address;
    }

    /** Setter for address.
     *
     * @param address String
     */
    public void setAddress(String address) {
        this.address = address;
    }


    /** Getter for postal code.
     *
     * @return string
     */
    public String getPostalCode() {
        return postalCode;
    }

    /** Setter for postal code.
     *
     * @param postalCode string
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /** Getter for phone number
     *
     * @return phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }


    /** Setter for phone number
     *
     * @param phoneNumber String
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    /** Getter for division name
     *
     * @return String
     */
    public String getDivisionName() {
        return divisionName;
    }


    /** Setter for division ID
     *
     * @param divisionName String
     */
    public void setDivisionID(String divisionName) {
        this.divisionName = divisionName;
    }


    /** Gets all customers from the database
     *
     * @return ObservableList<Customer> all customers
     */
    public static ObservableList<Customer> getAllCustomerDataBase() {
        ObservableList<Customer> Customers = FXCollections.observableArrayList();

        try {
            PreparedStatement preparedSQL = DBConnect.getConnection().prepareCall("SELECT * FROM customers, first_level_divisions WHERE customers.Division_ID = first_level_divisions.Division_ID");
            ResultSet resultSet = preparedSQL.executeQuery();

            while (resultSet.next()) {

                Customer c = new Customer(
                        resultSet.getInt("Customer_ID"),
                        resultSet.getString("Customer_Name"),
                        resultSet.getString("Address"),
                        resultSet.getString("Postal_Code"),
                        resultSet.getString("Phone"),
                        resultSet.getString("Division"));

                Customers.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }

        return Customers;

    }


    /** Gets an customer from the database based on the customer ID
     * @param customerID index for customer
     * @return Customer
     */
    public static Customer getCustomerDataBase(int customerID) {

        try {
            PreparedStatement preparedSQL = DBConnect.getConnection().
                    prepareCall("SELECT * FROM customers, first_level_divisions WHERE Customer_ID='" + customerID + "'"
                            + "AND customers.Division_ID = first_level_divisions.Division_ID");
            ResultSet resultSet = preparedSQL.executeQuery();

            if (resultSet.next()) {

                return new Customer(
                        resultSet.getInt("Customer_ID"),
                        resultSet.getString("Customer_Name"),
                        resultSet.getString("Address"),
                        resultSet.getString("Postal_Code"),
                        resultSet.getString("Phone"),
                        resultSet.getString("Division_ID"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }

        return null;

    }

    /**
     * Saves an customer to the DB, validates that save was successful
     * @param customer new customer
     * @return boolean
     */
    public static boolean saveNewCustomerToDB(Customer customer) {

        int maxCustomerID = 0;

        try {
            PreparedStatement preparedSQL = DBConnect.getConnection().prepareCall("SELECT MAX(Customer_ID) from customers");
            ResultSet resultSet = preparedSQL.executeQuery();

            if (resultSet.next()) {
                maxCustomerID = resultSet.getInt("MAX(Customer_ID)");
                System.out.println(maxCustomerID);
            }

        } catch (SQLException e) {
            System.out.println("Error: On Select Max");
            e.printStackTrace();
        }


        try {
            PreparedStatement preparedSQL = DBConnect.getConnection()
                    .prepareCall("INSERT INTO customers SET Customer_Name='" + customer.getName() + "'"
                            + ", Address='" + customer.getAddress() + "'"
                            + ", Postal_Code='" + customer.getPostalCode() + "'"
                            + ", Phone='" + customer.getPhoneNumber() + "'"
                            + ", Division_ID='" + Util.getDivisionID(customer.getDivisionName()) + "'"
                            + ", Create_Date = NOW()"
                            + ", Created_By='" + User.getCurrentUser().getName() + "'"
                            + ", Last_Update = NOW()"
                            + ", Last_Updated_By='" + User.getCurrentUser().getName() + "'"
                            + ", Customer_ID='" + (maxCustomerID + 1) + "'");


            int result = preparedSQL.executeUpdate();

            if (result == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error: On SAve");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }

        return false;

    }

    /**
     * Updates an customer in the DB at an index, validates that save was successful
     * @param customer updated customer
     * @param customerID index of customer to be updated
     * @return boolean
     */
    public static boolean updateCustomerInDB(int customerID, Customer customer) {

        try {
            PreparedStatement preparedSQL = DBConnect.getConnection()
                    .prepareCall("UPDATE customers SET Customer_Name='" + customer.getName() + "'"
                            + ", Address='" + customer.getAddress() + "'"
                            + ", Postal_Code='" + customer.getPostalCode() + "'"
                            + ", Phone='" + customer.getPhoneNumber() + "'"
                            + ", Division_ID='" + Util.getDivisionID(customer.getDivisionName()) + "'"
                            + ", Last_Update = NOW()"
                            + ", Last_Updated_By='" + User.getCurrentUser().getName() + "'"
                            + " WHERE Customer_ID='" + customerID + "'");


            int result = preparedSQL.executeUpdate();

            if (result == 1) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error: Failure outside of SQL");
            e.printStackTrace();
        }

        return false;

    }


    /**
     * Deletes an customer from the DB at an index, validates that save was successful
     * @param customerID index for customer deletion
     * @return boolean
     */
    public static boolean deleteCustomerFromDB(int customerID) throws SQLException {
        boolean returnValue = false;

        PreparedStatement preparedSQL1 = DBConnect.getConnection()
                .prepareCall("DELETE FROM appointments WHERE Customer_ID = " + customerID);

        PreparedStatement preparedSQL2 = DBConnect.getConnection()
                .prepareCall("DELETE FROM customers WHERE Customer_ID = " + customerID);

        int result1 = preparedSQL1.executeUpdate();
        int result2 = preparedSQL2.executeUpdate();

        if (result1 == 1 && result2 == 1
                || result1 == 0 && result2 == 1) {
            returnValue = true;
        }

        return returnValue;
    }


}

