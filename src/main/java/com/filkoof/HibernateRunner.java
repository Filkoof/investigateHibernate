package com.filkoof;

import com.filkoof.convertet.BirthdayConverter;
import com.filkoof.entity.Birthday;
import com.filkoof.entity.Role;
import com.filkoof.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

public class HibernateRunner {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
//        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAttributeConverter(new BirthdayConverter());
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = User.builder()
                    .username("petya1@gmail.com")
                    .firstname("Petya")
                    .lastname("Petrov")
                    .birthDate(new Birthday(LocalDate.of(2000, 1, 1)))
                    .role(Role.ADMIN)
                    .build();

            session.save(user);

            session.getTransaction().commit();
        }
    }
}
