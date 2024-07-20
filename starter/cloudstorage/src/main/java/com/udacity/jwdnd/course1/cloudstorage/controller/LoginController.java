package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
  private static final String USER_INFO_ERROR = "userInfoError";
  private static final String IS_LOGGED_OUT = "isLoggedOut";
  private static final String IS_LOGOUT = "isLogout";
  private static final String IS_SIGNUP_SUCCESS = "isSignUpSuccess";
  private final UserService userService;
  private final Map<String, String> commonModelObject;

  public LoginController(UserService userService) {
    this.userService = userService;
    this.commonModelObject = new HashMap<>();
    this.commonModelObject.put(USER_INFO_ERROR, Boolean.FALSE.toString());
    this.commonModelObject.put(IS_LOGGED_OUT, Boolean.FALSE.toString());
    commonModelObject.put("file", "active show");
    commonModelObject.put("fileTabPanel", "show active");
  }

  private void resetCommonModelObject() {
    commonModelObject.put(USER_INFO_ERROR, Boolean.FALSE.toString());
    commonModelObject.put(IS_LOGGED_OUT, Boolean.FALSE.toString());
    commonModelObject.put(IS_SIGNUP_SUCCESS, Boolean.FALSE.toString());
  }

  @GetMapping(value = "/login")
  public String loginView(Model model, HttpSession session) {
    resetCommonModelObject();
    if (Objects.nonNull(session.getAttribute(IS_LOGOUT))
        && (boolean) session.getAttribute(IS_LOGOUT)) {
      commonModelObject.put(IS_LOGGED_OUT, String.valueOf(session.getAttribute(IS_LOGOUT)));
    }
    commonModelObject.put(
        IS_SIGNUP_SUCCESS, String.valueOf(session.getAttribute(IS_SIGNUP_SUCCESS)));
    model.addAllAttributes(commonModelObject);
    session.removeAttribute(IS_SIGNUP_SUCCESS);
    session.removeAttribute(IS_LOGOUT);
    return "login";
  }

  @PostMapping(value = "/login-action")
  public String loginAction(@ModelAttribute User user, Model model, HttpSession session) {
    User userSavedInDataBase = userService.findByUserName(user.getUserName());
    if (userService.isUsernameAvailable(user.getUserName())) {
      commonModelObject.put(USER_INFO_ERROR, "true");
    }
    if (!Objects.isNull(userSavedInDataBase)) {
      session.setAttribute("userId", userSavedInDataBase.getUserId());
    }
    model.addAllAttributes(commonModelObject);
    if (Objects.equals(commonModelObject.get(USER_INFO_ERROR), "true")) {
      return "login";
    }
    return "redirect:/files";
  }
}
