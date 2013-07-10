package org.exoplatform.extension.synchronization.service.handler;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.exoplatform.extension.synchronization.service.api.AbstractResourceHandler;
import org.exoplatform.extension.synchronization.service.api.SynchronizationService;
import org.exoplatform.portal.mop.SiteType;

public class MOPSiteHandler extends AbstractResourceHandler {

  String parentPath;

  public MOPSiteHandler(SiteType siteType) {
    parentPath = "/site/" + siteType.getName() + "sites/";
  }

  @Override
  public String getParentPath() {
    return parentPath;
  }

  @Override
  public boolean synchronizeData(Set<String> resources, boolean isSSL, String host, String port, String username, String password, Map<String, String> options) {
    filterSubResources(resources);
    if (selectedResources == null || selectedResources.isEmpty()) {
      return false;
    }
    filterOptions(options);
    for (String resourcePath : selectedResources) {
      File file = getExportedFileFromOperation(resourcePath, selectedOptions.keySet().toArray(new String[0]));
      synhronizeData(file, isSSL, host, port, SynchronizationService.SITES_PARENT_PATH, username, password, selectedOptions);
    }
    clearTempFiles();
    return true;
  }
}
