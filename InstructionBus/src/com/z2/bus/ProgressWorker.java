package com.z2.bus;

import com.z2.bus.server.ActionResult;

//指令进程内部用于执行指令的线程类
class ProgressWorker extends Thread{
	private Server                 server;
	private Instruction           instruction;
	private Option[]              requestOptions;
	private Option[]              responseOptions;
	private Progress.Result    result;
	
	ProgressWorker(Server server,Instruction instruction,Option[] options){
		this.server              = server;
		this.instruction        = instruction;
		this.requestOptions = options;
	}
	
	public void run(){
        try{
			ServerType serverType = server.getServerType();
			responseOptions = serverType.process(server, instruction, requestOptions);
			if(ActionResult.isSuccessResult(responseOptions))
			      result = Progress.Result.SUCCESS;
			else if(ActionResult.isFailureResult(responseOptions))
				  result = Progress.Result.FAILURE;
			else if(ActionResult.isStoppedResult(responseOptions))
				  result = Progress.Result.STOPPED;
			else
			      throw new ProgressException(ActionResult.INVALID_RESULT_MESSAGE);
		}catch(ProgressException pe){
			result = Progress.Result.FAILURE;
			responseOptions = ActionResult.makeFailureResult(pe.getMessage());
	    }catch(Exception e){
			result = Progress.Result.FAILURE;
			responseOptions = ActionResult.makeFailureResult(e.getMessage());
		}
	}
	

	public boolean halt(){
		try{
			interrupt();
		}catch(SecurityException e){
			//中断线程失败
			return false;
		}
		return true;
	}
	
	
	public Option[] getRequestOptions(){
		return requestOptions;
	}
	
	public Option[] getResponseOptions(){
		return responseOptions;
	}
	
	public Progress.Result getResult(){
		return result;
	}
	
}
