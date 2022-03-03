package com.gngsn.demo;

import com.gngsn.demo.utils.JdbcProperties;
import com.gngsn.demo.utils.RandomString;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BulkInsertTest {

    private JdbcProperties jdbcProperties;

    public void batchInsert(String table) {
        String sql = "INSERT INTO " + table + " VALUES(?, ?, ?)";

        try {
            Connection conn = DriverManager.getConnection(
                    jdbcProperties.getUrl(),
                    jdbcProperties.getUsername(),
                    jdbcProperties.getPassword()
            );
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(sql);

            for (int i=0; i< 10000; i++) {
                pstmt.setString(1, "ninkname" + i);
                pstmt.setString(2, "gngsn" + i + "@email.com");
                pstmt.setString(2, new RandomString().nextString());

                pstmt.addBatch();
                pstmt.clearParameters();
            }


        } catch (SQLException se) {
            throw new RuntimeException("DB Connection Error");
        }
    }
}
