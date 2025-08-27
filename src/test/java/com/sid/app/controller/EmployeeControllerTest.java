package com.sid.app.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sid.app.constants.AppConstants;
import com.sid.app.model.Employee;
import com.sid.app.service.EmployeeService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EmployeeController.class)
@Import(EmployeeControllerTest.MockConfig.class) // Import custom mock configuration
class EmployeeControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private EmployeeService employeeService; // Inject the mock from config

  @Autowired private ObjectMapper objectMapper;

  private Employee employee;

  @BeforeEach
  void setUp() {
    employee = new Employee("101", "Siddhant", "Engineering", 100000.0);
  }

  @Test
  void testCreateEmployee() throws Exception {
    given(employeeService.saveEmployee(any(Employee.class))).willReturn(employee);

    mockMvc
        .perform(
            post(AppConstants.EMPLOYEE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("101"))
        .andExpect(jsonPath("$.name").value("Siddhant"));
  }

  @Test
  void testGetEmployeeById_Found() throws Exception {
    given(employeeService.getEmployeeById("101")).willReturn(Optional.of(employee));

    mockMvc
        .perform(get(AppConstants.EMPLOYEE_ENDPOINT + "/101"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("101"))
        .andExpect(jsonPath("$.name").value("Siddhant"));
  }

  @Test
  void testGetEmployeeById_NotFound() throws Exception {
    given(employeeService.getEmployeeById("999")).willReturn(Optional.empty());

    mockMvc.perform(get(AppConstants.EMPLOYEE_ENDPOINT + "/999")).andExpect(status().isNotFound());
  }

  @Test
  void testGetAllEmployees() throws Exception {
    List<Employee> employees = Arrays.asList(employee, new Employee("102", "John", "HR", 50000.0));
    given(employeeService.getAllEmployees()).willReturn(employees);

    mockMvc
        .perform(get(AppConstants.EMPLOYEE_ENDPOINT))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  void testUpdateEmployee_Found() throws Exception {
    Employee updatedEmployee = new Employee("101", "Siddhant Patni", "Technology", 120000.0);
    given(employeeService.getEmployeeById("101")).willReturn(Optional.of(employee));
    given(employeeService.saveEmployee(any(Employee.class))).willReturn(updatedEmployee);

    mockMvc
        .perform(
            put(AppConstants.EMPLOYEE_ENDPOINT + "/101")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Siddhant Patni"))
        .andExpect(jsonPath("$.salary").value(120000.0));
  }

  @Test
  void testUpdateEmployee_NotFound() throws Exception {
    Employee updatedEmployee = new Employee("101", "Siddhant Patni", "Technology", 120000.0);
    given(employeeService.getEmployeeById("101")).willReturn(Optional.empty());

    mockMvc
        .perform(
            put(AppConstants.EMPLOYEE_ENDPOINT + "/101")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)))
        .andExpect(status().isNotFound());
  }

  @Test
  void testDeleteEmployee_Found() throws Exception {
    given(employeeService.getEmployeeById("101")).willReturn(Optional.of(employee));
    doNothing().when(employeeService).deleteEmployee("101");

    mockMvc
        .perform(delete(AppConstants.EMPLOYEE_ENDPOINT + "/101"))
        .andExpect(status().isOk())
        .andExpect(content().string("Employee deleted successfully"));
  }

  @Test
  void testDeleteEmployee_NotFound() throws Exception {
    given(employeeService.getEmployeeById("101")).willReturn(Optional.empty());

    mockMvc
        .perform(delete(AppConstants.EMPLOYEE_ENDPOINT + "/101"))
        .andExpect(status().isNotFound());
  }

  /** ✅ Custom Test Configuration to provide mocked EmployeeService Replaces deprecated @MockBean */
  @TestConfiguration
  static class MockConfig {
    @Bean
    public EmployeeService employeeService() {
      return Mockito.mock(EmployeeService.class);
    }
  }
}
