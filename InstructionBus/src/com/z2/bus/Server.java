package com.z2.bus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.*;

//执行指令的服务器的抽象
@Entity
public class Server {
	
	private String            id;
	private String            caption;
	private String            address;
	private int                port;
	private ServerType    serverType;
	private List<Deploy> deployed = new ArrayList<Deploy>();
	private Map<Instruction,Deploy> deployedInstruction = new HashMap<Instruction,Deploy>();
	
	//已经使用的最大的进程号
	private int progressId = 0;
	//服务器正在运行的进程
	private Map<Integer,Progress> progresses           = new HashMap<Integer,Progress>();
	
	private List<ProgressListener>   progressListeners = new ArrayList<ProgressListener>();
	
	@Id
	@Basic
	@Column(name="SERVER_ID")
	public String getId(){ return id; }
	public void setId(String id){ this.id = id; }
	  
	@Basic
	@Column(name="CAPTION",nullable=false)
	public String getCaption(){ return caption; }
	public void setCaption(String c){ caption = c; }
	  
	@Basic
	@Column(name="ADDRESS")
	public String getAddress(){ return address; }
	public void setAddress(String addr){ address = addr; }
	  
	@Basic
	@Column(name="PORT")
	public int getPort(){ return port; }
	public void setPort(int p){ port = p; }
	  
	public ServerType getServerType(){
		return serverType;
	}
	
	public void setServerType(ServerType type){
		serverType = type;
	}	
	
	public List<Deploy> getDeployed(){
		return deployed;
	}
	
	public void SetDeployed(List<Deploy> deployed){
		this.deployed = deployed;
		updateDeployedInstruction();
	}
	
	private void updateDeployedInstruction(){
		deployedInstruction = new HashMap<Instruction,Deploy>();
		List<Deploy> deployed = getDeployed();
		Iterator<Deploy> iterDeployed = deployed.iterator();
		Deploy deploy = null;
		while( iterDeployed.hasNext()){
			deploy = iterDeployed.next();
			deployedInstruction.put(deploy.getInstruction(),deploy);
		}
	}
	
	public Deploy findDeploy(Instruction instruction){		
		return deployedInstruction.get(instruction);
	}
	
	public boolean equals(Object o){
	  if(o==this)
	  	 return true;
	  if(o.getClass() == Server.class)
	  	 return false;
	  Server p = (Server) o;
	  if(p.id.equals(this.id))
	  	 return true;
	  return false;
	 }
	  
	 public int hashCode(){
	     return 13*id.hashCode();
	 }
	
	 //本函数应该被设计成可重入，这样一个服务器可以同时执行多个指令
	public Option[] execute(Instruction instruction,Option[] options){
		Option[] result = new Option[4];
		Progress progress = new Progress(this,instruction,options);
		addProgress(progress);
		progress.process();
		removeProgress(progress.getId());
		makeResult(progress,result);		
		return result;
	}
	
	//生成一个进程编号
	public synchronized int newProgressId(){
		return ++progressId;
	}
	
	public  void addProgress(Progress progress){
		synchronized(progresses){
		   progresses.put(progress.getId(),progress);
		}
	}
	
	public  Progress removeProgress(int progressId){
		synchronized(progresses){
		   return progresses.remove(progressId);
		}
	}
	
	//获取一个进程拷贝，用于管理功能，这个是一个浅拷贝
	public Set<Progress> copyProgresses(){
		Set<Progress> progressSet = new HashSet<Progress>();
		synchronized(progresses){
			progressSet.addAll(progresses.values());
		}
		return progressSet;
	}
	
	public void addProgressListener(ProgressListener listener){
		synchronized(progressListeners){
		   progressListeners.add(listener);
		}
	}
	
	public void removeProgressListener(ProgressListener listener){
		synchronized(progressListeners){
		   progressListeners.remove(listener);
		}
	}
	
	private Set<ProgressListener> copyProgressListeners(){
		Set<ProgressListener> progressListenerSet = new HashSet<ProgressListener>();
		synchronized(progressListeners){
			progressListenerSet.addAll(progressListeners);
		}
		return progressListenerSet;
	}
	
	public void fireProgressStart(Progress progress){
		Set<ProgressListener> listenersCopy =  copyProgressListeners();		
		Iterator<ProgressListener> iter = listenersCopy.iterator();
		while(iter.hasNext())
			iter.next().progressStart(progress);
	}
	
	public void fireProgressStop(Progress progress){
		Set<ProgressListener> listenersCopy =  copyProgressListeners();
		Iterator<ProgressListener> iter = listenersCopy.iterator();
		while(iter.hasNext())
			iter.next().progressStop(progress);
	}
	
	private void makeResult(Progress progress,Option[] result){
		result[0] = new Option(Option.RESULT,progress.getResult());
		result[1] = new Option(Option.START_TIME,progress.getStartTime());
		result[2] = new Option(Option.END_TIME,progress.getEndTime());
		result[3] = new Option(Option.DATA,progress.getResponseOptions());
	}
	
}
