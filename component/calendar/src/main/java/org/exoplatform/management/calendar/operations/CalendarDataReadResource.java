/*
 * Copyright (C) 2003-2014 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.exoplatform.management.calendar.operations;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.gatein.management.api.exceptions.OperationException;
import org.gatein.management.api.exceptions.ResourceNotFoundException;
import org.gatein.management.api.operation.OperationContext;
import org.gatein.management.api.operation.OperationHandler;
import org.gatein.management.api.operation.OperationNames;
import org.gatein.management.api.operation.ResultHandler;
import org.gatein.management.api.operation.model.ReadResourceModel;

/**
 * @author <a href="mailto:bkhanfir@exoplatform.com">Boubaker Khanfir</a>
 * @version $Revision$
 */
public class CalendarDataReadResource implements OperationHandler {

  private boolean groupCalendar;

  public CalendarDataReadResource(boolean groupCalendar) {
    this.groupCalendar = groupCalendar;
  }

  @Override
  public void execute(OperationContext operationContext, ResultHandler resultHandler) throws ResourceNotFoundException, OperationException {
    Set<String> children = new LinkedHashSet<String>();
    OrganizationService organizationService = operationContext.getRuntimeContext().getRuntimeComponent(OrganizationService.class);

    try {
      if (groupCalendar) {
        @SuppressWarnings("unchecked")
        Collection<Group> groups = organizationService.getGroupHandler().getAllGroups();
        for (Group group : groups) {
          children.add(group.getId());
        }
      } else {
        ListAccess<User> users = organizationService.getUserHandler().findAllUsers();
        int size = users.getSize(), i = 0;
        while (i < size) {
          int length = i + 10 < size ? 10 : size - i;
          User[] usersArr = users.load(0, length);
          for (User user : usersArr) {
            children.add(user.getUserName());
          }
          i += 10;
        }
      }
    } catch (Exception e) {
      throw new OperationException(OperationNames.READ_RESOURCE, "Error while listing calendars.", e);
    }
    resultHandler.completed(new ReadResourceModel("All calendars:", children));
  }
}