package esprit.tn.controllers;

import esprit.tn.entities.Cours;

public class SharedDataModel {

    private static Cours currentCourse;

    public static Cours getCurrentCourse() {
        return currentCourse;
    }

    public static void setCurrentCourse(Cours course) {
        currentCourse = course;
    }
}

