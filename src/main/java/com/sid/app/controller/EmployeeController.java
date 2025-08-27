package com.sid.app.controller;

import com.sid.app.constants.AppConstants;
import com.sid.app.model.Employee;
import com.sid.app.service.EmployeeService;
import com.sid.app.utils.ApplicationUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Employee Controller for managing CRUD operations with Redis backend. */
@RestController
@Slf4j
public class EmployeeController {

  @Autowired private EmployeeService employeeService;

  @PostMapping(value = AppConstants.EMPLOYEE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Employee> createEmployee(@RequestBody Employee request) {
    log.info("createEmployee() : START");
    log.debug("Request -> {}", ApplicationUtils.getJSONString(request));

    Employee employeeResponse = employeeService.saveEmployee(request);

    log.info("Response -> {}", ApplicationUtils.getJSONString(employeeResponse));
    log.info("createEmployee() : END");
    return ResponseEntity.ok(employeeResponse);
  }

  @GetMapping(
      value = AppConstants.EMPLOYEE_ENDPOINT + "/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
    log.info("getEmployeeById() : START | ID -> {}", id);

    Employee employee =
        employeeService.getEmployeeById(id); // throws UserNotFoundException if not found

    log.info("Response -> {}", ApplicationUtils.getJSONString(employee));
    log.info("getEmployeeById() : END");
    return ResponseEntity.ok(employee);
  }

  @GetMapping(value = AppConstants.EMPLOYEE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Employee>> getAllEmployees() {
    log.info("getAllEmployees() : START");

    List<Employee> employees = employeeService.getAllEmployees();

    log.debug("Response Size -> {}", employees.size());
    log.info("getAllEmployees() : END");
    return ResponseEntity.ok(employees);
  }

  @PutMapping(
      value = AppConstants.EMPLOYEE_ENDPOINT + "/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Employee> updateEmployee(
      @RequestBody Employee request, @PathVariable String id) {
    log.info("updateEmployee() : START | ID -> {}", id);
    log.debug("Request -> {}", ApplicationUtils.getJSONString(request));

    Employee existingEmployee =
        employeeService.getEmployeeById(id); // throws exception if not found
    existingEmployee.setName(request.getName());
    existingEmployee.setDepartment(request.getDepartment());
    existingEmployee.setSalary(request.getSalary());

    Employee savedEmployee = employeeService.saveEmployee(existingEmployee);

    log.info("Response -> {}", ApplicationUtils.getJSONString(savedEmployee));
    log.info("updateEmployee() : END");
    return ResponseEntity.ok(savedEmployee);
  }

  @DeleteMapping(
      value = AppConstants.EMPLOYEE_ENDPOINT + "/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> deleteEmployee(@PathVariable String id) {
    log.info("deleteEmployee() : START | ID -> {}", id);

    employeeService.deleteEmployee(id); // throws exception if not found

    log.info("Employee deleted successfully for ID {}", id);
    log.info("deleteEmployee() : END");
    return ResponseEntity.ok("Employee deleted successfully");
  }
}
