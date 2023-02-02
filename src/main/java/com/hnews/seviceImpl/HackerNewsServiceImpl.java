package com.hnews.seviceImpl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hnews.model.Comment;
import com.hnews.model.ItemDetails;
import com.hnews.service.HackerNewsService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;

@CacheConfig(cacheNames = { "hackernewscache" })
@Service
public class HackerNewsServiceImpl implements HackerNewsService {

	@Autowired
	private RestTemplate restTemplate;
      
	
	@Override
	@Cacheable
	public ResponseEntity<Object> getTopStories() {

		System.out.println("inside beststories");

		final String uri = "https://hacker-news.firebaseio.com/v0/beststories.json?print=pretty";
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		Object response = restTemplate.exchange(uri, HttpMethod.GET, entity, Object.class).getBody();
		ArrayList<Integer> list1 = (ArrayList<Integer>) response;
		ArrayList<Integer> list2 = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			list2.add(list1.get(i));

		}
		JSONArray jsonarray = new JSONArray();

		ItemDetails responseEntity = null;
		for (int j = 0; j < 10; j++) {
			JSONObject jsonfinal = new JSONObject();
			final String urinext = "https://hacker-news.firebaseio.com/v0/item/" + list2.get(j) + ".json?print=pretty";
			responseEntity = restTemplate.exchange(urinext, HttpMethod.GET, entity, ItemDetails.class).getBody();
			jsonfinal.put("id", responseEntity.getId());
			jsonfinal.put("title", responseEntity.getTitle());
			jsonfinal.put("url", responseEntity.getUrl());
			jsonfinal.put("score", responseEntity.getScore());
			jsonfinal.put("time", responseEntity.getTime());
			jsonfinal.put("by", responseEntity.getBy());
			jsonfinal.put("type", responseEntity.getType());
			jsonarray.put(jsonfinal);
		}

		return new ResponseEntity<>(jsonarray.toList(), headers, HttpStatus.OK);

	}

	@Override
	@Cacheable
	public ResponseEntity<Object> getPastStories() {

		System.out.println("inside past stories");
		final String uri = "https://hacker-news.firebaseio.com/v0/topstories.json";
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		Object response = restTemplate.exchange(uri, HttpMethod.GET, entity, Object.class).getBody();
		ArrayList<Integer> list1 = (ArrayList<Integer>) response;
		ArrayList<Integer> list2 = new ArrayList<Integer>();
		Iterator itr = list1.iterator();
		while (itr.hasNext()) {
			Integer i = (Integer) itr.next();
			list2.add(i);
		}

		JSONArray jsonarray = new JSONArray();

		ItemDetails responseEntity = null;
		int counter = 0;
		while (counter < list2.size()) {

			JSONObject jsonfinal = new JSONObject();
			final String urinext = "https://hacker-news.firebaseio.com/v0/item/" + list2.get(counter)
					+ ".json?print=pretty";
			responseEntity = restTemplate.exchange(urinext, HttpMethod.GET, entity, ItemDetails.class).getBody();
			String type = responseEntity.getType();
			System.out.println("types : " + type);
			if (responseEntity.getType().equals("story")) {
				jsonfinal.put("id", responseEntity.getId());
				jsonfinal.put("title", responseEntity.getTitle());
				jsonfinal.put("url", responseEntity.getUrl());
				jsonfinal.put("score", responseEntity.getScore());
				jsonfinal.put("time", responseEntity.getTime());
				jsonfinal.put("by", responseEntity.getBy());
				jsonarray.put(jsonfinal);
				counter++;
			} else {
				break;
			}
		}

		return new ResponseEntity<>(jsonarray.toList(), headers, HttpStatus.OK);
	}

	@Override
	@Cacheable
	public ResponseEntity<Object> getComments(int id) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ItemDetails responseEntity = null;
		final String urinext1 = "https://hacker-news.firebaseio.com/v0/item/" + id + ".json?print=pretty";
		responseEntity = restTemplate.exchange(urinext1, HttpMethod.GET, entity, ItemDetails.class).getBody();

		// Fetching comment from id..sorting them and return
		int kids[] = responseEntity.getKids();
		int arraysize = 0;
		if (responseEntity.getKids().length > 10) {
			arraysize = 10;
		} else {
			arraysize = responseEntity.getKids().length;
		}

		List<Comment> comments = new ArrayList<Comment>();

		for (int j = 0; j < arraysize; j++) {
			Comment comment = fetchComment(kids[j]);
			comments.add(comment);
		}
		comments.sort((Comment comment1, Comment comment2) -> {
			int size1 = 0, size2 = 0;
			if (comment1.getKids() != null)
				size1 = comment1.getKids().size();
			if (comment2.getKids() != null)
				size2 = comment2.getKids().size();
			return size1 - size2;
		});
		return new ResponseEntity<>(comments, headers, HttpStatus.OK);
	}

//for fetching comment from id
	private Comment fetchComment(int id) {
		String jsonResponse = restTemplate
				.getForEntity("https://hacker-news.firebaseio.com/v0/item/" + id + ".json", String.class)
				.getBody();
		Comment item = null;
		try {
			item = new ObjectMapper().readValue(jsonResponse, Comment.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return item;
	}

}
