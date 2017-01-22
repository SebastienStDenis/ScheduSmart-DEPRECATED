package com.schedusmart.bootstrap;

import static spark.Spark.*;
import spark.servlet.SparkApplication;

// Bootstrap is used to bootstrap the web application
public class Bootstrap implements SparkApplication{

	public void init() {
		staticFiles.location("/public");
		new BuilderResource();		
	}
}
