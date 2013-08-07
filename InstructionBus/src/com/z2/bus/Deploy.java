package com.z2.bus;

//定义一个指令功能单元在一个服务器上的部署，
//部署后代表这个服务器可以处理该指令
public class Deploy {
    
	private Server server;
	private Instruction instruction;
	
	public Server getServer(){
		return server;
	}
	
	public Instruction getInstruction(){
		return instruction;
	}
	
	//部署信息，实际部署到不同服务器类型服务器应该使用不同信息
	//系统将根据此信息到服务器上查找执行代理
	//比如如果是SERVLET，该信息应该是SERVLET的相对路径
	//如果是EJB，该信息应该是绑定的JNDI名
	private String deployInfo;
		
	public void setDeployInfo(String deployInfo){
		this.deployInfo = deployInfo;
	}
		
	public String getDeployInfo(){
		return deployInfo;
	}
	
}
