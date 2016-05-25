package server;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;

/**
 * Session Bean implementation class ExampleBean
 */
@Stateless
@LocalBean
public class ExampleBean {

    /**
     * Default constructor. 
     */
    public ExampleBean() {
        // TODO Auto-generated constructor stub
    }
    
    public String SayHello(String string){
    	return "Hello " + string;
    }
    
    public String GetSystemJavaHome(){
    	return System.getProperty("java.home");
    }

}
