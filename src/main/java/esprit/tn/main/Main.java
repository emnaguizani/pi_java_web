package esprit.tn.main;

import esprit.tn.entities.Seance;
import esprit.tn.entities.Absence;
import esprit.tn.services.SeanceService;
import esprit.tn.services.AbsenceService;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection.getInstance();

        SeanceService seanceService = new SeanceService();
        AbsenceService absenceService = new AbsenceService();

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n📋 Menu Principal:");
            System.out.println("1️⃣ Ajouter une séance");
            System.out.println("2️⃣ Modifier une séance");
            System.out.println("3️⃣ Supprimer une séance");
            System.out.println("4️⃣ Afficher toutes les séances");
            System.out.println("5️⃣ Gérer les absences");
            System.out.println("0️⃣ Quitter");
            System.out.print("➡️ Choisissez une option: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Titre de la séance: ");
                    String titre = scanner.nextLine();
                    System.out.print("Contenu de la séance: ");
                    String contenu = scanner.nextLine();
                    System.out.print("ID du formateur: ");
                    int idFormateur = scanner.nextInt();

                    Seance newSeance = new Seance(0, titre, contenu, new Timestamp(System.currentTimeMillis()), idFormateur);
                    seanceService.ajouter(newSeance);
                    System.out.println("✅ Séance ajoutée avec succès.");
                    break;

                case 2:
                    System.out.print("ID de la séance à modifier: ");
                    int idMod = scanner.nextInt();
                    scanner.nextLine();
                    Seance seanceToUpdate = seanceService.getone(idMod);

                    if (seanceToUpdate != null) {
                        System.out.print("Nouveau titre: ");
                        seanceToUpdate.setTitre(scanner.nextLine());
                        System.out.print("Nouveau contenu: ");
                        seanceToUpdate.setContenu(scanner.nextLine());
                        seanceToUpdate.setDatetime(new Timestamp(System.currentTimeMillis()));

                        seanceService.modifier(seanceToUpdate);
                        System.out.println("✅ Séance modifiée avec succès.");
                    } else {
                        System.out.println("❌ Séance introuvable.");
                    }
                    break;

                case 3:
                    System.out.print("ID de la séance à supprimer: ");
                    int idSupp = scanner.nextInt();
                    seanceService.supprimer(new Seance(idSupp, "", "", new Timestamp(System.currentTimeMillis()), 0));
                    System.out.println("✅ Séance supprimée avec succès.");
                    break;

                case 4:
                    List<Seance> seances = seanceService.getAll();
                    if (seances.isEmpty()) {
                        System.out.println("✅ Aucune séance trouvée.");
                    } else {
                        for (Seance s : seances) {
                            System.out.println(s);
                        }
                    }
                    break;

                case 5:
                    System.out.println("1️⃣ Ajouter une absence");
                    System.out.println("2️⃣ Modifier une absence");
                    System.out.println("3️⃣ Supprimer une absence");
                    System.out.println("4️⃣ Afficher toutes les absences");
                    System.out.print("➡️ Choisissez une option pour la gestion des absences: ");
                    int absenceChoice = scanner.nextInt();
                    scanner.nextLine();

                    switch (absenceChoice) {
                        case 1:
                            System.out.print("ID de la séance: ");
                            int idSeance = scanner.nextInt();
                            System.out.print("ID de l'élève: ");
                            int idEleve = scanner.nextInt();
                            scanner.nextLine();
                            String etat;
                            do {
                                System.out.print("État (Présent/Absent): ");
                                etat = scanner.nextLine().trim();
                            } while (!etat.equalsIgnoreCase("Présent") && !etat.equalsIgnoreCase("Absent"));

                            Absence newAbsence = new Absence(0, idSeance, idEleve, etat);
                            absenceService.ajouter(newAbsence);
                            System.out.println("✅ Absence ajoutée avec succès.");
                            break;

                        case 2:
                            System.out.print("ID de l'absence à modifier: ");
                            int idAbsMod = scanner.nextInt();
                            scanner.nextLine();
                            Absence absenceToUpdate = absenceService.getone(idAbsMod);

                            if (absenceToUpdate != null) {
                                do {
                                    System.out.print("Nouveau état (Présent/Absent): ");
                                    etat = scanner.nextLine().trim();
                                } while (!etat.equalsIgnoreCase("Présent") && !etat.equalsIgnoreCase("Absent"));

                                absenceToUpdate.setEtat(etat);
                                absenceService.modifier(absenceToUpdate);
                                System.out.println("✅ Absence modifiée avec succès.");
                            } else {
                                System.out.println("❌ Absence introuvable.");
                            }
                            break;

                        case 3:
                            System.out.print("ID de l'absence à supprimer: ");
                            int idAbsSupp = scanner.nextInt();
                            absenceService.supprimer(new Absence(idAbsSupp, 0, 0, ""));
                            System.out.println("✅ Absence supprimée avec succès.");
                            break;

                        case 4:
                            List<Absence> absences = absenceService.getAll();
                            if (absences.isEmpty()) {
                                System.out.println("✅ Aucune absence trouvée.");
                            } else {
                                for (Absence a : absences) {
                                    System.out.println(a);
                                }
                            }
                            break;
                    }
                    break;

                case 0:
                    System.out.println("👋 Merci d'avoir utilisé notre programme.");
                    break;

                default:
                    System.out.println("❌ Option invalide. Veuillez réessayer.");
            }

        } while (choice != 0);
    }
}
