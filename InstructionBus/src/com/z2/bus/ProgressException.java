package com.z2.bus;

//指令进程异常
public class ProgressException extends RuntimeException{

	private static final long serialVersionUID = 20130725L;
    
	public ProgressException(String msg){
		super(msg);
	}
	
}
