import React, { Component } from "react";
import { app, server, util, widget } from "components";

import { WEntity } from 'core/widget/entity'
import { BeanObserver, ModifyBeanActions } from 'core/entity'
import { VGridConfigTool, VGridEntityList, VGridEntityListPlugin, WGridEntityListProps } from 'core/widget/vgrid';
import {
  WCommittableEntityList, WCommittableEntityListProps,
  WToolbar, WEntityEditorProps, WEntityEditor, WButtonEntityCommit, WComponent, WButtonEntityWrite,
} from "core/widget";

import { T, AccountRestURL } from "./Dependency";

import VGridConfig = widget.grid.VGridConfig;
import DisplayRecord = widget.grid.model.DisplayRecord;

export class UIBankAccountForm extends WEntity {
  render() {
    let { observer } = this.props;
    let writeCap = this.hasWriteCapability();
    let account = observer.getMutableBean();
    let errorCollector = observer.getErrorCollector();
    const { BBStringField, Form, FormGroup } = widget.input;
    let html = (
      <Form>
        <FormGroup label={T('Account Holder')}>
          <BBStringField bean={account} field={"accountHolder"} required errorCollector={errorCollector} disable={!writeCap} />
        </FormGroup>
        <FormGroup label={T('Account Number')}>
          <BBStringField bean={account} field={"accountNumber"} required errorCollector={errorCollector} disable={!writeCap} />
        </FormGroup>
        <FormGroup label={T('Bank Name')}>
          <BBStringField bean={account} field={"bankName"} disable={!writeCap} />
        </FormGroup>
        <FormGroup label={T('Bank Address')}>
          <BBStringField bean={account} field={"bankAdress"} disable={!writeCap} />
        </FormGroup>
      </Form >
    );
    return html;
  }
}

interface UIBankAccountEditorProps extends WEntityEditorProps {
  loginId: string
}
export class UIBankAccountEditor extends WEntityEditor<UIBankAccountEditorProps> {

  render() {
    let { appContext, pageContext, observer, loginId, onPostCommit, readOnly } = this.props;
    const { HSplit } = widget.component;
    const writeCap = this.hasWriteCapability();
    let html = (
      <HSplit>
        <UIBankAccountForm key={`list-bank-${util.common.IDTracker.next()}`}
          appContext={appContext} pageContext={pageContext} observer={observer}
          readOnly={!writeCap} />
        <WToolbar readOnly={!writeCap}>
          <WButtonEntityCommit
            appContext={appContext} pageContext={pageContext} readOnly={readOnly} observer={observer}
            label={T(`Bank Account For {{loginId}}`, { loginId: loginId })}
            hide={!this.hasWriteCapability()}
            commitURL={AccountRestURL.bankAccount.save(loginId)}
            onPostCommit={onPostCommit} />
        </WToolbar>
      </HSplit>
    )
    return html;
  }
}

interface UIBankAccountListProps extends WGridEntityListProps {
  loginId: string
  multiSelect: boolean;
}
export class BankAccountList extends VGridEntityList<UIBankAccountListProps> {
  createVGridConfig() {
    let { multiSelect } = this.props;
    let writeCap = this.hasWriteCapability();
    let config: VGridConfig = {
      record: {
        fields: [
          ...VGridConfigTool.FIELD_SELECTOR(multiSelect),
          VGridConfigTool.FIELD_INDEX(),
          VGridConfigTool.FIELD_ON_SELECT('accountHolder', T('Account Holder'), 150),

          { name: 'accountNumber', label: T('Account Number') },
          { name: 'bankName', label: T('Bank Name') },
          { name: 'bankAdress', label: T('Bank Address') },
        ],
      },
      toolbar: {
        actions: [
          ...VGridConfigTool.TOOLBAR_ON_DELETE(!writeCap, T("Del")),
        ]
      },
      view: {
        currentViewName: 'table',
        availables: {
          table: {
            viewMode: 'table'
          }
        }
      }
    };
    return config;
  }

  onDefaultSelect(dRecord: DisplayRecord) {
    let { appContext, pageContext, loginId } = this.props;
    let popupPageCtx = pageContext.createPopupPageContext();

    let onPostCommit = (_entity: any, _uiEditor?: WComponent) => {
      this.forceUpdate();
      popupPageCtx.onBack();
    }
    let html = (
      <UIBankAccountEditor appContext={appContext} pageContext={pageContext}
        observer={new BeanObserver(dRecord.record)}
        onPostCommit={onPostCommit} loginId={loginId} />
    );
    widget.layout.showDialog("Bank Account", 'md', html, popupPageCtx.getDialogContext());
  }

  onDeleteAction() {
    let { appContext, plugin, onModifyBean, loginId } = this.props;
    const successCB = (response: server.rest.RestResponse) => {
      plugin.getListModel().removeSelectedDisplayRecords();
      if (onModifyBean) onModifyBean(plugin.getListModel().getRecords(), ModifyBeanActions.DELETE);
      else this.forceUpdate();

      appContext.addOSNotification("success", T(`Delete Bank Account Success`));
    }
    let bankIds = plugin.getListModel().getSelectedRecordIds();
    appContext.serverDELETE(AccountRestURL.bankAccount.delete(loginId), bankIds, successCB);
  }

}

interface UIBankAccountListEditorProps extends WCommittableEntityListProps {
  loginId: string
}
export class UIBankAccountListEditor extends WCommittableEntityList<UIBankAccountListEditorProps> {
  plugin: VGridEntityListPlugin;

  constructor(props: UIBankAccountListEditorProps) {
    super(props);
    this.plugin = new VGridEntityListPlugin([]);
    this.markLoading(true);
    this.loadData();
  }

  loadData() {
    let { appContext, loginId } = this.props;
    const callBack = (response: server.rest.RestResponse) => {
      this.plugin = new VGridEntityListPlugin(response.data);
      this.markLoading(false);
      this.forceUpdate();
    }
    appContext.serverGET(AccountRestURL.bankAccount.findByLoginId(loginId), {}, callBack);
  }

  onNewBankAccount() {
    let { appContext, pageContext, loginId } = this.props;
    let popupPageCtx = pageContext.createPopupPageContext();
    let onPostCommit = (_entity: any, _uiEditor?: WComponent) => {
      this.plugin.getRecords().push(_entity);
      this.forceUpdate();
      popupPageCtx.onBack();
    }
    let html = (
      <UIBankAccountEditor appContext={appContext} pageContext={pageContext}
        observer={new BeanObserver({})}
        onPostCommit={onPostCommit} loginId={loginId} />
    );
    widget.layout.showDialog("Bank Account", 'md', html, popupPageCtx.getDialogContext());
  }

  render() {
    if (this.isLoading()) return this.renderLoading();
    let { appContext, pageContext, loginId } = this.props;
    const writeCap = this.hasWriteCapability();
    let html = (
      <div className='flex-vbox'>
        <BankAccountList key={`list-editor-${util.common.IDTracker.next()}`}
          plugin={this.plugin} multiSelect={true}
          appContext={appContext} pageContext={pageContext} readOnly={!writeCap}
          loginId={loginId} />
        <WToolbar readOnly={!writeCap}>
          <WButtonEntityWrite appContext={appContext} pageContext={pageContext}
            label={T("New Bank Account")} icon={widget.fa.fas.faPlus} onClick={() => this.onNewBankAccount()} />
        </WToolbar>
      </div>
    );
    return html;
  }
}