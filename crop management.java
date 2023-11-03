package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySql {

    public static void main(String[] args) throws Exception {

        // Load the Driver ----->step 1
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Establish the connection b/w Java with the MySQL database--->step 2
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/agriculture", "root", "likhith@123");

        // Create a statement object------>step 3
        Statement st = con.createStatement();

        // Write a query and execute the same to create the crops table if it doesn't exist
        String createTableQuery = "create table if not exists crops( cropname varchar(3) primary key, duration varchar(5), yield varchar(5), rate int(6), Investment int(6), earning int(6))";
        st.executeUpdate(createTableQuery);
        System.out.println("Crops table is created successfully!");

        // Retrieve investment and earning data from the crops table
        String selectQuery = "SELECT cropname, Investment, earning FROM crops";
        ResultSet resultSet = st.executeQuery(selectQuery);

        while (resultSet.next()) {
            String name = resultSet.getString("cropname");
            int investment = resultSet.getInt("Investment");
            int earning = resultSet.getInt("earning");

            // Calculate profit or loss
            int profitOrLoss = earning - investment;
            System.out.println("Crop: " + name);
            System.out.println("Investment: " + investment);
            System.out.println("Earning: " + earning);
            System.out.println("Profit/Loss: " + profitOrLoss);
            System.out.println();
        }

        // Close the connection ----->step 4
        resultSet.close();
        st.close();
        con.close();
    }
}
