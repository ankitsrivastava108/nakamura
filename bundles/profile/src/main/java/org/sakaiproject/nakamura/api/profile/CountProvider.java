package org.sakaiproject.nakamura.api.profile;

import org.sakaiproject.nakamura.api.lite.StorageClientException;
import org.sakaiproject.nakamura.api.lite.accesscontrol.AccessDeniedException;
import org.sakaiproject.nakamura.api.lite.authorizable.Authorizable;


/**
 * Provides counts of various entities assocaited with a user, 
 * e.g. number of groups user is a member of, number of contacts has user has, the number of content items a user owns or can view
 */
public interface CountProvider {

  /**
   * Property name for the parent of all counts in the profile.
   */
  public static final String COUNTS_PROP = "counts";
  /**
   * Property name for the number of contacts the user has.
   */
  public static final String CONTACTS_PROP = "contactsCount";
  /**
   * Property name for the number of groups that an authourizable is a member of.
   */
  public static final String GROUP_MEMBERSHIPS_PROP = "membershipsCount";  // the number of groups a user belongs to
  /**
   * Property name for the number of content items that the authorizable is listed as a manager or viewer.
   */
  public static final String CONTENT_ITEMS_PROP = "contentCount";
  /**
   * The epoch when the counts were last updated. 
   */
  public static final String COUNTS_LAST_UPDATE_PROP = "countLastUpdate";
  /**
   * Property name for the number of members that a group has (int)
   */
  public static final String GROUP_MEMBERS_PROP = "membersCount"; // the number of members that a group has
  
  /**
   * get total counts for group memberships, contacts and content items
   * @param au the authorizable, may be modified by the update operation.
   * @param session
   * @throws AccessDeniedException
   * @throws StorageClientException
   */
  public void update(Authorizable au) throws AccessDeniedException, StorageClientException;
  
  /**
   * are the counts null or too old
   * @param authorizable
   * @return
   * @throws AccessDeniedException
   * @throws StorageClientException
   */
  public boolean needsRefresh(Authorizable authorizable) throws AccessDeniedException, StorageClientException;
  

}
