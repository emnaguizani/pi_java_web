package esprit.tn.controllers;

import esprit.tn.entities.Users;
import esprit.tn.services.UserService;

import java.util.List;
import java.util.Scanner;

public class UsersController {
    private UserService userService;
    private Scanner scanner;

    public UsersController(UserService userService) {
        if (userService == null) {
            throw new IllegalArgumentException("UserService cannot be null");
        }
        this.userService = userService;
        this.scanner = new Scanner(System.in);

    }

    // Constructor
    public void UserController(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    // Add a user dynamically
    public void addUser() {
        System.out.println("Enter Full Name:");
        String fullName = scanner.nextLine();

        System.out.println("Enter Email:");
        String email = scanner.nextLine();

        System.out.println("Enter Password:");
        String password = scanner.nextLine();

        System.out.println("Enter Role (eleve/formateur):");
        String role = scanner.nextLine().toLowerCase();

        if (!role.equals("eleve") && !role.equals("formateur")) {
            System.out.println("Invalid role! Please enter 'eleve' or 'formateur'.");
            return;
        }

        Users user = new Users(fullName, email, new java.util.Date(), password, role);
        userService.ajouter(user);
    }

    public void updateUser() {
        System.out.println("Enter User ID to update:");
        int id = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.println("Enter New Full Name:");
        String newFullName = scanner.nextLine();

        System.out.println("Enter New Email:");
        String newEmail = scanner.nextLine();

        System.out.println("Enter New Password:");
        String newPassword = scanner.nextLine();

        Users user = new Users(id, newFullName, newEmail, new java.util.Date(), newPassword, ""); // Role remains unchanged
        userService.modifier(user);
    }


    public void deleteUser() {
        System.out.println("Enter User ID to delete:");
        int id = scanner.nextInt();
        Users user = new Users(id, "", "", null, "", ""); // Only ID is needed for deletion
        userService.supprimer(user);
    }


    // Display all users
    public void showUsers() {
        List<Users> users = userService.getall();
        System.out.println("\nList of Users:");
        for (Users user : users) {
            System.out.println(user);
        }
    }
}
