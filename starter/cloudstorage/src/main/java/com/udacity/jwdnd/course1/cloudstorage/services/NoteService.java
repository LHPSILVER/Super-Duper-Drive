package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class NoteService {
  private final NoteMapper noteMapper;

  public NoteService(NoteMapper noteMapper) {
    this.noteMapper = noteMapper;
  }

  public static void showToastMessage(
      String title, String content, String type, HttpSession session) {
    session.setAttribute("messageContent", content);
    session.setAttribute("messageTitle", title);
    session.setAttribute("messageType", type);
  }

  public List<Note> findByUserId(int userId) {
    return noteMapper.findByUserId(userId);
  }

  public Integer deleteByUserId(int noteId, int userId) {
    return noteMapper.deleteByUserId(noteId, userId);
  }

  public Integer create(Note note) {
    return noteMapper.create(note);
  }

  public Integer update(Note note) {

    return noteMapper.update(note);
  }

  public boolean isNoteContentInvalid(Note note, HttpSession session) {
    if (note.getNoteTitle().isBlank()) {
      showToastMessage("Note", "Note title is missing", "error", session);
      return true;
    }
    if (note.getNoteDescription().isBlank()) {
      showToastMessage("Note", "Note description is missing", "error", session);
      return true;
    }
    return false;
  }
}
