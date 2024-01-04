package com.tmszw.invoicemanagerv2.appuser;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("jdbc")
@RequiredArgsConstructor
public class AppUserJDBCDataAccessService implements AppUserDao {

    private final JdbcTemplate jdbcTemplate;
    private final AppUserRowMapper rowMapper;

    @Override
    public Optional<AppUser> selectAppUserById(Integer appUserId) {

        var sql = """
                SELECT id, username, email, password
                FROM app_user
                WHERE id = ?
                """;
        return jdbcTemplate.query(sql, rowMapper, appUserId)
                .stream()
                .findFirst();
    }

    @Override
    public void insertAppUser(AppUser appUser) {
        var sql = """
                INSERT INTO app_user (username, email, password)
                VALUES (?, ?, ?)
                """;

        int result = jdbcTemplate.update
                (
                        sql,
                        appUser.getUsername(),
                        appUser.getEmail(),
                        appUser.getPassword()

                );
        System.out.println("insertAppUser result = " + result);
    }

    @Override
    public boolean existsAppUserWithEmail(String email) {
        var sql = """
                SELECT count(id)
                FROM app_user
                WHERE email = ?;
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsAppUserById(Integer id) {
        var sql = """
                SELECT count(id)
                FROM app_user
                WHERE id = ?;
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void deleteAppUserById(Integer appUserId) {
        var sql = """
                DELETE
                FROM app_user
                WHERE id = ?;
                """;
        jdbcTemplate.update(sql, appUserId);
    }

    @Override
    public void updateAppUser(AppUser update) {

        if(update.getUsername() != null) {
            var sql = """
                    UPDATE app_user
                    SET username = ?
                    WHERE id = ?;
                    """;
            int result = jdbcTemplate.update(sql, update.getUsername(), update.getId());
            System.out.println("updateAppUser result = " + result);
        }
        if(update.getPassword() != null) {
            var sql = """
                    UPDATE app_user
                    SET password = ?
                    WHERE id = ?;
                    """;
            int result = jdbcTemplate.update(sql, update.getPassword(), update.getId());
            System.out.println("updateAppUser result = " + result);
        }
        if(update.getEmail() != null) {
            var sql = """
                    UPDATE app_user
                    SET email = ?
                    WHERE id = ?;
                    """;
            int result = jdbcTemplate.update(sql, update.getEmail(), update.getId());
            System.out.println("updateAppUser result = " + result);
        }
    }

    @Override
    public Optional<AppUser> selectAppUserByEmail(String email) {

        var sql = """
                SELECT id, username, email, password
                FROM app_user
                WHERE email = ?;
                """;
        return jdbcTemplate.query(sql, rowMapper, email)
                .stream()
                .findFirst();
    }
}