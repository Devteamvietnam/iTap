import React from 'react';
import { app } from 'components'

import { BaseAppRegistry } from 'core/app/api';
import { session } from 'core/app/session';

import { UILoadableAccountInfo } from 'module/account/UIAccountInfo';
import { UILoadableEmployeeInfo } from 'module/company/hr';
import { UIDirectoryList } from 'module/storage/UIDirectoryList';
import { VGridEntityListPlugin } from 'core/widget/vgrid';

class UIApplication extends app.UIMenuApplication {
  createNavigation(appContext: app.AppContext, pageContext: app.PageContext) {
    let loginId = session.getAccountAcl().getLoginId();
    let navigation: app.NavigationConfig = {
      defaultScreen: 'my-profile',
      screens: [
        {
          id: "my-profile", label: "My Profile",
          createUI: (appContext: app.AppContext, pageContext: app.PageContext) => {
            return (<UILoadableAccountInfo appContext={appContext} pageContext={pageContext} loginId={loginId} />)
          }
        },
        {
          id: "my-company", label: "My Company",
          createUI: (appContext: app.AppContext, pageContext: app.PageContext) => {
            return (
              <UILoadableEmployeeInfo appContext={appContext} pageContext={pageContext}
                loginId={loginId} readOnly={true} />
            )
          }
        }
      ],
      sections: [
        {
          label: 'Others', indentLevel: 1,
          screens: [
            {
              id: "my-storage", label: "My Storage",
              createUI: (appContext: app.AppContext, pageContext: app.PageContext) => {
                return (
                  <UIDirectoryList
                    plugin={new VGridEntityListPlugin()}
                    appContext={appContext} pageContext={pageContext} />
                );
              }
            }
          ]
        },
      ]
    }
    return navigation;
  }
}

export class AppRegistry extends BaseAppRegistry {
  module = 'user';
  name = 'my-space';
  label = 'My Space';
  description = 'My Space';
  requiredAppCapability: app.AppCapability = app.NONE;

  createUI(ctx: app.OSContext) { return <UIApplication osContext={ctx} appRegistry={this} />; };
}