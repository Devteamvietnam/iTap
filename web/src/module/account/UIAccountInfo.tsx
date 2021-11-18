import React from "react";
import { app, widget, server } from 'components';

import { WComponentProps, WComponent } from "core/widget/WLayout";

import { T, AccountRestURL } from "./Dependency";
import { UIProfile } from './UIProfile';
import { UIContactListEditor } from './UIContactList';
import { UIUserEducationListEditor } from './UIUserEducationList';
import { UIUserIdentityListEditor } from "./UIUserIdentityList";
import { UIUserWorkListEditor } from "./UIUserWorkList";
import { UIUserRelationListEditor } from "./UIUserRelationList";
import { UIBankAccountListEditor } from "./UIBankAccountList"

const { TabPane, Tab } = widget.layout;
const { VSplit, VSplitPane } = widget.component;
export interface IUIAccountInfoPlugin {
  createAdditionalTabs: (appContext: app.AppContext, pageContext: app.PageContext, loginId: string) => Array<any>;
}

interface UIAccountInfoProps extends WComponentProps {
  profile: any;
  plugin?: IUIAccountInfoPlugin;
  onModifiedData?: () => void
}
export class UIAccountInfo extends React.Component<UIAccountInfoProps> {
  render() {
    let { appContext, plugin, pageContext, profile } = this.props;
    let additionalTabs = [];

    if (profile.accountType == "USER") {
      additionalTabs.push(
        <Tab key='education' name="educations" label={T("Educations")}>
          <UIUserEducationListEditor appContext={appContext} pageContext={pageContext} loginId={profile.loginId} />
        </Tab>
      );
      additionalTabs.push(
        <Tab key={'identity'} name="identities" label={T("Identities")}>
          <UIUserIdentityListEditor appContext={appContext} pageContext={pageContext} loginId={profile.loginId} />
        </Tab>
      );
      additionalTabs.push(
        <Tab key={'work'} name="works" label={T("Works")}>
          <UIUserWorkListEditor appContext={appContext} pageContext={pageContext} loginId={profile.loginId} />
        </Tab>
      );
      additionalTabs.push(
        <Tab key={'relation'} name="relations" label={T("Relations")}>
          <UIUserRelationListEditor appContext={appContext} pageContext={pageContext} loginId={profile.loginId} />
        </Tab>
      );
      additionalTabs.push(
        <Tab key={'bankAccount'} name="bankAccounts" label={T("Bank Accounts")}>
          <UIBankAccountListEditor appContext={appContext} pageContext={pageContext} loginId={profile.loginId} />
        </Tab>
      )
    }

    let uiAccountInfo = (
      <VSplit>
        <VSplitPane className='pr-1' width={450}>
          <UIProfile appContext={appContext} pageContext={pageContext} profile={profile} />
        </VSplitPane>
        <VSplitPane>
          <TabPane className='mt-1'>
            <Tab key={'contact'} name="contact" label={T("Contacts")} active={true}>
              <UIContactListEditor
                appContext={appContext} pageContext={pageContext} loginId={profile.loginId} />
            </Tab>
            {additionalTabs}
          </TabPane>
        </VSplitPane>
      </VSplit>
    );

    if (plugin) {
      let pluginTabs = plugin.createAdditionalTabs(appContext, pageContext, profile.loginId);
      let html = (
        <TabPane storeId={`account-employee-detail`}>
          <Tab key={'accountModule'} name="accountModule" label={T("Account Info")} active={true}>
            {uiAccountInfo}
          </Tab>
          {pluginTabs}
        </TabPane>
      );
      return html;
    }
    return uiAccountInfo;
  }
}

export interface UILoadableAccountInfoProps extends WComponentProps {
  plugin?: IUIAccountInfoPlugin;
  loginId: string
}
export class UILoadableAccountInfo extends WComponent<UILoadableAccountInfoProps> {
  profile: any = {};

  constructor(props: UILoadableAccountInfoProps) {
    super(props);
    this.markLoading(true);

    let { appContext, loginId } = this.props;
    let callBack = (result: server.rest.RestResponse) => {
      this.profile = result.data;

      this.markLoading(false)
      this.forceUpdate();
    }
    appContext.serverGET(AccountRestURL.profile.load(loginId), {}, callBack);
  }

  render() {
    if (this.isLoading()) return this.renderLoading();
    let { appContext, pageContext, plugin } = this.props;
    return (
      <UIAccountInfo
        appContext={appContext} pageContext={pageContext} plugin={plugin} profile={this.profile} />
    );
  }
}
