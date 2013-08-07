package com.z2.bus;

import javax.persistence.Transient;

//执行指令的服务器的类型
public class ServerType {
    private Model model                       = null;
    private String serverTypeClassName = null;
    
    private static final String INVALID_CLASSNAME = "非法的服务器类别类名";
    
    public String getServerTypeClassName(){
    	return serverTypeClassName;
    }
    
    public void setServerTypeClassName(String newClassName){
    	if(newClassName.equals(serverTypeClassName))
    			return;
    	if (ServerType.isValidServerTypeClassName(newClassName))
    		throw new IllegalArgumentException(INVALID_CLASSNAME);
    	
    	serverTypeClassName = newClassName;
    	useNewModel();
    }
    
    private void useNewModel(){
    	try{
    	    model = (Model)Class.forName(serverTypeClassName).newInstance();
        }catch(Exception e){
        }
    }
    
    @Transient
	public Model getModel(){
	    return model;
	}
	
	public  Option[] process(Server server,Instruction instruction,Option[] options){
		Model model = getModel();
		return model.process(server, instruction, options);
    }
	
	@Transient
    public static boolean isValidServerTypeClassName(String className){
    	Object object = null;
    	try{
    	    object = Class.forName(className).newInstance();
        }catch(Exception e){
        	return false;
        }
    	
    	if(object instanceof Model)
    		return true;
    	return false;
    }
	
	public static interface Model{
		//执行指令
		public  Option[] process(Server server,Instruction instruction,Option[] options);
	}
}
