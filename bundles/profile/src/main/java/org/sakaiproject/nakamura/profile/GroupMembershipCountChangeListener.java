package org.sakaiproject.nakamura.profile;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.sakaiproject.nakamura.api.lite.StorageClientException;
import org.sakaiproject.nakamura.api.lite.StoreListener;
import org.sakaiproject.nakamura.api.lite.accesscontrol.AccessDeniedException;
import org.sakaiproject.nakamura.api.lite.authorizable.Authorizable;
import org.sakaiproject.nakamura.api.lite.authorizable.Group;
import org.sakaiproject.nakamura.api.lite.authorizable.User;
import org.sakaiproject.nakamura.api.profile.CountProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(metatype=true, immediate = true, inherit=true)
@Service(value=EventHandler.class)
@Properties(value = {
    @Property(name = "service.vendor", value = "The Sakai Foundation"),
    @Property(name = "service.description", value = "Event Handler counting Group Membership ADDED and UPDATED Events."),
    @Property(name = "event.topics", value = {
        "org/sakaiproject/nakamura/lite/authorizables/ADDED",
        "org/sakaiproject/nakamura/lite/authorizables/UPDATED"}) })
        
public class GroupMembershipCountChangeListener extends AbstractCountHandler implements EventHandler {
  
  private static final Logger LOG = LoggerFactory.getLogger(GroupMembershipCountChangeListener.class);
  private GroupMembershipCounter groupMembershipCounter = new GroupMembershipCounter();

  public void handleEvent(Event event) {
    try {
      if (LOG.isDebugEnabled()) LOG.debug("handleEvent() " + dumpEvent(event));
      // The members of a group are defined in the membership, so simply use that value, no need to increment or decrement.
      String groupId = (String) event.getProperty(StoreListener.PATH_PROPERTY);
      Authorizable au = authorizableManager.findAuthorizable(groupId);
      if ( au instanceof Group ) {
        int n = groupMembershipCounter.count(au, authorizableManager);
        Integer v = (Integer) au.getProperty(CountProvider.GROUP_MEMBERSHIPS_PROP);
        if ( v == null || n != v.intValue()) {
          au.setProperty(CountProvider.GROUP_MEMBERSHIPS_PROP, n);
          authorizableManager.updateAuthorizable(au);
        }
      }
      else if (au instanceof User) {
        String userId = (String) event.getProperty(StoreListener.PATH_PROPERTY);
        if (LOG.isDebugEnabled()) LOG.debug("got User event for " + userId);
      }
    } catch (StorageClientException e) {
      LOG.debug("Failed to update count ", e);
    } catch (AccessDeniedException e) {
      LOG.debug("Failed to update count ", e);
    }
  }
}

