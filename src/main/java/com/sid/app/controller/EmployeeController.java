package com.sid.app.controller;

import com.sid.app.constants.AppConstants;
import com.sid.app.model.Employee;
import com.sid.app.service.EmployeeService;
import com.sid.app.utils.ApplicationUtils;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Employee Controller for managing CRUD operations.
 *
 * <p>Provides REST endpoints for creating, retrieving, updating, and deleting Employee records.
 * Uses EmployeeService for business logic and ApplicationUtils for JSON logging.
 *
 * <p>Endpoints: - POST /employee: Create or update an employee - GET /employee/{id}: Retrieve
 * employee by ID - GET /employee: Retrieve all employees - PUT /employee/{id}: Update employee by
 * ID - DELETE /employee/{id}: Delete employee by ID
 *
 * @author Siddhant
 */
@RestController
@Slf4j
public class EmployeeController {

  /** Service for employee business logic. */
  @Autowired private EmployeeService employeeService;

  /**
   * Create or Update Employee.
   *
   * @param request Employee object from request body
   * @return ResponseEntity containing the created/updated Employee
   */
  @PostMapping(value = AppConstants.EMPLOYEE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Employee> createEmployee(@RequestBody Employee request) {
    log.info("createEmployee() : START");
    log.debug("createEmployee() : Request -> {}", ApplicationUtils.getJSONString(request));

    Employee employeeResponse = employeeService.saveEmployee(request);

    log.info("createEmployee() : Response -> {}", ApplicationUtils.getJSONString(employeeResponse));
    log.info("createEmployee() : END");
    return ResponseEntity.ok(employeeResponse);
  }

  /**
   * Get Employee by ID.
   *
   * @param id Employee ID from path variable
   * @return ResponseEntity containing the Employee if found, otherwise 404
   */
  @GetMapping(
      value = AppConstants.EMPLOYEE_ENDPOINT + "/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
    log.info("getEmployeeById() : START | ID -> {}", id);

    Optional<Employee> employee = employeeService.getEmployeeById(id);
    if (employee.isPresent()) {
      log.info(
          "getEmployeeById() : Response -> {}", ApplicationUtils.getJSONString(employee.get()));
      log.info("getEmployeeById() : END");
      return ResponseEntity.ok(employee.get());
    } else {
      log.warn("getEmployeeById() : Employee not found for ID {}", id);
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Get All Employees.
   *
   * @return ResponseEntity containing a list of all Employees
   */
  @GetMapping(value = AppConstants.EMPLOYEE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Employee>> getAllEmployees() {
    log.info("getAllEmployees() : START");

    List<Employee> employees = employeeService.getAllEmployees();

    log.debug("getAllEmployees() : Response Size -> {}", employees.size());
    log.info("getAllEmployees() : END");
    return ResponseEntity.ok(employees);
  }

  /**
   * Update Employee by ID.
   *
   * @param request Employee object from request body
   * @param id Employee ID from path variable
   * @return ResponseEntity containing the updated Employee if found, otherwise 404
   */
  @PutMapping(
      value = AppConstants.EMPLOYEE_ENDPOINT + "/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Employee> updateEmployee(
      @RequestBody Employee request, @PathVariable String id) {
    log.info("updateEmployee() : START | ID -> {}", id);
    log.debug("updateEmployee() : Request -> {}", ApplicationUtils.getJSONString(request));

    Optional<Employee> existingEmployee = employeeService.getEmployeeById(id);
    if (existingEmployee.isPresent()) {
      Employee updatedEmployee = existingEmployee.get();
      updatedEmployee.setName(request.getName());
      updatedEmployee.setDepartment(request.getDepartment());
      updatedEmployee.setSalary(request.getSalary());

      Employee savedEmployee = employeeService.saveEmployee(updatedEmployee);

      log.info("updateEmployee() : Response -> {}", ApplicationUtils.getJSONString(savedEmployee));
      log.info("updateEmployee() : END");
      return ResponseEntity.ok(savedEmployee);
    } else {
      log.warn("updateEmployee() : Employee not found for ID {}", id);
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Delete Employee by ID.
   *
   * @param id Employee ID from path variable
   * @return ResponseEntity with success message if deleted, otherwise 404
   */
  @DeleteMapping(
      value = AppConstants.EMPLOYEE_ENDPOINT + "/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> deleteEmployee(@PathVariable String id) {
    log.info("deleteEmployee() : START | ID -> {}", id);

    Optional<Employee> employee = employeeService.getEmployeeById(id);
    if (employee.isPresent()) {
      employeeService.deleteEmployee(id);
      log.info("deleteEmployee() : Employee deleted successfully for ID {}", id);
      log.info("deleteEmployee() : END");
      return ResponseEntity.ok("Employee deleted successfully");
    } else {
      log.warn("deleteEmployee() : Employee not found for ID {}", id);
      return ResponseEntity.notFound().build();
    }
  }
}
