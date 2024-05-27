package com.project.EmpSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.EmpSystem.entity.Employee;

public interface EmpRepo extends JpaRepository<Employee, Integer>{
   
}
