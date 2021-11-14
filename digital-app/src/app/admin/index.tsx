import React from 'react';
import { app } from 'components';

import { T } from './Dependency';

import { SystemStorage } from 'module/storage/Storage';
import { UIStoragePage } from 'module/storage/UIStorage';
import { UIAppList, UIAppListPlugin } from './permission/UIAppList';

class UIApplication extends app.UIMenuApplication {
  createNavigation(appContext: app.AppContext, pageContext: app.PageContext) {
    let navigation: app.NavigationConfig = {
      defaultScreen: 'companies',
      screens: [
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