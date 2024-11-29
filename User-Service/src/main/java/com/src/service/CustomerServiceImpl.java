package com.src.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.src.model.AccountDetails;
import com.src.model.CustomerDetails;
import com.src.model.EmployeeDetails;
import com.src.model.User;
import com.src.repository.AccountDetailsRepository;
import com.src.repository.AddressRepository;
import com.src.repository.CustomerDeatilsRepository;
import com.src.repository.UserRepository;

@Service
public class CustomerServiceImpl implements ICustomerService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CustomerDeatilsRepository customerDeatilsRepository;
	@Autowired 
	private AddressRepository addressRepository;
	@Autowired
	private AccountDetailsRepository accountDetailsRepository;
	@Autowired
	private PasswordEncoder encoder;
	
	User user=null;
	CustomerDetails customerDetails=null;
	AccountDetails accountDetails=null;
	@Override
	public ResponseEntity<?> saveCustomer(CustomerDetails customer, EmployeeDetails employee) {
		customerDetails = customerDeatilsRepository.findByAadharNumber(customer.getAadharNumber());
		boolean bool=true;
		if(customerDeatilsRepository.findByMobileNumber(customer.getMobileNumber())!=null &&
				customerDeatilsRepository.findByAadharNumber(customer.getAadharNumber())!=null &&
				customerDeatilsRepository.findByEmail(customer.getEmail())!=null){
				
		 if((customerDeatilsRepository.findByMobileNumber(customer.getMobileNumber()).getCif() ==
				customerDeatilsRepository.findByEmail(customer.getEmail()).getCif()) &&
		(customerDeatilsRepository.findByAadharNumber(customer.getAadharNumber()).getCif()==
		customerDeatilsRepository.findByMobileNumber(customer.getMobileNumber()).getCif())) {
			if(!customerDetails.isVerificationStatus()) {
				return new ResponseEntity<String>("Already Applied",HttpStatus.CONFLICT);
			}
			List<AccountDetails> accountsList=accountDetailsRepository.findAllByCif(customerDetails.getCif());
			for (AccountDetails acc : accountsList) {
				
				System.out.println("in list"+acc);
				if(acc.getAccountType().equalsIgnoreCase(customer.getAccountType())) {
					bool=false;
					System.out.println(bool);
				}
			}
			if(bool==false) {
				return new ResponseEntity<String>("already have an account",HttpStatus.CONFLICT);
			}
	
			
				System.out.println("in if");
				accountDetails=AccountDetails.builder()
						.cif(customerDetails.getCif())
						.accountType(customer.getAccountType())
						.status(true)
						.dateOfOpeningAccount(LocalDate.now())
						.ifscCode(employee.getIfsc())
						.branchName(employee.getBranchName())
						.branchCode(employee.getBranchCode())
						.build();
				if(customer.getAccountType().equalsIgnoreCase("savings")) {
					accountDetails.setMinimumbalanceRequired(500);
					accountDetails.setBalance(500.0);
				}else if(customer.getAccountType().equalsIgnoreCase("current"))  {
					accountDetails.setMinimumbalanceRequired(5000);
					accountDetails.setBalance(5000);			
					}else{
						accountDetails.setMinimumbalanceRequired(0);
						accountDetails.setBalance(0);	
					}
				
				return new ResponseEntity<AccountDetails>(	accountDetailsRepository.save(accountDetails),HttpStatus.CREATED);
				
				
		}
			else {
				return new ResponseEntity<String>("Email/Mobile/Aadhar Mismatch",HttpStatus.NOT_FOUND);
			}
		}else {
				return  new ResponseEntity<String>("Email/Mobile/Aadhar Mismatch",HttpStatus.NOT_FOUND);
			}
	}
	

	@Override
	public List<CustomerDetails> nonActiveCustomer() {
		return customerDeatilsRepository.findByVerificationStatus(false);
	}

	@Override
	public User updateCustomer(CustomerDetails customer) {
		customerDetails = customerDeatilsRepository.findById(customer.getCif()).get();
		if (!customerDetails.getMobileNumber().equals(customer.getMobileNumber())
				|| !customerDetails.getEmail().equals(customer.getEmail())) {
					
			user = userRepository.findById(customer.getCif()).get();
			user.setMobileNumber(customer.getMobileNumber());
			user.setEmail(customer.getEmail());
			
			customerDeatilsRepository.save(customer);
			userRepository.save(user);
			return user;
		}
		else {
			return user;
		}
	}

	@Override
	public boolean deleteCustomer(long cif) {
		customerDetails=customerDeatilsRepository.findById(cif).get();
		if(customerDetails!=null) {
			customerDeatilsRepository.deleteById(cif);
			addressRepository.delete(customerDetails.getAddress());
			userRepository.deleteById(cif);
			accountDetailsRepository.delete(accountDetailsRepository.findByCif(cif));
			return true;
		}else {
		return false;
		}
	}

	@Override
	public User findUserByMobileNumber(String mobileNumber) {
		
		return userRepository.findByMobileNumber(mobileNumber);
	}

	@Override
	public List<Object> approveCustomer(long cif,EmployeeDetails employee) {
		customerDetails=customerDeatilsRepository.findById(cif).get();
		
		if(customerDetails!=null) {
			customerDetails.setVerificationStatus(true);
			user=User.builder().userId(cif)
					.mobileNumber(customerDetails.getMobileNumber())
					.email(customerDetails.getEmail())
					.password(encoder.encode("AXIS@"+customerDetails.getMobileNumber().substring(0,4)))
					.validUpTo(LocalDate.now().plusDays(1))
					.status(true)
					.role("ROLE_CUSTOMER")
					.isEnabled(true)
					.build();
			userRepository.save(user);
			
			
			accountDetails=AccountDetails.builder()
					.cif(customerDetails.getCif())
					.accountType(customerDetails.getAccountType())
					.status(true)
					.dateOfOpeningAccount(LocalDate.now())
					.ifscCode(employee.getIfsc())
					.branchName(employee.getBranchName())
					.branchCode(employee.getBranchCode())
					.build();
			if(customerDetails.getAccountType().equalsIgnoreCase("savings")) {
				accountDetails.setMinimumbalanceRequired(500);
				accountDetails.setBalance(500.0);
			}else if(customerDetails.getAccountType().equalsIgnoreCase("current"))  {
				accountDetails.setMinimumbalanceRequired(5000);
				accountDetails.setBalance(5000);			
				}else if(customerDetails.getAccountType().equalsIgnoreCase("loan") || customerDetails.getAccountType().equalsIgnoreCase("salary")){
					accountDetails.setMinimumbalanceRequired(0);
					accountDetails.setBalance(0);	
				}
			accountDetailsRepository.save(accountDetails);		}
		return null;
	}

	@Override
	public CustomerDetails findCustumerDetailsByCif(long cif) {
		// TODO Auto-generated method stub
		return customerDeatilsRepository.findById(cif).get();
	}

	@Override
	public void rejectCustomer(long cif) {
		customerDeatilsRepository.deleteById(cif);
		
	}


	@Override
	public CustomerDetails findCustumerDetailsAadhar(String adhar) {
		// TODO Auto-generated method stub
		return customerDeatilsRepository.findByAadharNumber(adhar);
	}


	@Override
	public CustomerDetails findCustumerDetailsByMobile(String mobile) {
		// TODO Auto-generated method stub
		return customerDeatilsRepository.findByMobileNumber(mobile);
	}


	@Override
	public CustomerDetails findCustumerDetailsByEmail(String email) {
		// TODO Auto-generated method stub
		return customerDeatilsRepository.findByEmail(email);
	}


	@Override
	public CustomerDetails directSave(CustomerDetails customerDetails) {
		// TODO Auto-generated method stub
		return customerDeatilsRepository.save(customerDetails);
	}


	
}
