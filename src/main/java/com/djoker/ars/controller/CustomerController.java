package com.djoker.ars.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.djoker.ars.model.customer.Customer;
import com.djoker.ars.service.CustomerService;

@Controller
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/")
	public String showAllCustomer(Model model) {
		List<Customer> listCustomers = customerService.getAllCustomers();
		model.addAttribute("listCustomerAccounts", listCustomers);
		return "index";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		Customer customer = new Customer();
		model.addAttribute("customerObj", customer);		
		return "login";
	}
	
	@GetMapping("/showCustomerForm")
	public String showCustomerForm(Model model) {
		Customer customer = new Customer();
		model.addAttribute("customerObj", customer);
		return "register";
	}
	
	@PostMapping("/register")
	public String saveCustomer(@ModelAttribute("customerObj") @Valid Customer customer, BindingResult bindingResult, Model model) {
		
		Customer customerTemp = customerService.findByEmail(customer.getEmail());
		// tìm thấy customerTemp đã tồn tại trong DB --> báo lỗi
		if(customerTemp != null) {
			bindingResult.rejectValue("email", "There is already an account with this email");
		}		
		if(bindingResult.hasErrors()) {
			return "register";
		}		
		customerService.saveCustomer(customer);	
		model.addAttribute("successMessage", "Customer has been registered successfully");
		return "redirect:/";
	}
	
	@GetMapping("/deleteCustomer/{id}")
	public String deleteCustomer(@PathVariable("id") Long id, Model model) {
		customerService.deleteCustomerById(id);
		model.addAttribute("message", "Delete customer successfully!");
		return "redirect:/"; 
	}

	// đường dẫn tới trang đăng nhập thành công mặc định của admin("/admin/home")
	@GetMapping("/admin/home")
	public String adminHome(Model model) {
		
		// Spring Security lưu trữ user hiện đã được xác thực trong một ThreadLocal(bên trong SecurityContextHolder)
		// và được biểu diễn dưới dạng đối tượng Authentication --> để truy xuất user hiện tại đã được xác thực ta gọi tới SecurityContextHolder
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	
		
		// ta lấy được username của principal đã được xác thực (principal có thể hiểu là một user hoặc một thiết bị, có thể thực hiện một hành động trong ứng dụng của bạn)
		// với username ở đây ta nên hiểu là username trong cặp username - password mà user nhập vào khi đăng nhập
	    Object principal = authentication.getPrincipal();
	    String username = "";
	    
	    // Principal đơn giản chỉ là một đối tượng và sẽ được ép kiểu sang UserDetails.
	    if (principal instanceof UserDetails) {
	        username = ((UserDetails) principal).getUsername(); // trả về username đã dùng trong qúa trình xác thực
	    } else {
	        username = principal.toString();
	    }
		
	    model.addAttribute("adminObj", "Welcome " + username);
	    model.addAttribute("adminMessage", "Content available only for users with admin rights");
		return "admin/home";
	}
	
	
	
}






















