import { app } from 'components'

import AppCapability = app.AppCapability;

export interface IAccessToken {
  label: string;
  token: string;
  loginId: string;
  passPrase: string;
  accessType: 'Account' | 'Employee' | 'Partner' | 'Temporary' | 'None';
  accessCount: number;
  lastAccessTime: Date;
  expireTime: Date;
}

export interface IAppPermission {
  appModule: string;
  appName: string;
  capability: 'None' | 'Read' | 'Write' | 'Moderator' | 'Admin';
}

export interface ICompanyAclModel {
  companyId: any;
  companyParentId: any;
  companyCode: string;
  companyLabel: string;
  appPermissions: Array<IAppPermission>;
}

export interface IAclModel {
  sessionId: string;
  accessToken: IAccessToken;
  companyAcl: ICompanyAclModel;
  availableCompanyAcls?: Array<ICompanyAclModel>
};
export interface IAccountState {
  companyCode: string;
  loginId: string;
  accessToken?: string;
}
export class AccountAcl {
  aclModel: IAclModel;
  appPermissionMap: any;

  constructor(model: IAclModel) {
    this.aclModel = model;
    this.appPermissionMap = {};
    if (model.companyAcl) {
      let appPermissions = this.aclModel.companyAcl.appPermissions;
      for (let i = 0; i < appPermissions.length; i++) {
        let permission = appPermissions[i];
        this.appPermissionMap[`${permission.appModule}/${permission.appName}`] = permission;
      }
    }
  }

  getLoginId() { return this.aclModel.accessToken.loginId; }

  isAuthorized() { return this.aclModel.accessToken.accessType != 'None'; }

  getAccessToken() { return this.aclModel.accessToken.token }
  getFullName() { return this.aclModel.accessToken.label }

  getSelectedCompany() {
    if (this.aclModel.companyAcl) {
      return this.aclModel.companyAcl.companyCode;
    }
    return '';
  }

  getCompanyAcl() {
    if (!this.aclModel.companyAcl) {
      throw new Error("Company Acl Not Available");
    }
    return this.aclModel.companyAcl;
  }

  changeCompanyAcl(companyAcl: ICompanyAclModel) {
    this.aclModel.companyAcl = companyAcl;
  }

  getAvailableCompanyAcls() { return this.aclModel.availableCompanyAcls; }

  getUserAppCapability(module: string, appName: string): AppCapability {
    let key = `${module}/${appName}`;
    let appPermission: IAppPermission = this.appPermissionMap[key];
    if (appPermission == null) return app.NONE;
    return new app.AppCapability(appPermission.capability);
  }
};

export class BaseAppRegistry implements app.IAppRegistry {
  module: string = 'unknown';
  name: string = 'unknown';
  label: string = 'Unknown';
  description: string = '';

  requiredAppCapability: app.AppCapability = app.NONE;
  userAppCapability: app.AppCapability = app.NONE;

  getRequiredAppCapability() { return this.requiredAppCapability; }

  setRequiredAppCapability(capability: app.AppCapability) {
    this.requiredAppCapability = capability;
  }

  getUserAppCapability() { return this.userAppCapability; }

  setUserAppCapability(capability: app.AppCapability) {
    this.userAppCapability = capability;
  }

  createUI(_ctx: app.OSContext) {
    throw new Error('You need to override this method');
  };
}