package sfw.elasticsearch.common;

/**
 * Created by FrankWong on 11/28/14.
 */

import net.sf.json.JSONObject;

import java.io.Serializable;

public class CommonJSON implements Serializable {
    private int status;
    private String message;
    private Object data;

    public CommonJSON() {
    }

    public CommonJSON(int status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public CommonJSON(Messages messages){
        this.status = messages.getCode();
        this.message = messages.getDescription();
    }
    public CommonJSON(Messages messages,String additional){
    	this.data = additional;
        this.status = messages.getCode();
        this.message = messages.getDescription();
    }

    public CommonJSON(int status, String message, Object data) {
        super();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        JSONObject job = JSONObject.fromObject(this);
        return job.toString();
    }

}
