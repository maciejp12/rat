package com.maciejp.rat.user;

import com.maciejp.rat.RatApplication;
import com.maciejp.rat.exception.ApiException;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.fail;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = RatApplication.class)
public class UserServiceTest {

    private static boolean init = false;

    @Autowired
    private UserService userServiceField;

    private static UserService userService;

    private static UserValidator userValidator;

    private static User userToAdd = new User(
            -1,
            "test_username1",
            "test_email1@test.com",
            "test_password",
            "123456789",
            null
    );

    private static User fakeNameUserToAdd = new User(
            -1,
            "ab",
            "test_email2@test.com",
            "test_password",
            "123456789",
            null
    );

    private static User fakePasswordUserToAdd  = new User(
            -1,
            "test_username3",
            "test_email3test.com",
            "ab",
            "123456789",
            null
    );

    private static User fakeEmailUserToAdd = new User(
            -1,
            "test_username4",
            "test_email4@testcom",
            "test_password",
            "123456789",
            null
    );

    private static User fakePhoneNumberUserToAdd = new User(
            -1,
            "test_username5",
            "test_email5@test.com",
            "test_password",
            "x123456789",
            null
    );

    private static User userToDelete = new User(
            -1,
            "test_username6",
            "test_email6@test.com",
            "test_password",
            "123456789",
            null
    );

    private static User existingUser = new User(
            -1,
            "test_username7",
            "test_email7@test.com",
            "test_password",
            "123456789",
            null
    );

    private static User nonExistingUser = new User(
            -1,
            "test_username8",
            "test_email8@test.com",
            "test_password",
            "123456789",
            null
    );


    @Before
    public void before() {
        if (!init) {
            userService = userServiceField;
            userValidator = new UserValidator();

            try {
                userService.deleteUserByUsername(userToAdd.getUsername());
                userService.deleteUserByUsername(fakeNameUserToAdd.getUsername());
                userService.deleteUserByUsername(fakeEmailUserToAdd.getUsername());
                userService.deleteUserByUsername(fakePasswordUserToAdd.getUsername());
                userService.deleteUserByUsername(fakePhoneNumberUserToAdd.getUsername());
                userService.deleteUserByUsername(userToDelete.getUsername());
                userService.deleteUserByUsername(existingUser.getUsername());
                userService.deleteUserByUsername(nonExistingUser.getUsername());
            } catch (ApiException e) {

            }

            try {
                userService.addUser(userToDelete);
                userService.addUser(existingUser);
            } catch (ApiException e) {
                fail();
            }

            init = true;
        }
    }

    @AfterClass
    public static void clean() {
        try {
            userService.deleteUserByUsername(userToAdd.getUsername());
            userService.deleteUserByUsername(existingUser.getUsername());
        } catch (ApiException e) {
            fail();
        }
    }

    @Test
    public void testAddUser() {
        long id = userToAdd.getId();
        String username = userToAdd.getUsername();
        String password = userToAdd.getPassword();
        String email = userToAdd.getEmail();
        String phoneNumber = userToAdd.getPhoneNumber();
        Timestamp creationDate = userToAdd.getRegisterDate();

        User user = new User(
                id,
                username,
                email,
                password,
                phoneNumber,
                creationDate
        );

        try {
            userService.addUser(user);
        } catch (ApiException e) {
            fail(e.getMessage());
        }

        Boolean exists = userService.usernameExists(username);
        Assert.assertTrue(exists);

        exists = userService.emailExists(email);
        Assert.assertTrue(exists);

        User savedUser = userService.getUserByUsername(username);

        exists = userService.userIdExists(savedUser.getId());
        Assert.assertTrue(exists);

        Assert.assertNotNull(savedUser);
        Assert.assertEquals(username, savedUser.getUsername());
        Assert.assertEquals(email, savedUser.getEmail());
        Assert.assertEquals(phoneNumber, user.getPhoneNumber());

    }

    @Test
    public void testAddUserException() {
        String errorMessage = "method did not throw exception";
        String expectedMessage = userValidator.getUsernameErrorMessage();

        try {
            userService.addUser(fakeNameUserToAdd);
            fail(errorMessage);
        } catch (ApiException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }

        expectedMessage = userValidator.getPasswordErrorMessage();

        try {
            userService.addUser(fakePasswordUserToAdd);
            fail(errorMessage);
        } catch (ApiException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }

        expectedMessage = "Invalid email";

        try {
            userService.addUser(fakeEmailUserToAdd);
            fail(errorMessage);
        } catch (ApiException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }

        expectedMessage = "Invalid phone number";

        try {
            userService.addUser(fakePhoneNumberUserToAdd);
            fail(errorMessage);
        } catch (ApiException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testAddDuplicateUser() {
        User duplicateNameUser = new User(
                existingUser.getId(),
                existingUser.getUsername(),
                "x" + existingUser.getEmail(),
                existingUser.getPassword(),
                existingUser.getPhoneNumber(),
                existingUser.getRegisterDate()
        );

        Boolean exists = userService.usernameExists(duplicateNameUser.getUsername());
        Assert.assertTrue(exists);

        exists = userService.emailExists(duplicateNameUser.getEmail());
        Assert.assertFalse(exists);

        User duplicateEmailUser = new User(
                existingUser.getId(),
                "x" + existingUser.getUsername(),
                existingUser.getEmail(),
                existingUser.getPassword(),
                existingUser.getPhoneNumber(),
                existingUser.getRegisterDate()
        );

        exists = userService.usernameExists(duplicateEmailUser.getUsername());
        Assert.assertFalse(exists);

        exists = userService.emailExists(duplicateEmailUser.getEmail());
        Assert.assertTrue(exists);

        User duplicateNameAndEmailUser = new User(
                existingUser.getId(),
                existingUser.getUsername(),
                existingUser.getEmail(),
                existingUser.getPassword(),
                existingUser.getPhoneNumber(),
                existingUser.getRegisterDate()
        );

        exists = userService.usernameExists(duplicateNameAndEmailUser.getUsername());
        Assert.assertTrue(exists);

        exists = userService.emailExists(duplicateNameAndEmailUser.getEmail());
        Assert.assertTrue(exists);

        String errorMessage = "method did not throw exception";
        String expectedMessage = "Username already in use";

        try {
            userService.addUser(duplicateNameUser);
            fail(errorMessage);
        } catch (ApiException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }

        expectedMessage = "Email already in use";

        try {
            userService.addUser(duplicateEmailUser);
            fail(errorMessage);
        } catch (ApiException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }

        expectedMessage = "Username already in use";

        try {
            userService.addUser(duplicateNameAndEmailUser);
            fail(errorMessage);
        } catch (ApiException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testDeleteUser() {
        Boolean exists = userService.usernameExists(userToDelete.getUsername());
        Assert.assertTrue(exists);

        try {
            userService.deleteUserByUsername(userToDelete.getUsername());
        } catch (ApiException e) {
            fail(e.getMessage());
        }

        exists = userService.usernameExists(userToDelete.getUsername());
        Assert.assertFalse(exists);
    }

    @Test
    public void testDeleteUserException() {
        Boolean exists = userService.usernameExists(nonExistingUser.getUsername());
        Assert.assertFalse(exists);

        try {
            userService.deleteUserByUsername(nonExistingUser.getUsername());
            fail("method did not throw exception");
        } catch (ApiException e) {
            Assert.assertEquals("User does not exist", e.getMessage());
        }

        exists = userService.usernameExists(nonExistingUser.getUsername());
        Assert.assertFalse(exists);
    }

}
