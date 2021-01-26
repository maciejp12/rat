package com.maciejp.rat.offer;

import com.maciejp.rat.RatApplication;
import com.maciejp.rat.exception.ApiException;
import com.maciejp.rat.user.User;
import com.maciejp.rat.user.UserService;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = RatApplication.class)
public class OfferServiceTest {

    private static boolean init = false;

    @Autowired
    private OfferService offerServiceField;

    @Autowired
    private UserService userServiceField;

    private static OfferService offerService;

    private static UserService userService;

    private static OfferValidator offerValidator;

    private static User creatorUser = new User(
            -1,
            "test_username1",
            "test_email1@test.com",
            "test_password",
            "123456789",
            null
    );

    private static User nonExistingUser = new User(
            -1,
            "test_username2",
            "test_email2@test.com",
            "test_password",
            "123456789",
            null
    );

    private static User nonCreatorUser = new User(
            -1,
            "test_username3",
            "test_email3@test.com",
            "test_password",
            "123456789",
            null
    );

    private static User existingUser = new User(
            -1,
            "test_username4",
            "test_email4@test.com",
            "test_password",
            "123456789",
            null
    );

    private static Offer offerToAdd = new Offer(
            -1,
            "test_title1",
            "test_description_1",
            1.00f,
            creatorUser.getUsername(),
            null
    );


    private static Offer existingOffer = new Offer(
            -1,
            "test_title2",
            "test_description_2",
            1.00f,
            existingUser.getUsername(),
            null
    );

    private static Offer offerToDelete = new Offer(
            -1,
            "test_title3",
            "test_description_3",
            1.00f,
            existingUser.getUsername(),
            null
    );


    private static long offerToAddGeneratedId;
    private static long offerToDeleteGeneratedId;
    private static long existingOfferId;

    @Before
    public void before() {
        if (!init) {
            offerService = offerServiceField;
            userService = userServiceField;
            offerValidator = new OfferValidator();

            try {
                userService.deleteUserByUsername(creatorUser.getUsername());
                userService.deleteUserByUsername(nonExistingUser.getUsername());
                userService.deleteUserByUsername(nonCreatorUser.getUsername());
                userService.deleteUserByUsername(existingUser.getUsername());
            } catch (ApiException e) {

            }

            try {
                userService.addUser(creatorUser);
                userService.addUser(nonCreatorUser);
                userService.addUser(existingUser);

                offerToDeleteGeneratedId = offerService.addOffer(
                        offerToDelete,
                        existingUser.getUsername()
                );

                existingOfferId = offerService.addOffer(
                        existingOffer,
                        existingUser.getUsername()
                );
            } catch (ApiException e) {
                fail();
            }

            init = true;
        }
    }

    @AfterClass
    public static void clean() {
        try {
            offerService.deleteOfferById(offerToAddGeneratedId, creatorUser.getUsername());
            offerService.deleteOfferById(existingOfferId, existingUser.getUsername());

            userService.deleteUserByUsername(creatorUser.getUsername());
            userService.deleteUserByUsername(nonCreatorUser.getUsername());
            userService.deleteUserByUsername(existingUser.getUsername());
        } catch (ApiException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAddOffer() {
        long generatedId = -1;

        try {
            generatedId = offerService.addOffer(offerToAdd, creatorUser.getUsername());
        } catch (ApiException e) {
            fail(e.getMessage());
        }

        offerToAddGeneratedId = generatedId;

        Boolean exists = offerService.offerIdExists(generatedId);
        Assert.assertTrue(exists);

        Offer savedOffer = offerService.getOfferById(generatedId);

        Assert.assertNotNull(savedOffer);

        Assert.assertEquals(offerToAdd.getTitle(), savedOffer.getTitle());
        Assert.assertEquals(offerToAdd.getDescription(), savedOffer.getDescription());
        Assert.assertEquals(offerToAdd.getCreator(), savedOffer.getCreator());
        Assert.assertEquals(offerToAdd.getPrice(), savedOffer.getPrice());

    }

    @Test
    public void testAddOfferException() {
        String errorMessage = "method did not throw exception";
        String expectedMessage = "Please log in";

        try {
            offerService.addOffer(offerToAdd, nonExistingUser.getUsername());
            fail(errorMessage);
        } catch (ApiException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }

        String orginalTitle = offerToAdd.getTitle();
        String orginalDescription = offerToAdd.getDescription();
        Float orginalPrice = offerToAdd.getPrice();

        offerToAdd.setTitle("ab");

        try {
            offerService.addOffer(offerToAdd, creatorUser.getUsername());
            fail(errorMessage);
        } catch (ApiException e) {
            Assert.assertEquals(offerValidator.getTitleErrorMessage(), e.getMessage());
        }

        offerToAdd.setTitle(orginalTitle);
        StringBuilder tooLongDescriptionBuilder = new StringBuilder();
        for (int i = 0; i < 301; ++i) {
            tooLongDescriptionBuilder.append("x");
        }
        String tooLongDescription = tooLongDescriptionBuilder.toString();

        offerToAdd.setDescription(tooLongDescription);

        try {
            offerService.addOffer(offerToAdd, creatorUser.getUsername());
            fail(errorMessage);
        } catch (ApiException e) {
            Assert.assertEquals(offerValidator.getDescriptionErrorMessage(), e.getMessage());
        }

        offerToAdd.setDescription(orginalDescription);
        offerToAdd.setPrice((float) (Math.pow(10, 14)));

        try {
            offerService.addOffer(offerToAdd, creatorUser.getUsername());
            fail(errorMessage);
        } catch (ApiException e) {
            Assert.assertEquals(offerValidator.getPriceErrorMessage(), e.getMessage());
        }

        offerToAdd.setPrice(orginalPrice);
    }

    @Test
    public void testGetOfferByCreator() {
        List<Offer> creatorOfferList = null;

        try {
            creatorOfferList = offerService.getOfferByCreator(creatorUser.getUsername());
        } catch (ApiException e) {
            fail(e.getMessage());
        }

        Assert.assertEquals(1, creatorOfferList.size());

        Offer offer = creatorOfferList.get(0);

        Assert.assertEquals(offerToAdd.getTitle(), offer.getTitle());
        Assert.assertEquals(offerToAdd.getDescription(), offer.getDescription());
        Assert.assertEquals(offerToAdd.getCreator(), offer.getCreator());
        Assert.assertEquals(offerToAdd.getPrice(), offer.getPrice());
    }

    @Test
    public void testDeleteOffer() {
        try {
            offerService.deleteOfferById(offerToDeleteGeneratedId, existingUser.getUsername());
        } catch (ApiException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteOfferException() {
        String errorMessage = "method did not throw exception";
        String expectedMessage = "Please log in";

        try {
            offerService.deleteOfferById(existingOfferId, nonExistingUser.getUsername());
            fail(errorMessage);
        } catch (ApiException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }

        long nonExistingOfferId = -1;
        Boolean exists = offerService.offerIdExists(nonExistingOfferId);
        Assert.assertFalse(exists);

        expectedMessage = "Offer does not exist";

        try {
            offerService.deleteOfferById(nonExistingOfferId, existingUser.getUsername());
            fail(errorMessage);
        } catch (ApiException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }

        expectedMessage = "You can only delete your offers";

        try {
            offerService.deleteOfferById(existingOfferId, nonCreatorUser.getUsername());
            fail(errorMessage);
        } catch (ApiException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testUpdateOffer() {
        String titleUpdate = "updated_title";
        String descriptionUpdate = "updated_description";
        Float priceUpdate = 2.00f;

        Offer offerUpdate = new Offer(
                -1,
                titleUpdate,
                descriptionUpdate,
                priceUpdate,
                "",
                null
        );

        try {
            offerService.updateOffer(offerUpdate, existingUser.getUsername(), existingOfferId);
        } catch (ApiException e) {
            fail(e.getMessage());
        }

        Offer updatedOffer = offerService.getOfferById(existingOfferId);

        Assert.assertNotNull(updatedOffer);

        Assert.assertEquals(existingOfferId, updatedOffer.getId());
        Assert.assertEquals(titleUpdate, updatedOffer.getTitle());
        Assert.assertEquals(descriptionUpdate, updatedOffer.getDescription());
        Assert.assertEquals(priceUpdate, updatedOffer.getPrice());
        Assert.assertEquals(existingUser.getUsername(), updatedOffer.getCreator());
    }

    @Test
    public void testUpdateOfferException() {
        String errorMessage = "method did not throw exception";
        String expectedMessage = "Offer does not exist";

        String titleUpdate = "updated_title_1";
        String descriptionUpdate = "updated_description_1";
        Float priceUpdate = 3.00f;

        Offer offerUpdate = new Offer(
                -1,
                titleUpdate,
                descriptionUpdate,
                priceUpdate,
                "",
                null
        );

        long nonExistingOfferId = -1;
        Boolean exists = offerService.offerIdExists(nonExistingOfferId);
        Assert.assertFalse(exists);

        try {
            offerService.updateOffer(offerUpdate, existingUser.getUsername(), nonExistingOfferId);
            fail(errorMessage);
        } catch (ApiException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }

        expectedMessage = "Please log in";

        try {
            offerService.updateOffer(offerUpdate, nonExistingUser.getUsername(), existingOfferId);
            fail(errorMessage);
        } catch (ApiException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }

        expectedMessage = "You can only update your offers";

        try {
            offerService.updateOffer(offerUpdate, nonCreatorUser.getUsername(), existingOfferId);
            fail(errorMessage);
        } catch (ApiException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }

        String orginalTitle = offerUpdate.getTitle();
        String orginalDescription = offerUpdate.getDescription();
        Float orginalPrice = offerUpdate.getPrice();

        offerUpdate.setTitle("ab");

        expectedMessage = offerValidator.getTitleErrorMessage();

        try {
            offerService.updateOffer(offerUpdate, existingUser.getUsername(), existingOfferId);
            fail(errorMessage);
        } catch (ApiException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }

        offerUpdate.setTitle(orginalTitle);
        StringBuilder tooLongDescriptionBuilder = new StringBuilder();
        for (int i = 0; i < 301; ++i) {
            tooLongDescriptionBuilder.append("x");
        }
        String tooLongDescription = tooLongDescriptionBuilder.toString();
        offerUpdate.setDescription(tooLongDescription);

        expectedMessage = offerValidator.getDescriptionErrorMessage();

        try {
            offerService.updateOffer(offerUpdate, existingUser.getUsername(), existingOfferId);
            fail(errorMessage);
        } catch (ApiException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }

        offerUpdate.setDescription(orginalDescription);
        offerUpdate.setPrice(-1.00f);

        expectedMessage = offerValidator.getPriceErrorMessage();

        try {
            offerService.updateOffer(offerUpdate, existingUser.getUsername(), existingOfferId);
            fail(errorMessage);
        } catch (ApiException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }
}
