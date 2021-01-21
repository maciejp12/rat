package com.maciejp.rat.user;

import com.maciejp.rat.exception.RegisterException;
import com.maciejp.rat.exception.UserSelectionException;
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

    public User getUserByUsername(String username) throws UserSelectionException {
        User user = userDao.selectUserByUsername(username);

        if (user == null) {
            throw new UserSelectionException("User does not exist", HttpStatus.BAD_REQUEST);
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

    public long addUser(User user) throws RegisterException {
        if (!userValidator.validateName(user.getUsername())) {
            throw new RegisterException(userValidator.getUsernameErrorMessage(), HttpStatus.BAD_REQUEST);
        }

        if (!userValidator.validatePassowrd(user.getPassword())) {
            throw new RegisterException(userValidator.getPasswordErrorMessage(), HttpStatus.BAD_REQUEST);
        }

        if (!userValidator.validateEmail(user.getEmail())) {
            throw new RegisterException("Invalid email", HttpStatus.BAD_REQUEST);
        }

        if (!userValidator.validatePhoneNumber(user.getPhoneNumber())) {
            throw new RegisterException("Invalid phone number", HttpStatus.BAD_REQUEST);
        }

        if (usernameExists(user.getUsername())) {
            throw new RegisterException("Username already in use", HttpStatus.CONFLICT);
        }

        if (emailExists(user.getEmail())) {
            throw new RegisterException("Email already in use", HttpStatus.CONFLICT);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDao.insertUser(user);
    }


}
