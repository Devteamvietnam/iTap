import React from "react";
import { app, widget, server } from 'components';

import { WComponentProps, WComponent } from "core/widget/WLayout";
import { WEntityEditor, WEntityEditorProps } from "core/widget/entity";

import { T, AccountRestURL } from "./Dependency";
import { UIProfile } from './UIProfile';
import { UIContactListEditor } from './UIContactList';
import { UIUserEducationListEditor } from './UIUserEducationList';
import { UIUserIdentityListEditor } from "./UIUserIdentityList";
import { UIUserWorkListEditor } from "./UIUserWorkList";
import { UIUserRelationListEditor } from "./UIUserRelationList";
import { UIBankAccountListEditor } from "./UIBankAccountList"
import { BeanObserver } from "core/widget";

const { TabPane, Tab } = widget.layout;
const { VSplit, VSplitPane } = widget.component;
const { FAButton } = widget.fa;

export interface IUIAccountInfoPlugin {
  createAdditionalTabs: (appContext: app.AppContext, pageContext: app.PageContext, loginId: string) => Array<any>;
}

interface UIAccountInfoProps extends WEntityEditorProps {
  plugin?: IUIAccountInfoPlugin;
  onModifiedData?: () => void
}
export class UIAccountInfo extends WEntityEditor<UIAccountInfoProps> {
  render() {
    let { appContext, plugin, pageContext, observer } = this.props;
    let additionalTabs = [];
    const writeCap = this.hasWriteCapability();
    const profile = observer.getMutableBean();

    let uiAccountInfo = (
      <VSplit>
        <VSplitPane className='pr-2' width={600}>
          <UIProfile appContext={appContext} pageContext={pageContext} profile={profile} readOnly={!writeCap} />
        </VSplitPane>
        <VSplitPane>
          <div>Comming soon</div>
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
  observer: BeanObserver = new BeanObserver({});

  constructor(props: UILoadableAccountInfoProps) {
    super(props);
    this.markLoading(true);

    let { appContext, loginId } = this.props;
    let callBack = (result: server.rest.RestResponse) => {
      const profile = result.data;
      this.observer.replaceWith(profile);
      this.markLoading(false)
      this.forceUpdate();
    }
    appContext.serverGET(AccountRestURL.profile.load(loginId), {}, callBack);
  }

  render() {
    if (this.isLoading()) return this.renderLoading();
    let { appContext, pageContext, plugin } = this.props;
    const writeCap = this.hasWriteCapability();

    return (
      <div className='flex-vbox'>
        <UIAccountInfo appContext={appContext} pageContext={pageContext} plugin={plugin}
          observer={this.observer} readOnly={!writeCap} />
        {this.renderRemoteButton()}
      </div>
    );
  }

  renderRemoteButton() {
    let onHelloRemote = () => {
    }

    let html = (
      <FAButton onClick={onHelloRemote}>Digital @devteam</FAButton>
    )
    return html;
  }
}
