package com.liudehuang.constants;

/**
 * @author liudehuang
 * @date 2018/12/13 13:57
 */
public interface Constants {
    /**
     * 响应请求成功
     */
    String HTTP_RES_CODE_200_VALUE = "SUCCESS";
    /**
     * 系统错误
     */
    String HTTP_RES_CODE_500_VALUE = "FAILED";
    /**
     * 响应请求成功code
     */
    Integer HTTP_RES_CODE_200 = 200;
    /**
     * 响应请求失败：系统错误
     */
    Integer HTTP_RES_CODE_500 = 500;

    /**
     *  未关联qq账号
     */
    Integer HTTP_RES_CODE_201 = 201;
    /**
     * 发送邮件
     */
    String MSG_EMAIL = "email";

    /**
     * token
     */
    String TOKEN_MEMBER = "TOKEN_MEMBER";

    /**
     * 支付token前缀
     */
    String PAY_TOEKN = "PAY_TOKEN";

    /**
     * 支付失败
     */
    String PAY_FAIL = "FAIL";

    /**
     * 支付成功
     */
    String PAY_SUCCESS = "SUCCESS";

    /**
     *  COOKIE 会员token名称
     */
    String COOKIE_MEMBER_TOKEN = "COOKIE_MEMBER_TOKEN";

    /**
     * form表单token前缀
     */
    String FORM_COMMIT_TOKEN = "FORM_COMMIT_TOKEN";

    /**
     * token过期时间
     */
    Long TOKEN_MEMBER_TIME = 60*60*24*90L;

    /**
     * 支付token的过期时间
     */
    Long PAY_TOKEN_TIME = 60*15L;

    /**
     * cookie
     */
    int COOKIE_TOKEN_MEMBER_TIME = 60*60*24*90;
    /**
     * form表单提交token过期时间
     */
    Long FORM_COMMIT_TOKEN_TIME = 60*60L;
    /**
     * token放在请求头
     */
    String EXTAPIHEAD = "head";
    /**
     * token放在form表单中
     */
    String EXTAPIFROM = "from";
}
