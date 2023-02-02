package com.hnews.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hnews.seviceImpl.HackerNewsServiceImpl;

@RestController
@CrossOrigin
public class HackerNewsController {
	
	@Autowired
	private HackerNewsServiceImpl hackerNewsService;
	
	 @RequestMapping("/top-stories")
	   public Object getBestStories() throws Exception {
		 	
	      try {
	    	  
	    	  ResponseEntity<Object> response  = hackerNewsService.getTopStories();
	    	 if (response == null) {
	    	            throw new Exception("Server responded with no data");
	    	 }
	    	 
	          return response;
	      } catch (Exception e) {
	    
	    	   e.printStackTrace();
	    	   
	      }
	      return null;
	   }
	 
	 @RequestMapping("/past-stories")
	   public Object getTopPastStories() throws Exception {  
		 	
	      try {
	    	  
	    	  ResponseEntity<Object> response  = hackerNewsService.getPastStories();
	    	 if (response == null) {
	    	            throw new Exception("Server responded with no data");
	    	 }
	    	 
	          return response;
	      } catch (Exception e) {
	    
	    	   e.printStackTrace();
	    	   
	      }
	      return null;
	   }
	 
	 
	 @RequestMapping("/comments/{id}")
	   public Object getTopComments(@PathVariable("id") int id) throws Exception { 
		 	
	      try {
	    	  
	    	  ResponseEntity<Object> response  = hackerNewsService.getComments(id);
	    	 if (response == null) {
	    	            throw new Exception("Server responded with no data");
	    	 }
	    	 
	          return response;
	      } catch (Exception e) {
	    
	    	   e.printStackTrace();
	    	   
	      }
	      return null;
	   }

	 
	 
	 

}
