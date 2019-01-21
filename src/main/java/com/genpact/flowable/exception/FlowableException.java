package com.genpact.flowable.exception;

import org.springframework.http.HttpStatus;

/**
 * @author sxia
 * @Description: TODO(自定义异常)
 * @date 2017-6-23 15:07
 */
public class FlowableException extends RuntimeException {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String msg;

    
    public FlowableException(String msg) {
		super(msg);
		this.msg = msg;
	}
    
    public FlowableException(HttpStatus status, Throwable e) {
		super(status.getReasonPhrase(),e);
		this.msg = status.getReasonPhrase();
	}
	
	public FlowableException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	
	
}
