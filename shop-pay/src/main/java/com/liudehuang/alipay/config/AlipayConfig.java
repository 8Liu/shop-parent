package com.liudehuang.alipay.config;

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

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016092500590981";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCz3LdDJWlx+BmSQ1Bgt+Zw0KjsocDbp+qsk51kVdLAANHcTSUJs3WdcK+S5fRH8ZrFpNke/TLiXJhzCG/T8XLU0ycnPNoUpatpbTJUQwbDjLzXcXUA06lIcshud7e5KSJy13CKuraxu/wxUkx3GjeC16tTJO2L7anfFLEWnBJDyRgWJjIZKBV3Di1yUILhxhF5DfbgmqLlxtkipZbNeqSK8WGvLBSx8SRPettHmUAZlNg4V7vgNbs6iUZq8ziO9+vGadU9bmeh4/SpDTjE/vgfS3AItRx5dtapWaHlgksiFKas3+7Q1WXQejuAz5D/3crDUaFEA0swBvC9YQFxmTaJAgMBAAECggEAIWfA8culBfHabfvqlcwSFKnsdeak9yb8wwi3lscJ2XdNgukhE+gs1Az1zwa+lA4ffM3dNZA5PymLdvH+MxMJDNYa04LNzj/m0jHhwxyfyPn+qNT9siWdr0MCTuIVUWJ+b23sDRgYjnInqNFsOkK4RKjhvROCuxZtEm25ZkWebtA+whktR2BpOeb2jWsXSQpU3gpNbPN3yrbeb7mRWAQ22vYKv+AUA3eO8pcJf2epaA/sGm+nrSzEBiad+7E+s45o27YdFPINRzJ2J7jP2g7l7ZoyTDvthghpJ3lC+IM1QWTs466p9wHmxQR2I8jnL2h7A1zFLRAcrgC/g194/61uMQKBgQDidqdDFXkPtlgW5OWTdmLi/myqrMG/5Tt0A+9Af1r1VfNEk3YmPT80FEIkzP90Nl6iAYNtwCFz3NHKPZaPjLQ6xXVvTOM1sOESfpYmk166GESpXx5UhQF59wu+8w7k0n1Z+QeS6ELAYpQoNBbij2uEU+6xurCCtPhAdjtUEXkX0wKBgQDLUhmStwILLCNcM7QG+PCk1q7aelPICRHOeiik2NE3mFQOWHmy0ao7ZWlruOo0jEe08dmPjx5DxXl682oR+63aA2p/HRC+J94s6MiD9T+qCWEhjZyiDmnFBFykPBnERMbeJW065TXme4smulwUJJIGP8Zc8p+5IABa4s3sqEF6swKBgQCYhOT9bzbLeDk3og1kFTVgP44cHKGYheBWsFewDpXJ6YsfQlkuQ82PW514f4AEWqG9ZluHzQA6shuwo24FW/P+KQKAZT4Y6PmMpSim7lW766BCmccpkTJ/W/zTK4t6XLEDqY6KSFa8jyfNM9e92m3E/2og1OUQAqW3dGlcoy/09QKBgBNWTppWj3oyi3ZkcHXNTU3QITckNT2sJyZlFeJ/wH4yHaRzo7dvG2qkvX8CSjwPL261JcTyjqVRbblU1zy4nBNFCebnA8WiEw5gYVmzhIeOds9zIXeEOWkEfi8cjDLPe756/sWAMT0neNeJF+LOi5jOjfYfGTPJKF98fKtKYVfjAoGAZs9wkZhjnRYghHWFbTI+b+Yg9exkRj+RSy2JOJTjgYFFoz+WA9EhUKNlfhyxZOJQeOMPKUpRpAR/abrUJNO3QHGmR2JHWyJF0FYwbaLpEKZZ7DgQV2C6q1bt1fxYlQVU7eClA+cyeC3uSjkCUz9ktN+C2BFUBM+c+FIsMtnNfNM=";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnYC/s0b5Cxl1PzE1tuh9nE+iTw0oetjPHyxwIHo/7k22fTRQwtiF1aB2EHm43l+9uakI/By62AGvxbwbJ0hMmJ+g6cYd+02RWbUVBesUV63fURD8AI2yF+Q1oDo+RrQe6kGpl5jLeW0NfiQ4/R6Y3B38p6NtotA9v0tBcRtk/OsPkD7El3GjRex4ABbiYmgfyWz2A4zSg5g0q1Vk6UAyUlZqCCBHqGUs1OUjhnuPFZtRt4LAPbxyb1/pyvKtVjw8ADr7k3xAzr6goILlvNsCx9vDTsIry+UgoqKcVUxaR2Bv0kKK9dXqC26t0qdOFn2I+9Bqbakghltzh2Qz/hVHHQIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://liudehuang.nat300.top/alibaba/callBack/asynCallBack";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://liudehuang.nat300.top/alibaba/callBack/synCallBack";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "C:\\";


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

