package com.src.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.src.model.EmployeeDetails;
import com.src.model.User;
import com.src.repository.AddressRepository;
import com.src.repository.EmployeeDetailsRepository;
import com.src.repository.UserRepository;

@Service
public class EmployeeService implements IEmployeeService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepository;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private AddressRepository addressRepository;
	
	EmployeeDetails employeeDetails = null;
	User user = null;

	@Override
	public User saveEmployee(EmployeeDetails employee) {
		employeeDetails = employeeDetailsRepository.findByAadharNumber(employee.getAadharNumber());
		if (employeeDetails == null) {
			employee.setPosition("Employee");
			employeeDetailsRepository.save(employee);
			employeeDetails = employeeDetailsRepository.findByMobileNumber(employee.getMobileNumber());
			user = User.builder().userId(employeeDetails.getEmployeeId()).email(employeeDetails.getEmail())
					.mobileNumber(employeeDetails.getMobileNumber()).role("ROLE_EMPLOYEE,ROLE_CUSTOMER").status(true)
					.password(encoder.encode("AXIS@" + employeeDetails.getMobileNumber().substring(0, 4)))
					.validUpTo(LocalDate.now().plusDays(1)).isEnabled(true).build();
			userRepository.save(user);

			return user;
		} else {
			return user;
		}
	}

	@Override
	public User saveManager(EmployeeDetails employee) {
		employeeDetails = employeeDetailsRepository.findByAadharNumber(employee.getAadharNumber());
		if (employeeDetails == null) {
//			employee.setPosition("Manager");
			employeeDetailsRepository.save(employee);
			employeeDetails = employeeDetailsRepository.findByMobileNumber(employee.getMobileNumber());
			user = User.builder()
					.userId(employeeDetails.getEmployeeId())
					.email(employeeDetails.getEmail())
					.mobileNumber(employeeDetails.getMobileNumber())
					.status(true).password(encoder.encode("AXIS@" + employeeDetails.getMobileNumber().substring(0, 4)))
					.validUpTo(LocalDate.now().plusDays(12)).isEnabled(true).build();
			if(employee.getPosition().equalsIgnoreCase("admin")) {
				user.setRole("ROLE_ADMIN,ROLE_MANAGER,ROLE_EMPLOYEE,ROLE_CUSTOMER");
			}else if(employee.getPosition().equalsIgnoreCase("manager")) {
				user.setRole("ROLE_MANAGER,ROLE_EMPLOYEE,ROLE_CUSTOMER");
			}else if(employee.getPosition().equalsIgnoreCase("employee")) {
				user.setRole("ROLE_EMPLOYEE,ROLE_CUSTOMER");
			}
			userRepository.save(user);

			return user;
		} else {
			return user;
		}
	}

	@Override
	public User saveAdmin(EmployeeDetails employee) {
		employeeDetails = employeeDetailsRepository.findByAadharNumber(employee.getAadharNumber());
		if (employeeDetails == null) {
			employee.setPosition("Admin");
			employeeDetailsRepository.save(employee);
			employeeDetails = employeeDetailsRepository.findByMobileNumber(employee.getMobileNumber());
			user = User.builder().userId(employeeDetails.getEmployeeId()).email(employeeDetails.getEmail())
					.mobileNumber(employeeDetails.getMobileNumber())
					.role("ROLE_ADMIN,ROLE_MANAGER,ROLE_EMPLOYEE,ROLE_CUSTOMER").status(true)
					.password(encoder.encode("AXIS@" + employeeDetails.getMobileNumber().substring(0, 4)))
					.validUpTo(LocalDate.now().plusDays(1)).isEnabled(true).build();
			userRepository.save(user);

			return user;
		} else {
			return user;
		}
	}

	@Override
	public EmployeeDetails updateEmployee(EmployeeDetails employee) {
		employeeDetails = employeeDetailsRepository.findById(employee.getEmployeeId()).get();
		if (!employeeDetails.getMobileNumber().equals(employee.getMobileNumber())
				|| !employeeDetails.getEmail().equals(employee.getEmail())) {
			
			
			user = userRepository.findById(employee.getEmployeeId()).get();
			user.setMobileNumber(employee.getMobileNumber());
			user.setEmail(employee.getEmail());

		}
		if (!employee.getPosition().equalsIgnoreCase(employeeDetails.getPosition())) {
			if (employee.getPosition().equalsIgnoreCase("Admin")) {
				user.setRole("ROLE_ADMIN,ROLE_MANAGER,ROLE_EMPLOYEE,ROLE_CUSTOMER");
			}
			if (employee.getPosition().equalsIgnoreCase("Manager")) {
				user.setRole("ROLE_MANAGER,ROLE_EMPLOYEE,ROLE_CUSTOMER");
			}
		}
		userRepository.save(user);
		employeeDetailsRepository.save(employee);
		return employee;
	}

	@Override
	public EmployeeDetails updateManager(EmployeeDetails employee) {
		employeeDetails = employeeDetailsRepository.findById(employee.getEmployeeId()).get();
		System.out.println("in service"+employee);
		if (!employeeDetails.getMobileNumber().equalsIgnoreCase(employee.getMobileNumber())
				|| !employeeDetails.getEmail().equals(employee.getEmail())) {
			System.out.println("in 1st if");

			
			user = userRepository.findByMobileNumber(employee.getMobileNumber());
			user.setMobileNumber(employee.getMobileNumber());
			user.setEmail(employee.getEmail());

		}
		if (!employee.getPosition().equalsIgnoreCase(employeeDetails.getPosition())) {
			System.out.println("in 2n if");

			if (employee.getPosition().equalsIgnoreCase("Admin")) {
				user.setRole("ROLE_ADMIN,ROLE_MANAGER,ROLE_EMPLOYEE,ROLE_CUSTOMER");
			}
			if (employee.getPosition().equalsIgnoreCase("Employee")) {
				user.setRole("ROLE_EMPLOYEE,ROLE_CUSTOMER");
			}
		}
		System.out.println("before update " + user +"& " +employee);

		userRepository.save(user);
		employeeDetailsRepository.save(employee);
		return employee;
	}

	@Override
	public EmployeeDetails updateAdmin(EmployeeDetails employee) {
		employeeDetails = employeeDetailsRepository.findById(employee.getEmployeeId()).get();
		if (!employeeDetails.getMobileNumber().equals(employee.getMobileNumber())
				|| !employeeDetails.getEmail().equals(employee.getEmail())) {
			
			
			user = userRepository.findById(employee.getEmployeeId()).get();
			user.setMobileNumber(employee.getMobileNumber());
			user.setEmail(employee.getEmail());

		}
		if (!employee.getPosition().equalsIgnoreCase(employeeDetails.getPosition())) {
			if (employee.getPosition().equalsIgnoreCase("Manager")) {
				user.setRole("ROLE_MANAGER,ROLE_EMPLOYEE,ROLE_CUSTOMER");
			}
			if (employee.getPosition().equalsIgnoreCase("Employee")) {
				user.setRole("ROLE_EMPLOYEE,ROLE_CUSTOMER");
			}
		}
		userRepository.save(user);
		employeeDetailsRepository.save(employee);
		return employee;
	}

	@Override
	public boolean deleteEmployee(long employeeId) {
		employeeDetails=employeeDetailsRepository.findById(employeeId).get();
		if(employeeDetails!=null && employeeDetails.getPosition().equalsIgnoreCase("Employee")) {
			addressRepository.delete(employeeDetails.getAddress());
			userRepository.deleteById(employeeDetails.getEmployeeId());
			employeeDetailsRepository.deleteById(employeeId);
			return true;
		}else
		{
		return false;
		}
	}

	@Override
	public boolean deleteManager(long employeeId) {
		employeeDetails=employeeDetailsRepository.findById(employeeId).get();
		if(employeeDetails!=null){
			addressRepository.delete(employeeDetails.getAddress());
			userRepository.deleteById(employeeDetails.getEmployeeId());
			employeeDetailsRepository.deleteById(employeeId);
			return true;
		}else
		{
		return false;
		}
	}

	@Override
	public boolean deleteAdmin(long employeeId) {
		employeeDetails=employeeDetailsRepository.findById(employeeId).get();
		if(employeeDetails!=null && employeeDetails.getPosition().equalsIgnoreCase("Admin")) {
			addressRepository.delete(employeeDetails.getAddress());
			userRepository.deleteById(employeeDetails.getEmployeeId());
			employeeDetailsRepository.deleteById(employeeId);
			return true;
		}else
		{
		return false;
		}
	}

	@Override
	public EmployeeDetails findById(long employeeId) {
		
		return employeeDetailsRepository.findById(employeeId).get();
	}

	@Override
	public User findUserByMobileNumber(String mobileNumber) {
		
		return userRepository.findByMobileNumber(mobileNumber);
	}

	@Override
	public EmployeeDetails findEmployeeByMobileNumber(String mobileNumber) {
		
		return employeeDetailsRepository.findByMobileNumber(mobileNumber);
	}

	@Override
	public List<EmployeeDetails> findAllEmployee() {
		// TODO Auto-generated method stub
		return employeeDetailsRepository.findAll();
	}

	@Override
	public EmployeeDetails updateWorker(EmployeeDetails details) {
		User updateUser=userRepository.findByMobileNumber(details.getMobileNumber());
		if(updateUser !=null) {
			updateUser.setEmail(details.getEmail());
			if(details.getPosition().equalsIgnoreCase("admin")) {
				updateUser.setRole("ROLE_ADMIN,ROLE_MANAGER,ROLE_EMPLOYEE,ROLE_CUSTOMER");
			}else if(details.getPosition().equalsIgnoreCase("manager")) {
				updateUser.setRole("ROLE_MANAGER,ROLE_EMPLOYEE,ROLE_CUSTOMER");
			}else if(details.getPosition().equalsIgnoreCase("employee")) {
				updateUser.setRole("ROLE_EMPLOYEE,ROLE_CUSTOMER");
			}
			userRepository.save(updateUser);
		}
		return	employeeDetailsRepository.save(details);
		

	}



}
