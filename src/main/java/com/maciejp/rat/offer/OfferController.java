package com.maciejp.rat.offer;

import com.maciejp.rat.exception.ApiException;
import com.maciejp.rat.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<?> getOfferById(@PathVariable("id") long id) {
        Offer offer;
        try {
            offer = offerService.getOfferById(id);
        } catch (ApiException e) {
            return ResponseEntity
                    .status(e.getHttpStatus())
                    .body(e.buildResponseBody());
        }

        return ResponseEntity
                .ok()
                .body(offer);
    }

    @GetMapping("/creator/{id}")
    public ResponseEntity<?> getOfferByCreator(@PathVariable("id") String username) {
        List<Offer> offers;
        try {
            offers = offerService.getOfferByCreator(username);
        } catch (ApiException e) {
            return ResponseEntity
                    .status(e.getHttpStatus())
                    .body(e.buildResponseBody());
        }

        return ResponseEntity
                .ok()
                .body(offers);
    }

    @GetMapping("/visits/{id}")
    public ResponseEntity<?> getOfferVisitCount(@PathVariable("id") long id) {
        Integer visits;
        try  {
            visits = offerService.getOfferVisitCount(id);
        } catch (ApiException e) {
            return ResponseEntity
                    .status(e.getHttpStatus())
                    .body(e.buildResponseBody());
        }

        return ResponseEntity
                .ok()
                .body(new OfferVisitsResponse(visits));
    }

    @PostMapping("/visits/{id}")
    public ResponseEntity<?> addOfferVisit(@PathVariable("id") long id) {
        Integer response;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (!(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
                String username = authentication.getName();

                try {
                    response = offerService.addOfferUserVisit(id, username);
                } catch (ApiException e) {
                    return ResponseEntity
                            .status(e.getHttpStatus())
                            .body(e.buildResponseBody());
                }

                return ResponseEntity
                        .ok()
                        .body(new OfferVisitsResponse(response));
            }
        }

        try {
            response = offerService.addOfferVisit(id);
        } catch (ApiException e) {
            return ResponseEntity
                    .status(e.getHttpStatus())
                    .body(e.buildResponseBody());
        }

        return ResponseEntity
                .ok()
                .body(new OfferVisitsResponse(response));
    }

    @PostMapping
    public ResponseEntity<?> addOffer(@RequestBody Offer offer) {
        long id;
        try {
            String creatorUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            id = offerService.addOffer(offer, creatorUsername);
        } catch (ApiException e) {
            return ResponseEntity
                    .status(e.getHttpStatus())
                    .body(e.buildResponseBody());
        }

        return ResponseEntity
                .ok()
                .body(offerService.getOfferById(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOfferById(@PathVariable("id") long id) {
        Offer offer;
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            offer = offerService.deleteOfferById(id, auth.getName());
        } catch (ApiException e) {
            return ResponseEntity
                    .status(e.getHttpStatus())
                    .body(e.buildResponseBody());
        }

        return ResponseEntity
                .ok()
                .body(offer);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateOffer(@RequestBody Offer offer, @PathVariable("id") long id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            offer = offerService.updateOffer(offer, auth.getName(), id);
        } catch (ApiException e) {
            return ResponseEntity
                    .status(e.getHttpStatus())
                    .body(e.buildResponseBody());
        }

        return ResponseEntity
                .ok()
                .body(offer);
    }
}
