package com.z2.bus.server.ejb3;

import java.util.HashMap;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingException;
import com.z2.bus.*;
import com.z2.bus.server.Action;
import com.z2.bus.server.ActionResult;
import com.z2.util.ejb3.ContextFactory;
import com.z2.util.ejb3.JBoss5ContextFactory;
import javax.rmi.PortableRemoteObject;

//指令服务器 JBOSS5类型
public class JBoss5 implements EJB3Server{
    
	private Map<Server,ContextFactory> contextFactorys = new HashMap<Server,ContextFactory>();
	
	@Override
	public Option[] process(Server server, Instruction instruction,Option[] options) {
		Action action = getActionRemote(server,instruction,options);
		if(null == action)
			return ActionResult.INVALID_DEPLOY_RESULT;
		
		return action.process(instruction, options);
	}

	@Override
	public Context createContext(Server server) throws NamingException {
		ContextFactory contextFactory = contextFactorys.get(server);
		if(null == contextFactory){
			contextFactory = new JBoss5ContextFactory(server.getAddress(),server.getPort());
			contextFactorys.put(server, contextFactory);
		}
		return contextFactory.createContext();
	}

	@Override
	public Action getActionRemote(Server server, Instruction instruction,Option[] options) {
		Context context = null;
		try{
		  context = createContext(server);
		}catch(Exception e){			
		}
		
		if(null == context)
			return null;
		
		try{
		  Deploy deploy = server.findDeploy(instruction);		  
		  String binding = deploy.getDeployInfo();
		  Object ref = context.lookup(binding);
		  ActionRemote ar = (ActionRemote)PortableRemoteObject.narrow(ref, ActionRemote.class);
		  return ar;
		}catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}  
	}

	@Override
	public Action getActionLocal(Server server, Instruction instruction,Option[] options) {
		Context context = null;
		try{
		  context = createContext(server);
		}catch(Exception e){			
		}
		
		if(null == context)
			return null;
		
		try{
			Deploy deploy = server.findDeploy(instruction);		  
			String binding = deploy.getDeployInfo();
		    Object ref = context.lookup(binding);
		    ActionLocal al = (ActionLocal)PortableRemoteObject.narrow(ref, ActionLocal.class);
		    return al;
		}catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}  		
	}

}
