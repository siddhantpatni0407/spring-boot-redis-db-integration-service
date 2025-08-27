package com.sid.app.service;

import com.sid.app.exception.UserNotFoundException;
import com.sid.app.model.Employee;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmployeeService {

  public static final String HASH_KEY = "Employee";

  private final RedisTemplate<String, Object> redisTemplate;

  @Autowired
  public EmployeeService(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  /** Save or update an employee in Redis. */
  public Employee saveEmployee(Employee employee) {
    log.info("Saving employee with ID: {}", employee.getId());
    redisTemplate.opsForHash().put(HASH_KEY, employee.getId(), employee);
    log.debug("Employee saved successfully: {}", employee);
    return employee;
  }

  /** Get an employee by ID from Redis. */
  public Employee getEmployeeById(String id) {
    log.info("Fetching employee with ID: {}", id);
    Employee employee = (Employee) redisTemplate.opsForHash().get(HASH_KEY, id);
    if (employee == null) {
      log.warn("Employee with ID {} not found", id);
      throw new UserNotFoundException(id);
    }
    log.debug("Employee found: {}", employee);
    return employee;
  }

  /** Get all employees from Redis. */
  public List<Employee> getAllEmployees() {
    log.info("Fetching all employees");

    List<Employee> employees =
        redisTemplate.opsForHash().values(HASH_KEY).stream()
            .map(obj -> (Employee) obj)
            .toList(); //

    log.debug("Total employees fetched: {}", employees.size());
    return employees;
  }

  /** Delete an employee by ID from Redis. */
  public String deleteEmployee(String id) {
    log.info("Deleting employee with ID: {}", id);
    Long removed = redisTemplate.opsForHash().delete(HASH_KEY, id);
    if (removed == null || removed == 0) {
      log.warn("Employee with ID {} not found for deletion", id);
      throw new UserNotFoundException(id);
    }
    log.debug("Employee with ID {} deleted successfully", id);
    return "Employee removed!!";
  }
}
