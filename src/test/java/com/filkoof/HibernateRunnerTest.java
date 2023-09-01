package com.filkoof;

import com.filkoof.entity.Company;
import com.filkoof.entity.User;
import com.filkoof.util.HibernateUtil;
import lombok.Cleanup;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

class HibernateRunnerTest {

    @Test
    void oneToMany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();

        session.beginTransaction();

        session.get(Company.class, 1);
        System.out.println();

        session.getTransaction().commit();
    }

    @Test
    void checkGetReflectionApi() throws SQLException, InvocationTargetException, InstantiationException,
            IllegalAccessException, NoSuchFieldException, NoSuchMethodException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = preparedStatement.executeQuery();

        Class<User> userClass = User.class;
        Constructor<User> constructor = userClass.getConstructor();
        User user = constructor.newInstance();

        Field usernameField = userClass.getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(user, resultSet.getString("username"));
        usernameField.set(user, resultSet.getString("firstname"));
        usernameField.set(user, resultSet.getString("lastname"));
    }

    @Test
    void checkReflectionApi() throws SQLException, IllegalAccessException {
        User user = User.builder()
                .build();

        String sql = """
                INSERT
                INTO
                %s
                (%s)
                VALUES
                (%s)
                """;

        String tableName = ofNullable(user.getClass().getAnnotation(Table.class))
                .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
                .orElse(user.getClass().getName());

        Field[] declaredFields = user.getClass().getDeclaredFields();

        String columnNames = Arrays.stream(declaredFields)
                .map(field -> ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName()))
                .collect(joining(", "));

        String columnValues = Arrays.stream(declaredFields)
                .map(field -> "?")
                .collect(joining(", "));

        System.out.printf((sql) + "%n", tableName, columnNames, columnValues);

        Connection connection = null;
        PreparedStatement preparedStatement = connection.prepareStatement(sql.formatted(tableName, columnNames, columnValues));

        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            preparedStatement.setObject(1, declaredField.get(user));
        }
    }

}