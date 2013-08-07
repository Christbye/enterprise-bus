package com.z2.bus;

//指令
public class Instruction {
	
	//在指定服务器执行
	public Option[] execute(Server server,Option[] options){
		return server.execute(this, options);
	}
}
