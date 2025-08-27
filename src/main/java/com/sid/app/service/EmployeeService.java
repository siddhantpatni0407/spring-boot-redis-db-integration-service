package com.sid.app.service;

import com.sid.app.exception.UserNotFoundException;
import com.sid.app.model.Employee;
import com.sid.app.repository.EmployeeRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing Employee entities. Handles business logic for creating, retrieving,
 * updating, and deleting employees. Uses EmployeeRepository for data access operations.
 *
 * @author Siddhant Patni
 */
@Slf4j
@Service
public class EmployeeService {

  /** Repository for Employee data access. */
  private final EmployeeRepository employeeRepository;

  /**
   * Constructs EmployeeService with the required EmployeeRepository.
   *
   * @param employeeRepository the repository for employee persistence
   */
  @Autowired
  public EmployeeService(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  /**
   * Creates or updates an Employee record in the database.
   *
   * @param employee Employee object to be saved or updated
   * @return the saved Employee object
   */
  public Employee saveEmployee(Employee employee) {
    log.info("Saving employee with ID: {}", employee.getId());
    Employee savedEmployee = employeeRepository.save(employee);
    log.debug("Employee saved successfully: {}", savedEmployee);
    return savedEmployee;
  }

  /**
   * Retrieves an Employee by their unique ID.
   *
   * @param id the unique identifier of the Employee
   * @return an Optional containing the Employee if found, otherwise empty
   */
  public Optional<Employee> getEmployeeById(String id) {
    log.info("Fetching employee with ID: {}", id);
    Optional<Employee> employee = employeeRepository.findById(id);
    if (employee.isPresent()) {
      log.debug("Employee found: {}", employee.get());
      return employee;
    } else {
      log.warn("Employee with ID {} not found", id);
      throw new UserNotFoundException(id);
    }
  }

  /**
   * Retrieves all Employee records from the database.
   *
   * @return a List of all Employee objects
   */
  public List<Employee> getAllEmployees() {
    log.info("Fetching all employees");
    List<Employee> employees = (List<Employee>) employeeRepository.findAll();
    log.debug("Total employees fetched: {}", employees.size());
    return employees;
  }

  /**
   * Deletes an Employee record by their unique ID.
   *
   * @param id the unique identifier of the Employee to be deleted
   */
  public void deleteEmployee(String id) {
    log.info("Deleting employee with ID: {}", id);
    if (!employeeRepository.existsById(id)) {
      log.warn("Employee with ID {} not found for deletion", id);
      throw new UserNotFoundException(id);
    }
    employeeRepository.deleteById(id);
    log.debug("Employee with ID {} deleted successfully", id);
  }
}
