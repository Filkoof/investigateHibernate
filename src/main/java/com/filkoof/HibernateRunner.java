package com.filkoof;

import com.filkoof.entity.User;
import com.filkoof.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.enableFetchProfile("withCompanyAndPayment");

            User user = session.get(User.class, 1L);
            System.out.println(user.getPayments().size());
            System.out.println(user.getCompany().getName());

//            List<User> users = session.createQuery(
//                    """
//                    SELECT u FROM User u
//                    JOIN FETCH u.payments
//                    JOIN FETCH u.company
//                    WHERE 1 = 1
//                    """, User.class)
//                    .list();
//            users.forEach(user -> System.out.println(user.getPayments().size()));
//            users.forEach(user -> System.out.println(user.getCompany().getName()));

            session.getTransaction().commit();
        }
    }
}
