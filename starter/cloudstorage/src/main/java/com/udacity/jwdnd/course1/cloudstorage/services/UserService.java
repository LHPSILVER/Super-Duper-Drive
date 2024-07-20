package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserMapper userMapper;
  private final HashService hashService;

  public UserService(UserMapper userMapper, HashService hashService) {
    this.userMapper = userMapper;
    this.hashService = hashService;
  }

  public boolean isUsernameAvailable(String username) {
    return userMapper.findByUserName(username) == null;
  }

  public int create(User user) {
    var random = new SecureRandom();
    byte[] salt = new byte[16];
    random.nextBytes(salt);
    String encodedSalt = Base64.getEncoder().encodeToString(salt);
    String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
    return userMapper.create(
        new User(
            null,
            user.getUserName(),
            encodedSalt,
            hashedPassword,
            user.getFirstName(),
            user.getLastName()));
  }

  public User findByUserName(String userName) {
    return userMapper.findByUserName(userName);
  }
}
