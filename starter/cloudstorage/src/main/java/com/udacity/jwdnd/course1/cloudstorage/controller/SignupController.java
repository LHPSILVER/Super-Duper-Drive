package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {
  private static final String IS_SIGNUP_SUCCESS = "isSignUpSuccess";
  private static final String IS_REGISTERED = "isRegistered";
  private static final String SIGNUP = "signup";
  private final UserService userService;
  private final EncryptionService encryptionService;
  private final Map<String, String> commonModelObject;

  @Value("${ENCRYPTION_KEY}")
  private String encryptionKey;

  public SignupController(UserService userService, EncryptionService encryptionService) {
    this.userService = userService;
    this.encryptionService = encryptionService;
    commonModelObject = new HashMap<>() {};
  }

  private void resetCommonModelObject() {
    commonModelObject.put(IS_REGISTERED, Boolean.FALSE.toString());
    commonModelObject.put(IS_SIGNUP_SUCCESS, Boolean.FALSE.toString());
  }

  @GetMapping(value = "/signup")
  private String initSignup(Model model) {
    resetCommonModelObject();
    User mockUser = new User(null, "", "", "", "", "");
    model.addAttribute("user", mockUser);
    return SIGNUP;
  }

  @PostMapping(value = "/signup")
  public String createUser(
      @ModelAttribute User user,
      Model model,
      HttpServletResponse httpServletResponse,
      HttpSession session)
      throws IOException {
    String userName = user.getUserName();
    resetCommonModelObject();
    if (!userService.isUsernameAvailable(userName)) {
      commonModelObject.put(IS_REGISTERED, Boolean.TRUE.toString());
      model.addAllAttributes(commonModelObject);
      return SIGNUP;
    }
    user.setPassword(encryptionService.encryptValue(user.getPassword(), encryptionKey));
    int index = userService.create(user);

    if (index > -1) {
      session.setAttribute(IS_SIGNUP_SUCCESS, Boolean.TRUE.toString());
      model.addAllAttributes(commonModelObject);
      httpServletResponse.sendRedirect("/login");
    } else {
      session.setAttribute(IS_SIGNUP_SUCCESS, Boolean.FALSE.toString());
      model.addAllAttributes(commonModelObject);
    }
    return SIGNUP;
  }
}
