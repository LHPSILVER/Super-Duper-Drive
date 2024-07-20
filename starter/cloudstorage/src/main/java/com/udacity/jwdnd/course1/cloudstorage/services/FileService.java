package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpSession;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
  private final FileMapper fileMapper;

  public FileService(FileMapper fileMapper) {
    this.fileMapper = fileMapper;
  }

  public static void showToastMessage(
      String title, String content, String type, HttpSession session) {
    session.setAttribute("messageContent", content);
    session.setAttribute("messageTitle", title);
    session.setAttribute("messageType", type);
  }

  public int create(MultipartFile file, Integer userId) throws IOException {
    byte[] bytes = file.getInputStream().readAllBytes();
    File newUploadedFile =
        new File(
            null,
            file.getOriginalFilename(),
            file.getContentType(),
            Long.valueOf(file.getSize()).toString(),
            userId,
            Base64.encodeBase64String(bytes));
    if (isDuplicateFileName(newUploadedFile)) {
      return -1;
    }
    return fileMapper.create(newUploadedFile);
  }

  public File findByFileNameAndUseId(String fileName, int userId) {
    return fileMapper.findByFileNameAndUseId(fileName, userId);
  }

  public List<File> findByUserId(Integer userId) {
    return fileMapper.findByUserId(userId);
  }

  public int deleteByFileNameAndUserId(String fileName, Integer userId) {
    return fileMapper.deleteByFileNameAndUserId(fileName, userId);
  }

  public boolean isDuplicateFileName(File file) {
    return Objects.nonNull(findByFileNameAndUseId(file.getFileName(), file.getUserId()));
  }
}
