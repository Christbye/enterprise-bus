package com.z2.bus;

import java.io.Serializable;

//指令选项，用于执行参数和结果
public  class Option implements Serializable{

	private static final long serialVersionUID = 20130729L;
	
	public static final String RESULT         = "RESULT";
	public static final String START_TIME  = "START_TIME";
	public static final String END_TIME     = "END_TIME";
	public static final String DATA             = "DATA";
	
	private String         name;
	private Serializable value;
	
	public Option(String name,Serializable value){
		this.name = name;
		this.value = value;
	}
	
    public String getName(){
    	return name;
    }
    
    public Serializable getValue(){
    	return value;
    }
    

}
