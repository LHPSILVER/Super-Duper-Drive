package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FileController {
  private static final String IS_SHOW_MODAL = "isShowModal";
  private static final String ERROR = "error";
  private static final String USER_ID = "userId";
  private static final String REDIRECT_FILES = "redirect:/files";
  private static final String FILE_UPLOAD = "FileUpload";
  private final Map<String, String> commonModelObject;
  private final FileService fileService;

  public FileController(FileService fileService) {
    this.fileService = fileService;
    this.commonModelObject = new HashMap<>();
    commonModelObject.put("file", "active show");
    commonModelObject.put("fileTabPanel", "show active");
  }

  private void resetCommonModelObject() {
    commonModelObject.put(IS_SHOW_MODAL, "false");
  }

  @GetMapping(value = "/files")
  public String findAllFiles(Model model, HttpSession session) {
    resetCommonModelObject();
    if (Objects.isNull(session.getAttribute(USER_ID))) {
      return "redirect:/login";
    }
    int userId = (int) session.getAttribute(USER_ID);
    List<File> fileList =
        fileService.findByUserId(userId).stream()
            .map(
                file ->
                    new File(
                        file.getFileId(),
                        file.getFileName(),
                        file.getContentType(),
                        file.getFileSize(),
                        file.getUserId(),
                        file.getFileData()))
            .collect(Collectors.toList());
    model.addAttribute("fileList", fileList);
    commonModelObject.put(IS_SHOW_MODAL, (String) session.getAttribute(IS_SHOW_MODAL));
    model.addAllAttributes(commonModelObject);
    session.removeAttribute(IS_SHOW_MODAL);
    session.removeAttribute("isSignUpSuccess");
    return "home";
  }

  @GetMapping(value = "/logout")
  public ModelAndView logoutAction(HttpSession session) {
    session.setAttribute("isLogout", true);
    session.removeAttribute(USER_ID);
    return new ModelAndView("redirect:/login");
  }

  @GetMapping(value = "/files/delete-file")
  public String deleteFile(@RequestParam String fileName, HttpSession session) {
    resetCommonModelObject();
    int userId = (int) session.getAttribute(USER_ID);
    int isDeleteSuccessful = fileService.deleteByFileNameAndUserId(fileName, userId);
    if (isDeleteSuccessful > -1) {
      FileService.showToastMessage(
          "FileDelete", "File is successfully deleted", "success", session);
    } else {
      FileService.showToastMessage("FileDelete", "File is not found", ERROR, session);
    }
    session.setAttribute(IS_SHOW_MODAL, "true");
    return REDIRECT_FILES;
  }

  @PostMapping(value = "/files/upload-file")
  public String uploadFile(
      @RequestParam("fileUpload") MultipartFile fileUpload, HttpSession session)
      throws IOException, SQLException {
    resetCommonModelObject();
    int userId = (int) session.getAttribute(USER_ID);
    if (fileUpload.isEmpty()) {
      FileService.showToastMessage(FILE_UPLOAD, "File is not selected", ERROR, session);
      session.setAttribute(IS_SHOW_MODAL, "true");
      return REDIRECT_FILES;
    }
    int isCreateFileSuccessful = fileService.create(fileUpload, userId);
    if (isCreateFileSuccessful > -1) {
      FileService.showToastMessage(
          FILE_UPLOAD, "FileUpload is successfully uploaded", "success", session);
    } else {
      FileService.showToastMessage(FILE_UPLOAD, "FileName is duplicate", ERROR, session);
    }
    session.setAttribute(IS_SHOW_MODAL, "true");
    return REDIRECT_FILES;
  }
}
