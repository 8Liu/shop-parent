package com.liudehuang.controller;

import com.alibaba.fastjson.JSONObject;
import com.liudehuang.base.TextMessage;
import com.liudehuang.util.CheckUtil;
import com.liudehuang.util.HttpClientUtil;
import com.liudehuang.util.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author liudehuang
 * @date 2018/12/25 20:52
 */
@Slf4j
@RestController
public class DispatCherController {
    /**
     * 青云客智能聊天机器人API
     */
    private static final String REQUEST_URL = "http://api.qingyunke.com/api.php?key=free&appid=0&msg=";
    /**
     * 服务器验证的接口地址
     * @param signature
     * @param timestamp
     * @return
     */
    @RequestMapping(value = "/dispatCherGet",method = RequestMethod.GET)
    public String dispatCherGet(String signature, String timestamp, String nonce, String echostr){
        //1、验证参数
        boolean checkSignature = CheckUtil.checkSignature(signature, timestamp, nonce);
        //2、参数验证成功之后，返回随机数
        if(!checkSignature){
            return null;
        }
        return echostr;
    }

    /**
     * 微信动作的推送
     * @return
     */
    @RequestMapping(value = "/dispatCherGet",method = RequestMethod.POST)
    public void dispatCherGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        //1、将xml转换成map格式
        Map<String, String> map = XmlUtils.parseXml(request);
        log.info("###收到微信消息###map:"+map.toString());
        if (StringUtils.isEmpty(map)){
            return ;
        }
        //2、判断消息类型
        String msgType = map.get("MsgType");
        //3、如果是文本类型，返回结果给微信服务器端
        PrintWriter writer = response.getWriter();
        /**
         * 根据不同的消息类型，返回不同的消息（）
         */
        switch (msgType){
            case "text":
                //开发者微信公众号
                String toUserName = map.get("ToUserName");
                //消息来自公众号
                String fromUserName = map.get("FromUserName");
                //消息内容
                String content = map.get("Content");
                //调用智能机器人接口
                String requestResultJson = HttpClientUtil.doGet(REQUEST_URL+content);
                JSONObject jsonObject = JSONObject.parseObject(requestResultJson);
                Integer resultCode = jsonObject.getInteger("result");
                String msg = null;
            /*    if(content.equals("刘德煌")){
                    //返回你爸爸
                    msg = setText("你爸爸",toUserName,fromUserName);
                }*/
                //如果调用机器人接口返回0，表示正确
                //result　状态，0表示正常，其它数字表示错误 content　信息内容 
                if (resultCode == 0) {
                    String resultContent = jsonObject.getString("content");
                    msg = setText(resultContent, toUserName,fromUserName);
                }else {
                    msg = setText("我现在有点忙.稍后回复您!", toUserName,fromUserName);
                }
                log.info("###给微信发送消息###map:"+msg);
                writer.write(msg);
                break;
            default:
                break;
        }
        writer.close();

    }


    public String setText(String content,String fromUserName,String toUserName){
        TextMessage textMessage = new TextMessage();
        textMessage.setContent(content);
        textMessage.setFromUserName(fromUserName);
        textMessage.setToUserName(toUserName);
        textMessage.setContent(content);
        textMessage.setMsgType("text");
        textMessage.setCreateTime(System.currentTimeMillis());
        //将实体类转换成xml格式
        String msg = XmlUtils.messageToXml(textMessage);
        return msg;
    }
}
