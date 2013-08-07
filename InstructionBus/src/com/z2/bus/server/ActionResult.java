package com.z2.bus.server;

import com.z2.bus.Option;

//服务器执行指令结果
public enum ActionResult {
   	
	SUCCESS,       //成功	
	FAILURE,        //失败
	STOPPED;      //强行终止ֹ
		
	public static  String RESULT = "RESULT"; 
	public static  String MESSAGE = "MESSAGE";
	//执行结果消息部分
	public static String SUCCESS_MESSAGE                    = "执行成功";
	public static String FAILURE_MESSAGE                     = "执行失败";
	public static String STOPPED_MESSAGE                    = "执行终止";
	public static String INVALID_PARAMETER_MESSAGE  = "非法的参数";
	public static String INVALID_RESULT_MESSAGE         = "非法的执行结果";
	public static String INVALID_DEPLOY_MESSAGE         = "指令未正确部署";
	public static String ERROR_BACKWARD_MESSAGE      = "执行后返回发生错误";
		
	//单个的执行结果
	private static Option SUCCESS_OPTION  = new Option(RESULT,SUCCESS);
	private static Option FAILURE_OPTION   = new Option(RESULT,FAILURE);
	private static Option STOPPED_OPTION  = new Option(RESULT,STOPPED);
	//用于不需要消息的结果
	public static Option[] SUCCESS_RESULT  = {SUCCESS_OPTION};
	public static Option[] FAILURE_RESULT   = {FAILURE_OPTION};
	public static Option[] STOPPED_RESULT  = {STOPPED_OPTION};
	
	public static Option INVALID_RESULT_MESSAGE_OPTION        = new Option(MESSAGE,INVALID_RESULT_MESSAGE);
	public static Option INVALID_DEPLOY_MESSAGE_OPTION        = new Option(MESSAGE,INVALID_DEPLOY_MESSAGE);
	public static Option INVALID_PARAMETER_MESSAGE_OPTION  = new Option(MESSAGE,INVALID_PARAMETER_MESSAGE);
	public static Option ERROR_BACKWARD_MESSAGE_OPTION     = new Option(MESSAGE,ERROR_BACKWARD_MESSAGE);
	
	public static Option[] INVALID_RESULT_RESULT        = {FAILURE_OPTION,INVALID_RESULT_MESSAGE_OPTION};
	public static Option[] INVALID_DEPLOY_RESULT        = {FAILURE_OPTION,INVALID_DEPLOY_MESSAGE_OPTION};
	public static Option[] INVALID_PARAMETER_RESULT  = {FAILURE_OPTION,INVALID_PARAMETER_MESSAGE_OPTION};
	public static Option[] ERROR_BACKWARD_RESULT     = {FAILURE_OPTION,ERROR_BACKWARD_MESSAGE_OPTION};
	
	public static Option makeMessageOption(String message){
		return new Option(MESSAGE,message);
	}
	
	public static Option[] makeSuccessResult(String message){
		Option[] successResult = {SUCCESS_OPTION,makeMessageOption(message)};
		return successResult;
	}
	
	public static Option[] makeFailureResult(String message){
		Option[] failureResult = {FAILURE_OPTION,makeMessageOption(message)};
		return failureResult;
	}
	
	public static Option[] makeStoppedResult(String message){
		Option[] stoppedResult = {STOPPED_OPTION,makeMessageOption(message)};
		return stoppedResult;
	}
	
	public static boolean isSuccessResult(Option[] options){
		for(int i=0,count=options.length ; i<count ; ++i){
			if((options[i].getName().equals(ActionResult.RESULT)) &&
			options[i].getValue().equals(ActionResult.SUCCESS))
				return true;
		}
		return false;
	}
	
	public static boolean isFailureResult(Option[] options){
		for(int i=0,count=options.length ; i<count ; ++i){
			if((options[i].getName().equals(ActionResult.RESULT)) &&
			options[i].getValue().equals(ActionResult.FAILURE))
				return true;
		}
		return false;
	}
	
	public static boolean isStoppedResult(Option[] options){
		for(int i=0,count=options.length ; i<count ; ++i){
			if((options[i].getName().equals(ActionResult.RESULT)) &&
			options[i].getValue().equals(ActionResult.STOPPED))
				return true;
		}
		return false;
	}
	
	public static boolean isValidResult(Option[] options){
		return isSuccessResult(options) || isFailureResult(options) || (isStoppedResult(options));
	}
	
}
