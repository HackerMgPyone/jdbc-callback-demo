package com.example.jdbccallbackdemo.dao;

import com.example.jdbccallbackdemo.ds.Employee;
import lombok.SneakyThrows;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EmployeeDao {
    private JdbcTemplate jdbcTemplate;

    @SneakyThrows
    private Employee mapEmployee(ResultSet rs, int i){
        return new Employee(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone_number"),
                rs.getDate("hired_date"),
                rs.getDouble("salary")
        );
    }

    public double averageWithRowCallBack(){
        CustomRowCallBackHandler handler =
                new CustomRowCallBackHandler();
        this.jdbcTemplate
                .query("select salary from employee",
                       handler );

        return handler.average();
    }

    public double averageWithResultSetExtractor(){
        return jdbcTemplate
                .query("select salary from employee",
                        new CustomResultSetExtractor());
    }

    public Employee findEmployeeByEmail(String email){
        return jdbcTemplate
                .query(new PreparedStatementCreator() {
                           @Override
                           public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                               return con.prepareStatement("select * from employee where email = ?");
                           }
                       },
                        new PreparedStatementSetter() {

                            @Override
                            public void setValues(PreparedStatement ps) throws SQLException {

                                ps.setString(1, email);
                            }
                        },
                        new ResultSetExtractor<Employee>() {
                            @Override
                            public Employee extractData(ResultSet rs) throws SQLException, DataAccessException {
                                if (rs.next()){
                                    return new Employee(
                                            rs.getInt("id"),
                                            rs.getString("first_name"),
                                            rs.getString("last_name"),
                                            rs.getString("email"),
                                            rs.getString("phone_number"),
                                            rs.getDate("hired_date"),
                                            rs.getDouble("salary")
                                    );
                                }
                                return null;
                            }
                        }
                );
    }

    private class CustomResultSetExtractor implements ResultSetExtractor<Double>{

        @Override
        public Double extractData(ResultSet rs) throws SQLException, DataAccessException {
           double total = 0;
           int count = 0;

            while (rs.next()){
                total += rs.getDouble("salary");
                count++;

            }
            return total / (double) count;
        }
    }

    class CustomRowCallBackHandler implements RowCallbackHandler{

        double total;
        int count;


        @Override
        public void processRow(ResultSet rs) throws SQLException {

            total += rs.getDouble("salary");
            count++;
        }

        public double average(){
            return total / (double) count;
        }
    }

    public List<Employee> findAllEmployee(){
        return jdbcTemplate.query(
                "select * from employee",
                this::mapEmployee
                );
    }

    public EmployeeDao(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
    }

    public int countEmployee(){
        return jdbcTemplate.queryForObject("select count(id) from employee",
                Integer.class);
    }
}
