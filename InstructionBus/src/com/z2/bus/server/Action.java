package com.z2.bus.server;

import com.z2.bus.Instruction;
import com.z2.bus.Option;

//服务器执行的指令的功能抽象
public interface Action{
			
	public  Option[] process(Instruction instruction,Option[] options);
	
}
