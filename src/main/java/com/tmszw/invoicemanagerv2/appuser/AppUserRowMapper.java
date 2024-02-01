package com.tmszw.invoicemanagerv2.appuser;

import com.tmszw.invoicemanagerv2.company.Company;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@Component
public class AppUserRowMapper implements RowMapper<AppUser> {
    @Override
    public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new AppUser(
                rs.getString("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getBoolean("isEnabled")
        );
    }
}