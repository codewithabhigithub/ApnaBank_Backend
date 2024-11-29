package com.src.service;

import java.util.List;

import com.src.model.EmployeeDetails;
import com.src.model.User;

public interface IEmployeeService {
	User saveEmployee(EmployeeDetails employee);
	User saveManager(EmployeeDetails employee);
	User saveAdmin(EmployeeDetails employee);
	
	EmployeeDetails updateEmployee(EmployeeDetails employee);
	EmployeeDetails updateManager(EmployeeDetails employee);
	EmployeeDetails updateAdmin(EmployeeDetails employee);
	
	boolean deleteEmployee(long employeeId);
	boolean deleteManager(long employeeId);
	boolean deleteAdmin(long employeeId);

	EmployeeDetails findById(long employeeId);
	User findUserByMobileNumber(String mobileNumber);
	EmployeeDetails findEmployeeByMobileNumber(String mobileNumber);
	
	List<EmployeeDetails> findAllEmployee();
	EmployeeDetails updateWorker(EmployeeDetails details);
}
