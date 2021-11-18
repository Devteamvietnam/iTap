import React from 'react';
import { app } from 'components';

import { T } from 'core/widget/Dependency';
import { CompanyStorage } from 'module/storage/Storage';
import { UIStoragePage } from 'module/storage/UIStorage';

import { UIEmployeeList, UIEmployeeListPlugin } from 'module/company/hr/UIEmployeeList';
import { UIWorkPositionListPlugin, UIWorkPossitionList } from 'module/company/hr/UIWorkPossitionList';

export class UIApplication extends app.UIMenuApplication {
  createNavigation(appContext: app.AppContext, pageContext: app.PageContext) {
    let navigation: app.NavigationConfig = {
      defaultScreen: 'employees',
      screens: [
        {
          id: "employees", label: T("Employees"),
          ui: (<UIEmployeeList appContext={appContext} pageContext={pageContext}
            type='page' plugin={new UIEmployeeListPlugin()} />)
        },
        {
          id: "work-positions", label: T("Positions"),
          ui: (<UIWorkPossitionList appContext={appContext} pageContext={pageContext}
            type='page' plugin={new UIWorkPositionListPlugin()} />)
        },
        {
          id: "storage-company", label: T("Company Storage"),
          ui: (<UIStoragePage context={this.context} appContext={appContext} pageContext={pageContext} storage={new CompanyStorage()} />),
        }
      ],
      sections: [
        {
          label: T('HR'), indentLevel: 1,
          screens: [
           
          ]
        },

        {
          label: T('Settings'), indentLevel: 1,
          screens: [
           
          ]
        },
      ]
    }
    return navigation;
  }
}

export class CompanyAppRegistry extends app.BaseAppRegistry {
  module: string = 'company';
  name: string = 'company';
  label: string = 'Company';
  description: string = 'Company App';
  requiredAppCapability = app.READ;

  createUI(ctx: app.OSContext) {
    return (<UIApplication osContext={ctx} appRegistry={this} />);
  };
}
