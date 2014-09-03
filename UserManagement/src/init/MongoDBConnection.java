package init;

import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.sun.istack.logging.Logger;
import com.sun.jersey.spi.resource.Singleton;

@Startup
public class MongoDBConnection  implements ServletContextListener {
	//print in glassfish log file
	private static Logger log = Logger.getLogger(MongoDBConnection.class);
	
	public static MongoDBConnection mongoDBConnection= null;
	public static MongoClient mongoClient  = null;
	
	public MongoClient getConnection() throws UnknownHostException{
		mongoClient = new MongoClient( "localhost" , 27017 );
		//test local to see initialization is ok
		log.info("MongoClient has been initialized.#####################################");
		return mongoClient; 
	}
	
	public static void closeConnection() {
		if (mongoClient != null){
			mongoClient.close();
			mongoClient = null;
		}
	}
	/*
	public static MongoDBConnection getInstance(){
		if (mongoDBConnection == null) {
			mongoDBConnection = new MongoDBConnection();	
		}
		return mongoDBConnection;
	}*/

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			getConnection();
		} catch (UnknownHostException e) {
			log.severe(e.toString());
		}
	}
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		closeConnection();
		
	}

	
}
