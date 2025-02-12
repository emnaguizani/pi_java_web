package esprit.tn.controllers;

import esprit.tn.entities.Cours;
import esprit.tn.entities.Niveau;
import esprit.tn.entities.Status;
import esprit.tn.service.CourService;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CoursController {
    static CourService service = new CourService();



    public static void addCourse() {
        Scanner scanner = new Scanner(System.in);
        Cours cours = new Cours();


        String title, description, date, statusInput, niveauInput;
        int idFormateur;

        System.out.println("Title of the course :");
        while ((title = scanner.nextLine()).isEmpty()) {
            System.out.println("Title invalid , try again :");
        }
        cours.setTitle(title);

        System.out.println("Course description :");
        while ((description = scanner.nextLine()).isEmpty()) {
            System.out.println("Empty description !!! , try again :");
        }
        cours.setDescription(description);

        System.out.println("Creation date of the course (format YYYY-MM-DD) :");
        while (!(date = scanner.nextLine()).matches("\\d{4}-\\d{2}-\\d{2}")) {
            System.out.println("Format invalid, try again with the format YYYY-MM-DD :");
        }
        cours.setDateCreation(date);

        System.out.println("Id of the trainer :");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid id !!! , try again :");
            scanner.next();
        }
        idFormateur = scanner.nextInt();
        scanner.nextLine();
        cours.setIdFormateur(idFormateur);

        System.out.println("Course Status (HIDDEN or PUBLIC) :");
        while (true) {
            statusInput = scanner.nextLine().toUpperCase();
            try {
                cours.setStatus(Status.valueOf(statusInput));
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(" Invalid Statut, you need to entrer HIDDEN ou PUBLIC :");
            }
        }

        System.out.println("Level of the course (BEGINNER, INTERMEDIATE, ADVANCED) :");
        while (true) {
            niveauInput = scanner.nextLine().toUpperCase();
            try {
                cours.setNiveau(Niveau.valueOf(niveauInput));
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Level Invalid , you can only enter  BEGINNER, INTERMEDIATE or ADVANCED :");
            }
        }
        System.out.println("Enter video paths separated by commas:");
        String videoInput = scanner.nextLine();
        List<String> videoPaths = Arrays.asList(videoInput.split(","));
        cours.setVideoPaths(videoPaths);

        scanner.close();
        System.out.println("Added Successfully !!!");

        service.add(cours);

    }
    public static void listAll() {
        List<Cours> courses= service.findAll();
        System.out.println(courses);


    }
    public static void listById() {
        Scanner scanner = new Scanner(System.in);
        int id;

        do {
            System.out.println("Enter Course ID :");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid course ID number.");
                scanner.next();
            }
            id = scanner.nextInt();
        } while (id < 1);
        Cours course = service.findById(id);
        if (course != null) {
            System.out.println(course);
        } else {
            System.out.println("No course found with ID: " + id);
        }
    }


    public static void deleteCourse() {
        Scanner scanner = new Scanner(System.in);
        int id;

        do {
            System.out.println("Enter Course ID :");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid course ID number.");
                scanner.next();
            }
            id = scanner.nextInt();
        } while (id < 1);
        service.delete(id);
        }

        public static void updateCourse() {
            Scanner scanner = new Scanner(System.in);
            Cours cours = new Cours();

            String title, description, date, statusInput, niveauInput;
            int idFormateur, idCours;


            System.out.println("Enter the ID of the course to update:");
            while (true) {
                if (scanner.hasNextInt()) {
                    idCours = scanner.nextInt();
                    scanner.nextLine();
                    if (idCours > 0) {
                        cours.setIdCours(idCours);
                        break;
                    }
                } else {
                    scanner.next();
                }
                System.out.println("Invalid course ID! It must be a number greater than 0. Try again:");
            }

            System.out.println("Title of the course:");
            while ((title = scanner.nextLine()).isEmpty()) {
                System.out.println("Title invalid, try again:");
            }
            cours.setTitle(title);

            System.out.println("Course description:");
            while ((description = scanner.nextLine()).isEmpty()) {
                System.out.println("Empty description! Try again:");
            }
            cours.setDescription(description);

            System.out.println("Creation date of the course (format YYYY-MM-DD):");
            while (!(date = scanner.nextLine()).matches("\\d{4}-\\d{2}-\\d{2}")) {
                System.out.println("Format invalid, try again with the format YYYY-MM-DD:");
            }
            cours.setDateCreation(date);

            System.out.println("Id of the trainer:");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid id! Try again:");
                scanner.next();
            }
            idFormateur = scanner.nextInt();
            scanner.nextLine();
            cours.setIdFormateur(idFormateur);

            System.out.println("Course Status (HIDDEN or PUBLIC):");
            while (true) {
                statusInput = scanner.nextLine().toUpperCase();
                try {
                    cours.setStatus(Status.valueOf(statusInput));
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid Status! Enter HIDDEN or PUBLIC:");
                }
            }

            System.out.println("Level of the course (BEGINNER, INTERMEDIATE, ADVANCED):");
            while (true) {
                niveauInput = scanner.nextLine().toUpperCase();
                try {
                    cours.setNiveau(Niveau.valueOf(niveauInput));
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Level Invalid! Enter BEGINNER, INTERMEDIATE, or ADVANCED:");
                }
            }

            System.out.println("Enter video paths separated by commas:");
            String videoInput = scanner.nextLine();
            List<String> videoPaths = Arrays.asList(videoInput.split(","));
            cours.setVideoPaths(videoPaths);

            service.update(cours);

        }


}
