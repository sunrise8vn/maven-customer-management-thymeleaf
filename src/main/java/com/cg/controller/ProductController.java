package com.cg.controller;

import com.cg.model.Product;
import com.cg.model.ProductForm;
import com.cg.model.upload.UploadOneFile;
import com.cg.service.IProductService;
import com.cg.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/product")
public class ProductController {
    private final IProductService productService = new ProductService();

    @Value("${file-upload}")
    private String fileUpload;

    @GetMapping("")
    public String index(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "/product/index";
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/product/create");
        modelAndView.addObject("productForm", new ProductForm());
        modelAndView.addObject("uploadOneFile", new UploadOneFile());
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView showAddForm() {
        ModelAndView modelAndView = new ModelAndView("/product/add");
        modelAndView.addObject("product", new Product());
        modelAndView.addObject("uploadOneFile", new UploadOneFile());
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addProduct(@ModelAttribute Product product, HttpServletRequest request, @ModelAttribute("uploadOneFile") UploadOneFile uploadOneFile) {
        String uploadRootPath = request.getServletContext().getRealPath("/upload");
        if (fileUpload == null || fileUpload.equals("")) {
            fileUpload = uploadRootPath;
        }

        File uploadRootDir = new File(uploadRootPath);
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdirs();
        }

        CommonsMultipartFile[] filesData = uploadOneFile.getFilesData();

        for (CommonsMultipartFile fileData : filesData) {

            String fileName = fileData.getOriginalFilename();

            if (fileName != null && fileName.length() > 0) {
                try {
                    FileCopyUtils.copy(fileData.getBytes(), new File(fileUpload + "/avatar/" + fileName));
                    product.setImage(fileName);
                    productService.save(product);
                } catch (Exception e) {
                    System.out.println("Error Write file: " + fileName);
                }
            }
        }

        ModelAndView modelAndView = new ModelAndView("/product/add");
        modelAndView.addObject("product", product);
        modelAndView.addObject("success", "Created new product successfully !");
        return modelAndView;
    }


    @PostMapping("/save")
    public ModelAndView saveProduct(@ModelAttribute ProductForm productForm) {
        MultipartFile multipartFile = productForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(productForm.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Product product = new Product(productForm.getId(), productForm.getName(),
                                      productForm.getDescription(), fileName);
        productService.save(product);
        ModelAndView modelAndView = new ModelAndView("/product/create");
        modelAndView.addObject("productForm", productForm);
        modelAndView.addObject("message", "Created new product successfully !");
        return modelAndView;
    }
}
