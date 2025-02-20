package com.cockroachlabs.selectstardemo.payrollspringjdbcexample.payrollspringjdbcclientexample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiController {

	private final EmployeeRepository repository;

	public ApiController(EmployeeRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/api/employees")
	List<Employee> findAll() {
		return repository.findAll();
	}

	@GetMapping("/api/employees/{role}")
	List<Employee> findByRole(@PathVariable String role) {
		return repository.findByRole(role);
	}

	@GetMapping("/api/employees/followers/{role}")
	List<Employee> findByRoleWithfollowReads(@PathVariable String role) {
		return repository.findByRoleWithFollowerRead(role);
	}
}
