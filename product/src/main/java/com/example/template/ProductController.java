package com.example.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "product")
public class ProductController {

    @Autowired
    ProductService productService;

    /**
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/slotOffer", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public void slotOffer(HttpServletRequest request, HttpServletResponse response
    ) throws Exception {
        this.productService.slotOffer();
    }

}
