package esprit.tn.main;

import esprit.tn.entities.Forum;
import esprit.tn.entities.Response;
import esprit.tn.services.ForumService;
import esprit.tn.services.GeminiService;
import esprit.tn.services.ResponseService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {



        Scanner scanner = new Scanner(System.in);
        ForumService forumService = new ForumService();
        ResponseService responseService = new ResponseService();

        Forum forum = forumService.getForumById(52);
        System.out.println(forum);
      //  System.out.println("\n===== Forum Management =====");
        // int forumId = addForum(scanner, forumService);
        // updateForum(scanner, forumService);
        // deleteForum(scanner, forumService);
        //viewAllForums(forumService);

      //  System.out.println("\n===== Response Management =====");
      //  int responseId = addResponse(scanner, responseService, forumService);
        // updateResponse(scanner, responseService);
        // deleteResponse(scanner, responseService);
       // viewAllForums(forumService);

        // Test getForumById
       // testGetForumById(forumService, scanner);

        scanner.close();
    }

    // ================== Method Declarations ==================

    private static int addForum(Scanner scanner, ForumService forumService) {
        System.out.println("\nCreate forum");
        String title = getValidTitle(scanner, "Enter forum title: ");
        String description = getValidString(scanner, "Enter forum description: ");
        int authorId = getValidInt(scanner, "Enter author ID: ");

        Forum forum = new Forum(title, description, authorId, new ArrayList<>());
        try {
            forumService.ajouterForum(forum);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Forum added");
        return forum.getIdForum();
    }

    private static void updateForum(Scanner scanner, ForumService forumService) {
        System.out.println("\nUpdate forum");
        int forumId = getValidForumId(scanner, forumService, "Enter the forum ID to update: ");
        String title = getValidTitle(scanner, "Enter new forum title: ");
        String description = getValidString(scanner, "Enter new forum description: ");
        int authorId = getValidInt(scanner, "Enter new author ID: ");

        Forum updatedForum = new Forum(forumId, title, description, authorId, new ArrayList<>());
        forumService.updateForum(updatedForum);
        System.out.println("Forum updated ");
    }

    private static void deleteForum(Scanner scanner, ForumService forumService) {
        System.out.println("\nDelete a forum");
        int forumId = getValidForumId(scanner, forumService, "Enter the forum ID to delete: ");
        forumService.deleteForum(forumId);
        System.out.println("Forum deleted");
    }

    private static int addResponse(Scanner scanner, ResponseService responseService, ForumService forumService) {
        System.out.println("\nCreate response");
        int forumId = getValidForumId(scanner, forumService, "Enter the forum ID to add a response: ");
        String content = getValidString(scanner, "Enter response content: ");
        int authorId = getValidInt(scanner, "Enter author ID: ");

        Response response = new Response(content, authorId, LocalDateTime.now());
        responseService.ajouter(response, forumId);
        System.out.println("Response added");
        return response.getIdResponse();
    }

    private static void updateResponse(Scanner scanner, ResponseService responseService) {
        System.out.println("\nUpdate response");
        int responseId = getValidInt(scanner, "Enter the response ID to update: ");
        String content = getValidString(scanner, "Enter new response content: ");
        int authorId = getValidInt(scanner, "Enter new author ID: ");

        Response updatedResponse = new Response(responseId, content, authorId, LocalDateTime.now());
        responseService.updateResponse(updatedResponse);
        System.out.println("Response updated ");
    }

    private static void deleteResponse(Scanner scanner, ResponseService responseService) {
        System.out.println("\nDelete response");
        int responseId = getValidInt(scanner, "Enter the response ID to delete: ");
        responseService.deleteResponse(responseId);
        System.out.println("Response deleted successfully!");
    }

    private static void viewAllForums(ForumService forumService) {
        System.out.println("\nDisplaying all forums and responses...");
        List<Forum> forums = forumService.getAllForumsWithResponses();

        if (forums.isEmpty()) {
            System.out.println("No forums available.");
            return;
        }

        for (Forum f : forums) {
            System.out.println("\nForum ID: " + f.getIdForum());
            System.out.println("Title: " + f.getTitle());
            System.out.println("Description: " + f.getDescription());
            System.out.println("Author ID: " + f.getIdAuthor());
            System.out.println("Image path: " + f.getImagePath());
            System.out.println("Responses:");
            for (Response res : f.getResponses()) {
                System.out.println("  - " + res);
            }
            System.out.println("----------------------");
        }
    }

    private static void testGetForumById(ForumService forumService, Scanner scanner) {
        System.out.println("\nTesting getForumById method...");

        // Prompt the user to enter a forum ID
        System.out.print("Enter the forum ID: ");
        int forumId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        // Call the getForumById method
        Forum forum = forumService.getForumById(forumId);

        if (forum != null) {
            // Display the forum details
            System.out.println("\nForum Details:");
            System.out.println("ID: " + forum.getIdForum());
            System.out.println("Title: " + forum.getTitle());
            System.out.println("Description: " + forum.getDescription());
            System.out.println("Author ID: " + forum.getIdAuthor());
            System.out.println("Date Created: " + forum.getDateCreation());
            System.out.println("Image Path: " + forum.getImagePath()); // Display imagePath
            System.out.println("Is Blocked: " + forum.isBlocked());
        } else {
            System.out.println("No forum found with ID: " + forumId);
        }
    }

    private static boolean forumExists(int forumId, ForumService forumService) {
        List<Forum> forums = forumService.getAllForumsWithResponses();
        return forums.stream().anyMatch(f -> f.getIdForum() == forumId);
    }

    private static int getValidInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
            }
        }
    }

    private static String getValidString(Scanner scanner, String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Input cannot be empty. Please try again.");
            }
        }
    }

    private static String getValidTitle(Scanner scanner, String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.matches("[a-zA-Z0-9 ]+")) {
                return input;
            } else {
                System.out.println("Invalid title! Only letters, numbers, and spaces are allowed. Try again.");
            }
        }
    }

    private static int getValidForumId(Scanner scanner, ForumService forumService, String prompt) {
        int forumId;
        while (true) {
            System.out.print(prompt);
            forumId = getValidInt(scanner, "");

            if (forumExists(forumId, forumService)) {
                return forumId;
            } else {
                System.out.println("Forum with ID " + forumId + " does not exist. Please enter a valid ID.");
            }
        }
    }
}