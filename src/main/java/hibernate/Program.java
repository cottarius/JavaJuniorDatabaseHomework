package hibernate;

import models.Course;
import models.CourseFabrica;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Program {
    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Course.class)
                .buildSessionFactory();

        Session session = sessionFactory.getCurrentSession();

        try {
            session.beginTransaction();

            Course course = CourseFabrica.create();
            session.save(course);

            Course retrieveCourse = session.get(Course.class, course.getId());

            retrieveCourse.setTitle("перемещение во времени");
            retrieveCourse.setDuration(50);
            session.update(retrieveCourse);

            // session.delete(retrieveCourse);

            session.getTransaction().commit();
        } finally {
            sessionFactory.close();
        }
    }
}
