package com.filkoof;

import com.filkoof.entity.User;
import com.filkoof.entity.UserChat;
import com.filkoof.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.graph.SubGraph;

import java.util.List;
import java.util.Map;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
//            session.enableFetchProfile("withCompanyAndPayment");

            RootGraph<User> userGraph = session.createEntityGraph(User.class);
            userGraph.addAttributeNodes("company", "userChats");
            SubGraph<UserChat> userChatSubGraph = userGraph.addSubgraph("userChats", UserChat.class);
            userChatSubGraph.addAttributeNodes("chat");

            Map<String, Object> properties = Map.of(
                    GraphSemantic.LOAD.getJpaHintName(), userGraph
            );
            User user = session.find(User.class, 1L, properties);
            System.out.println(user.getCompany().getName());
            System.out.println(user.getUserChats().size());
//            System.out.println(user.getPayments().size());


            List<User> users = session.createQuery(
                            """
                                    SELECT u FROM User u
                                    WHERE 1 = 1
                                    """, User.class)
//                    .setHint(GraphSemantic.LOAD.getJpaHintName(), session.getEntityGraph("WithCompanyAndChat"))
                    .setHint(GraphSemantic.LOAD.getJpaHintName(), userGraph)
                    .list();
            users.forEach(it -> System.out.println(it.getUserChats().size()));
            users.forEach(it -> System.out.println(it.getCompany().getName()));

            session.getTransaction().commit();
        }
    }
}
