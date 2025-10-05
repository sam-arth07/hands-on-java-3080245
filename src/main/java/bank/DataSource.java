package bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataSource {

  public static Connection connect() {
    String db_file_url = "jdbc:sqlite:resources/bank.db";
    Connection connection = null;

    try {
      connection = DriverManager.getConnection(db_file_url);
      System.err.println("We are connected");
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return connection;
  }

  public static Customer getCustomer(String username) {
    String query = "SELECT * FROM customers WHERE username = ?";
    Customer customer = null;
    try (Connection connection = connect();
        PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, username);

      try (ResultSet rs = statement.executeQuery()) {
        customer = new Customer(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getInt("account_id"));
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return customer;
  }

  public static Account getAccount(int account_id) {
    String query = "select * from accounts where id = ?";
    Account account = null;

    try (Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, account_id);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        account = new Account(
            rs.getInt("id"),
            rs.getString("type"),
            rs.getInt("balance"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return account;
  }

  public static void main(String[] args) {
    Customer customer = getCustomer("telloy3x@bigcartel.com");
    Account account = getAccount(customer.getAccountId());
    System.out.println(customer.getName());
    System.out.println(customer.getUsername());

    System.out.println("------------------------");
    System.out.println(account.getId());
    System.out.println(account.getBalance());
  }
}
