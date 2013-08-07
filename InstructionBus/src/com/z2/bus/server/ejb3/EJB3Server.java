package com.z2.bus.server.ejb3;

import javax.naming.Context;
import javax.naming.NamingException;
import com.z2.bus.Instruction;
import com.z2.bus.Option;
import com.z2.bus.Server;
import com.z2.bus.ServerType;
import com.z2.bus.server.Action;

//通过EJB3来实现指令运行服务器的抽象
public interface EJB3Server extends ServerType.Model{
	//查找服务器
	public Context createContext(Server server) throws NamingException;
	//获取服务器指令对象
	public Action getActionRemote(Server server, Instruction instruction,Option[] options);
	//获取服务器指令对象
	public Action getActionLocal(Server server, Instruction instruction,Option[] options);
}
