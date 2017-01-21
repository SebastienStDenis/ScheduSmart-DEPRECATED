package com.schedusmart.bootstrap;


import static spark.Spark.*;

// Bootstrap is used to bootstrap the web application
public class Bootstrap {
	private static final int PORT = 8080;

	public static void main(String[] args) throws Exception {
		port(PORT);
		staticFiles.location("/public");
		new BuilderResource();		
	}
}
