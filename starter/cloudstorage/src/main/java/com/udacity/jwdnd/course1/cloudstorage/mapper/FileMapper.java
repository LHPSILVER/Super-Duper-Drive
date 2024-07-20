package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FileMapper {
  @Insert(
      "INSERT INTO FILES (filename, contenttype, filesize, userid, filedata)"
          + "VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
  @Options(useGeneratedKeys = true, keyProperty = "fileId")
  int create(File file);

  @Select("SELECT * FROM FILES WHERE filename = #{fileName} AND userid = #{userId}")
  File findByFileNameAndUseId(String fileName, Integer userId);

  @Select("SELECT * FROM FILES WHERE userid = #{userId}")
  List<File> findByUserId(Integer userId);

  @Delete("DELETE FROM FILES WHERE filename = #{fileName} AND userid = #{userId}")
  int deleteByFileNameAndUserId(String fileName, Integer userId);
}
