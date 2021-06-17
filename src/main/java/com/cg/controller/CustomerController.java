package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.upload.UploadOneFile;
import com.cg.service.CustomerService;
import com.cg.service.ICustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping({"", "/customer"})
public class CustomerController {
    private final ICustomerService customerService = new CustomerService();

    @GetMapping()
    public String showList(Model model) {
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        return "/customer/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable int id, Model model) {
        model.addAttribute("customer", customerService.findById(id));
        return "/customer/view";
    }

    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("/customer/create");
        modelAndView.addObject("customer", new Customer());
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        model.addAttribute("customer", customerService.findById(id));
        return "/customer/edit";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, Model model) {
        model.addAttribute("customer", customerService.findById(id));
        return "/customer/delete";
    }

    @PostMapping("/save")
    public String save(Customer customer, RedirectAttributes redirect) {
        customer.setId((int) (Math.random() * 10000));
        customerService.save(customer);
        redirect.addFlashAttribute("success", "Created customer successfully!");
        return "redirect:/customer";
    }

    @PostMapping("/update")
    public String update(Customer customer) {
        customerService.update(customer.getId(), customer);
        return "redirect:/customer";
    }

    @PostMapping("/delete")
    public String delete(Customer customer, RedirectAttributes redirect) {
        customerService.remove(customer.getId());
        redirect.addFlashAttribute("success", "Removed customer successfully!");
        return "redirect:/customer";
    }


}