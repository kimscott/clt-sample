package com.example.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * /product/slotOffer/3?username=aa
     * @throws Exception
     */
    @RequestMapping(value = "/slotOffer/{offerId}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public void slotOffer(HttpServletRequest request, HttpServletResponse response
            , @RequestParam("username") String username
            , @PathVariable("offerId") long offerId

    ) throws Exception {
        this.productService.slotOffer(username, offerId);
    }

}
