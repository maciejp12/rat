package com.maciejp.rat.offer;

import com.maciejp.rat.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class OfferDao {

    private final UserService userService;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public OfferDao(UserService userService, JdbcTemplate jdbcTemplate) {
        this.userService = userService;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Offer> selectAllOffers() {
        String sql = "" +
                "SELECT offer_id, title, description, price, creator, creation_date " +
                "FROM offer";

        return jdbcTemplate.query(
                sql,
                mapOffer()
        );
    }

    public Offer selectOfferById(long id) {
        String sql = "" +
                "SELECT offer_id, title, description, price, creator, creation_date " +
                "FROM offer " +
                "WHERE offer_id = ?";

        Offer offer;
        try {
            offer = jdbcTemplate.queryForObject(
                    sql,
                    mapOffer(),
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

        return offer;
    }

    public Boolean selectIdExists(long id) {
        String sql = "" +
                "SELECT EXISTS(" +
                "SELECT offer_id " +
                "FROM offer " +
                "WHERE offer_id = ?)";

        return jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                id
        );
    }

    public List<Offer> selectOfferByCreator(String username) {
        String sql = "" +
                "SELECT offer_id, title, description, price, creator, creation_date " +
                "FROM offer JOIN user_profile ON creator = user_id " +
                "WHERE username = ? ";

        return jdbcTemplate.query(
                sql,
                mapOffer(),
                username
        );
    }

    public Integer selectOfferVisitCount(long id) {
        String sql = "" +
                "SELECT COUNT(*) AS count " +
                "FROM offer_visit " +
                "WHERE visited_offer = ?";

        return jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                id
        );
    }

    public long insertOffer(Offer offer, long creatorId) {
        String sql = "" +
                "INSERT INTO offer(offer_id, title, description, price, creator, creation_date) " +
                "VALUES (DEFAULT, ?, ?, ?, ?, now())";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int add = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, offer.getTitle());
                statement.setString(2, offer.getDescription());
                statement.setFloat(3, offer.getPrice());
                statement.setLong(4, creatorId);
                return statement;
            }
        }, keyHolder);

        long id = (long) keyHolder.getKeys().get("offer_id");
        return id;
    }

    public int deleteOfferById(long id) {
        String sql = "" +
                "DELETE FROM offer " +
                "WHERE offer_id = ?";

        return jdbcTemplate.update(
                sql,
                id
        );
    }

    public int updateOfferTitleById(long id, String title) {
        String sql = "" +
                "UPDATE offer " +
                "SET title = ? " +
                "WHERE offer_id = ?";

        return jdbcTemplate.update(
                sql,
                title,
                id
        );
    }

    public int updateOfferDescriptionById(long id, String description) {
        String sql = "" +
                "UPDATE offer " +
                "SET description = ? " +
                "WHERE offer_id = ?";

        return jdbcTemplate.update(
                sql,
                description,
                id
        );
    }

    public int updateOfferPriceById(long id, Float price) {
        String sql = "" +
                "UPDATE offer " +
                "SET price = ? " +
                "WHERE offer_id = ?";

        return jdbcTemplate.update(
                sql,
                price,
                id
        );
    }

    private RowMapper<Offer> mapOffer() {
        return (resultSet, i) -> {
            long id = resultSet.getLong("offer_id");
            String title = resultSet.getString("title");
            String description = resultSet.getString("description");
            Float price = resultSet.getFloat("price");
            String creator = userService.getUserById(resultSet.getLong("creator")).getUsername();
            Timestamp creationDate = resultSet.getTimestamp("creation_date");

            return new Offer(id, title,  description, price, creator, creationDate);
        };
    }

}
