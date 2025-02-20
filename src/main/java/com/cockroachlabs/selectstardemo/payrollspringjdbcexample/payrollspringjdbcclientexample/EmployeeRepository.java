package com.cockroachlabs.selectstardemo.payrollspringjdbcexample.payrollspringjdbcclientexample;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeRepository {

	private final JdbcClient jdbcClient;

	public EmployeeRepository(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	public void save(Employee employee) {

		if (employee.getId() == null) {

			Employee employeeToSave = new Employee(employee.getName(), employee.getRole());
			employeeToSave.setId(UUID.randomUUID());
			jdbcClient //
					.sql("""
							INSERT INTO employee (id, name, role)
							VALUES (:id, :name, :role)
							""") //
					.paramSource(new BeanPropertySqlParameterSource(employeeToSave)) //
					.update();

		} else {

			jdbcClient //
					.sql("""
							UPDATE employee
							SET name = :name, role = :role
							WHERE id = :id
							""") //
					.paramSource(new BeanPropertySqlParameterSource(employee)) //
					.update();
		}
	}

	List<Employee> findAll() {

		return jdbcClient.sql("""
						SELECT *
						FROM employee
						""") //
				.query(Employee.class) //
				.list();
	}

	List<Employee> findByRole(String role) {

		return jdbcClient //
				.sql("""
						SELECT *
						FROM employee
						WHERE role = :role
						""") //
				.param("role", role) //
				.query(Employee.class) //
				.list();
	}

	List<Employee> findByRoleWithFollowerRead(String role) {

		return jdbcClient //
				.sql("""
						SELECT *
						FROM employee
						AS OF SYSTEM TIME '-5s'
						WHERE role = :role
						""") //
				.param("role", role) //
				.query(Employee.class) //
				.list();
	}
}
