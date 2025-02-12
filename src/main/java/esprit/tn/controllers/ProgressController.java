package esprit.tn.controllers;

import esprit.tn.entities.Progress;
import esprit.tn.entities.StProgress;
import esprit.tn.service.ProgressService;

import java.util.Scanner;

public class ProgressController {

    static ProgressService service = new ProgressService();

    public static void updateProgress() {
        Scanner scanner = new Scanner(System.in);
        Progress progress = new Progress();

        int idProgress;
        String progressInput;

        System.out.println("Enter the ID of the progress to update:");
        while (true) {
            if (scanner.hasNextInt()) {
                idProgress = scanner.nextInt();
                scanner.nextLine();
                if (idProgress > 0) {
                    progress.setIdProgress(idProgress);
                    break;
                }
            } else {
                scanner.next();
            }
            System.out.println("Invalid Progress ID! It must be a number greater than 0. Try again:");
        }

        System.out.println("Course Status INPROGRESS , COMPLETED :");
        while (true) {
            progressInput = scanner.nextLine().toUpperCase();
            try {
                progress.setProgress(StProgress.valueOf(progressInput));
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(" Invalid Statut, you need to entrer HIDDEN ou PUBLIC :");
            }
        }

        service.update(progress);
    }

}
