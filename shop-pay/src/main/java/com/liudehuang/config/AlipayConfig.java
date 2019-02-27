package com.liudehuang.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */
@Component
@PropertySource(value={"classpath:alipayConfig.properties"},encoding = "utf-8")
public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id;
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key;
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key;

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url;

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url;

	// 签名方式
	public static String sign_type;
	
	// 字符编码格式
	public static String charset;
	
	// 支付宝网关
	public static String gatewayUrl;
	
	// 支付宝网关
	public static String log_path;

	@Value("${app_id}")
    public void setApp_id(String app_id) {
        AlipayConfig.app_id = app_id;
    }

    @Value("${merchant_private_key}")
    public void setMerchant_private_key(String merchant_private_key) {
        AlipayConfig.merchant_private_key = merchant_private_key;
    }

    @Value("${alipay_public_key}")
    public void setAlipay_public_key(String alipay_public_key) {
        AlipayConfig.alipay_public_key = alipay_public_key;
    }

    @Value("${notify_url}")
    public void setNotify_url(String notify_url) {
        AlipayConfig.notify_url = notify_url;
    }

    @Value("${return_url}")
    public void setReturn_url(String return_url) {
        AlipayConfig.return_url = return_url;
    }

    @Value("${sign_type}")
    public void setSign_type(String sign_type) {
        AlipayConfig.sign_type = sign_type;
    }

    @Value("${charset}")
    public void setCharset(String charset) {
        AlipayConfig.charset = charset;
    }

    @Value("${gatewayUrl}")
    public void setGatewayUrl(String gatewayUrl) {
        AlipayConfig.gatewayUrl = gatewayUrl;
    }

    @Value("${log_path}")
    public void setLog_path(String log_path) {
        AlipayConfig.log_path = log_path;
    }


    //↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

