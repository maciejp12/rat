package com.maciejp.rat.offer;

import com.maciejp.rat.exception.ApiException;
import com.maciejp.rat.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferService {

    private final OfferDao offerDao;

    private final UserService userService;

    private final OfferValidator offerValidator;

    @Autowired
    public OfferService(OfferDao offerDao, UserService userService) {
        this.offerDao = offerDao;
        this.userService = userService;
        this.offerValidator = new OfferValidator();
    }

    public List<Offer> getAllOffers() {
        return offerDao.selectAllOffers();
    }

    public Offer getOfferById(long id) throws ApiException {
        Offer offer =  offerDao.selectOfferById(id);

        if (offer == null) {
            throw new ApiException("Offer does not exist", HttpStatus.NOT_FOUND);
        }

        return offer;
    }

    public Boolean offerIdExists(long id) {
        return offerDao.selectIdExists(id);
    }

    public List<Offer> getOfferByCreator(String username) throws ApiException {
        if (!userService.usernameExists(username)) {
            throw new ApiException("User does not exist", HttpStatus.BAD_REQUEST);
        }

        return offerDao.selectOfferByCreator(username);
    }

    public long addOffer(Offer offer, String creatorUsername) throws ApiException {
        if (!userService.usernameExists(creatorUsername)) {
            throw new ApiException("Please log in", HttpStatus.UNAUTHORIZED);
        }

        if (!offerValidator.validateTitleLength(offer.getTitle())) {
            throw new ApiException(offerValidator.getTitleErrorMessage(), HttpStatus.BAD_REQUEST);
        }

        if (!offerValidator.validateDescriptionLength(offer.getDescription())) {
            throw new ApiException(offerValidator.getDescriptionErrorMessage(), HttpStatus.BAD_REQUEST);
        }

        if (!offerValidator.validatePrice(offer.getPrice())) {
            throw new ApiException(offerValidator.getPriceErrorMessage(), HttpStatus.BAD_REQUEST);
        }

        long creatorId = userService.getUserByUsername(creatorUsername).getId();
        return offerDao.insertOffer(offer, creatorId);
    }

    public Offer deleteOfferById(long id, String username) throws ApiException {
        if (!userService.usernameExists(username)) {
            throw new ApiException("Please log in", HttpStatus.UNAUTHORIZED);
        }

        if (!offerIdExists(id)) {
            throw new ApiException("Offer does not exist", HttpStatus.CONFLICT);
        }

        Offer offer = getOfferById(id);

        if (!offer.getCreator().equals(username)) {
            throw new ApiException("You can only delete your offers", HttpStatus.CONFLICT);
        }

        int delete = offerDao.deleteOfferById(id);

        if (delete == 0) {
            throw new ApiException("Cannot delete this offer", HttpStatus.BAD_REQUEST);
        }

        return offer;
    }

    public Offer updateOffer(Offer offer, String username, long id) throws ApiException {
        Offer toUpdate = getOfferById(id);

        if (toUpdate == null) {
            throw new ApiException("Offer does not exist", HttpStatus.NOT_FOUND);
        }

        if (!userService.usernameExists(username)) {
            throw new ApiException("Please log in", HttpStatus.UNAUTHORIZED);
        }

        if (!toUpdate.getCreator().equals(username)) {
            throw new ApiException("You can only update your offers", HttpStatus.CONFLICT);
        }

        String title = offer.getTitle();
        String description = offer.getDescription();
        Float price = offer.getPrice();

        if (title != null) {
            if (!offerValidator.validateTitleLength(title)) {
                throw new ApiException(offerValidator.getTitleErrorMessage(), HttpStatus.BAD_REQUEST);
            }
            offerDao.updateOfferTitleById(id, title);
        }

        if (description != null) {
            if (!offerValidator.validateDescriptionLength(description)) {
                throw new ApiException(offerValidator.getDescriptionErrorMessage(), HttpStatus.BAD_REQUEST);
            }
            offerDao.updateOfferDescriptionById(id, description);
        }

        if (price != null) {
            if (!offerValidator.validatePrice(price)) {
                throw new ApiException(offerValidator.getPriceErrorMessage(), HttpStatus.BAD_REQUEST);
            }
            offerDao.updateOfferPriceById(id, price);
        }

        toUpdate = getOfferById(id);

        return toUpdate;
    }
}
