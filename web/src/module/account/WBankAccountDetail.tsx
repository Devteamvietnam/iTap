import React from "react";
import { widget, app, server } from 'components'

import {
  BBEntityAutoComplete, EntityAutoCompletePlugin, BBEntityAutoCompleteProps, ComplexBeanObserver
} from 'core/widget';
import { VGridEntityListPlugin } from "core/widget/vgrid";

import { AccountRestURL, T } from "./Dependency";
import { BankAccountList, UIBankAccountForm, UIBankAccountEditor } from "./UIBankAccountList";

import BBAutoComplete = widget.input.BBAutoComplete2;

const { fa } = widget;
const { BBStringField, FormContainer, ColFormGroup, Row } = widget.input;
export class BankAccountAutoCompletePlugin extends EntityAutoCompletePlugin {
  loginId: string;
  accountHolder: string;
  accountNumber: string;
  bankName: string;
  bankAdress: string;

  constructor(appContext: app.AppContext, pageContext: app.PageContext, loginId: string, accountHolder: string, accountNumber: string, bankName: string, bankAdress: string) {
    super(appContext, pageContext);
    this.accountHolder = accountHolder;
    this.accountNumber = accountNumber;
    this.bankName = bankName;
    this.bankAdress = bankAdress;
    this.loginId = loginId;
  }

  filter(pattern: string, onChangeCallback: (selOptions: any[]) => void) {
    let searchParams: widget.sql.SqlSearchParams = {
      "filters": [...widget.sql.createSearchFilter(pattern)],
      "params": { loginId: this.loginId },
      maxReturn: 1000
    }
    this.doSearch(AccountRestURL.bankAccount.search, searchParams, onChangeCallback);
  }

  onShowMoreInfo(ui: BBAutoComplete, bean: any) {
    let bankAccount = { accountHolder: bean.bankAccountOwner, accountNumber: bean.bankAccountNumber, bankName: bean.bankAccountName, bankAdress: bean.bankAdress };
    let observer: ComplexBeanObserver = new ComplexBeanObserver(bankAccount);
    let uiContent = (
      <UIBankAccountForm
        appContext={this.appContext} pageContext={this.pageContext} observer={observer} readOnly={true} />
    );
    ui.dialogShow(T('BankAccount Info'), 'md', uiContent)
  }

  onCustomSelect(ui: BBAutoComplete) {
    let onSelect = (bankAccount: any) => {
      this.replaceWithSelect(ui, bankAccount, bankAccount.accountNumber);
      this.updateDetailAfterSelect(ui, ui.props.bean, bankAccount);
      ui.onPostSelect(bankAccount.accountNumber);
      ui.dialogClose();
    }
    const callBack = (response: server.rest.RestResponse) => {
      let plugin = new VGridEntityListPlugin(response.data);
      let uiContent = (
        <div className='flex-vbox' style={{ height: 600 }}>
          <BankAccountList
            plugin={plugin.withExcludeRecords()} type={'selector'} loginId={this.loginId}
            appContext={this.appContext} pageContext={this.pageContext} readOnly={true}
            onSelect={(_appContext, _pageContext, selBankAccount) => onSelect(selBankAccount)} multiSelect={false} />
        </div>);
      ui.dialogShow(T('Select BankAccount'), 'lg', uiContent);
    }
    this.appContext.serverGET(AccountRestURL.bankAccount.findByLoginId(this.loginId), {}, callBack);
  }

  onInputChange(ui: BBAutoComplete, bean: any, field: string, selectOpt: any, oldVal: any, newVal: any) {
    super.onInputChange(ui, bean, field, selectOpt, oldVal, newVal);
    this.updateDetailAfterSelect(ui, bean, selectOpt);
  }

  updateDetailAfterSelect(ui: BBAutoComplete, bean: any, selectBean: any) {
    if (selectBean != null) {
      if (this.accountHolder) {
        let { searchDescField } = ui.props;
        if (searchDescField) {
          bean[this.accountHolder] = selectBean["accountHolder"];
          bean[this.bankName] = selectBean["bankName"];
          bean[this.bankAdress] = selectBean["bankAdress"];
          ui.forceUpdate();
        }
      }
    }
  }

  onCreateNew(ui: BBAutoComplete) {
    let popupPageCtx = this.pageContext.createPopupPageContext();

    let onPostCreate = (bankAccount: any) => {
      this.replaceWithSelect(ui, bankAccount, bankAccount.accountNumber);
      this.updateDetailAfterSelect(ui, ui.props.bean, bankAccount);
      ui.onPostSelect(bankAccount.accountNumber);
      popupPageCtx.onClose();
    }
    let uiContent = (
      <UIBankAccountEditor
        appContext={this.appContext} pageContext={popupPageCtx}
        observer={new ComplexBeanObserver({})} loginId={this.loginId}
        onPostCommit={onPostCreate} />
    );
    widget.layout.showDialog(T('Create New Bank Account'), 'md', uiContent, popupPageCtx.getDialogContext());
  }
}

interface BBBankAccountAutoCompleteProps extends BBEntityAutoCompleteProps {
  accountHolder: string;
  accountNumber: string;
  bankName: string;
  bankAdress: string;
  loginId: string
}
export class BBBankAccountAutoComplete extends BBEntityAutoComplete<BBBankAccountAutoCompleteProps> {

  onPostSelect = (option: any, val: any) => {
    let { onPostSelect } = this.props;
    if (onPostSelect) onPostSelect(option, val);
    this.forceUpdate();
  }

  render() {
    let { accountHolder, bankName, bankAdress } = this.props;
    if (accountHolder) return this.renderWithDetail(accountHolder, bankName, bankAdress);
    return this.renderAutocomplete();
  }

  renderWithDetail(accountHolder: string, bankName: string, bankAdress: string) {
    let { bean } = this.props;

    let html = (
      <FormContainer fluid>
        <Row>
          <ColFormGroup span={6} label={T('Bank Account Number')}>
            {this.renderAutocomplete()}
          </ColFormGroup>
          <ColFormGroup span={6} label={T('Bank Account Holder')} >
            <BBStringField
              bean={bean} field={accountHolder} disable placeholder={T('Bank Account Number')} />
          </ColFormGroup>
        </Row>
        <Row>
          <ColFormGroup span={6} label={T('Bank Account Name')}>
            <BBStringField
              bean={bean} field={bankName} disable placeholder={T('Bank Account Name')} />
          </ColFormGroup>
          <ColFormGroup span={6} label={T('Bank Account Address')}>
            <BBStringField
              bean={bean} field={bankAdress} disable placeholder={T('Bank Account Adress')} />
          </ColFormGroup>
        </Row>
      </FormContainer>
    );
    return html;
  }

  renderAutocomplete() {
    let { appContext, pageContext, disable, style, bean, field, useSelectBean, accountHolder, accountNumber, bankName, bankAdress, validators, loginId } = this.props;
    let html = (
      <BBAutoComplete
        style={style}
        plugin={
          new BankAccountAutoCompletePlugin(appContext, pageContext, loginId, accountHolder, accountNumber, bankName, bankAdress).withAllowCreateNew(true).withValidators(validators)}
        bean={bean} field={field} useSelectBean={useSelectBean}
        searchField={'accountNumber'} searchDescField={'loginId'} disable={disable}
        onPostSelect={this.onPostSelect} />
    );
    return html;
  }
}
