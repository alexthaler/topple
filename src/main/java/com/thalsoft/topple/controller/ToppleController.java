package com.thalsoft.topple.controller;

import com.thalsoft.topple.model.ToppleResult;
import com.thalsoft.topple.service.ToppleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ToppleController {

    @Autowired
    private ToppleService toppleService;

    public ToppleController() {
    }

    @RequestMapping("/tag")
    public ToppleResult getByTag(@RequestParam(value="tag", required = true) String tag) {
        String url = toppleService.getRandomPicFromTag(tag);

        return new ToppleResult(url);
    }

    @RequestMapping("/blog")
    public ToppleResult getByBlog(@RequestParam(value="blog", required = true) String blog) {
        String url = toppleService.getRandomPicFromBlog(blog);

        return new ToppleResult(url);
    }


    public ToppleService getToppleService() {
        return toppleService;
    }

    public void setToppleService(ToppleService toppleService) {
        this.toppleService = toppleService;
    }
}
