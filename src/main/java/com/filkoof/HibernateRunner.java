package com.filkoof;

import com.filkoof.entity.Company;
import com.filkoof.entity.User;
import com.filkoof.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) {

        Company company = Company.builder()
                .name("Amazon")
                .build();

        User user = null;


        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            try (session) {
                session.beginTransaction();

                session.save(user);

                session.getTransaction().commit();
            }
        }
    }
}
