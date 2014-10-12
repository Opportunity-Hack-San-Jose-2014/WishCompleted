package com.wishpal.donate;

import static spark.Spark.get;
import static spark.Spark.post;

import static spark.Spark.setPort;
import static spark.Spark.staticFileLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.impl.SimpleLogger;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.freemarker.FreeMarkerRoute;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
    	
    	final ModifyData o = new ModifyData();
    	
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");

        staticFileLocation("/");

        //Heroku will pass $PORT to the app. The default port is 4567
        String portStr = System.getenv("PORT");
        if(portStr != null) setPort(Integer.parseInt(portStr));

    	
        get(new Route("/") {
        	@Override
        	public Object handle(Request req, Response resp) {
        		List<Item> itemList = new ArrayList<Item>();
        		itemList = o.readOne(1);
        		StringBuilder sb = new StringBuilder();
        		for(Item i:itemList){
        			sb.append("WishCardID: "+ i.WishCardID);
        			sb.append(" AgencyCode: "+i.AgencyCode);
        			sb.append(" AgencyZone: "+ i.AgencyZone);
        			sb.append(System.getProperty("line.separator"));
        		}
        		
        		return sb.toString();
        	}
        });
        
        
        get(new FreeMarkerRoute("/search") {
            @Override
            public Object handle(Request request, Response response) {
                	Map<String, Object> viewObjects = new HashMap<String, Object>();
                	
            		return modelAndView(viewObjects,"search.ftl");
                   }

            
        });
//        post(new Route("/") {
//            @Override
//            public Object handle(Request request, Response response) {
//                Integer id      = Integer.parseInt(request.queryParams("article-id"));
//                String title    = request.queryParams("article-title");
//                String summary  = request.queryParams("article-summary");
//                String content  = request.queryParams("article-content");
//
////                articleDbService.update(id, title, summary, content);
//
//                response.status(200);
//                response.redirect("/");
//                return "";
//            }
//        });
        post(new Route("/update") {
            @Override
            public Object handle(Request request, Response response) {
                String SWCID = request.queryParams("WCID");
                int id = Integer.parseInt(SWCID);
                o.update(id);

                response.redirect("/search");
                return "";
            }
        });
        
        post(new FreeMarkerRoute("/read") {
            @Override
            public Object handle(Request request, Response response) {
            	String sid = request.queryParams("searchFilter");
            	int id = 0;
            	if (sid != null){
            		
            		 id = Integer.parseInt(sid);
            	}
            	List<Item> itemList = new ArrayList<Item>();
        		itemList = o.readOne(id);
//        		StringBuilder sb = new StringBuilder();
        		for(Item i:itemList){
//        			sb.append("WishCardID: "+ i.WishCardID);
//        			sb.append(" AgencyCode: "+i.AgencyCode);
//        			sb.append(" AgencyZone: "+ i.AgencyZone);
//        			sb.append(System.getProperty("line.separator"));
        		}
//                Integer id = Integer.parseInt(request.params(":id"));
        		ArrayList<Integer> test = new ArrayList<Integer>();
        		test.add(1);
        		test.add(2);
                Map<String, Object> viewObjects = new HashMap<String, Object>();
                	
                viewObjects.put("records", itemList);
                viewObjects.put("pass", "hello");
                

//                viewObjects.put("article", articleDbService.readOne(id));

                return modelAndView(viewObjects, "searchResult.ftl");
            }
        });
    }

	

	
}
