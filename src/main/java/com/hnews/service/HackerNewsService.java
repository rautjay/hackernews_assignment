package com.hnews.service;


public interface HackerNewsService {
	
	public Object getTopStories();
    
	public Object getPastStories();
	
	public Object getComments(int id);

}
