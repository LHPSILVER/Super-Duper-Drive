package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NoteController {
  private static final String IS_SHOW_MODAL = "isShowModal";
  private static final String SUCCESS = "success";
  private static final String ERROR = "error";
  private static final String USER_ID = "userId";
  private final NoteService noteService;
  private final Map<String, String> commonModelObject;

  public NoteController(NoteService noteService) {
    this.noteService = noteService;
    this.commonModelObject = new HashMap<>();
    commonModelObject.put("note", "active show");
    commonModelObject.put("noteTabPanel", "show active");
  }

  private void resetCommonModelObject() {
    commonModelObject.put(IS_SHOW_MODAL, "false");
  }

  @GetMapping(value = "/notes")
  public String getNotes(Model model, HttpSession session) {
    resetCommonModelObject();
    if (Objects.isNull(session.getAttribute(USER_ID))) {
      return "redirect:/login";
    }
    int userId = (int) session.getAttribute(USER_ID);
    List<Note> noteList = noteService.findByUserId(userId);
    commonModelObject.put(IS_SHOW_MODAL, (String) session.getAttribute(IS_SHOW_MODAL));
    model.addAttribute("noteList", noteList);
    model.addAllAttributes(commonModelObject);
    session.removeAttribute(IS_SHOW_MODAL);
    return "home";
  }

  @PostMapping(value = "/notes/create-note")
  public String createNote(@ModelAttribute Note note, HttpSession session) {
    int isCreateNoteSuccess;
    int isUpdateNoteSuccess;
    resetCommonModelObject();
    int userId = (int) session.getAttribute(USER_ID);
    note.setUserId(userId);
    if (!noteService.isNoteContentInvalid(note, session)) {
      if (!Objects.isNull(note.getNoteId())) {
        isUpdateNoteSuccess = noteService.update(note);
        if (isUpdateNoteSuccess > -1) {
          NoteService.showToastMessage(
              "UpdateNote", "Note is successfully updated", SUCCESS, session);
        } else {
          NoteService.showToastMessage(
              "UpdateNote", "Note can not be updated. There's an error", ERROR, session);
        }
      } else {
        isCreateNoteSuccess = noteService.create(note);
        if (isCreateNoteSuccess > -1) {
          NoteService.showToastMessage(
              "CreateNote", "Note is successfully created", SUCCESS, session);
        } else {
          NoteService.showToastMessage(
              "CreateNote", "Note can not be created. There's an error", ERROR, session);
        }
      }
    }
    session.setAttribute(IS_SHOW_MODAL, "true");
    return "redirect:/notes";
  }

  @GetMapping(value = "/notes/delete-note")
  public String deleteNote(@RequestParam Integer noteId, HttpSession session) {
    resetCommonModelObject();
    int userId = (int) session.getAttribute(USER_ID);
    int isDeleteNoteSuccess = noteService.deleteByUserId(noteId, userId);
    if (isDeleteNoteSuccess > -1) {
      NoteService.showToastMessage("DeleteNote", "Note is successfully deleted", SUCCESS, session);
    } else {
      NoteService.showToastMessage("DeleteNote", "Note is not found", ERROR, session);
    }
    session.setAttribute(IS_SHOW_MODAL, "true");
    return "redirect:/notes";
  }
}
