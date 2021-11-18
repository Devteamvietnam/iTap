import React from 'react';
import { widget, util, app } from 'components';

import { ComplexBeanObserver } from "core/entity";
import { WButtonEntityCommit, WButtonEntityReset } from 'core/widget/entity';
import { WToolbar, WComponentProps, WComponent } from 'core/widget';

import { AccountRestURL, T } from './Dependency';
import { WDetailAccountAutoComplete } from './WAccount';
import { AccountType } from './model';

import Validator = util.validator.Validator

const {
  BBStringField, BBPasswordField, BBRadioInputField, Form, FormGroup, BBStringArrayField
} = widget.input
const { TabPane, Tab } = widget.layout;

class PasswordValidator implements Validator {
  validate(val: string): void {
    if (!val) {
      throw new Error("Field cannot be empty");
    }
    if (val.length < 8) {
      throw new Error("Password length must be at least 8 characters");
    }
  }
}

interface UINewAccountProps extends WComponentProps {
  observer: ComplexBeanObserver;
}
class UINewAccountForm extends WComponent<UINewAccountProps> {
  render() {
    const { observer } = this.props;
    const account = observer.getBeanProperty('account', { accountType: AccountType.USER });
    const accountGroupPaths = observer.getBeanProperty('accountGroupPaths', null);
    const partnerGroupPaths = observer.getBeanProperty('partnerGroupPaths', null);
    const departmentIds = observer.getBeanProperty('departmentIds', null);
    let accountTypes = [AccountType.USER, AccountType.ORGANIZATION];
    let passwordValidator = new PasswordValidator();

    let html = (
      <Form>
        <FormGroup label={T('Account Type')}>
          <BBRadioInputField bean={account} field={'accountType'} options={accountTypes} />
        </FormGroup >
        <FormGroup label={T('Login Id')}>
          <BBStringField bean={account} field={'loginId'} errorCollector={observer.getErrorCollector()} required validators={[util.validator.NAME_VALIDATOR]} />
        </FormGroup >
        <FormGroup label={T('Password')}>
          <BBPasswordField bean={account} field={'password'}
            validators={[passwordValidator]}
            errorCollector={observer.getErrorCollector()} required />
        </FormGroup >
        {accountGroupPaths ? (
          <FormGroup label={T('Group Path')}>
            <BBStringArrayField bean={observer.getMutableBean()} field={'accountGroupPaths'} disable />
          </FormGroup >
        ) : null}

        {partnerGroupPaths ? (
          <FormGroup label={T('Group Path')}>
            <BBStringArrayField bean={observer.getMutableBean()} field={'partnerGroupPaths'} disable />
          </FormGroup >
        ) : null}

        {departmentIds ? (
          <FormGroup label={T('Department Id')}>
            <BBStringField bean={observer.getMutableBean()} field={'departmentIds'} disable />
          </FormGroup >
        ) : null}
        <FormGroup label={T('Full Name')}>
          <BBStringField bean={account} field={'fullName'} required errorCollector={observer.getErrorCollector()} />
        </FormGroup >
        <FormGroup label={T('Email')}>
          <BBStringField bean={account} field={'email'} required errorCollector={observer.getErrorCollector()} validators={[util.validator.EMAIL_VALIDATOR]} />
        </FormGroup >
        <FormGroup label={T('Mobile')}>
          <BBStringField bean={account} field={'mobile'} required errorCollector={observer.getErrorCollector()} />
        </FormGroup >
      </Form>
    );
    return html;
  }
}

export class UINewAccountEditorPlugin {
  createAdditionalTabs(appContext: app.AppContext, pageContext: app.PageContext, observer: ComplexBeanObserver): Array<any> {
    return [];
  }
}

interface UINewAccountEditorProps extends UINewAccountProps {
  label?: string;
  commitURL: string;
  plugin?: UINewAccountEditorPlugin;
  onPostCommit?: (newAccountModel: any) => void;
}
export class UINewAccountEditor extends React.Component<UINewAccountEditorProps> {

  render() {
    let { appContext, pageContext, readOnly, plugin, observer, label, commitURL, onPostCommit } = this.props;
    if (!label) label = T('New Account');
    let pluginTabs = new Array<any>();
    if (plugin) {
      pluginTabs = plugin.createAdditionalTabs(appContext, pageContext, observer);
    }

    let html = (
      <div>
        <TabPane laf='outline'>
          <Tab name='account' label={T('Account')} active>
            <UINewAccountForm appContext={appContext} pageContext={pageContext} observer={observer} />
          </Tab>
          {pluginTabs}
        </TabPane>
        <WToolbar>
          <WButtonEntityCommit
            appContext={appContext} pageContext={pageContext} readOnly={readOnly} observer={observer}
            label={`${label}`} commitURL={commitURL} onPostCommit={onPostCommit} />
          <WButtonEntityReset
            appContext={appContext} pageContext={pageContext} readOnly={readOnly} observer={observer} />
        </WToolbar>
      </div>
    );
    return html;
  }
}

export class UIConvertAccountEditor extends React.Component<UINewAccountEditorProps> {
  onPostCommit(account: any) {
    let { onPostCommit: onPostSave } = this.props;
    if (onPostSave) onPostSave(account);
  }

  onSelectAccount(selAccount: any) {
    const { observer } = this.props;
    let model = observer.getMutableBean();
    model.account = selAccount;
    this.forceUpdate();
  }

  render() {
    let { appContext, pageContext, readOnly, plugin, observer, label } = this.props;
    if (!label) label = 'New Account';
    let pluginTabs = new Array<any>();
    if (plugin) {
      pluginTabs = plugin.createAdditionalTabs(appContext, pageContext, observer);
    }

    let html = (
      <div className='flex-vbox' style={{ height: 300 }}>
        <TabPane laf='outline'>
          <Tab className='py-1' name='account' label={T('Account')} active>
            <WDetailAccountAutoComplete
              appContext={appContext} pageContext={pageContext}
              onPostSelect={(selectedBean) => this.onSelectAccount(selectedBean)}
              bean={observer.getMutableBean()} field='loginId' useSelectBean />
          </Tab>
          {pluginTabs}
        </TabPane>
        <WToolbar>
          <WButtonEntityCommit
            appContext={appContext} pageContext={pageContext} readOnly={readOnly} observer={observer}
            label={label} commitURL={AccountRestURL.account.create} onPostCommit={(account) => this.onPostCommit(account)} />
          <WButtonEntityReset
            appContext={appContext} pageContext={pageContext} readOnly={readOnly} observer={observer} />
        </WToolbar>
      </div>
    );
    return html;
  }
}