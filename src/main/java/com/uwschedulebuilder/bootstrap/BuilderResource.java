package com.uwschedulebuilder.bootstrap;

import static spark.Spark.get;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uwschedulebuilder.builder.Builder;
import com.uwschedulebuilder.builder.Component;
import com.uwschedulebuilder.builder.Schedule;
import com.uwschedulebuilder.builder.ScorePreferences;
import com.uwschedulebuilder.builder.Term;
import com.uwschedulebuilder.uwapiclient.UWAPIClient;
import com.uwschedulebuilder.uwapiclient.UWAPIException;

import spark.Request; 

// BuilderResource uses Spark to set up endpoints for accessing the Builder service
public class BuilderResource {
	private static final String API_CONTEXT = "api/v1";
	
	private final Builder builder;
	private Logger logger;
		
	public BuilderResource() {
		logger = LoggerFactory.getLogger(BuilderResource.class);
		this.builder = new Builder();
		setupEndpoints();
	}
	
	private void setupEndpoints() {
		get(API_CONTEXT + "/schedules", "application/json", (request, response) -> {
			try {
				return getSchedules(request);
			} catch (UWAPIException e) {
				response.status(400);
				String msg = e.getMessage();
				logger.info(msg);
				return msg;
			} catch(Throwable e) {
				response.status(500);
				String msg = e.getMessage();
				logger.info(msg);
				return "Unexpected error";				
			}
		}, new JsonTransformer());
		
		get(API_CONTEXT + "/allcourses", "application/json", (request, response) -> {
			try {
				return UWAPIClient.getTerms();
			} catch (UWAPIException e) {
				response.status(400);
				String msg = e.getMessage();
				logger.info(msg);
				return msg;
			} catch(Throwable e) {
				response.status(500);
				String msg = e.getMessage();
				logger.info(msg);
				return "Unexpected error";				
			}
		}, new JsonTransformer());
		
		get(API_CONTEXT + "/hello", "application/json", (request, response) -> "Hello, world!", new JsonTransformer());
	}	
	
	// getSchedules calls Builder.getSchedules with information from request's query string
	//    and converts the returned schedules to CollectionSchedules
	public CollectionSchedule[] getSchedules(Request request) throws UWAPIException {
		String[] courses = request.queryParamsValues("courses");
		String term = request.queryParams("term");
		String[] ignoredSecs = request.queryParamsValues("ignore");
		int classTime = Integer.parseInt(request.queryParams("classtime"));
		int dayPref = Integer.parseInt(request.queryParams("daylength"));
		boolean omitClosed = (request.queryParams("omitclosed") == "1");
		
		ArrayList<Schedule> scheduleList = builder.getSchedules(courses, term, ignoredSecs, omitClosed, new ScorePreferences(classTime, dayPref));
		int scheduleCount = scheduleList.size();
		
		CollectionSchedule[] schedules = new CollectionSchedule[scheduleCount];
		
		for (int i = 0; i < scheduleCount; ++i) {		
			schedules[i] = new CollectionSchedule(scheduleList.get(i));
		}
		
		return schedules;
	}
}