package com.example.template.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping(value = "market")
public class SseRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SseRestController.class);

    private static final CopyOnWriteArrayList<SseOfferEmitter> userBaseEmitters = new CopyOnWriteArrayList<>();

    private ExecutorService nonBlockingService = Executors.newCachedThreadPool();

    @GetMapping("/sse")
    public SseEmitter onEmitter(
            HttpServletRequest request,
           HttpServletResponse response,
           @RequestParam(value = "username") String username,
           @RequestParam(value = "offerId") Long offerId
    ) {
        SseOfferEmitter emitter = new SseOfferEmitter(username, offerId);
        userBaseEmitters.add(emitter);

        emitter.onCompletion(() -> this.userBaseEmitters.remove(emitter));
        emitter.onTimeout(() -> {
            emitter.complete();
            this.userBaseEmitters.remove(emitter);
        });

        return emitter;
    }

    @EventListener
    public void onOfferSse(SseBaseMessage appEntityBaseMessage) {
        LOGGER.info("appEntityBaseMessage");
        System.out.println(this.userBaseEmitters.size());
        List<SseEmitter> deadEmitters = new ArrayList<>();
        this.userBaseEmitters.forEach(emitter -> {
            try {
                LOGGER.info("emitter.getUsername() : " + emitter.getUsername() + " , emitter.getOfferId() : " + emitter.getOfferId() );
                LOGGER.info("appEntityBaseMessage.getUsername() : " + appEntityBaseMessage.getUsername() + " , appEntityBaseMessage.getOfferId() : " + appEntityBaseMessage.getOfferId() );
                if (appEntityBaseMessage.getUsername().toLowerCase().equals(emitter.getUsername().toLowerCase())
                        && appEntityBaseMessage.getOfferId().equals(emitter.getOfferId())) {
                    emitter.send(appEntityBaseMessage);
                }

            } catch (Exception e) {
//                e.printStackTrace();
                LOGGER.info("dead");
                deadEmitters.add(emitter);
            }
        });
        this.userBaseEmitters.remove(deadEmitters);
    }
}
