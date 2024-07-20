package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CredentialController {
  private static final String IS_SHOW_MODAL = "isShowModal";
  private static final String SUCCESS = "success";
  private static final String ERROR = "error";
  private static final String USER_ID = "userId";

  private final CredentialService credentialService;
  private final EncryptionService encryptionService;
  private final Map<String, String> commonModelObject;

  @Value("${ENCRYPTION_KEY}")
  private String encryptionKey;

  public CredentialController(
      CredentialService credentialService, EncryptionService encryptionService) {
    this.credentialService = credentialService;
    this.encryptionService = encryptionService;
    this.commonModelObject = new HashMap<>();
    commonModelObject.put("credential", "active show");
    commonModelObject.put("credentialTabPanel", "show active");
  }

  private void resetCommonModelObject() {
    commonModelObject.put(IS_SHOW_MODAL, "false");
  }

  @GetMapping(value = "/credentials")
  public String findCredentials(Model model, HttpSession session) {
    resetCommonModelObject();
    if (Objects.isNull(session.getAttribute(USER_ID))) {
      return "redirect:/login";
    }
    int userId = (int) session.getAttribute(USER_ID);
    List<Credential> credentialList =
        credentialService.findByUserId(userId).stream()
            .map(
                credential ->
                    new Credential(
                        credential.getCredentialId(),
                        credential.getUrl(),
                        credential.getUserName(),
                        credential.getKey(),
                        credential.getPassword(),
                        credential.getUserId()))
            .collect(Collectors.toList());
    commonModelObject.put(IS_SHOW_MODAL, (String) session.getAttribute(IS_SHOW_MODAL));
    model.addAttribute("credentialList", credentialList);
    model.addAttribute("encryptionService", encryptionService);
    model.addAttribute("encryptionKey", encryptionKey);
    model.addAllAttributes(commonModelObject);
    session.removeAttribute(IS_SHOW_MODAL);
    return "home";
  }

  @PostMapping(value = "/credentials/create-credential")
  public String createCredential(@ModelAttribute Credential credential, HttpSession session) {
    int isCreateCredentialSuccess;
    int isUpdateCredentialSuccess;
    resetCommonModelObject();
    int userId = (int) session.getAttribute(USER_ID);
    credential.setUserId(userId);
    if (!credentialService.isCredentialContentInvalid(credential, session)) {
      if (!Objects.isNull(credential.getCredentialId())) {
        isUpdateCredentialSuccess = credentialService.update(credential);
        if (isUpdateCredentialSuccess > -1) {
          CredentialService.showToastMessage(
              "UpdateCredential", "Credential is successfully updated", SUCCESS, session);
        } else {
          CredentialService.showToastMessage(
              "UpdateCredential", "Credential is not found", ERROR, session);
        }
      } else {
        isCreateCredentialSuccess = credentialService.create(credential);
        if (isCreateCredentialSuccess > -1) {
          CredentialService.showToastMessage(
              "CreateCredential", "Credential is successfully created", SUCCESS, session);
        } else {
          CredentialService.showToastMessage(
              "CreateCredential",
              "Credential can not be created. There's an error",
              ERROR,
              session);
        }
      }
    }
    session.setAttribute(IS_SHOW_MODAL, "true");
    return "redirect:/credentials";
  }

  @GetMapping(value = "/credentials/delete-credential")
  public String deleteCredential(@RequestParam Integer credentialId, HttpSession session) {
    resetCommonModelObject();
    int userId = (int) session.getAttribute(USER_ID);
    int isDeleteCredentialSuccess =
        credentialService.deleteByCredentialIdAndUserid(credentialId, userId);
    if (isDeleteCredentialSuccess > -1) {
      CredentialService.showToastMessage(
          "DeleteCredential", "Credential is successfully deleted", SUCCESS, session);
    } else {
      CredentialService.showToastMessage(
          "DeleteCredential", "Credential is not found", ERROR, session);
    }
    session.setAttribute(IS_SHOW_MODAL, "true");
    return "redirect:/credentials";
  }
}
