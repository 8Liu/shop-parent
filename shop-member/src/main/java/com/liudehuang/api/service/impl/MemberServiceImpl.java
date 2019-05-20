package com.liudehuang.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.liudehuang.api.service.MemberService;
import com.liudehuang.base.BaseApiService;
import com.liudehuang.base.BaseRedisService;
import com.liudehuang.base.ResponseBase;
import com.liudehuang.constants.Constants;
import com.liudehuang.dao.MemberDao;
import com.liudehuang.entity.UserEntity;
import com.liudehuang.mq.RegisterMailboxProducer;
import com.liudehuang.util.MD5Utils;
import com.liudehuang.util.TokenUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liudehuang
 * @date 2018/12/15 11:07
 */
@Slf4j
@RestController
public class MemberServiceImpl extends BaseApiService implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Autowired
    private RegisterMailboxProducer registerMailboxProducer;

    @Autowired
    private BaseRedisService baseRedisService;

    @Value("${messages.queue}")
    private String MESSAGESQUEUE;

    @ApiOperation("根据userId查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId",value = "用户id",required = true,dataType = "int")
    })
    @Override
    public ResponseBase findUserById(Integer userId) {
        UserEntity user = memberDao.findByID(userId);
        if(user==null){
            return setResultError("未查找到用户信息");
        }
        return setResultSuccess(user);
    }

    @Override
    public ResponseBase regUser(@RequestBody UserEntity user) {
        //参数验证
        String password = user.getPassword();
        if(StringUtils.isEmpty(password)){
            return setResultError("密码不能为空");
        }
        String newPassword = MD5Utils.MD5(password);
        user.setPassword(newPassword);
        int row = memberDao.insertUser(user);
        if(row<=0){
            return setResultError("注册用户信息失败！！");
        }

        //采用mq异步发送邮件通知
        String email = user.getEmail();
        String messAageJson = message(email);
        sendMsg(messAageJson);
        log.info("######email:{},messAageJson:{}",email,messAageJson);
        return setResultSuccess("用户注册成功");
    }

    @Override
    public ResponseBase login(@RequestBody UserEntity user) {
        //1、验证参数
        String username = user.getUsername();
        if(StringUtils.isEmpty(username)){
            return setResultError("用户名称不能为空");
        }
        String password = user.getPassword();
        if(StringUtils.isEmpty(password)){
            return setResultError("密码不能为空");
        }
        //2、数据库查找账号密码是否正确
        String newPassword = MD5Utils.MD5(password);
        UserEntity entity = memberDao.login(username, newPassword);

        return setLogin(entity);
    }

    @Override
    public ResponseBase findByTokenUser(@RequestParam("token") String token) {
        //1、验证参数
        if(StringUtils.isEmpty(token)){
            return setResultError("token不能为空!");
        }
        //2、从redis中，使用token查找对应的userid
        String strUserId = (String)baseRedisService.getString(token);
        if(StringUtils.isEmpty(strUserId)){
            return setResultError("token无效或者过期!");
        }
        //3、使用userids数据库查用户信息返回客户端
        Integer userId = Integer.parseInt(strUserId);
        UserEntity entity = memberDao.findByID(userId);
        if(entity==null){
            return setResultError("未查找到该用户信息");
        }
        entity.setPassword(null);
        return setResultSuccess(entity);
    }

    /**
     * 根据qqopenid获取客户信息
     * @param openid
     * @return
     */
    @Override
    public ResponseBase findByOpenIdUser(@RequestParam("openid") String openid) {
        //1、验证参数
        if(StringUtils.isEmpty(openid)){
            return setResultError("openid不能为空");
        }
        //2、使用openid查询数据库user表对应的数据信息
        UserEntity userEntity = memberDao.findByOpenIdUser(openid);
        if(StringUtils.isEmpty(userEntity)){
            return setResultError(Constants.HTTP_RES_CODE_201,"该openid没有关联");
        }
        //3、自动登录
        return setLogin(userEntity);
    }


    private ResponseBase setLogin(UserEntity entity){
        if(entity==null){
            return setResultError("账号或密码不正确");
        }
        //3、如果账号密码正确，对应生成token
        String token = TokenUtils.getMemberToken();
        //4、存放在redis中，key为token value为userid
        Integer userId = entity.getId();
        log.info("#####用户信息token存放在redis中... key为:{},value为:{}",token,userId);
        baseRedisService.setString(token,String.valueOf(userId), Constants.TOKEN_MEMBER_TIME);
        //5、直接返回token
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token",token);
        return setResultSuccess(jsonObject);
    }

    /**
     * qq授权登录
     * @param user
     * @return
     */
    @Override
    public ResponseBase qqLogin(@RequestBody UserEntity user) {
        //1、验证参数
        String openid = user.getOpenid();
        if(StringUtils.isEmpty(openid)){
            return setResultError("openid不能为空");
        }
        //2、先进行账号登录
        ResponseBase responseBase = login(user);
        if(!responseBase.getCode().equals(Constants.HTTP_RES_CODE_200)){
            return responseBase;
        }
        // 3.自动登录
        JSONObject jsonObjcet = (JSONObject) responseBase.getData();
        // 4. 获取token信息
        String memberToken = jsonObjcet.getString("token");
        ResponseBase userToken = findByTokenUser(memberToken);
        if (!userToken.getCode().equals(Constants.HTTP_RES_CODE_200)) {
            return userToken;
        }
        UserEntity userEntity = (UserEntity) userToken.getData();
        //5、修改用户openid
        Integer row = memberDao.updateByOpenIdUser(openid,userEntity.getId());
        if(row<=0){
            return setResultError("qq账号关联失败");
        }
        return responseBase;
    }

    /**
     * 根据微信openid查询用户信息
     * @param wxOpenid
     * @return
     */
    @Override
    public ResponseBase findByWxOpenIdUser(@RequestParam("wxOpenid") String wxOpenid) {
        //1、验证参数
        if(StringUtils.isEmpty(wxOpenid)){
            return setResultError("wxOpenid不能为空");
        }
        //2、使用openid查询数据库user表对应的数据信息
        UserEntity userEntity = memberDao.findByWxOpenIdUser(wxOpenid);
        if(StringUtils.isEmpty(userEntity)){
            return setResultError(Constants.HTTP_RES_CODE_201,"该用户没有关联微信账号");
        }
        //3、自动登录
        return setLogin(userEntity);
    }

    /**
     * 微信授权登录
     * @param user
     * @return
     */
    @Override
    public ResponseBase wxLogin(UserEntity user) {
        //1、验证参数
        String wxopenid = user.getWxopenid();
        if(StringUtils.isEmpty(wxopenid)){
            return setResultError("wxopenid不能为空");
        }
        //2、先进行账号登录
        ResponseBase responseBase = login(user);
        if(!responseBase.getCode().equals(Constants.HTTP_RES_CODE_200)){
            return responseBase;
        }
        // 3.自动登录
        JSONObject jsonObjcet = (JSONObject) responseBase.getData();
        // 4. 获取token信息
        String memberToken = jsonObjcet.getString("token");
        ResponseBase userToken = findByTokenUser(memberToken);
        if (!userToken.getCode().equals(Constants.HTTP_RES_CODE_200)) {
            return userToken;
        }
        UserEntity userEntity = (UserEntity) userToken.getData();
        //5、修改用户openid
        Integer row = memberDao.updateByWxOpenIdUser(wxopenid,userEntity.getId());
        if(row<=0){
            return setResultError("微信账号关联失败");
        }
        return responseBase;
    }

    @Override
    public ResponseBase memberService() {
        return setResultSuccess("我是会员服务");
    }

    /**
     * 用户满足mq接口协定
     * @param mail
     * @return
     */
    private String message(String mail) {
        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();
        header.put("interfaceType", "email");
        JSONObject content = new JSONObject();
        content.put("email", mail);
        root.put("header", header);
        root.put("content", content);
        return root.toJSONString();
    }

    /**
     *
     * @param json
     */
    private void sendMsg(String json) {
        ActiveMQQueue activeMQQueue = new ActiveMQQueue(MESSAGESQUEUE);
        registerMailboxProducer.sendMsg(activeMQQueue, json);

    }
}
