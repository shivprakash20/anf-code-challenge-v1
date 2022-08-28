/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.anf.core.servlets;

import com.anf.core.configurations.AgeValidationNodePath;
import com.anf.core.services.ContentService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Objects;

@Component(service = { Servlet.class })
@SlingServletPaths(value = "/bin/saveUserDetails")
public class UserServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    @Reference
    private ContentService contentService;

    private AgeValidationNodePath ageValidationNodePath;

    @Activate
    public void activate(AgeValidationNodePath config) {
        this.ageValidationNodePath = config;
    }

    private int currnetAge;

    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws ServletException, IOException {
        // Make use of ContentService to write the business logic
        //Getting Current Age from request
        currnetAge = Integer.parseInt(req.getParameter("inputAge"));

        //Getting resource from given path
        Resource resource = req.getResourceResolver().getResource(ageValidationNodePath.getAgeLimitPath());

        assert resource != null;
        ValueMap valueMap = resource.adaptTo(ValueMap.class);

        //Fetching Max and Min age
        assert valueMap != null;
        int maxAge = Integer.parseInt(Objects.requireNonNull(valueMap.get("maxAge", String.class)));
        int minAge = Integer.parseInt(Objects.requireNonNull(valueMap.get("minAge", String.class)));

        //Sending response after validation
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        if(currnetAge >= minAge && currnetAge <= maxAge)
            resp.getWriter().write("true");
        else
            resp.getWriter().write("false");
    }
}
