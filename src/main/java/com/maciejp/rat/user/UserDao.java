package com.maciejp.rat.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User selectUserById(long id) {
        String sql = "" +
                "SELECT user_id, username, email, password, phone_number, register_date " +
                "FROM user_profile " +
                "WHERE user_id = ?";

        User user;
        try {
            user = jdbcTemplate.queryForObject(
                    sql,
                    mapUser(),
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

        return user;
    }

    public User selectUserByUsername(String username) {
        String sql = "" +
                "SELECT user_id, username, email, password, phone_number, register_date " +
                "FROM user_profile " +
                "WHERE username = ?";

        User user;
        try {
            user = jdbcTemplate.queryForObject(
                    sql,
                    mapUser(),
                    username
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

        return user;
    }

    public Boolean selectUsernameExists(String username) {
        String sql = "" +
                "SELECT EXISTS(" +
                "SELECT user_id " +
                "FROM user_profile " +
                "WHERE username = ?)";

        return jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                username
        );
    }

    public Boolean selectEmailExists(String email) {
        String sql = "" +
                "SELECT EXISTS(" +
                "SELECT user_id " +
                "FROM user_profile " +
                "WHERE email = ?)";

        return jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                email
        );
    }

    public Boolean selectIdExists(long id) {
        String sql = "" +
                "SELECT EXISTS(" +
                "SELECT user_id " +
                "FROM user_profile " +
                "WHERE user_id = ?)";

        return jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                id
        );
    }

    public long insertUser(User user) {
        String sql = "" +
                "INSERT INTO user_profile(" +
                "user_id, " +
                "username, " +
                "email, " +
                "password, " +
                "phone_number, " +
                "register_date) " +
                "VALUES (" +
                "DEFAULT, " +
                "?, " +
                "?, " +
                "?, " +
                "?, " +
                "now())";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int add = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getEmail());
                statement.setString(3, user.getPassword());
                statement.setString(4, user.getPhoneNumber());
                return statement;
            }
        }, keyHolder);

        long id = (long) keyHolder.getKeys().get("user_id");
        return id;
    }

    public int deleteUserByUsername(String username) {
        String sql = "" +
                "DELETE FROM user_profile " +
                "WHERE username = ?";

        return jdbcTemplate.update(
                sql,
                username
        );
    }

    private RowMapper<User> mapUser() {
        return ((resultSet, i) -> {
            long id = resultSet.getLong("user_id");
            String username = resultSet.getString("username");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            String phoneNumber = resultSet.getString("phone_number");
            Timestamp registerDate = resultSet.getTimestamp("register_date");

            return new User(
                    id,
                    username,
                    email,
                    password,
                    phoneNumber,
                    registerDate
            );
        });
    }
}
