package com.example.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "market")
public class MarketController {

    @Autowired
    MarketService marketService;

    /**
     * @param request
     * @param response
     * @param offerId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{offerId}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public Offers marketOffers(HttpServletRequest request, HttpServletResponse response,
                     @PathVariable(value = "offerId") Long offerId
    ) throws Exception {
        return this.marketService.marketOffers(offerId);
    }

}
