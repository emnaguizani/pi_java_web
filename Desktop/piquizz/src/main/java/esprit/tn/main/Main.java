package esprit.tn.main;

import esprit.tn.entities.Quiz;
import esprit.tn.entities.Exercice;
import esprit.tn.services.QuizService;
import esprit.tn.services.ExerciceService;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Connection cnx = DatabaseConnection.getInstance().getCnx();

        QuizService quizService = new QuizService(cnx);
        ExerciceService exerciceService = new ExerciceService(cnx);

        try {
            Quiz newQuiz = new Quiz(
                    "Quiz3",
                    "A quiz to test Java knowledge",
                    60,
                    0,
                    LocalDateTime.now(),
                    "John ",
                    null
            );

            quizService.ajouterQuiz(newQuiz);
            System.out.println("Quiz added: " + newQuiz.getTitle());

            int quizId = newQuiz.getQuiz_id();
            System.out.println("Generated quiz ID: " + quizId);

            Exercice newExercice1 = new Exercice("?????", Arrays.asList("0", "1", "undefined"), "", 50, "/ex1.jpg",true);
            Exercice newExercice2 = new Exercice("?????", Arrays.asList("true", "false", "undefined"), "false", 10,"/ex2.jpg", true);
            Exercice newExercice3 = new Exercice("?????", Arrays.asList("0", "1", "undefined"), "0", 50, "/ex1.jpg",true);
            Exercice newExercice4 = new Exercice("?????", Arrays.asList("0", "1", "undefined"), "0", 50, "/ex1.jpg",true);

            exerciceService.ajouterExercice(newExercice1,quizId);
            exerciceService.ajouterExercice(newExercice2,quizId);
            exerciceService.ajouterExercice(newExercice4,quizId);
            exerciceService.ajouterExercice(newExercice3,quizId);


            System.out.println("Exercises added for Quiz ID " + quizId);

            Quiz quizWithExercises = quizService.getQuizById(quizId);
            System.out.println("Quiz with exercises: " + quizWithExercises.getTitle());
            for (Exercice ex : quizWithExercises.getExercices()) {
                System.out.println(" - " + ex.getQuestion() + ' '+ ex.getImagePath()+' ' + ' ' + ex.getCorrectAnswer());
            }

            newQuiz.setTitle("anisossss");
            newQuiz.setDescription("Updated Description");
            newQuiz.setDuration(90);
            quizService.updateQuiz(newQuiz);
            System.out.println("Quiz updated: " + newQuiz.getTitle());





        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
