package com.sid.app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sid.app.exception.UserNotFoundException;
import com.sid.app.model.Employee;
import com.sid.app.repository.EmployeeRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Unit tests for EmployeeService class. */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

  @Mock private EmployeeRepository employeeRepository;

  @InjectMocks private EmployeeService employeeService;

  private Employee employee;

  @BeforeEach
  void setUp() {
    employee = new Employee();
    employee.setId("101");
    employee.setName("John Doe");
    employee.setDepartment("Engineering");
  }

  @Test
  @DisplayName("Should save an employee successfully")
  void testSaveEmployee() {
    when(employeeRepository.save(employee)).thenReturn(employee);

    Employee saved = employeeService.saveEmployee(employee);

    assertNotNull(saved);
    assertEquals("101", saved.getId());
    assertEquals("John Doe", saved.getName());
    assertEquals("Engineering", saved.getDepartment());
    verify(employeeRepository, times(1)).save(employee);
  }

  @Test
  @DisplayName("Should return employee when ID exists")
  void testGetEmployeeById_Found() {
    when(employeeRepository.findById("101")).thenReturn(Optional.of(employee));

    Optional<Employee> result = employeeService.getEmployeeById("101");

    assertTrue(result.isPresent());
    assertEquals("John Doe", result.get().getName());
    verify(employeeRepository, times(1)).findById("101");
  }

  @Test
  @DisplayName("Should throw UserNotFoundException when ID does not exist")
  void testGetEmployeeById_NotFound() {
    String id = "999";
    when(employeeRepository.findById(id)).thenReturn(Optional.empty());

    UserNotFoundException exception =
        assertThrows(UserNotFoundException.class, () -> employeeService.getEmployeeById(id));

    assertEquals("Could not found the user with id " + id, exception.getMessage());
    verify(employeeRepository, times(1)).findById(id);
  }

  @Test
  @DisplayName("Should return list of all employees")
  void testGetAllEmployees() {
    Employee emp2 = new Employee();
    emp2.setId("102");
    emp2.setName("Jane Smith");
    emp2.setDepartment("Finance");

    when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee, emp2));

    List<Employee> employees = employeeService.getAllEmployees();

    assertEquals(2, employees.size());
    assertEquals("John Doe", employees.get(0).getName());
    assertEquals("Jane Smith", employees.get(1).getName());
    verify(employeeRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("Should delete employee when ID exists")
  void testDeleteEmployee_Success() {
    when(employeeRepository.existsById("101")).thenReturn(true);
    doNothing().when(employeeRepository).deleteById("101");

    assertDoesNotThrow(() -> employeeService.deleteEmployee("101"));

    verify(employeeRepository, times(1)).existsById("101");
    verify(employeeRepository, times(1)).deleteById("101");
  }

  @Test
  @DisplayName("Should throw UserNotFoundException when deleting non-existent ID")
  void testDeleteEmployee_NotFound() {
    String id = "999";
    when(employeeRepository.existsById(id)).thenReturn(false);

    UserNotFoundException exception =
        assertThrows(UserNotFoundException.class, () -> employeeService.deleteEmployee(id));

    assertEquals("Could not found the user with id " + id, exception.getMessage());
    verify(employeeRepository, times(1)).existsById(id);
    verify(employeeRepository, never()).deleteById(id);
  }
}
