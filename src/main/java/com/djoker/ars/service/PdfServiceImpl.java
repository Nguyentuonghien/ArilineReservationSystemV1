package com.djoker.ars.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

@Service
public class PdfServiceImpl implements PdfService{

	private static final String PDF_RESOURCES = "/static/"; 
	private CustomerService customerService;
	private SpringTemplateEngine springTemplateEngine;
	
	@Autowired
	public PdfServiceImpl(CustomerService customerService, SpringTemplateEngine springTemplateEngine) {
		this.customerService = customerService;
		this.springTemplateEngine = springTemplateEngine;
	}

	@Override
	public File generatePdf() throws IOException, DocumentException {
		Context context = getContext();
		String html = loadAndFillTemplate(context);
		return renderPdf(html);
	}

	@Override
	public File renderPdf(String html) throws IOException, DocumentException {				
		// InputStream: được sử dụng để "đọc dữ liệu" từ một nguồn (source).
		// OutputStream: được sử dụng để "ghi dữ liệu" đến đích (destination).
		
		File file = File.createTempFile("customerObj", ".pdf");
		// FileOutputStream được sử dụng để tạo một file và ghi dữ liệu vào trong nó.
		// Luồng này sẽ tạo một file, nếu nó đã không tồn tại, trước khi mở nó để ghi output.
		OutputStream outputStream = new FileOutputStream(file);
		
		//  nhận HTML đã tạo trước đó làm đầu vào và ghi tệp PDF vào thư mục chính 
		ITextRenderer textRenderer = new ITextRenderer(20f * 4f / 3f, 20);
		textRenderer.setDocumentFromString(html, new ClassPathResource(PDF_RESOURCES).getURL().toExternalForm());
		textRenderer.layout();
		textRenderer.createPDF(outputStream);
		outputStream.close();
		
		file.deleteOnExit();
		
		return file;
	}

	@Override
	public Context getContext() {
		Context context = new Context();		
		/* tạo HTML bằng cách sử dụng Thymeleaf template */
		context.setVariable("customerObj", customerService.getAllCustomers());
		return context;
	}

	@Override
	public String loadAndFillTemplate(Context context) {
		return springTemplateEngine.process("pdf_customerAccounts", context);
	}
	
}



