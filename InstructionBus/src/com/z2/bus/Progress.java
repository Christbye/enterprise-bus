package com.z2.bus;

import java.util.Date;

//服务器上执行指令的过程的抽象，可能需要持久化，所以这样设计
public class Progress {
	
	private int                     id;
	private Server     	         server;
	private Instruction 	     instruction;
	private Option[]             requestOptions;
	private Option[]             responseOptions;
	private Date                  startTime;
	private Date                  endTime;	
	private Status                status = Status.NOT_START;
	
	private Result                result;

	private ProgressWorker worker;
	
	private static final String CANNOT_START_WITH_INVALID_STATE = "进程状态错误，无法启动.";
	private static final String CANNOT_STOP_WITH_INVALID_STATE   = "进程状态错误，无法终止.";
	
	public Progress(Server server,Instruction instruction,Option[] options){
		this.id = server.newProgressId();
		this.server = server;
		this.instruction = instruction;
		this.requestOptions = options;
	}
	
	public int getId(){
		return id;
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
	
	public Option[] getResponseOptions(){
		return responseOptions;
	}
	
	public Status getStatus(){
		return status;
	}
	
	public Date getStartTime(){
		return startTime;
	}
	
	public Date getEndTime(){
		return endTime;
	}
	
	public Result getResult(){
		return result;
	}
	
	//此处可以设计成允许终止ֹ
	public  void process(){
		start();
		work();
		end();		
	}
	
	protected void work(){
		worker = new ProgressWorker(server,instruction,requestOptions);
		worker.start();	
		try{  
		   worker.join();
		   result = worker.getResult();
		   responseOptions = worker.getResponseOptions();
	    }catch(InterruptedException e){
	    	//本线程在等待新线程的时候被其他线程中断
			doWhenBeInterrupted();
		}		
	}
	
	//停止当前进程和任务子进程,服务器管理功能
	public  boolean stop(){
		if((status != Status.STARTED)||(null == worker)||(!worker.isAlive()))
			throw new IllegalStateException();
		
		if(stopWork()){
		  doWhenBeInterrupted();
		  return true;
		}
		return false;
	}
	
	protected boolean stopWork(){
		return worker.halt();
	}
	
	//被终止ֹ
	private void doWhenBeInterrupted(){
		result = Result.STOPPED;
		responseOptions = new Option[0];
	}
	
	//设置开始时间
	private void start(){
		if(status != Status.NOT_START)
			throw new ProgressException(CANNOT_START_WITH_INVALID_STATE);
		status = Status.STARTED;
		startTime = new Date();
	}
	
	//设置结束时间
	private void end(){
		if(status != Status.STARTED)
			throw new ProgressException(CANNOT_STOP_WITH_INVALID_STATE);
		status = Status.ENDED;
		endTime = new Date();
	}	
	
	static enum Status{
		NOT_START,   //未启动
		STARTED,       //正在运行
		ENDED          //运行结束
	}	
	
	public static enum Result{	
		
		SUCCESS (ResultMessage.RESULT_SUCCESS_MESSAGE ),       //成功
		FAILURE  (ResultMessage.RESULT_FAILURE_MESSAGE  ),        //失败
		STOPPED (ResultMessage.RESULT_STOPPED_MESSAGE);        //强行终止
		
		private String message;
		private Result(String msg){
			message = msg;
		}
		
		public String getMessage(){
			return message;
		}
		
		private static class ResultMessage{
			  static final String RESULT_SUCCESS_MESSAGE = "成功";
			  static final String RESULT_FAILURE_MESSAGE = "失败";
			  static final String RESULT_STOPPED_MESSAGE = "被终止";
		}			
	}
	
}
