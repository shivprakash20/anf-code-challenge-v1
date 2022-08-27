
package com.anf.core.servlets;

import com.anf.core.services.ResourceResolverService;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author shiv
 * SlingSafeMethodsServlet shall be used for HTTP get methods
 * Fetching first 10 pages under /content/anf-code-challenge/us/en using Query Builder API
 * Output: http://localhost:4502/content/anf-code-challenge/us/en/jcr:content.queryBuilder.txt
 */
@Component(service = {Servlet.class})
@SlingServletResourceTypes(resourceTypes = "anf-code-challenge/components/page",
        methods = HttpConstants.METHOD_GET, selectors = "queryBuilder", extensions = "txt")
@ServiceDescription("First 10 pages with Query Builder API")
public class Exercise3QueryBuilderAPI extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Exercise3QueryBuilderAPI.class);
    private static final String PAGE_PATH = "/content/anf-code-challenge/us/en";

    @Reference
    ResourceResolverService resourceResolverService;

    @Reference
    transient QueryBuilder queryBuilder;

    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) {
        try (ResourceResolver resourceResolver = resourceResolverService.getResourceResolver()) {

            final Session session = resourceResolver.adaptTo(Session.class);

            //Creating Query Statement with Hashmap
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("path", PAGE_PATH);
            hashMap.put("type", "cq:Page");
            hashMap.put("property", "jcr:content/anfCodeChallenge");
            hashMap.put("property.operation", "exists");
            hashMap.put("orderby","@jcr:created");
            hashMap.put("p.limit", "10");

            //Creating Query using query statement
            Query query = queryBuilder.createQuery(PredicateGroup.create(hashMap), session);
            SearchResult result = query.getResult();

            //Storing all page path in LinkedList
            List<String> pageList = new LinkedList<>();
            for (Hit hit : result.getHits()) {
                pageList.add(hit.getResource().getPath());
            }

            resp.setContentType("text/plain");
            resp.getWriter().write("First 10 Pages Using Query Builder API" + pageList);

        } catch (LoginException | RepositoryException | IOException e) {
            LOGGER.error("Exception in Query Builder API Execution : {}", e.getMessage());
        }
    }
}
