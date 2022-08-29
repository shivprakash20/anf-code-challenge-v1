package com.anf.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author shiv
 * News feed model class to read the news and display in the component.
 */
@Model(adaptables = Resource.class)
public class NewsFeedModel {

    private static final String NEWS_FEED_PATH = "/var/commerce/products/anf-code-challenge/newsData";

    List<NewsFeedBean> news;

    @SlingObject
    private ResourceResolver resourceResolver;

    @PostConstruct
    protected void init() {

        news = new ArrayList<>();
        //Getting news feed resource
        Iterator<Resource> resource = Objects.requireNonNull(resourceResolver.getResource(NEWS_FEED_PATH)).listChildren();

        while (resource.hasNext()) {
            //Getting ValueMap
            ValueMap valueMap = resource.next().getValueMap();
            NewsFeedBean anfBean = new NewsFeedBean();
            anfBean.setTitle(valueMap.get("title", String.class));
            anfBean.setAuthor(valueMap.get("author", String.class));
            anfBean.setDescription(valueMap.get("description", String.class));
            anfBean.setUrl(valueMap.get("url", String.class));
            anfBean.setUrlImage(valueMap.get("urlImage", String.class));
            //Storing all value in ArrayList
            news.add(anfBean);
        }

    }

    public List<NewsFeedBean> getNews() {
        return news;
    }
}
