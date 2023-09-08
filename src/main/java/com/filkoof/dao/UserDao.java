package com.filkoof.dao;

import com.filkoof.entity.Payment;
import com.filkoof.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {

    private static final UserDao INSTANCE = new UserDao();

    /**
     * Return all employee
     */
    public List<User> findAll(Session session) {
//        return session.createQuery("SELECT u FROM User u", User.class).list(); - HQL
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);

        criteria.select(user);

        return session.createQuery(criteria).list();
    }

    /**
     * Return all employees with name
     */
    public List<User> findAllByFirstname(Session session, String firstname) {
        /*return session.createQuery(
                        """
                                SELECT u FROM User u
                                WHERE u.personalInfo.firstname = :firstname
                                """, User.class)
                .setParameter("firstname", firstname)
                .list(); - HQL*/
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);

        criteria.select(user).where(
                cb.equal(user.get("personalInfo").get("firstname"), firstname)
        );

        return session.createQuery(criteria).list();
    }

    /**
     * Return first {limit} employees, ordered by birthdate (Ascending)
     */
    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
       /* return session.createQuery("SELECT u FROM User u ORDER BY u.personalInfo.birthDate ", User.class)
                .setMaxResults(limit)
                .list(); - HQL*/

        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);

        criteria.select(user).orderBy(cb.asc(user.get("personalInfo").get("birthDate")));

        return session.createQuery(criteria)
                .setMaxResults(limit)
                .list();
    }

    /**
     * Return all employees by company name
     */
    public List<User> findAllByCompanyName(Session session, String companyName) {
        return session.createQuery(
                        """
                                SELECT u FROM Company c 
                                JOIN c.users u
                                WHERE c.name = :companyName
                                """, User.class)
                .setParameter("companyName", companyName)
                .list();
    }

    /**
     * Return all payments, received by employees of the company by the specified company name
     * ordered by employee name, and then by payment amount
     */
    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
        return session.createQuery(
                        """
                                SELECT p FROM Payment p
                                JOIN p.receiver u
                                JOIN u.company c
                                WHERE c.name = :companyName
                                ORDER BY u.personalInfo.firstname, p.amount
                                """, Payment.class)
                .setParameter("companyName", companyName)
                .list();
    }

    /**
     * Return average payment employee by name and lastname
     */
    public Double findAveragePaymentAmountByFirstAndLastNames(Session session, String firstname, String lastname) {
        return session.createQuery(
                        """
                                SELECT avg(p.amount) FROM Payment p
                                JOIN p.receiver u
                                WHERE u.personalInfo.firstname = :firstname AND u.personalInfo.lastname = :lastname
                                """, Double.class)
                .setParameter("firstname", firstname)
                .setParameter("lastname", lastname)
                .uniqueResult();
    }

    /**
     * Return for each company: name, average payments all employees and ordered by company name
     */
    public List<Object[]> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
        return session.createQuery(
                        """
                                SELECT c.name, avg(p.amount) FROM Company c
                                JOIN c.users u
                                JOIN u.payments p
                                GROUP BY c.name
                                ORDER BY c.name
                                """, Object[].class)
                .list();
    }

    /**
     * Return employees list (Object User), average payments amount, but only employee who have
     * bigger than average payment all employees
     * order by employee name
     */
    public List<Object[]> isItPossible(Session session) {
        return session.createQuery(
                        """
                                SELECT u, avg(p.amount) FROM User u
                                JOIN u.payments p
                                GROUP BY u
                                HAVING avg(p.amount) > (SELECT avg(p.amount) FROM Payment p)
                                ORDER BY u.personalInfo.firstname
                                """, Object[].class)
                .list();
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}
