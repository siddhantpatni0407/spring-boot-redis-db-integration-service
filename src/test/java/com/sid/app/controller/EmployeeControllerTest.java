package com.sid.app.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sid.app.constants.AppConstants;
import com.sid.app.exception.ExceptionHandler;
import com.sid.app.exception.UserNotFoundException;
import com.sid.app.model.Employee;
import com.sid.app.service.EmployeeService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/** Unit tests for EmployeeController */
@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

  private MockMvc mockMvc;

  @Mock private EmployeeService employeeService;

  @InjectMocks private EmployeeController employeeController;

  private ObjectMapper objectMapper = new ObjectMapper();

  private Employee employee1;
  private Employee employee2;

  @BeforeEach
  void setup() {
    // Initialize MockMvc with controller and exception handler
    mockMvc =
        MockMvcBuilders.standaloneSetup(employeeController)
            .setControllerAdvice(new ExceptionHandler())
            .build();

    // Sample employees
    employee1 = new Employee();
    employee1.setId("1");
    employee1.setName("John Doe");
    employee1.setDepartment("IT");
    employee1.setSalary(50000);

    employee2 = new Employee();
    employee2.setId("2");
    employee2.setName("Jane Smith");
    employee2.setDepartment("HR");
    employee2.setSalary(60000);
  }

  @Test
  void testCreateEmployee() throws Exception {
    when(employeeService.saveEmployee(any(Employee.class))).thenReturn(employee1);

    mockMvc
        .perform(
            post(AppConstants.EMPLOYEE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee1))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(employee1.getId())))
        .andExpect(jsonPath("$.name", is(employee1.getName())))
        .andExpect(jsonPath("$.department", is(employee1.getDepartment())))
        .andExpect(jsonPath("$.salary", is(employee1.getSalary())));
  }

  @Test
  void testGetEmployeeById_Found() throws Exception {
    when(employeeService.getEmployeeById("1")).thenReturn(employee1);

    mockMvc
        .perform(
            get(AppConstants.EMPLOYEE_ENDPOINT + "/{id}", "1").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(employee1.getId())))
        .andExpect(jsonPath("$.name", is(employee1.getName())));
  }

  @Test
  void testGetEmployeeById_NotFound() throws Exception {
    when(employeeService.getEmployeeById("999"))
        .thenThrow(new UserNotFoundException("User not found"));

    mockMvc
        .perform(
            get(AppConstants.EMPLOYEE_ENDPOINT + "/{id}", "999").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(
            jsonPath("$.errorMessage", is("Could not found the user with id User not found")));
  }

  @Test
  void testGetAllEmployees() throws Exception {
    List<Employee> employees = Arrays.asList(employee1, employee2);
    when(employeeService.getAllEmployees()).thenReturn(employees);

    mockMvc
        .perform(get(AppConstants.EMPLOYEE_ENDPOINT).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id", is(employee1.getId())))
        .andExpect(jsonPath("$[1].id", is(employee2.getId())));
  }

  @Test
  void testUpdateEmployee_Found() throws Exception {
    Employee updatedEmployee = new Employee();
    updatedEmployee.setId("1");
    updatedEmployee.setName("John Updated");
    updatedEmployee.setDepartment("Finance");
    updatedEmployee.setSalary(70000);

    when(employeeService.getEmployeeById("1")).thenReturn(employee1);
    when(employeeService.saveEmployee(any(Employee.class))).thenReturn(updatedEmployee);

    mockMvc
        .perform(
            put(AppConstants.EMPLOYEE_ENDPOINT + "/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(updatedEmployee.getName())))
        .andExpect(jsonPath("$.department", is(updatedEmployee.getDepartment())))
        .andExpect(jsonPath("$.salary", is(updatedEmployee.getSalary())));
  }

  @Test
  void testUpdateEmployee_NotFound() throws Exception {
    Employee updatedEmployee = new Employee();
    updatedEmployee.setId("999");
    updatedEmployee.setName("Non Existent");
    updatedEmployee.setDepartment("Finance");
    updatedEmployee.setSalary(70000);

    when(employeeService.getEmployeeById("999")).thenThrow(new UserNotFoundException("999"));

    mockMvc
        .perform(
            put(AppConstants.EMPLOYEE_ENDPOINT + "/{id}", "999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage", is("Could not found the user with id 999")));
  }

  @Test
  void testDeleteEmployee_Found() throws Exception {
    when(employeeService.deleteEmployee("1")).thenReturn("Employee removed!!");

    mockMvc
        .perform(
            delete(AppConstants.EMPLOYEE_ENDPOINT + "/{id}", "1")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Employee deleted successfully"));
  }

  @Test
  void testDeleteEmployee_NotFound() throws Exception {
    when(employeeService.deleteEmployee("999")).thenThrow(new UserNotFoundException("999"));

    mockMvc
        .perform(
            delete(AppConstants.EMPLOYEE_ENDPOINT + "/{id}", "999")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage", is("Could not found the user with id 999")));
  }
}
