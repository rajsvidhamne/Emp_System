package com.project.EmpSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.EmpSystem.entity.Employee;
import com.project.EmpSystem.service.EmpService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private EmpService empService;

	@GetMapping("/")
	public String index(Model m) {
		List<Employee> list = empService.getAllEmp();
		m.addAttribute("empList", list);
		return "index";
	}

	@GetMapping("/loadEmpSave")
	public String loadEmpSave() {
		return "emp_save";
	}

	@GetMapping("/EditEmp/{id}")
	public String EditEmp(@PathVariable int id, Model m) {
		// System.out.println(id);
		Employee emp = empService.getEmpById(id);
		m.addAttribute("emp", emp);
		return "edit_emp";
	}

	@GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = empService.getAllEmp();
        return ResponseEntity.ok().body(employees);
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable int id) {
        Employee employee = empService.getEmpById(id);
        if (employee != null) {
            return ResponseEntity.ok().body(employee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
	@PostMapping("/saveEmp")
	public String saveEmp(@ModelAttribute Employee emp, HttpSession session) {
		// System.out.println(emp);

		Employee newEmp = empService.saveEmp(emp);

		if (newEmp != null) {
			// System.out.println("save success");
			session.setAttribute("msg", "Register sucessfully");
		} else {
			// System.out.println("something wrong on server");
			session.setAttribute("msg", "something wrong on server");
		}

		return "redirect:/loadEmpSave";
	}

	@PostMapping(value = "/saveEmp", consumes = "application/json")
    public ResponseEntity<String> saveEmpJson(@RequestBody Employee emp, HttpSession session) {
        Employee newEmp = empService.saveEmp(emp);

        if (newEmp != null) {
            session.setAttribute("msg", "Register successfully");
            return new ResponseEntity<>("Employee added successfully", HttpStatus.CREATED);
        } else {
            session.setAttribute("msg", "Something went wrong on server");
            return new ResponseEntity<>("Failed to add employee", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



	@PutMapping("/updateEmp/{id}")
    public ResponseEntity<String> updateEmpJson(@PathVariable int id, @RequestBody Employee emp) {
        emp.setId(id); // Set the ID of the employee to be updated
        Employee updatedEmp = empService.saveEmp(emp);
        if (updatedEmp != null) {
            return ResponseEntity.status(HttpStatus.OK).body("Employee updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update employee");
        }
    }

	@PostMapping("/updateEmpDtls")
	public String updateEmp(@ModelAttribute Employee emp, HttpSession session) {
		// System.out.println(emp);

		Employee updateEmp = empService.saveEmp(emp);

		if (updateEmp != null) {
			// System.out.println("save success");
			session.setAttribute("msg", "Update sucessfully");
		} else {
			// System.out.println("something wrong on server");
			session.setAttribute("msg", "something wrong on server");
		}

		return "redirect:/";
	}

	@DeleteMapping("/deleteEmp/{id}")
    public ResponseEntity<String> deleteEmpJson(@PathVariable int id, HttpSession session) {
        boolean deleted = empService.deleteEmp(id);
        if (deleted) {
            session.setAttribute("msg", "Employee deleted successfully");
            return ResponseEntity.status(HttpStatus.OK).body("Employee deleted successfully");
        } else {
            session.setAttribute("msg", "Failed to delete employee");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete employee");
        }
    }

	@GetMapping("/deleteEmp/{id}")
	public String loadEmpSave(@PathVariable int id, HttpSession session) {
		boolean f = empService.deleteEmp(id);
		if (f) {
			session.setAttribute("msg", "Delete sucessfully");
		} else {
			session.setAttribute("msg", "something wrong on server");
		}
		return "redirect:/";
	}

}
