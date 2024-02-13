package models;


import jdbc.Program;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

public class CourseFabrica {
    private static final Scanner scanner = new Scanner(System.in);

    public static Course create() {
        System.out.print("Введите название курса: ");
        String title = scanner.nextLine();
        System.out.print("Введите длительность курса: ");
        try {
            int duration = scanner.nextInt();
            scanner.nextLine();
            return new Course(title, duration);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static Course update(HashMap<Integer, Course> hashMap) {
        System.out.print("Введите ID курса:");
        Course course;
        try {
            int id = Integer.parseInt(scanner.nextLine());
            course = hashMap.get(id);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        System.out.print("Введите название курса: ");
        String title = scanner.nextLine();
        System.out.print("Введите длительность курса: ");
        try {
            int duration = Integer.parseInt(scanner.nextLine());
            course.setTitle(title);
            course.setDuration(duration);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return course;
    }
}
