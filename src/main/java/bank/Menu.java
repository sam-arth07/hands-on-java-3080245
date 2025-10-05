package bank;

import java.util.Scanner;

import javax.security.auth.login.LoginException;

import bank.exceptions.AmountException;

public class Menu {

  private Scanner scanner;

  public static void main(String[] args) {
    System.out.println("Welcome to KasuKabe Bank International!");
    Menu menu = new Menu();
    menu.scanner = new Scanner(System.in);

    Customer customer = menu.authenticateUser();
    if (customer != null) {
      Account account = DataSource.getAccount(customer.getAccountId());
      menu.showMenu(customer, account);
    }

    menu.scanner.close();
  }

  private void showMenu(Customer customer, Account account) {
    int selection = 0;
    while (selection != 4 && customer.isAuthenticated()) {
      System.out.println("Please select one of the following options: ");
      System.out.println("------------------------------------------------");
      System.out.println("1. Deposit");
      System.out.println("2. Withdraw");
      System.out.println("3. Check Balance");
      System.out.println("4. Exit");
      System.out.println("-----------------------------------------------");

      selection = scanner.nextInt();
      double amount = 0;
      switch (selection) {
        case 1:
          System.out.println("How much would you like to deposit? (in Rs.): ");
          amount = scanner.nextDouble();

          try {
            account.deposit(amount);
          } catch (AmountException e) {
            System.out.println(e.getMessage());
            System.out.println("Please Try again later!");
          }

          System.out.println(amount + "Rs. Successfully deposited in account with id, " + account.getId());
          break;
        case 2:
          System.out.println("How much would you like to withdraw? (in Rs.): ");
          amount = scanner.nextDouble();
          account.withdraw(amount);
          System.out.println("Remaining Balance: " + account.getBalance());
          break;

        case 3:
          System.out.println(
              "Current Balance in your account with account id, "
                  + account.getId() + " is Rs. " + account.getBalance());

          break;

        case 4:
          Authenticator.logout(customer);
          System.out.println("Thanks for banking at KasuKabe Bank International!");
          break;

        default:
          System.out.println("Invalid Option. Please try again!");
          break;
      }
    }
  }

  private Customer authenticateUser() {
    System.out.println("Enter username :");
    String username = scanner.next();
    System.out.println("Enter password :");
    String password = scanner.next();

    Customer customer = null;
    try {
      customer = Authenticator.login(username, password);
    } catch (LoginException e) {
      System.out.println("Error: " + e.getMessage());
    }
    return customer;
  }
}
