import React from 'react';
import { app } from 'components'

import { T } from './Dependency';
import { UIJVMPage } from './monitor/UIJVM';
import { UIRestPage } from './monitor/UIRest';
import { UIEntityListPage } from './db/UIEntityList';

class UIApplication extends app.UIMenuApplication {
  createNavigation(appContext: app.AppContext, pageContext: app.PageContext) {
    let navigation: app.NavigationConfig = {
      defaultScreen: 'rest',
      screens: [
        {
          id: "jvm", label: T("JVM"),
          ui: <UIJVMPage appContext={appContext} pageContext={pageContext} />
        },
        {
          id: "rest", label: T("Rest"),
          ui: <UIRestPage appContext={appContext} pageContext={pageContext} />,
        },
        {
          id: "db", label: T("DB"),
          ui: <UIEntityListPage appContext={appContext} pageContext={pageContext} />,
        },
      ],
      sections: [
      ]
    }
    return navigation;
  }
}

export class SystemAppRegistry extends app.BaseAppRegistry {
  module = 'system';
  name = 'system';
  label = 'system';
  description = 'System Utilities';
  requiredAppCapability: app.AppCapability = app.ADMIN;

  createUI(ctx: app.OSContext) { return (<UIApplication osContext={ctx} appRegistry={this} />); };

}