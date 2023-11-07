package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class MySql {

    public static void main(String[] args) throws Exception {

        // Load the Driver ----->step 1
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Establish the connection b/w Java with the MySQL database--->step 2
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/agriculture", "root", "likhith@123");

        // Create a statement object------>step 3
        Statement st = con.createStatement();

        // Write a query and execute the same to create the crops table if it doesn't exist

     // Create a table to store profit/loss data
     // Create a table to store profit/loss data (if not exists)
        String createTableQuery = "CREATE TABLE IF NOT EXISTS crops(" +
                "cropname VARCHAR(30) PRIMARY KEY," +
                "cropduration VARCHAR(5)," +
                "yield_pr_acre VARCHAR(5)," +
                "crop_rate_per_kg INT(6)," +
                "Investment INT(6)," +
                "earning INT(6)," +
                "profit_loss INT(6))";
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

        //  user input for new crop data
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter crop name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter crop duration:  ");
        String newDuration = scanner.nextLine();
        
        System.out.print("Enter crop yield: ");
        String newYield_pr_acre = scanner.nextLine();
        System.out.print("Enter investment: ");
        int newInvestment = scanner.nextInt();
        System.out.print("Enter rate per kg: ");
        int newRate = scanner.nextInt();

        // Calculate earning, profit or loss for the new crop
        int newEarning = calculateEarning(newRate, newYield_pr_acre);
        int newProfitOrLoss = newEarning - newInvestment;

        // Update the 'crops' table with the new crop data
        String insertQuery = "INSERT INTO crops (cropname, cropduration, yield_pr_acre, Investment, crop_rate_per_kg, earning, profit_loss) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement insertStatement = con.prepareStatement(insertQuery);
        insertStatement.setString(1, newName);
        insertStatement.setString(2, newDuration);
        insertStatement.setString(3, newYield_pr_acre);
        insertStatement.setInt(4, newInvestment);
        insertStatement.setInt(5, newRate);
        insertStatement.setInt(6, newEarning);
        insertStatement.setInt(7, newProfitOrLoss);
        insertStatement.executeUpdate();
        insertStatement.close();

        System.out.println("New Crop: " + newName);
        System.out.println("Investment: " + newInvestment);
        System.out.println("Earning: " + newEarning);
        System.out.println("Profit/Loss: " + newProfitOrLoss);

        // Close the connection
        resultSet.close();
        st.close();
        con.close();
    } 


// Function to calculate earning based on rate (per kg) and yield
private static int calculateEarning(int ratePerKg, String yieldString) {
    int yield_pr_acre = extractYield(yieldString);
    return yield_pr_acre * ratePerKg;
}

// Function to extract yield as an integer from a string like "100 kg"
private static int extractYield(String yieldString) {
    String[] parts = yieldString.split(" ");
    if (parts.length == 2) {
        try {
            int yield_pr_acre = Integer.parseInt(parts[0]);
            return yield_pr_acre;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
    return 0; // Default yield
}
}



