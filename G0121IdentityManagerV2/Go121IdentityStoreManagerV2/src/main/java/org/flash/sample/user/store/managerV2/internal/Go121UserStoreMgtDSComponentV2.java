package org.flash.sample.user.store.managerV2.internal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flash.sample.user.store.managerV2.Go121CustomUserStoreManagerV2;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * @scr.component name="custom.user.store.manager.dscomponent" immediate=true
 * @scr.reference name="user.realmservice.default"
 * interface="org.wso2.carbon.user.core.service.RealmService"
 * cardinality="1..1" policy="dynamic" bind="setRealmService"
 * unbind="unsetRealmService"
 */
public class Go121UserStoreMgtDSComponentV2 {
    private static Log log = LogFactory.getLog(Go121UserStoreMgtDSComponentV2.class);
    private static RealmService realmService;
    protected void activate(ComponentContext ctxt) {
        Go121CustomUserStoreManagerV2 go121CustomUserStoreManagerV2 = new Go121CustomUserStoreManagerV2();
        ctxt.getBundleContext().registerService(UserStoreManager.class.getName(), go121CustomUserStoreManagerV2, null);
        log.info("Go121CustomUserStoreManagerV2 bundle activated successfully..");
    }
    protected void deactivate(ComponentContext ctxt) {
        if (log.isDebugEnabled()) {
            log.debug("Custom User Store Manager is deactivated ");
        }
    }
    protected void setRealmService(RealmService rlmService) {
        realmService = rlmService;
    }
    protected void unsetRealmService(RealmService realmService) {
        realmService = null;
    }
}
