		
package webservice;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import model.User;
 
import com.sun.jersey.api.view.Viewable;

import dao.DataServiceHandler;



@Path("/user")
public class WebService {
	@Context HttpServletRequest request;
	@Context HttpServletResponse response;
	
	@Path("read")
	@GET
    @Produces("text/html")
    public Response allUsers() throws UnknownHostException {
		//connect controller and model
    	DataServiceHandler dbHandler = new DataServiceHandler();
    	dbHandler.setUserCollection();
    	List<User> users = dbHandler.findAllUsers();
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("users",users);
    	map.put("result","Show All Users");
    	request.setAttribute("finale", map);
    	return Response.ok(new Viewable("/read.vm")).build();
    }
	
	@Path("create")
	@GET
    @Produces("text/html")
    public Response requestCreateUser() throws UnknownHostException {
		
    	return Response.ok(new Viewable("/create.vm")).build();
    }
	
	@POST
	@Path("create")
	public Response createUser(@FormParam("username") String name,
							@FormParam("age") String age,
							@FormParam("pwd") String pwd,
							@FormParam("email") String email) throws UnknownHostException{
		DataServiceHandler dbHandler = new DataServiceHandler();
		dbHandler.setUserCollection();
		Map<String, Object> map = new HashMap<String, Object>();
		User user = new User(age, name, email, pwd);
		String result = dbHandler.insertOneUser(user);
		if(result.equals("ok")){
	    	map.put("result","Successfully insert one user");
		}
		else{
			map.put("result", "Some errors happened in MongoDB");
		}
		List<User> users= dbHandler.findOneUser(name);
		map.put("users", users);
		request.setAttribute("finale",map);
		return Response.ok(new Viewable("/create.vm")).build();
	}
	
	
	@GET
	@Path("update/{name}")
	public Response requestUpdateUser(@PathParam ("name") String name) throws UnknownHostException{
		DataServiceHandler dbHandler = new DataServiceHandler();
    	dbHandler.setUserCollection();
    	List<User> users= dbHandler.findOneUser(name);
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("users",users);
    	map.put("result","Update One Users");
    	request.setAttribute("finale",map);
		return Response.ok(new Viewable("/update.vm")).build();
	}
	
	
	//update. Searched by username. 
	//If there is no such user name, then no update operation.
	@PUT
	@Path("update/oldname/{oldname}/name/{name}/age/{age}/pwd/{pwd}/email/{email}")	
	public String updateUser(  @PathParam("oldname") String oldname, 
							   @PathParam("name") String name, 
							   @PathParam("age") String age,
							   @PathParam("pwd") String pwd,
							   @PathParam("email") String email) throws UnknownHostException{
		String oldUserName = oldname;
		
		User updateUser = new User(age, name, email, pwd);
		DataServiceHandler dbHandler = new DataServiceHandler();
		dbHandler.setUserCollection();
		String updateResult = dbHandler.updateOneUser(oldUserName, updateUser);
		//Map<String, Object> map = new HashMap<String, Object>();
		//map.put("result", "update user age: " + updateUser.getAge());
		//return Response.ok(new Viewable("/index.vm",map)).build();
		return updateResult;
	}
	
	//this function is basically the same as requestUpdateUser
	//both handle GET request, and return all information of one user. 
	//Then let user chose next move.
	@GET
	@Path("delete/{name}")
	public Response requestDeleteUser(@PathParam("name") String name) throws UnknownHostException{
		DataServiceHandler dbHandler = new DataServiceHandler();
    	dbHandler.setUserCollection();
    	List<User> users= dbHandler.findOneUser(name);
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("users",users);
    	map.put("result","Delete One Users");
    	request.setAttribute("finale",map);
		return Response.ok(new Viewable("/delete.vm")).build();
	}
	
	
	@DELETE
	@Path("delete/{name}")
	public String deleteUser(@PathParam("name") String name) throws UnknownHostException{
		DataServiceHandler dbHandler = new DataServiceHandler();
		dbHandler.setUserCollection();
		String deleteResult = dbHandler.deleteOneUser(name);
		return deleteResult;
	}
	
}
