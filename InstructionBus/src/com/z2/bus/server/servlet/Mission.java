package com.z2.bus.server.servlet;

import com.z2.bus.Instruction;
import com.z2.bus.Option;
import com.z2.bus.Server;

import java.io.Serializable;

//与servlet一次交互的数据与结果，用于在网络间传送
public class Mission implements Serializable{

	private static final long serialVersionUID = 20130801L;
	
	private Server       server;
	private Instruction instruction;
    private Option[]    requestOptions;
    private Option[]    responseOptions;
    
    public Mission(Server server,Instruction instruction,Option[] options){
    	this.server              = server;
    	this.instruction        = instruction;
    	this.requestOptions = options;
    }
    
    public Server getServer(){
    	return server;
    }
    
    public Instruction getInstruction(){
    	return instruction;
    }
    
    public Option[] getRequestOptions(){
    	return requestOptions;
    }
    
    public void setResponseOptions(Option[] options){
    	responseOptions = options;
    }
    
    public Option[] getResponseOptions(){
    	return responseOptions;
    }
    
}
