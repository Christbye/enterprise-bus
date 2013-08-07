package com.z2.util.ejb3;

import javax.naming.Context;
import javax.naming.NamingException;
import java.util.Properties;
import javax.naming.InitialContext;

//JBOSS5上下文工厂
public class JBoss5ContextFactory implements ContextFactory {

	private String address;
	private int port;
	
	public JBoss5ContextFactory(String address,int port){
		this.address = address;
		this.port = port;
	}
	
	public Context createContext() throws NamingException{
		return JBoss5ContextFactory.createContext(address, port);
	}
	
	public static Context createContext(String address,int port) throws NamingException{
		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
		props.put(Context.PROVIDER_URL, "jnp://"+address+":"+port);
				
		return new InitialContext(props);
	}
	

}
