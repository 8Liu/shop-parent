package com.liudehuang.base;

import com.liudehuang.constants.Constants;

/**
 * @author liudehuang
 * @date 2018/12/13 13:51
 */
public class BaseApiService {
    /**
     *  通用封装
     */
    public ResponseBase setResult(Integer code, String msg, Object data){
        return new ResponseBase(code,msg,data);
    }

    /**
     * 返回成功
     * @return
     */
    public ResponseBase setResultSuccess(){
        return setResult(Constants.HTTP_RES_CODE_200,Constants.HTTP_RES_CODE_200_VALUE,null);
    }

    /**
     * 返回成功
     * @return
     */
    public ResponseBase setResultSuccess(String msg){
        return setResult(Constants.HTTP_RES_CODE_200,msg,null);
    }

    /**
     * 返回成功
     * @param data
     * @return
     */
    public ResponseBase setResultSuccess(Object data){
        return setResult(Constants.HTTP_RES_CODE_200,Constants.HTTP_RES_CODE_200_VALUE,data);
    }

    /**
     * 失败
     * @param msg
     * @return
     */
    public ResponseBase setResultError(String msg){
        return setResult(Constants.HTTP_RES_CODE_500,msg,null);
    }

    /**
     * 失败
     * @param msg
     * @return
     */
    public ResponseBase setResultError(Integer code,String msg){
        return setResult(Constants.HTTP_RES_CODE_201,msg,null);
    }

}
