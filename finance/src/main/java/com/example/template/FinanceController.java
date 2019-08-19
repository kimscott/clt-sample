package com.example.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "budget")
public class FinanceController {

    @Autowired
    FinanceService financeService;

    /**
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/check/{name}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Budget> checkBudget(HttpServletRequest request, HttpServletResponse response,
                                    @PathVariable(value = "name") String name
    ) throws Exception {
        return this.financeService.checkBudget(name);
    }

}
