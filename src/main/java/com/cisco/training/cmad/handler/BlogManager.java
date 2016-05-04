package com.cisco.training.cmad.handler;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.cisco.training.cmad.dto.BlogDTO;
import com.cisco.training.cmad.dto.UserDTO;
import com.cisco.training.cmad.util.MongoDBUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BlogManager {
	Logger logger = Logger.getLogger(UserManager.class.getName());
	ObjectMapper mapper = new ObjectMapper();
	Datastore dataStore = MongoDBUtil.getMongoDB();

	public void getBlogs(RoutingContext routingContext) {
		List<BlogDTO> blogs = dataStore.createQuery(BlogDTO.class).asList();
		if (blogs.size() > 0) {
			routingContext
					.response()
					.setStatusCode(201)
					.putHeader("content-type",
							"application/json; charset=utf-8")
					.end(Json.encodePrettily(blogs));
		} else {
			routingContext
					.response()
					.setStatusCode(201)
					.putHeader("content-type",
							"application/json; charset=utf-8")
					.end(Json
							.encodePrettily("No blogs found!!! Go ahead & create one."));
		}
	}

	public void postBlog(RoutingContext routingContext) {

		logger.log(Level.INFO,
				"Inside postBlog API!! " + routingContext.getBodyAsString());

		try {
			BlogDTO blogDTO = Json.decodeValue(
					routingContext.getBodyAsString(), BlogDTO.class);
			blogDTO.setBlogId(UUID.randomUUID().toString());
			Session session = routingContext.session();
			UserDTO userSession = session.get("userData");
			blogDTO.setUserId(userSession.getUserName());
			blogDTO.setComments(null);
			dataStore.save(blogDTO);

			/*
			 * vertx.executeBlocking(future -> { dataStore.save(userDTO); }, res
			 * -> {
			 * logger.log(Level.INFO,"Added New User Details Successsfully. : "
			 * + res.result()); });
			 */

			routingContext
					.response()
					.setStatusCode(201)
					.putHeader("content-type",
							"application/json; charset=utf-8")
					.end(Json.encodePrettily(blogDTO));

		} catch (Exception e) {
			logger.log(Level.INFO,
					"Exception while posting blog " + e.getMessage());
		}

	}

	public void updateBlog(RoutingContext routingContext) {

	}

	public void getBlog(RoutingContext routingContext) {
		logger.log(Level.INFO,
				"Inside getBlog API!! " + routingContext.getBodyAsString());
		try {
			BlogDTO frontEngBlogDTO = mapper.readValue(
					routingContext.getBodyAsString(), BlogDTO.class);
			Query<BlogDTO> q = dataStore.find(BlogDTO.class, "title",
					frontEngBlogDTO.getTitle());
			logger.log(Level.INFO,
					"The given title is :" + frontEngBlogDTO.getTitle());

			if (q != null && q.get() != null)
				logger.log(Level.INFO, "The retrieved blog content is : "
						+ q.get().getContent());
		} catch (Exception e) {
			logger.log(Level.INFO,
					"Exception while retriving blog " + e.getMessage());
		}

	}

	public void addComment(RoutingContext routingContext) {
		try {
			logger.log(Level.INFO, "Inside getBlog API!! addComment"
					+ routingContext.getBodyAsString());

			String blogID = routingContext.request().getParam("blogId");

			String blogComment = java.net.URLDecoder.decode(routingContext
					.request().getParam("comment"), "UTF-8");
			Query<BlogDTO> q = dataStore.find(BlogDTO.class, "blogId", blogID);
			Session session = routingContext.session();
			UserDTO userSession = session.get("userData");
			String commentId = UUID.randomUUID().toString();
			Map<String, String> commentMap = new HashMap<String, String>();
			commentMap.put(userSession.getUserName(), blogComment);
			Map<String, Map<String, String>> existingMap = new HashMap<String, Map<String, String>>();
			if (q.get().getComments() != null)
				existingMap = q.get().getComments();
			existingMap.put(commentId, commentMap);
			UpdateOperations<BlogDTO> ops = dataStore.createUpdateOperations(
					BlogDTO.class).set("comments", existingMap);
			dataStore.update(q.get(), ops);
			routingContext
					.response()
					.setStatusCode(201)
					.putHeader("content-type",
							"application/json; charset=utf-8")
					.end("Comment Added Successfully!!!!");
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void searchBlog(RoutingContext routingContext) {
		try {
			logger.log(Level.INFO, "Inside searchBlog API!!");

			String searchTag = java.net.URLDecoder.decode(routingContext
					.request().getParam("tag"), "UTF-8");
			logger.log(Level.INFO, "Search tag: " + searchTag);
			
			Query<BlogDTO> q = dataStore.createQuery(BlogDTO.class);
			q.or(
			    q.criteria("title").contains(searchTag),
			    q.criteria("tags").contains(searchTag)
			);
			List<BlogDTO> blogs = q.asList();
			logger.log(Level.INFO, "Search tag: " + blogs.size());
			if (blogs.size() > 0) {
				routingContext
						.response()
						.setStatusCode(201)
						.putHeader("content-type",
								"application/json; charset=utf-8")
						.end(Json.encodePrettily(blogs));
			} else {
				routingContext
						.response()
						.setStatusCode(201)
						.putHeader("content-type",
								"application/json; charset=utf-8")
						.end(Json
								.encodePrettily("No blogs found!!!"));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
