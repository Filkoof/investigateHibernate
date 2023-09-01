package com.filkoof;

import com.filkoof.entity.Birthday;
import com.filkoof.entity.Company;
import com.filkoof.entity.PersonalInfo;
import com.filkoof.entity.User;
import com.filkoof.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) {

        Company company = Company.builder()
                .name("Amazon")
                .build();

        User user = User.builder()
                .username("ivan@gmail.com")
                .personalInfo(PersonalInfo.builder()
                        .firstname("Ivan")
                        .lastname("Ivanov")
                        .birthDate(new Birthday(LocalDate.of(2000, 1, 1)))
                        .build())
                .company(company)
                .build();


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
