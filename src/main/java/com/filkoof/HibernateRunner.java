package com.filkoof;

import com.filkoof.convertet.BirthdayConverter;
import com.filkoof.entity.User;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateRunner {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
//        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAttributeConverter(new BirthdayConverter());
        configuration.registerTypeOverride(new JsonBinaryType());
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

//            User user = User.builder()
//                    .username("petya@gmail.com")
//                    .firstname("Petya")
//                    .lastname("Petrov")
//                    .birthDate(new Birthday(LocalDate.of(2000, 1, 1)))
//                    .info("""
//                            {
//                                "name": "Ivan",
//                                "id": 25
//                            }
//                            """)
//                    .role(Role.ADMIN)
//                    .build();

            User user = session.get(User.class, "petya@gmail.com");

            session.getTransaction().commit();
        }
    }
}
