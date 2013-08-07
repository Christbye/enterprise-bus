package com.z2.bus.server.servlet;

import com.z2.bus.Instruction;
import com.z2.bus.Option;
import com.z2.bus.Server;
import com.z2.bus.ServerType;

public abstract class AbstractServletServer implements ServerType.Model{

	public abstract Option[] process(Server server, Instruction instruction,Option[] options);
	
	
}
