package com.maciejp.rat.offer;

import com.maciejp.rat.exception.OfferCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/offer")
public class OfferController {

    private final OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping
    public ResponseEntity<List<Offer>> getAllOffers() {
        List<Offer> offers =  offerService.getAllOffers();
        return ResponseEntity
                .ok()
                .body(offers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Offer> getOfferById(@PathVariable("id") long id) {
        Offer offer = offerService.getOfferById(id);
        return ResponseEntity
                .ok()
                .body(offer);
    }

    @PostMapping
    public ResponseEntity<?> addOffer(@RequestBody Offer offer) {
        long id;
        try {
            String creatorUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            id = offerService.addOffer(offer, creatorUsername);
        } catch (OfferCreationException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.buildResponse());
        }

        return ResponseEntity
                .ok()
                .body(offerService.getOfferById(id));
    }
}
