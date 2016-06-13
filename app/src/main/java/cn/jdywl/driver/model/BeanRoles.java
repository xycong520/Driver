package cn.jdywl.driver.model;

import java.util.List;

/**
 * Created by xycong on 2016/6/12.
 */
public class BeanRoles {
    int status_code;
    String message;
    List<String> roles;

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
