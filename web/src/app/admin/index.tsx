import React from 'react';
import { app } from 'components';

import { T } from './Dependency';

import { UIAccountList, UIAccountListPlugin } from 'module/admin/account/UIAccountList';
import { UICompanyList, UICompanyListPlugin } from 'module/admin/company/UICompanyList';
import { SystemStorage } from 'module/admin/storage/Storage';
import { UIStoragePage } from 'module/admin/storage/UIStorage';
import { UIAppList, UIAppListPlugin } from './permission/UIAppList';

class UIApplication extends app.UIMenuApplication {
  createNavigation(appContext: app.AppContext, pageContext: app.PageContext) {
    let navigation: app.NavigationConfig = {
      defaultScreen: 'companies',
      screens: [
        {
          id: "companies", label: T("Companies"),
          ui: (<UICompanyList plugin={new UICompanyListPlugin()} appContext={appContext} pageContext={pageContext}
            type={"page"} />)
        },
        {
          id: "users", label: T("Users"),
          ui: (
            <UIAccountList
              appContext={appContext} pageContext={pageContext} type='page' plugin={new UIAccountListPlugin()} />)
        },
        {
          id: "app-permissions", label: T("App Permissions"), requiredCapability: app.WRITE,
          ui: (<UIAppList plugin={new UIAppListPlugin()} appContext={appContext} pageContext={pageContext} />),
        },
        {
          id: "storage-system", label: T("Storage"), requiredCapability: app.ADMIN,
          ui: (<UIStoragePage context={this.context} appContext={appContext} pageContext={pageContext} storage={new SystemStorage()} />)
        }
      ],
      sections: [
        {
          label: T('Settings'), indentLevel: 1, requiredCapability: app.READ,
          screens: [
          ]
        }
      ]
    }
    return navigation;
  }
}

export class AdminAppRegistry extends app.BaseAppRegistry {
  module = 'admin';
  name = 'admin';
  label = 'Admin';
  description = 'Admin App';
  requiredAppCapability = app.ADMIN;

  createUI(ctx: app.OSContext) { return (<UIApplication osContext={ctx} appRegistry={this} />); };
}