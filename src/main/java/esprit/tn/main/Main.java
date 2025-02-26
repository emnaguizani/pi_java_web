/*package esprit.tn.main;

import esprit.tn.controllers.UsersController;
import esprit.tn.entities.Users;
import esprit.tn.services.UserService;

import java.util.Date;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        DatabaseConnection.getInstance();
        UserService userService = new UserService();
        UsersController userController = new UsersController(userService);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nChoose an operation:");
            System.out.println("1. Add User");
            System.out.println("2. Update User");
            System.out.println("3. Delete User");
            System.out.println("4. Show Users");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    userController.addUser();
                    break;
                case 2:
                    userController.updateUser();
                    break;
                case 3:
                    userController.deleteUser();
                    break;
                case 4:
                    userController.showUsers();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();

                    return;
                default:
                    System.out.println("Invalid choice! Please enter 1-5.");
            }
        }




    }
}*/