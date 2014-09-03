package model;

public class User {
	private String age;
	private String username;
	private String email;
	private String pwd;
	
	public User(String age,String username,String email,String pwd){
		this.age = age;
		this.username = username;
		this.email = email;
		this.pwd = pwd;
	}
	
	public String getAge(){
		return this.age;
	}
	public String getUserName(){
		return this.username;
	}
	public String getEmail(){
		return this.email;
	}
	public String getPwd(){
		return this.pwd;
	}

	
}
