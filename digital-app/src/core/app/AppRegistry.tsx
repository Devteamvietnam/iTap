import {app} from  'components';

import { session } from 'core/app/session'

export class AppRegistryGroup {
  name: string;
  label: string;
  visible: boolean = false;
  registries: Record<string, app.IAppRegistry> = {};

  constructor(name: string, label: string) {
    this.name = name;
    this.label = label;
  }

  get(name: string): null | app.IAppRegistry {
    if (this.registries[name]) return this.registries[name];
    else return null;
  }

  add(registry: app.IAppRegistry) {
    this.registries[registry.name] = registry;
  }
}

class BaseAppRegistryManager {
  appRegistryNav: Record<string, AppRegistryGroup> = {};
  appRegistries: Record<string, app.IAppRegistry> = {};
  defaultAppRegistry: null | app.IAppRegistry = null;

  get(name: string, retDefault: boolean): null | app.IAppRegistry {
    if (this.appRegistries[name]) return this.appRegistries[name];
    else if (retDefault) return this.defaultAppRegistry;
    else return null;
  }

  addGroupApps(group: AppRegistryGroup, registries: Array<app.IAppRegistry>) {
    let addCount = 0;
    for (let i = 0; i < registries.length; i++) {
      let registry = registries[i];
      group.add(registry);
      addCount++;
    }
    if (addCount > 0) this.addGroup(group);
  }

  protected addGroup(group: AppRegistryGroup) {
    this.appRegistryNav[group.name] = group;
    for (let name in group.registries) {
      let registry = group.registries[name];
      this.appRegistries[registry.name] = registry;
    }
  }
}

export const AppRegistryManager = new BaseAppRegistryManager();

export class UserAppRegistryManager extends BaseAppRegistryManager {
  constructor() {
    super();
    for(let groupName in AppRegistryManager.appRegistryNav) {
      let group = AppRegistryManager.appRegistryNav[groupName];
      this.addUserGroup(group);
    }
  }

  private addUserGroup(group: AppRegistryGroup) {
    let userGroup = new AppRegistryGroup(group.name, group.label);
    let addCount = 0;
    for(let registryName in group.registries) {
      let registry = group.registries[registryName];
      if (!this.userCanAccessApp(registry)) continue;
      userGroup.add(registry);
      addCount++;
    }
    if (addCount > 0) this.addGroup(userGroup);
  }

  private userCanAccessApp(registry: app.IAppRegistry) {
    let userAppCap = session.getAccountAcl().getUserAppCapability(registry.module, registry.name);
    registry.setUserAppCapability(userAppCap);
    return userAppCap.hasCapability(registry.getRequiredAppCapability());
  }
}
