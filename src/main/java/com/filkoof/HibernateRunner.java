package com.filkoof;

import com.filkoof.entity.Birthday;
import com.filkoof.entity.PersonalInfo;
import com.filkoof.entity.User;
import com.filkoof.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDate;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) {

        User user = User.builder()
                .username("petr@gmail.com")
                .personalInfo(PersonalInfo.builder()
                        .firstname("Petr")
                        .lastname("Petrov")
                        .birthDate(new Birthday(LocalDate.of(2000, 1, 1)))
                        .build())
                .build();

        log.info("User entity is in transient state, object {}", user);

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            try (session) {
                Transaction transaction = session.beginTransaction();
                log.trace("Transaction is created, {}", transaction);

                session.saveOrUpdate(user);
                log.trace("User is in persistent state: {}, session {}", user, session);

                session.getTransaction().commit();
            }
            log.warn("User is in detached state: {}, session is closed {}", user, session);
            try (Session session1 = sessionFactory.openSession()) {
                PersonalInfo key = PersonalInfo.builder()
                        .firstname("Petr")
                        .lastname("Petrov")
                        .birthDate(new Birthday(LocalDate.of(2000, 1, 1)))
                        .build();

                User user2 = session1.get(User.class, key);
                System.out.println(user2);
            }
        } catch (Exception exception) {
            log.error("Exception occurred", exception);
            throw exception;
        }
    }
}
