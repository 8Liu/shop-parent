package com.liudehuang.dao;

import com.liudehuang.entity.UserEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @author liudehuang
 * @date 2018/12/15 11:09
 */
@Mapper
@Repository
public interface MemberDao {
    @Select("select wxopenid,openid,id,username,password,phone,email,created,updated from mb_user where id=#{userId}")
    UserEntity findByID(@Param("userId") Integer userId);

    @Insert("insert into mb_user(username,password,phone,email)values(#{username},#{password},#{phone},#{email})")
    Integer insertUser(UserEntity userEntity);

    @Select("select wxopenid,openid,id,username,password,phone,email,created,updated from mb_user where username=#{username} and password=#{password}")
    UserEntity login(@Param("username") String username,@Param("password") String password);

    @Select("select wxopenid,openid,id,username,password,phone,email,created,updated from mb_user where openid=#{openid}")
    UserEntity findByOpenIdUser(@Param("openid") String openid);

    @Select("select wxopenid,openid,id,username,password,phone,email,created,updated from mb_user where wxopenid=#{wxopenid}")
    UserEntity findByWxOpenIdUser(@Param("wxOpenid") String wxOpenid);

    @Update("update mb_user set openid = #{openid} where id=#{userId}")
    Integer updateByOpenIdUser(@Param("openid") String openid,@Param("userId") Integer userId);

    @Update("update mb_user set wxopenid = #{wxopenid} where id=#{userId}")
    Integer updateByWxOpenIdUser(@Param("wxopenid")String wxopenid,@Param("userId") Integer userId);


}
