import { Component } from 'react'
import { app } from 'components'
import config from 'core/app/config'

import { AppRegistryManager, UserAppRegistryManager, AppRegistryGroup } from 'core/app/AppRegistry'
import { Workspaces } from 'app/webos/Workspaces'

import { AppRegistry as HomeAppRegistry } from 'app/user/home'

import { AppRegistry as SampleAppRegistry } from 'app/sample';
import { AppRegistry as HRAppRegistry } from 'app/company';

import { AppRegistry as AdminAppRegistry } from 'app/admin'
import { AppRegistry as SystemAppRegistry } from 'app/system'

import "./stylesheet.scss";

let userGroup = new AppRegistryGroup('user', 'User');
let userApps = [
  new HomeAppRegistry()
];
AppRegistryManager.addGroupApps(userGroup, userApps);

let systemGroup = new AppRegistryGroup('system', 'System');
let systemApps = [
  new AdminAppRegistry(), new SystemAppRegistry()
];
AppRegistryManager.addGroupApps(systemGroup, systemApps);

let companyGroup = new AppRegistryGroup('company', 'Company');
let companyApps = [
  new HRAppRegistry()
];
AppRegistryManager.addGroupApps(companyGroup, companyApps);

let otherGroup = new AppRegistryGroup('others', 'Others');
let otherApps = [new SampleAppRegistry()];
AppRegistryManager.addGroupApps(otherGroup, otherApps);
export class WebosContext {
  appRegistryManager: UserAppRegistryManager;
  osContext: app.OSContext;
  workspaces: Workspaces;
  defaultAppName: string = 'project';

  constructor(uiOS: Component) {
    let configModel = config.getModel();
    this.appRegistryManager = new UserAppRegistryManager();
    let serverCtx = new app.ServerContext(configModel.hosting.domain, configModel.serverUrl, configModel.restUrl);
    this.osContext = new app.OSContext(uiOS, serverCtx);
    this.workspaces = new Workspaces();

  }

  getDefaultAppName() { return this.defaultAppName; }

  getAppRegistryManager() { return this.appRegistryManager; }
}