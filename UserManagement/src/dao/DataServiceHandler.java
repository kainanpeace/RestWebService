package dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import model.User;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import init.MongoDBConnection;

/*
 * For each client request, there would be a handler to serve. 
 * Share the same db connection pool.
 */
public class DataServiceHandler {
    private DBCollection userColl = null;
	private DB userDB = null;
	private String collName = null;
	
	public DataServiceHandler() throws UnknownHostException{
		//userDB = DataServiceHelper.getInstance().getConnection();
		MongoClient mongoClient = MongoDBConnection.mongoClient;
		userDB = mongoClient.getDB( "mydb" );
	}
	
	/*
	 * In this project, there is only one collection invovled.
	 */
	public void setUserCollection(){
		this.collName = "users";
	}
	
	public ArrayList<User> findAllUsers(){
		ArrayList<User> allUsers = new ArrayList<User>();
		userColl = userDB.getCollection(collName);
		DBCursor cursor = userColl.find();
		
		try{
	
			while(cursor.hasNext()){
				DBObject item = cursor.next();
				System.out.println(item.get("user_name"));
				User addUser = fromObToUser(item);
			    if (addUser != null){
			    	allUsers.add(addUser);
			    }
			    else{
			    	System.out.println("addUser is Null");
			    }
			}
		}finally{
			cursor.close();
		}
		
		return allUsers;
	}
	
	/*Supposely, this function return a User type value
	 *But, for using the same result velocity template
	 *we also return a list of Users
	 *If No found, return empty list.
	 **/
	public ArrayList<User> findOneUser (String name){
		ArrayList<User> allUsers = new ArrayList<User>();
		userColl = userDB.getCollection(collName);
		BasicDBObject query = new BasicDBObject("user_name", name);
		DBCursor cursor = userColl.find(query);
		try {
			   while(cursor.hasNext()) {
				   DBObject item = cursor.next();
					System.out.println(item.get("user_name"));
					User addUser = fromObToUser(item);
					allUsers.add(addUser);
			   }
			} finally {
			   cursor.close();
			}
		return allUsers;
	}
	
	//insert into db one user
	public String insertOneUser(User user){
		userColl = userDB.getCollection(collName);
		BasicDBObject doc = new BasicDBObject("user_name",user.getUserName()).
												  append("age",user.getAge()).
												  append("email",user.getEmail()).
												  append("pwd",user.getPwd());
		try{
			userColl.insert(doc);
			return "ok";
		}catch(Exception e){
			System.out.println("Error Occured");
		}
		return "fail";
	}
	
	//update user
	public String updateOneUser(String oldUserName, User user){
		userColl = userDB.getCollection(collName);
		BasicDBObject doc = new BasicDBObject("user_name",user.getUserName()).
					  append("age",user.getAge()).
					  append("email",user.getEmail()).
					  append("pwd",user.getPwd());
		BasicDBObject searchQuery = new BasicDBObject("user_name", oldUserName);	
		WriteResult wr = userColl.update(searchQuery, doc);
		return wr.toString();
	}
	
	//delete
	public String deleteOneUser(String userName){
		userColl = userDB.getCollection(collName);
		BasicDBObject searchQuery = new BasicDBObject("user_name", userName);	
		WriteResult wr = userColl.remove(searchQuery);
		return wr.toString();
	}
	
	//Transfer a mongoDB doc type result into a User 
	private User fromObToUser(DBObject ob){
		String userName = (String)ob.get("user_name");
		String pwd = (String)ob.get("pwd");
		String email = (String)ob.get("email");
		String age = (String)ob.get("age");
		System.out.println(userName+pwd+email+age);
		User user = new User(age, userName, email, pwd);
		return user;
	}
}
