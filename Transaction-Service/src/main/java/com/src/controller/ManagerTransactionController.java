package com.src.controller;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.src.model.AccountDetails;
import com.src.model.GiftCardsOffer;
import com.src.model.Transaction;
import com.src.service.IAccountService;


@RestController
@RequestMapping("/transactionByManager")
public class ManagerTransactionController {

	@Autowired
	private IAccountService service;
	AccountDetails saving,current,digital,result=null;
	
	@GetMapping("/hello")
	public String hello() {
		return "Hello World";
	}
	@GetMapping("/pending")
	public ResponseEntity<?> pendingTransaction() {
		return new ResponseEntity<List<Transaction>>(service.findAllPendingTransaction(), HttpStatus.OK);
	}

	@GetMapping("/approve/{transactionId}")
	public ResponseEntity<?> approveTransaction(@PathVariable("transactionId") long transactionId) {
		return new ResponseEntity<Transaction>(service.approveTransaction(transactionId), HttpStatus.OK);
	}

	@GetMapping("/reject/{transactionId}")
	public ResponseEntity<?> rejectTransaction(@PathVariable("transactionId") long transactionId) {
		return new ResponseEntity<Transaction>(service.rejectTransaction(transactionId), HttpStatus.OK);
	}

	@GetMapping("/all/transaction")
	public ResponseEntity<?> getAllTransaction() {
		return new ResponseEntity<List<Transaction>>(service.findAllTranasction(), HttpStatus.OK);
	}

	@GetMapping("/all/transaction/{accountNumber}")
	public ResponseEntity<?> getAllTransactionBySenderAccountNumber(@PathVariable("accountNumber") long accountNumber) {
		return new ResponseEntity<List<Transaction>>(service.findTransactionBySenderAccountNumber(accountNumber),
				HttpStatus.OK);
	}

	@GetMapping("/all/transaction/pdf")
	public ResponseEntity<ByteArrayResource> getAllTransactionAsPdf() {

		List<Transaction> transactionList = service.findAllTranasction();

		// Create a new PDF document
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfDocument pdfDocument = new PdfDocument(new PdfWriter(baos));
		Document document = new Document(pdfDocument, PageSize.A4.rotate());

		// Create a table for the transaction data
		Table table = new Table(new float[] { 1, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
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
		return ResponseEntity.ok().headers(headers).contentLength(baos.size()).body(resource);
	}

	@GetMapping("/all/transaction/date/{date}")
	public ResponseEntity<ByteArrayResource> getAllTransactionByDateOfTransaction(
			@PathVariable("date") LocalDate date) {

		List<Transaction> transactionList = service.findAllTransactionOfDateOfTransaction(date);

		// Create a new PDF document
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfDocument pdfDocument = new PdfDocument(new PdfWriter(baos));
		Document document = new Document(pdfDocument, PageSize.A4.rotate());

		// Create a table for the transaction data
		Table table = new Table(new float[] { 1, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
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
		return ResponseEntity.ok().headers(headers).contentLength(baos.size()).body(resource);
	}
	@GetMapping("/accountDetails/findAll/{cif}")
	public ResponseEntity<?> getAccount(@PathVariable("cif") long cif) {
		List<AccountDetails> accountDetails=service.findAccoundsByCif(cif);
		System.out.println(accountDetails);
		for (AccountDetails accounts : accountDetails) {
			if(accounts.getAccountType().equalsIgnoreCase("savings")) {
				saving=accounts;
			}
			else if(accounts.getAccountType().equalsIgnoreCase("current")) {
				current=accounts;
			}else {
				digital=accounts;
			}
		}
		if(saving!=null) {
			result=saving;
		}else if(current!=null) {
			result=current;
		}
		else {
			result=digital;
		}
		return new ResponseEntity<AccountDetails>(result,HttpStatus.OK);
	}
	@PostMapping("/create/giftCardOffer")
	public ResponseEntity<?> createGiftCardOffer(@RequestBody GiftCardsOffer giftCardsOffer) {
		return new ResponseEntity<GiftCardsOffer>(service.createGiftCardsOffer(giftCardsOffer),HttpStatus.CREATED);
	}
	@PutMapping("/update/giftCardOffer")
	public ResponseEntity<?> updateGiftCardOffer(@RequestBody GiftCardsOffer giftCardsOffer) {
		return new ResponseEntity<GiftCardsOffer>(service.updateGiftCardsOffer(giftCardsOffer),HttpStatus.OK);
	}
	@DeleteMapping("/delete/giftCardOffer/{id}")
	public ResponseEntity<?> deleteGiftCardOffer(@PathVariable("id") long id) {
		service.deleteGiftCardsOffer(id);
		return new ResponseEntity<String>("Deleted succesfully",HttpStatus.OK);
	}
}
