
package com.anf.core.servlets;

import com.anf.core.services.ResourceResolverService;
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

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.servlet.Servlet;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author shiv
 * SlingSafeMethodsServlet shall be used for HTTP get methods
 * Fetching first 10 pages under /content/anf-code-challenge/us/en using SQL2
 * Output: http://localhost:4502/content/anf-code-challenge/us/en/jcr:content.sql2.txt
 */
@Component(service = {Servlet.class})
@SlingServletResourceTypes(resourceTypes = "anf-code-challenge/components/page",
        methods = HttpConstants.METHOD_GET, selectors = "sql2", extensions = "txt")
@ServiceDescription("First 10 pages with JCR SQL 2 Query")
public class Exercise3JCRSQL2 extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Exercise3JCRSQL2.class);
    private static final String PAGE_PATH = "/content/anf-code-challenge/us/en";

    @Reference
    ResourceResolverService resourceResolverService;

    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) {
        try (ResourceResolver resourceResolver = resourceResolverService.getResourceResolver()) {

            //Creating SQL Query Statement
            String sql2Statement = "SELECT page.* FROM [cq:Page] AS page INNER JOIN [cq:PageContent] AS jcrcontent ON ISCHILDNODE(jcrcontent, page) WHERE ISDESCENDANTNODE(page, \"" + PAGE_PATH + "\") AND jcrcontent.[anfCodeChallenge] IS NOT NULL ORDER BY jcrcontent.[jcr:created] ASC";
            final Session session = resourceResolver.adaptTo(Session.class);
            assert session != null;

            //Creating JCR SQL2 Query and setting the limit as 10
            Query sql2Query = session.getWorkspace().getQueryManager().createQuery(sql2Statement, Query.JCR_SQL2);
            sql2Query.setLimit(10);
            final QueryResult queryResult = sql2Query.execute();
            NodeIterator pages = queryResult.getNodes();
            List<String> pageList = new LinkedList<>();

            //Storing all page path in LinkedList
            while (pages.hasNext()) {
                pageList.add(pages.nextNode().getPath());
            }

            resp.setContentType("text/plain");
            resp.getWriter().write("First 10 Pages Using JCR-SQL2" + pageList);

        } catch (LoginException | RepositoryException | IOException e) {
            LOGGER.error("Exception in SQL2 Query Execution : {}", e.getMessage());
        }
    }
}
