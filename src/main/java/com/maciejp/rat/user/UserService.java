package com.maciejp.rat.user;

import com.maciejp.rat.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;

    private final UserValidator userValidator;

    @Autowired
    public UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.userValidator = new UserValidator();
    }

    public User getUserById(long id) {
        return userDao.selectUserById(id);
    }

    public User getUserByUsername(String username) throws ApiException {
        User user = userDao.selectUserByUsername(username);

        if (user == null) {
            throw new ApiException("User does not exist", HttpStatus.BAD_REQUEST);
        }

        return user;
    }

    public Boolean usernameExists(String username) {
        return userDao.selectUsernameExists(username);
    }

    public Boolean emailExists(String email) {
        return userDao.selectEmailExists(email);
    }

    public Boolean userIdExists(long id) {
        return userDao.selectIdExists(id);
    }

    public long addUser(User user) throws ApiException {
        if (!userValidator.validateName(user.getUsername())) {
            throw new ApiException(userValidator.getUsernameErrorMessage(), HttpStatus.BAD_REQUEST);
        }

        if (!userValidator.validatePassowrd(user.getPassword())) {
            throw new ApiException(userValidator.getPasswordErrorMessage(), HttpStatus.BAD_REQUEST);
        }

        if (!userValidator.validateEmail(user.getEmail())) {
            throw new ApiException("Invalid email", HttpStatus.BAD_REQUEST);
        }

        if (!userValidator.validatePhoneNumber(user.getPhoneNumber())) {
            throw new ApiException("Invalid phone number", HttpStatus.BAD_REQUEST);
        }

        if (usernameExists(user.getUsername())) {
            throw new ApiException("Username already in use", HttpStatus.CONFLICT);
        }

        if (emailExists(user.getEmail())) {
            throw new ApiException("Email already in use", HttpStatus.CONFLICT);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDao.insertUser(user);
    }

    public User deleteUserByUsername(String username) throws ApiException {
        if (!usernameExists(username)) {
            throw new ApiException("User does not exist", HttpStatus.NOT_FOUND);
        }

        User deletedUser = getUserByUsername(username);
        int delete = userDao.deleteUserByUsername(username);

        if (delete == 0) {
            throw new ApiException("Cannot delete this user", HttpStatus.BAD_REQUEST);
        }

        return deletedUser;
    }
}
