package com.example.progettoSS.repository;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Repository;
import com.example.progettoSS.entity.Booking;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import java.util.UUID;
import java.io.IOException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;



/**************************STATEMENT NON PREPARED********************************/

@Repository
public class BookingR {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addBooking(String idUser, String namePlant, String quantity) {
        String id = UUID.randomUUID().toString();
        final String query = "INSERT INTO `booking` (`id`, `namePlant`, `idUser`, `quantity`) VALUES ('" + id + "', '" + namePlant + "', '" + idUser + "', '" + quantity + "')";
        jdbcTemplate.execute(query);
    }

    public List<Booking> getAllBookings() {
        final String query = "SELECT * FROM `booking`";
        return jdbcTemplate.query(query, (resultSet, rowNum) ->
                new Booking(resultSet.getString("id"), resultSet.getString("namePlant"),
                        resultSet.getString("idUser"), resultSet.getString("quantity"))
        );
    }

    public List<Booking> getUserBookings(String id) {
        final String query = "SELECT * FROM `booking` WHERE `idUser` LIKE '" + id + "'";
        return jdbcTemplate.query(query, (resultSet, rowNum) ->
                new Booking(resultSet.getString("id"), resultSet.getString("namePlant"),
                        resultSet.getString("idUser"), resultSet.getString("quantity"))
        );
    }

    public int deleteBooking(String id) {
        final String query = "DELETE FROM `booking` WHERE `id` LIKE '" + id + "'";
        return jdbcTemplate.update(query);
    }

    public int updateBookingQuantity(String id, String quantity) {
        final String query = "UPDATE `booking` SET `quantity` = '" + quantity + "' WHERE `id` LIKE '" + id + "'";
        return jdbcTemplate.update(query);
    }

    public Booking getBookingById(String id) {
        final String query = "SELECT * FROM `booking` WHERE `id` LIKE '" + id + "'";
        List<Booking> bookings = jdbcTemplate.query(query, (resultSet, rowNum) ->
                new Booking(resultSet.getString("id"), resultSet.getString("namePlant"),
                        resultSet.getString("idUser"), resultSet.getString("quantity"))
        );
        return bookings.isEmpty() ? null : bookings.get(0);
    }
}



/**************************STATEMENT  PREPARED********************************/
/*
@Repository
public class BookingR {

      @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addBooking(String idUser, String namePlant, String quantity) {
        String id = UUID.randomUUID().toString();
        final String query = "INSERT INTO `booking` (`id`, `namePlant`, `idUser`, `quantity`) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(query, id, namePlant, idUser, quantity);
    }

    public List<Booking> getAllBookings() {
        final String query = "SELECT * FROM `booking`";
        return jdbcTemplate.query(query, (resultSet, rowNum) ->
                new Booking(resultSet.getString("id"), resultSet.getString("namePlant"),
                        resultSet.getString("idUser"), resultSet.getString("quantity"))
        );
    }

    public List<Booking> getUserBookings(String id) {
        final String query = "SELECT * FROM `booking` WHERE `idUser` LIKE ?";
        return jdbcTemplate.query(query, (resultSet, rowNum) ->
                new Booking(resultSet.getString("id"), resultSet.getString("namePlant"),
                        resultSet.getString("idUser"), resultSet.getString("quantity")),
                id
        );
    }

    public int deleteBooking(String id) {
        final String query = "DELETE FROM `booking` WHERE `id` LIKE ?";
        return jdbcTemplate.update(query, id);
    }

    public int updateBookingQuantity(String id, String quantity) {
        final String query = "UPDATE `booking` SET `quantity` = ? WHERE `id` LIKE ?";
        return jdbcTemplate.update(query, quantity, id);
    }

    public Booking getBookingById(String id) {
        final String query = "SELECT * FROM `booking` WHERE `id` LIKE ?";
        return jdbcTemplate.queryForObject(query, (resultSet, rowNum) ->
                new Booking(resultSet.getString("id"), resultSet.getString("namePlant"),
                        resultSet.getString("idUser"), resultSet.getString("quantity")),
                id
        );
    }
}
*/