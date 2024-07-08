package com.example.progettoSS.repository;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Repository;
import com.example.progettoSS.entity.Plant;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import java.util.UUID;
import java.io.IOException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.PreparedStatement;

import java.sql.Blob;
import java.nio.charset.StandardCharsets;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;


@Repository
public class SessionR {

    @Autowired
    private JdbcTemplate jdbcTemplate;

 public String getSessionIdByUserId(String userId) {
    String sql = "SELECT SESSION_PRIMARY_ID, ATTRIBUTE_BYTES FROM SPRING_SESSION_ATTRIBUTES WHERE ATTRIBUTE_NAME = 'id' AND ATTRIBUTE_BYTES = (SELECT ATTRIBUTE_BYTES FROM SPRING_SESSION_ATTRIBUTES WHERE ATTRIBUTE_NAME = 'userid' AND ATTRIBUTE_BYTES = ?)";
    
    return jdbcTemplate.query(sql, new ResultSetExtractor<String>() {
        @Override
        public String extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (rs.next()) {
                String sessionId = rs.getString("SESSION_PRIMARY_ID");
                return sessionId;
            } else {
                return null;
            }
        }
    }, userId);
}

    public void deleteSessionDataFromDatabase(String sessionId) {
        // Esegui la query per eliminare i dati della sessione dal database
        String sql = "DELETE FROM SPRING_SESSION WHERE session_id = ?";
        jdbcTemplate.update(sql, sessionId);
        System.out.println("Dati della sessione eliminati dal database.");
    }
}
