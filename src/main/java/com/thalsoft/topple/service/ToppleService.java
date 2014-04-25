package com.thalsoft.topple.service;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Photo;
import com.tumblr.jumblr.types.PhotoPost;
import com.tumblr.jumblr.types.PhotoSize;
import com.tumblr.jumblr.types.Post;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

@Service
public class ToppleService implements InitializingBean {

    private static final Logger log = Logger.getLogger(ToppleService.class.getName());

    private static final String PHOTO = "photo";

    @Value("${tumblr_consumer_key}")
    private String consumerKey;

    @Value("${tumblr_consumer_secret}")
    private String consumerSecret;

    private JumblrClient jumblrClient;

    public ToppleService() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.jumblrClient = new JumblrClient(consumerKey, consumerSecret);
    }

    public String getRandomPicFromTag(String tag) {
        List<PhotoPost> picturePosts = getPostsFromTags(tag);

        return getRandomUrlFromPosts(picturePosts);
    }

    public String getRandomPicFromBlog(String tag) {
        List<PhotoPost> picturePosts = getPostsFromBlogs(tag);

        return getRandomUrlFromPosts(picturePosts);
    }

    private String getRandomUrlFromPosts(List<PhotoPost> posts) {
        if (posts == null) {
            return "";
        }

        List<Photo> picturePostsPhotos = new ArrayList<Photo>();

        for (Post post : posts) {
            PhotoPost photoPost = (PhotoPost) post;
            picturePostsPhotos.addAll(photoPost.getPhotos());
        }

        List<String> photoPostsUrls = new ArrayList<String>();

        for (Photo photo : picturePostsPhotos) {

            List<PhotoSize> photoSizes = photo.getSizes();
            PhotoSize largestSize = null;

            for (PhotoSize photoSize : photoSizes) {
                if (largestSize == null || largestSize.getHeight() * largestSize.getWidth() < photoSize.getHeight() *
                        photoSize.getWidth()) {
                    largestSize = photoSize;
                }
            }

            photoPostsUrls.add(largestSize.getUrl());
        }

        log.info("Size of list " + photoPostsUrls.size());
        int randomIndex = new Random().nextInt(photoPostsUrls.size() - 1);

        log.info("random Int " + randomIndex);
        return photoPostsUrls.get(randomIndex);
    }

    private List<PhotoPost> getPostsFromTags(String... tags) {
        String tag = tags[0];

        List<Post> posts = jumblrClient.tagged(tag);

        if (posts.size() == 0) {
            log.info("No posts found with tag " + tag + " returning empty.");
            return null;
        }

        List<PhotoPost> picturePosts = new ArrayList<PhotoPost>();
        boolean picturePostFound = false;

        while (!picturePostFound) {

            Post lastPost = null;

            for (Post post : posts) {
                if (post.getType().equals(PHOTO)) {
                    picturePosts.add((PhotoPost) post);
                    picturePostFound = true;
                } else {
                    lastPost = post;
                }
            }

            if (!picturePostFound) {
                Long lastPostTimestamp = lastPost.getTimestamp();

                HashMap<String, Object> options = new HashMap<String, Object>();
                options.put("before", lastPostTimestamp);

                posts = jumblrClient.tagged(tag, options);
            }
        }

        return picturePosts;
    }

    private List<PhotoPost> getPostsFromBlogs(String... blogs) {
        String blog = blogs[0];

        List<Post> posts = jumblrClient.blogPosts(blog);

        if (posts.size() == 0) {
            log.info("No posts found from blog " + blog + " returning empty.");
            return null;
        }

        List<PhotoPost> picturePosts = new ArrayList<PhotoPost>();
        boolean picturePostFound = false;

        while (!picturePostFound) {

            Post lastPost = null;

            for (Post post : posts) {
                if (post.getType().equals(PHOTO)) {
                    picturePosts.add((PhotoPost) post);
                    picturePostFound = true;
                } else {
                    lastPost = post;
                }
            }

            if (!picturePostFound) {
                Long lastPostTimestamp = lastPost.getTimestamp();

                HashMap<String, Object> options = new HashMap<String, Object>();
                options.put("before", lastPostTimestamp);

                posts = jumblrClient.blogPosts(blog, options);
            }
        }

        return picturePosts;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public JumblrClient getJumblrClient() {
        return jumblrClient;
    }

    public void setJumblrClient(JumblrClient jumblrClient) {
        this.jumblrClient = jumblrClient;
    }

}
