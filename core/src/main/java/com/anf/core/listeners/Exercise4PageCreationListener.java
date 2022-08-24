package com.anf.core.listeners;

import com.anf.core.services.ResourceResolverService;
import com.day.crx.JcrConstants;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * @author Shiv
 * Resource Change Listner on Page Creation to set 'pageCreated' Property
 */
@Component(service = ResourceChangeListener.class, property = {
        ResourceChangeListener.PATHS + "=" + "/content/anf-code-challenge/us/en",
        ResourceChangeListener.CHANGES + "=" + "ADDED"
})
public class Exercise4PageCreationListener implements ResourceChangeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Exercise4PageCreationListener.class);

    @Reference
    ResourceResolverService resourceResolverService;

    @Override
    public void onChange(List<ResourceChange> list) {
        list.forEach(change -> {
            String addedPath = change.getPath();
            //Checking if the created node page have jcr:content
            if (addedPath.endsWith(JcrConstants.JCR_CONTENT)) {
                try (ResourceResolver resourceResolver = resourceResolverService.getResourceResolver();) {
                    Resource resource = Objects.requireNonNull(resourceResolver.getResource(addedPath));
                    //Using ModifiableValueMap to update the page created properties
                    ModifiableValueMap modifiableValueMap = resource.adaptTo(ModifiableValueMap.class);
                    assert modifiableValueMap != null;
                    //Updating the pageCreated Properties
                    modifiableValueMap.put("pageCreated", true);
                    //Committing the resource resolver
                    resource.getResourceResolver().commit();
                } catch (LoginException | PersistenceException e) {
                    LOGGER.error("Error Occurred in Resource Change Listener on Page Creation : {}", e.getMessage());
                }
            }
        });
    }
}
