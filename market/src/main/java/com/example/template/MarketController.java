package com.example.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    ////////// 아래부터는 redis 관련 예제 입니다. ////////////////

    @RequestMapping(value = "/offer", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public Offers getOffers(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @RequestParam("name") String name
    ) throws Exception {

        return marketService.getOffers(name);
//        return marketService.getOffersWithoutAnnotation(name);
    }

    @RequestMapping(value = "/save", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public Offers saveOffers(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam("id") Long id,
                                     @RequestParam("name") String name
    ) throws Exception {

        Offers c = new Offers();
        c.setId(id);
        c.setName(name);

        return marketService.saveCache(c);
//        return marketService.saveCacheWithoutAnnotation(c);
    }

    @RequestMapping(value = "/offer2", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public Offers getOffers2(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @RequestParam("name") String name
    ) throws Exception {

//        return marketService.getOffers(name);
        return marketService.getOffersWithoutAnnotation(name);
    }

    @RequestMapping(value = "/save2", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public Offers saveOffers2(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam("id") Long id,
                                     @RequestParam("name") String name
    ) throws Exception {

        Offers c = new Offers();
        c.setId(id);
        c.setName(name);

//        return marketService.saveCache(c);
        return marketService.saveCacheWithoutAnnotation(c);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String deleteOffers(HttpServletRequest request,
                            HttpServletResponse response,
                            @RequestParam("name") String name
    ) throws Exception {

        return marketService.deketeKeyByRedis(name);
    }

    @RequestMapping(value = "/offerList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Offers> getOffersList(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestParam("name") String name
    ) throws Exception {

        return marketService.getListByRedis(name);
    }

}
