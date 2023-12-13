package com.example.jdbccallbackdemo.service;

import com.example.jdbccallbackdemo.dao.EmployeeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeDao employeeDao;

    public void doAction(){
        System.out.println("Employee count::" +
                employeeDao.countEmployee());
        System.out.println("FindAllEmployee");
        employeeDao.findAllEmployee()
                .forEach(System.out::println);
        System.out.println("Average with RowCallBackHandler::");
        System.out.println(employeeDao.averageWithRowCallBack());
        System.out.println("Average With ResultSetExtractor::");
        System.out.println(
                String.format("Average: %.3f",employeeDao.averageWithResultSetExtractor())

        );
        System.out.println("Find Employee By Email:");
        System.out.println(employeeDao.findEmployeeByEmail("charles@gmail.com"));
    }
}
