package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CredentialMapper {
  @Insert(
      "INSERT INTO CREDENTIALS (url, username, key, password, userid) "
          + "VALUES(#{url}, #{userName}, #{key}, #{password}, #{userId})")
  @Options(useGeneratedKeys = true, keyProperty = "credentialId")
  int create(Credential credential);

  @Select("SELECT * FROM CREDENTIALS WHERE url = #{url} AND userid = #{userId}")
  Credential findByUrlAndUserId(String url, Integer userId);

  @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
  List<Credential> findByUserId(int userId);

  @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId} AND userid = #{userId}")
  int deleteByCredentialIdAndUserid(Integer credentialId, Integer userId);

  @Update(
      "UPDATE CREDENTIALS SET url = #{url},username = #{userName},password = #{password},key = #{key}"
          + "WHERE credentialid = #{credentialId} AND userid = #{userId}")
  int update(Credential credential);
}
