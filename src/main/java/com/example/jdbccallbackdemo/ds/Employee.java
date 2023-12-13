package com.example.jdbccallbackdemo.ds;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.sql.Date;
@AllArgsConstructor
@ToString
public class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Date hiredDate;
    private double salary;
}
