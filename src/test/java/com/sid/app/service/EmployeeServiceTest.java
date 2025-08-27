package com.sid.app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sid.app.exception.UserNotFoundException;
import com.sid.app.model.Employee;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

  @Mock private RedisTemplate<String, Object> redisTemplate;

  @Mock private HashOperations<String, String, Employee> hashOperations;

  @InjectMocks private EmployeeService employeeService;

  private Employee employee;
  private static final String HASH_KEY = "Employee";

  @BeforeEach
  void setUp() {
    employee = new Employee();
    employee.setId("101");
    employee.setName("John Doe");
    employee.setDepartment("Engineering");

    // Mock RedisTemplate to return our HashOperations
    doReturn(hashOperations).when(redisTemplate).opsForHash();
  }

  @Test
  @DisplayName("Should save an employee successfully")
  void testSaveEmployee() {
    doNothing().when(hashOperations).put(HASH_KEY, employee.getId(), employee);

    Employee saved = employeeService.saveEmployee(employee);

    assertNotNull(saved);
    assertEquals("101", saved.getId());
    assertEquals("John Doe", saved.getName());
    assertEquals("Engineering", saved.getDepartment());
    verify(hashOperations, times(1)).put(HASH_KEY, employee.getId(), employee);
  }

  @Test
  @DisplayName("Should return employee when ID exists")
  void testGetEmployeeById_Found() {
    when(hashOperations.get(HASH_KEY, "101")).thenReturn(employee);

    Employee result = employeeService.getEmployeeById("101");

    assertNotNull(result);
    assertEquals("John Doe", result.getName());
    verify(hashOperations, times(1)).get(HASH_KEY, "101");
  }

  @Test
  @DisplayName("Should throw UserNotFoundException when ID does not exist")
  void testGetEmployeeById_NotFound() {
    when(hashOperations.get(HASH_KEY, "999")).thenReturn(null);

    UserNotFoundException exception =
        assertThrows(UserNotFoundException.class, () -> employeeService.getEmployeeById("999"));

    assertEquals("Could not found the user with id 999", exception.getMessage());
    verify(hashOperations, times(1)).get(HASH_KEY, "999");
  }

  @Test
  @DisplayName("Should return list of all employees")
  void testGetAllEmployees() {
    Employee emp2 = new Employee();
    emp2.setId("102");
    emp2.setName("Jane Smith");
    emp2.setDepartment("Finance");

    when(hashOperations.values(HASH_KEY)).thenReturn(Arrays.asList(employee, emp2));

    List<Employee> employees = employeeService.getAllEmployees();

    assertEquals(2, employees.size());
    assertEquals("John Doe", employees.get(0).getName());
    assertEquals("Jane Smith", employees.get(1).getName());
    verify(hashOperations, times(1)).values(HASH_KEY);
  }

  @Test
  @DisplayName("Should delete employee when ID exists")
  void testDeleteEmployee_Success() {
    // HashOperations.delete() returns Long (number of removed entries)
    when(hashOperations.delete(HASH_KEY, "101")).thenReturn(1L);

    assertDoesNotThrow(() -> employeeService.deleteEmployee("101"));

    verify(hashOperations, times(1)).delete(HASH_KEY, "101");
  }

  @Test
  @DisplayName("Should throw UserNotFoundException when deleting non-existent ID")
  void testDeleteEmployee_NotFound() {
    when(hashOperations.delete(HASH_KEY, "999")).thenReturn(0L); // nothing deleted

    UserNotFoundException exception =
        assertThrows(UserNotFoundException.class, () -> employeeService.deleteEmployee("999"));

    assertEquals("Could not found the user with id 999", exception.getMessage());
    verify(hashOperations, times(1)).delete(HASH_KEY, "999");
  }
}
