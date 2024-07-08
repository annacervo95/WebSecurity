package com.example.progettoSS.repository;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Repository;
import com.example.progettoSS.entity.User;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.mindrot.jbcrypt.BCrypt;
import java.nio.charset.StandardCharsets;




/**************************STATEMENT NON PREPARED********************************/

@Repository
public class UserR {
   
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User findUser(String email, String password) {
        final String query = "SELECT * FROM `user` WHERE `email` LIKE '" + email + "' AND `password` LIKE '" + password + "'";
        System.out.println(query);

        return jdbcTemplate.query(query, resultSet -> {
                System.out.println(resultSet);
            if (resultSet.next()) {
                return new User(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password"),
                        resultSet.getString("cpassword"),
                        resultSet.getString("role")
                );
            }
            return null;
        });
    }

    public void registerUser(User user) {
        String id = UUID.randomUUID().toString();
        final String query = "INSERT INTO `user` (`id`,`cpassword`, `email`, `name`, `password`, `phone`, `surname`, `role`) VALUES ('" + id + "','" + user.getCpassword() + "','" + user.getEmail() + "','" + user.getName() + "','" + user.getPassword() + "','" + user.getPhone() + "','" + user.getSurname() + "','" + user.getRole() + "')";
        System.out.println(query);

        jdbcTemplate.update(query);
    }

    public User findUserById(String id) {
        final String query = "SELECT * FROM `user` WHERE `id` LIKE '" + id + "'";
        System.out.println(query);

        return jdbcTemplate.query(query, resultSet -> {
            if (resultSet.next()) {
                return new User(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password"),
                        resultSet.getString("cpassword"),
                        resultSet.getString("role")
                );
            }
            return null;
        });
    }
}


/**************************PREPARED STATEMENT********************************/
/*
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Repository
public class UserR {
  
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User findUser(String email, String password) {
        final String query = "SELECT * FROM `user` WHERE `email` = ?";
        
        return jdbcTemplate.query(query, new Object[]{email}, resultSet -> {
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");
                System.out.println(hashedPassword);
                if (verifyPassword(password, hashedPassword)) {
                    return new User(
                            resultSet.getString("id"),
                            resultSet.getString("name"),
                            resultSet.getString("surname"),
                            resultSet.getString("email"),
                            resultSet.getString("phone"),
                            hashedPassword,
                            resultSet.getString("cpassword"),
                            resultSet.getString("role")
                    );
                }
            }
            return null;
        });
    }

    public void registerUser(User user) {
        String id = UUID.randomUUID().toString();
        String hashedPassword = hashPassword(user.getPassword());
        String hashedCPassword = hashPassword(user.getCpassword());

        final String query = "INSERT INTO `user` (`id`, `cpassword`, `email`, `name`, `password`, `phone`, `surname`, `role`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(query, id, hashedCPassword, user.getEmail(), user.getName(), hashedPassword, user.getPhone(), user.getSurname(), user.getRole());
    }

    public User findUserById(String id) {
        final String query = "SELECT * FROM `user` WHERE `id` = ?";

        return jdbcTemplate.query(query, new Object[]{id}, resultSet -> {
            if (resultSet.next()) {
                return new User(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password"),
                        resultSet.getString("cpassword"),
                        resultSet.getString("role")
                );
            }
            return null;
        });
    }

    private boolean verifyPassword(String password, String hashedPassword) {
        String hashedInputPassword = hashPassword(password);
        System.out.println(hashedInputPassword);
        System.out.println(hashedPassword);
        return hashedInputPassword.equals(hashedPassword);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            //SPOTBUS:
            //  byte[] hashedBytes = md.digest(password.getBytes());
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Gestisce l'errore di algoritmo di hashing non disponibile
            e.printStackTrace();
        }

        return null;
    }
}
*/
