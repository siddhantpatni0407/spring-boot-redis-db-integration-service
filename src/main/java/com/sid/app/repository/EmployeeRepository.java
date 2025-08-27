package com.sid.app.repository;

import com.sid.app.model.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Employee entities.
 *
 * <p>Extends CrudRepository to provide CRUD operations for Employee objects. Spring Data will
 * automatically implement this interface.
 *
 * @author Siddhant Patni
 */
@Repository
public interface EmployeeRepository extends CrudRepository<Employee, String> {}
