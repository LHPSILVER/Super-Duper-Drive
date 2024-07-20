package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface NoteMapper {
  @Insert(
      "INSERT INTO NOTES (notetitle, notedescription, userid) "
          + "VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
  @Options(useGeneratedKeys = true, keyProperty = "noteId")
  int create(Note note);

  @Update(
      "UPDATE NOTES SET notetitle = #{noteTitle},notedescription = #{noteDescription} "
          + "WHERE noteid = #{noteId} AND userid = #{userId}")
  int update(Note newNote);

  @Select("SELECT * FROM NOTES WHERE noteid = #{noteId} AND userid = #{userId}")
  Note findByNoteIdAndUserId(int noteId, int userId);

  @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
  List<Note> findByUserId(int userId);

  @Delete("DELETE FROM NOTES WHERE noteid = #{noteId} AND userid = #{userId}")
  int deleteByUserId(int noteId, int userId);
}
