package com.filkoof;

import com.filkoof.entity.PersonalInfo;
import com.filkoof.entity.User;
import com.filkoof.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) {

        User user = User.builder()
                .username("petr@gmail.com")
                .personalInfo(PersonalInfo.builder()
                        .firstname("Petr")
                        .lastname("Petrov")
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
        } catch (Exception exception) {
            log.error("Exception occurred", exception);
            throw exception;
        }
    }
}
