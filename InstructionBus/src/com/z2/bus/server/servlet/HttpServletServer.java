package com.z2.bus.server.servlet;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.z2.bus.Deploy;
import com.z2.bus.Instruction;
import com.z2.bus.Option;
import com.z2.bus.Server;
import com.z2.bus.ServerType;
import com.z2.bus.server.ActionResult;

public class HttpServletServer extends AbstractServletServer implements ServerType.Model{
    
	private static final String HTTP_PREFIX = "http://";
	private static final String COLON = ":";
	private static final String SLASH = "/";
	private static final String CONTENT_TYPE_NAME = "Content-Type";
	private static final String CONTENT_TYPE_VALUE = "application/x-java-serialized-object";
	
	@Override
	public Option[] process(Server server, Instruction instruction,Option[] options) {
		HttpServletWorker worker = new HttpServletWorker(server,instruction,options);
		return worker.work();
	}
	
	static class HttpServletWorker {
		private URLConnection con         = null;
		private ObjectOutputStream oos = null;
		private ObjectInputStream ois     = null;
		
		protected Server       server;
		protected Instruction instruction;
		protected Option[]    options;
		
		HttpServletWorker(Server server, Instruction instruction,Option[] options){
			this.server       = server;
			this.instruction = instruction;
			this.options     = options;
		}
		
		protected  Option[] work(){
			   try{
					openConnection(server,instruction);
					Mission requestMission = new Mission(server,instruction,options);
					send(requestMission);
					Mission responseMission = receive();
					return dealResponse(responseMission);
				}catch(Exception e){
					return ActionResult.makeFailureResult(e.getMessage());
				}finally{
					try{
						if(null != oos)
							oos.close();
					}catch(Exception e1){}
					
					try{
						if(null != ois)
							ois.close();
					}catch(Exception e2){}
					
					//conn不需要关闭，自动关闭
				}
		}

		protected void openConnection(Server server, Instruction instruction) throws Exception {
			URL url = makeURL(server,instruction);
		    con = url.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setDefaultUseCaches(false);
            con.setRequestProperty(CONTENT_TYPE_NAME , CONTENT_TYPE_VALUE);		
		}

		protected void send(Mission mission) throws Exception {
			oos = new ObjectOutputStream(con.getOutputStream());	
            oos.writeObject(mission);
            oos.flush();
		}

		protected Mission receive() throws Exception {
			ois = new ObjectInputStream(con.getInputStream());
            Object object = ois.readObject();
            return (Mission)object;
		}

		protected Option[] dealResponse(Mission mission) throws Exception {
			return mission.getResponseOptions();
		}
		
		protected URL makeURL(Server server,Instruction instruction) throws Exception{
			Deploy deploy = server.findDeploy(instruction);		  
			String servletPath = deploy.getDeployInfo();
			return new URL(HTTP_PREFIX+server.getAddress()+COLON+String.valueOf(server.getPort())+SLASH+servletPath);
		}
		
	}

	

}
