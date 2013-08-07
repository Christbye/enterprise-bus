package com.z2.bus.server.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.z2.bus.Instruction;
import com.z2.bus.Option;
import com.z2.bus.server.Action;
import com.z2.bus.server.ActionResult;

//用于服务器实现指令功能的类的基础类
public abstract class BaseServlet extends HttpServlet implements Action{

	private static final long serialVersionUID = 20130801L;
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException{
		Mission mission = null;
		//如果获取输出对象有问题,不要往下运行了
		OutputStream os = response.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		InputStream is = request.getInputStream();	
		ObjectInputStream ois = new ObjectInputStream(is);				
		try{		  
		  Object object = ois.readObject();    		
    	  mission = (Mission)object;
		  Instruction instruction = mission.getInstruction();
		  Option[] requestOptions = mission.getRequestOptions();
		  Option[] responseOptions = process(instruction,requestOptions);
		  mission.setResponseOptions(responseOptions);
		  oos.writeObject(mission);
		  oos.flush();
		}catch(ClassNotFoundException cnfe){
			returnInvalidParameterMission(oos,mission);
		}catch(ClassCastException cce){
			returnInvalidParameterMission(oos,mission);
	    }finally{						
		   try{
			  if(null != ois)
			    ois.close();
		   }catch(Exception e1){}
		   try{
			 if(null != oos)
		       oos.close();
		   }catch(Exception e2){}
		}
	}
	
	private void returnInvalidParameterMission(ObjectOutputStream oos,Mission mission) throws IOException{
		Mission invalidParameterMission = new Mission(mission.getServer(),mission.getInstruction(),mission.getRequestOptions());
		invalidParameterMission.setResponseOptions(ActionResult.INVALID_PARAMETER_RESULT);
		oos.writeObject(mission);
		oos.flush();
	}
	    
}
