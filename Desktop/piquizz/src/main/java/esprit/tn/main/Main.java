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
                    10,
                    LocalDateTime.now(),
                    "John ",
                    null
            );

            quizService.ajouterQuiz(newQuiz);
            System.out.println("Quiz added: " + newQuiz.getTitle());

            int quizId = newQuiz.getQuiz_id();
            System.out.println("Generated quiz ID: " + quizId);

            Exercice newExercice1 = new Exercice("What is the default value of an int?", Arrays.asList("0", "1", "undefined"), "0", "0", 10, "/ex1.jpg",true,0);
            Exercice newExercice2 = new Exercice("What is the default value of a boolean?", Arrays.asList("true", "false", "undefined"), "true", "false", 10,"/ex2.jpg", true, 0);

            exerciceService.ajouterExercice(newExercice1,quizId);
            exerciceService.ajouterExercice(newExercice2,quizId);
            System.out.println("Exercises added for Quiz ID " + quizId);

            Quiz quizWithExercises = quizService.getQuizById(quizId);
            System.out.println("Quiz with exercises: " + quizWithExercises.getTitle());
            for (Exercice ex : quizWithExercises.getExercices()) {
                System.out.println(" - " + ex.getQuestion() + ' '+ ex.getImagePath()+' ' + ex.getAnswer() + ' ' + ex.getCorrectAnswer());
            }

            newQuiz.setTitle("anisossss");
            newQuiz.setDescription("Updated Description");
            newQuiz.setDuration(90);
            newQuiz.setTotalScore(15);
            quizService.updateQuiz(newQuiz);
            System.out.println("Quiz updated: " + newQuiz.getTitle());

            Exercice updatedExercice = quizWithExercises.getExercices().get(0);
            updatedExercice.setQuestion("Updated question for int default value?");
            updatedExercice.setAnswer("1");
            exerciceService.updateExercice(updatedExercice);
            System.out.println("Exercise updated: " + updatedExercice.getQuestion());




        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
