package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {
  private static final String CREDENTIAL = "Credential";
  private static final String ERROR = "error";
  private final CredentialMapper credentialMapper;

  public CredentialService(CredentialMapper credentialMapper) {
    this.credentialMapper = credentialMapper;
  }

  public static void showToastMessage(
      String title, String content, String type, HttpSession session) {
    session.setAttribute("messageContent", content);
    session.setAttribute("messageTitle", title);
    session.setAttribute("messageType", type);
  }

  public List<Credential> findByUserId(int userId) {
    return credentialMapper.findByUserId(userId);
  }

  public int create(Credential credential) {
    return credentialMapper.create(credential);
  }

  public int deleteByCredentialIdAndUserid(Integer credentialId, int userId) {
    return credentialMapper.deleteByCredentialIdAndUserid(credentialId, userId);
  }

  public int update(Credential credential) {
    return credentialMapper.update(credential);
  }

  public boolean isCredentialContentInvalid(Credential credential, HttpSession session) {
    if (credential.getUrl().isBlank()) {
      showToastMessage(CREDENTIAL, "Url is missing", ERROR, session);
      return true;
    }
    if (credential.getUserName().isBlank()) {
      showToastMessage(CREDENTIAL, "UserName is missing", ERROR, session);
      return true;
    }
    if (credential.getPassword().isBlank()) {
      showToastMessage(CREDENTIAL, "Password is missing", ERROR, session);
      return true;
    }
    return false;
  }
}
