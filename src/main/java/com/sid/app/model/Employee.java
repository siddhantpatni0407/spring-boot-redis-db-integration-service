package com.sid.app.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * Represents an Employee entity stored in Redis. Uses Lombok annotations for boilerplate code
 * generation. Annotated with @RedisHash to indicate Redis storage.
 *
 * <p>Fields: - id: Unique identifier for the employee. - name: Name of the employee. - department:
 * Department where the employee works. - salary: Employee's salary.
 *
 * <p>Implements Serializable for object serialization.
 *
 * @author Siddhant Patni
 */
@Data // Generates getters, setters, equals, hashCode, and toString methods
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields
@RedisHash("Employee") // Specifies Redis hash storage with key "Employee"
public class Employee implements Serializable {

  /** Unique identifier for the employee. */
  @Id private String id;

  /** Name of the employee. */
  private String name;

  /** Department where the employee works. */
  private String department;

  /** Employee's salary. */
  private double salary;
}
