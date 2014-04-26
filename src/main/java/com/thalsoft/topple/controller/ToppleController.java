package com.thalsoft.topple.controller;

import com.thalsoft.topple.model.ToppleResult;
import com.thalsoft.topple.service.ToppleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ToppleController {

    private static final String URL_LIST_SEPARATOR = ";";

    @Autowired
    private ToppleService toppleService;

    public ToppleController() {
    }

    @RequestMapping("/tag")
    public ToppleResult getByTag(@RequestParam(value="tag", required = true) String tag) {
        String[] tagList = StringUtils.split(tag, URL_LIST_SEPARATOR);

        String url = toppleService.getRandomPicFromTag(tagList);

        return new ToppleResult(url);
    }

    @RequestMapping("/blog")
    public ToppleResult getByBlog(@RequestParam(value="blog", required = true) String blog) {
        String[] blogList = StringUtils.split(blog, URL_LIST_SEPARATOR);

        String url = toppleService.getRandomPicFromBlog(blogList);

        return new ToppleResult(url);
    }


    public ToppleService getToppleService() {
        return toppleService;
    }

    public void setToppleService(ToppleService toppleService) {
        this.toppleService = toppleService;
    }
}
