package com.schedusmart.bootstrap;

import com.schedusmart.schedulebuilder.Builder;
import com.schedusmart.schedulebuilder.Schedule;
import com.schedusmart.schedulebuilder.ScorePreferences;
import com.schedusmart.uwapiclient.UWAPIClient;
import com.schedusmart.uwapiclient.UWAPIException;
import lombok.extern.slf4j.Slf4j;
import spark.Filter;
import spark.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.get;

// SparkResources uses Spark to set up endpoints for accessing the Builder service
@Slf4j
public class SparkResources {
	private static final String API_CONTEXT = "api/v1";

	public static void setupEndpoints() {
        before((request, response) -> response.type("application/json"));

        after((Filter) (request, response) -> {
            response.header("Access-Control-Allow-Methods", "GET");
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
            response.header("Access-Control-Allow-Credentials", "true");
        });

		get(API_CONTEXT + "/schedules", "application/json", (request, response) -> {
			//response.header("Content-Encoding", "gzip");
			try {
                log.info("Running /schedules handler.");
				return buildSchedules(request);
			} catch (UWAPIException e) {
				response.status(400);
				String msg = e.getMessage();
				log.error(msg);
				return msg;
			} catch(Throwable e) {
				response.status(500);
				String msg = e.getMessage();
				log.error(msg);
				return "Unexpected error: " + msg;
			}
		}, new JsonTransformer());
		
		get(API_CONTEXT + "/allcourses", "application/json", (request, response) -> {
			//response.header("Content-Encoding", "gzip");
			try {
			    log.info("Running /allcourses handler.");
				return UWAPIClient.getTerms();
			} catch (UWAPIException e) {
				response.status(400);
				String msg = e.getMessage();
				log.error("abcd" + msg);
				return msg;
			} catch(Throwable e) {
				response.status(500);
				String msg = e.getMessage();
				log.error("efgh" + msg);
				return "Unexpected error: " + msg;
			}
		}, new JsonTransformer());

		get(API_CONTEXT + "/ping", (req, res) -> {
			Map<String, String> pong = new HashMap<>();
			pong.put("pong", "Hello, World!");
			res.status(200);
			return pong;
		}, new JsonTransformer());}
	
	// buildSchedules calls Builder.buildSchedules with information from request's query string
	//    and converts the returned schedules to CollectionSchedules
	public static CollectionSchedule[] buildSchedules(Request request) throws UWAPIException {
		String[] courses = getQueryListParam(request, "courses");
		String term = request.queryParams("term");
		String[] ignoredSecs = getQueryListParam(request, "ignore");
		int classTime = Integer.parseInt(request.queryParams("classtime"));
		int dayPref = Integer.parseInt(request.queryParams("daylength"));
		boolean omitClosed = request.queryParams("omitclosed").equals("1");

		ArrayList<Schedule> scheduleList =
				new Builder().getSchedules(courses, term, ignoredSecs, omitClosed,
						new ScorePreferences(classTime, dayPref));
		int scheduleCount = scheduleList.size();

		// scheduleList to array of CollectionSchedule objects
		CollectionSchedule[] schedules = new CollectionSchedule[scheduleCount];
		
		for (int i = 0; i < scheduleCount; ++i) {
			schedules[i] = new CollectionSchedule(scheduleList.get(i));
		}
		
		return schedules;
	}

	// For query string lists, SparkJava requires the "name=val1&name=val2" format, but APIGateway doesn't support this.
    // As an alternative, we use a string of comma-separated list items: "name=value1,value2".
    private static String[] getQueryListParam(Request request, String name) {
	    String params = request.queryParams(name);
		if (params == null) {
		    return null;
        }

        return params.split(",");
	}
}