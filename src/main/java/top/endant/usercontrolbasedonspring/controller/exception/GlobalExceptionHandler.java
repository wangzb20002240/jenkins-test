package top.endant.usercontrolbasedonspring.controller.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.endant.usercontrolbasedonspring.controller.Result;

import static top.endant.usercontrolbasedonspring.controller.Code.SYSTEM_ERR;

/**
 * 全局异常收集，主要针对token
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        String msg = e.getMessage();
        if (msg == null || msg.equals("")) {
            msg = "服务器出错";
        }
        return new Result(SYSTEM_ERR, null, msg);
    }
}