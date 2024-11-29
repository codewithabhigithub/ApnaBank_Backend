package com.src.controller;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.src.model.AccountDetails;
import com.src.model.OtherBank;
import com.src.model.Transaction;
import com.src.model.User;
import com.src.service.IAccountService;
import com.src.service.IUserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

	@Autowired
	private IAccountService service;
	@Autowired
	private IUserService userService;
	AccountDetails account = null;

	@GetMapping("/hello")
	public String hello() {
		return "Hello World";
	}
	@GetMapping("/account/{accountNumber}")
	public ResponseEntity<?> getAccountDetails(@PathVariable("accountNumber") long accountNumber,HttpServletRequest request) {
		try {
			User user=userService.findByMobileNumber(request.getAttribute("username").toString());
			AccountDetails accountDetails=service.findAccountDetailsByAccountNumber(accountNumber);
//			if(user.)
			return new ResponseEntity<AccountDetails>(accountDetails, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("NOT FOUND", HttpStatus.NOT_FOUND);
		}
	}
@PostMapping("sendMoney/{accountNumber}/{amount}")
	public ResponseEntity<?> sendMoney(@PathVariable("accountNumber") long accountNumber,@PathVariable("amount") double amount, @RequestBody OtherBank otherBankDetails) {
		try {
			if(amount>1) {
			AccountDetails accountDetails=service.findAccountDetailsByAccountNumber(accountNumber);
			
			double bal = accountDetails.getBalance()-amount;
			double min=accountDetails.getMinimumbalanceRequired();
			System.out.println(bal>min);
			if(bal>=min && accountDetails.isStatus()) {
				System.out.println("inside");
				AccountDetails recieverAccount=service.findAccountDetailsByAccountNumber(otherBankDetails.getAccountNumber());
				if(recieverAccount==null) {
					System.out.println("inside");
					OtherBank otherBank=service.findOtherBankByAccountNumber(otherBankDetails.getAccountNumber());
					if(otherBank!=null) {
						if(otherBank.getIfsc().equalsIgnoreCase(otherBankDetails.getIfsc())) {
							return new ResponseEntity<Transaction>(service.transferAmountToOtherBank(accountDetails, otherBank, amount),HttpStatus.OK);
						}else {
							return  new ResponseEntity<String>("Wrong IFSC", HttpStatus.BAD_REQUEST);
						}
					}else {
						return  new ResponseEntity<String>("Wrong Account Number", HttpStatus.BAD_REQUEST);
					}
				}else {
					if(!recieverAccount.isStatus()) {
						return new ResponseEntity<String>("Receiver's Accout is not active",HttpStatus.BAD_REQUEST);
					}else {
						return new ResponseEntity<Transaction>(service.transferAmountToAxisBank(accountDetails, recieverAccount, amount),HttpStatus.OK);
					}
				}
				
			}else {
				return  new ResponseEntity<String>("Cant Send below minimun balance", HttpStatus.BAD_REQUEST);
			}
		}else {
			return  new ResponseEntity<String>("Amount should be greater than one", HttpStatus.BAD_REQUEST);
		}
		}catch (Exception e) {
			// TODO: handle exception
			return  new ResponseEntity<String>("Account Number is invalid", HttpStatus.CONFLICT);
		}
	}

	//transfer amount to other person
	@PostMapping("/transfer/{amount}")
	public ResponseEntity<?> transferAmountToOtherPerson(@PathVariable("amount") double amount , @RequestBody OtherBank receiverAccount,HttpServletRequest request) {
		try {
			AccountDetails senderAccount=service.findAccountDetailsByMobileNumber(request.getAttribute("username").toString());
			
			AccountDetails receiverAccountDetails=service.findAccountDetailsByAccountNumber(receiverAccount.getAccountNumber());	
			if(receiverAccountDetails!=null) {
			return new ResponseEntity<Transaction>(service.transferAmountToAxisBank(senderAccount, receiverAccountDetails, amount),HttpStatus.OK);
			}else {
				if(receiverAccount.getIfsc()!=null && receiverAccount.getBranchName()!=null && receiverAccount.getBankName()!=null) {
					System.out.println(receiverAccount);
					return new ResponseEntity<Transaction>(service.transferAmountToOtherBank(senderAccount, receiverAccount, amount),HttpStatus.OK);
				}else {
					return new ResponseEntity<String>("Enter All credentials",HttpStatus.NOT_FOUND);
				}
			}
			
			
		}catch (Exception e) {
			return new ResponseEntity<String>("Account Number does not exist", HttpStatus.NOT_FOUND);
		}
	}
	@GetMapping("/self/allTransaction/{accountNumber}")
	public ResponseEntity<?> getAllTransactionBySenderAccountNumber(@PathVariable("accountNumber") long accountNumber) {
		AccountDetails senderAccount=service.findAccountDetailsByAccountNumber(accountNumber);

		return new ResponseEntity<List<Transaction>>(service.findTransactionBySenderAccountNumber(senderAccount.getAccountNumber()),HttpStatus.OK);
	}
	@GetMapping("/self/pdf")
	public ResponseEntity<ByteArrayResource> getAllTransactionAsPdf(HttpServletRequest request) {
		AccountDetails senderAccount=service.findAccountDetailsByMobileNumber(request.getAttribute("username").toString());
	    List<Transaction> transactionList =service.findTransactionBySenderAccountNumber(senderAccount.getAccountNumber());

	    // Create a new PDF document
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    PdfDocument pdfDocument = new PdfDocument(new PdfWriter(baos));
	    Document document = new Document(pdfDocument, PageSize.A4.rotate()); 

	    // Create a table for the transaction data
	    Table table = new Table(new float[]{1, 2, 2, 2, 2, 2, 2, 2, 2, 2});
	    table.setWidth(UnitValue.createPercentValue(100)); // Set the table width to 100% of the page

	    // Add table headers
	    table.addCell(new Cell().add(new Paragraph("Transaction Id")));
	    table.addCell(new Cell().add(new Paragraph("SenderAccount")));
	    table.addCell(new Cell().add(new Paragraph("Receiver Account")));
	    table.addCell(new Cell().add(new Paragraph("Receiver-Bank")));
	    table.addCell(new Cell().add(new Paragraph("Amount")));
	    table.addCell(new Cell().add(new Paragraph("Transaction-Type")));
	    table.addCell(new Cell().add(new Paragraph("Date of Transaction")));
	    table.addCell(new Cell().add(new Paragraph("Description")));
	    table.addCell(new Cell().add(new Paragraph("Status")));
	    table.addCell(new Cell().add(new Paragraph("Balance")));

	    // Add transactions to the table
	    for (Transaction transaction : transactionList) {
	        table.addCell(new Cell().add(new Paragraph(String.valueOf(transaction.getTransactionId()))));
	        table.addCell(new Cell().add(new Paragraph(String.valueOf(transaction.getSenderAccountNumber()))));
	        table.addCell(new Cell().add(new Paragraph(String.valueOf(transaction.getReceiverAccountNumber()))));
	        table.addCell(new Cell().add(new Paragraph(String.valueOf(transaction.getReceiverBankName()))));
	        table.addCell(new Cell().add(new Paragraph(String.valueOf(transaction.getAmount()))));
	        table.addCell(new Cell().add(new Paragraph(String.valueOf(transaction.getTransactionType()))));
	        table.addCell(new Cell().add(new Paragraph(transaction.getDateOfTransaction().toString())));
	        table.addCell(new Cell().add(new Paragraph(String.valueOf(transaction.getDescription()))));
	        table.addCell(new Cell().add(new Paragraph(String.valueOf(transaction.getStatus()))));
	        table.addCell(new Cell().add(new Paragraph(String.valueOf(transaction.getRemainingBalance()))));
	    }

	    // Add the table to the document
	    document.add(table);

	    // Close the document
	    document.close();

	    // Prepare the response headers
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_PDF);
	    headers.setContentDispositionFormData("attachment", "transaction_list.pdf");

	    // Create a ByteArrayResource from the PDF content
	    ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

	    // Return the PDF as a ResponseEntity
	    return ResponseEntity.ok()
	            .headers(headers)
	            .contentLength(baos.size())
	            .body(resource);
	}
	@GetMapping("/find/allAccounts")
	public ResponseEntity<?> FindAllAccounts(HttpServletRequest request) {
		return new  ResponseEntity<List<AccountDetails>>(service.findAccountByMobileNumber(request.getAttribute("username").toString()),HttpStatus.OK);
	}
	@GetMapping("/transfer/same/{amount}/{accountNumber}/{receiverAccountNumber}")
	public ResponseEntity<?> transferAmountToSameBank(@PathVariable("amount") double amount , @PathVariable("accountNumber") long accountNumber,@PathVariable("receiverAccountNumber") long receiverAccountNumber) {
		AccountDetails reciverAccount=service.findAccountDetailsByAccountNumber(receiverAccountNumber);
	if(reciverAccount==null) {
		return new ResponseEntity<String>("Receiver Account Number does not exist",HttpStatus.NOT_FOUND);
	}else if(reciverAccount.isStatus()) {
		AccountDetails senderAccount=service.findAccountDetailsByAccountNumber(accountNumber);
		if(senderAccount.getBalance()<amount) {
			return new ResponseEntity<String>("Insufficient Fund",HttpStatus.BAD_REQUEST);
		}
		else if((senderAccount.getBalance()-amount)<=senderAccount.getMinimumbalanceRequired()) {
			return new ResponseEntity<String>("Can not withdraw below minimun balance",HttpStatus.BAD_REQUEST);
		}else {
			return new ResponseEntity<Transaction>(service.transferAmountToAxisBank(senderAccount, reciverAccount, amount),HttpStatus.OK);
		}
	}else {
		return new ResponseEntity<String>("Receiver's Account is not active",HttpStatus.BAD_REQUEST);
	}
		}
	@PostMapping("/transfer/other/{amount}/{accountNumber}")
	public ResponseEntity<?> transferAmountToOtherBank(@PathVariable("amount") double amount , @PathVariable("accountNumber") long accountNumber,@RequestBody OtherBank accountDetails) {
		System.out.println(accountDetails);
		OtherBank reciverAccount=service.findOtherBankByAccountNumber(accountDetails.getAccountNumber());
		System.out.println(reciverAccount);
	if(reciverAccount==null) {
		return new ResponseEntity<String>("Receiver Account Number does not exist",HttpStatus.NOT_FOUND);
	}else {
		AccountDetails senderAccount=service.findAccountDetailsByAccountNumber(accountNumber);
		if(senderAccount.getBalance()<amount) {
			return new ResponseEntity<String>("Insufficient Fund",HttpStatus.BAD_REQUEST);
		}
		else if((senderAccount.getBalance()-amount)<=senderAccount.getMinimumbalanceRequired()) {
			return new ResponseEntity<String>("Can not withdraw below minimun balance",HttpStatus.BAD_REQUEST);
		}else if(reciverAccount.getIfsc().equalsIgnoreCase(accountDetails.getIfsc())) {
			return new ResponseEntity<Transaction>(service.transferAmountToOtherBank(senderAccount, reciverAccount, amount),HttpStatus.OK);

		}
		else {
			return new ResponseEntity<String>("Wrong IFSC number",HttpStatus.BAD_REQUEST);
		}
	}
		}
}
