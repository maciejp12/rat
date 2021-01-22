package com.maciejp.rat.offer;

import com.maciejp.rat.exception.OfferCreationException;
import com.maciejp.rat.exception.OfferDeleteException;
import com.maciejp.rat.exception.OfferSelectionException;
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

    public Offer getOfferById(long id) {
        return offerDao.selectOfferById(id);
    }

    public Boolean offerIdExists(long id) {
        return offerDao.selectIdExists(id);
    }

    public List<Offer> getOfferByCreator(String username) throws OfferSelectionException {
        if (!userService.usernameExists(username)) {
            throw new OfferSelectionException("User does not exists", HttpStatus.BAD_REQUEST);
        }

        return offerDao.selectOfferByCreator(username);
    }

    public long addOffer(Offer offer, String creatorUsername) throws OfferCreationException {
        if (!userService.usernameExists(creatorUsername)) {
            throw new OfferCreationException("Please log in", HttpStatus.UNAUTHORIZED);
        }

        if (!offerValidator.validateTitleLength(offer.getTitle())) {
            throw new OfferCreationException(offerValidator.getTitleErrorMessage(), HttpStatus.BAD_REQUEST);
        }

        if (!offerValidator.validateDescriptionLength(offer.getDescription())) {
            throw new OfferCreationException(offerValidator.getDescriptionErrorMessage(), HttpStatus.BAD_REQUEST);
        }

        if (!offerValidator.validatePrice(offer.getPrice())) {
            throw new OfferCreationException(offerValidator.getPriceErrorMessage(), HttpStatus.BAD_REQUEST);
        }

        long creatorId = userService.getUserByUsername(creatorUsername).getId();
        return offerDao.insertOffer(offer, creatorId);
    }

    public Offer deleteOfferById(long id, String username) throws OfferDeleteException {
        if (!userService.usernameExists(username)) {
            throw new OfferDeleteException("Please log in", HttpStatus.CONFLICT);
        }

        if (!offerIdExists(id)) {
            throw new OfferDeleteException("Offer does not exist", HttpStatus.CONFLICT);
        }

        Offer offer = getOfferById(id);

        if (!offer.getCreator().equals(username)) {
            throw new OfferDeleteException("You can only delete your offers", HttpStatus.CONFLICT);
        }

        int delete = offerDao.deleteOfferById(id);

        if (delete == 0) {
            throw new OfferDeleteException("Cannot delete this offer", HttpStatus.BAD_REQUEST);
        }

        return offer;
    }
}
