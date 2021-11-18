import React from 'react';
import { app, util, widget, server } from 'components';

import { BeanObserver, ModifyBeanActions } from 'core/entity'
import { WEntity } from 'core/widget/entity'
import { VGridConfigTool, VGridEntityList, VGridEntityListPlugin, WGridEntityListProps } from 'core/widget/vgrid';

import {
  WButtonEntityCommit,
  WButtonEntityWrite,
  WCommittableEntityList, WCommittableEntityListProps,
  WComponent, WEntityEditor, WEntityEditorProps, WToolbar
} from 'core/widget';

import {
  T, AccountRestURL
} from './Dependency';

import VGridConfig = widget.grid.VGridConfig;
import FieldConfig = widget.grid.FieldConfig;
import VGridContext = widget.grid.VGridContext;
import DisplayRecord = widget.grid.model.DisplayRecord;

const { HSplit } = widget.component;
const {
  BBStringField, BBStringArrayField, Form, FormGroup
} = widget.input;

class UIContactForm extends WEntity {

  render() {
    let { observer, appContext, pageContext } = this.props;
    let errorCollector = observer.getErrorCollector();
    let writeCap = this.hasWriteCapability();
    let contact = observer.getMutableBean();
    if (!contact.countryName) {
      contact.countryName = "VN";
      contact.countryLabel = "VIETNAM";
    }

    let html = (
      <Form>
        <widget.component.VSplit>
          <widget.component.VSplitPane className='px-1' width={450}>
            <FormGroup label={T('Label')}>
              <BBStringField
                bean={contact} field={'label'} errorCollector={errorCollector} disable={!writeCap} required />
            </FormGroup>
            <FormGroup label={T('Address')}>
              <BBStringField
                bean={contact} field={'address'} errorCollector={errorCollector} disable={!writeCap} required />
            </FormGroup>
            <FormGroup label={T('District')}>
              <BBStringField bean={contact} field={'district'} disable={!writeCap} />
            </FormGroup>
          </widget.component.VSplitPane>
          <widget.component.VSplitPane>
            <FormGroup label={T('Email')}>
              <BBStringArrayField
                bean={contact} field={'email'} disable={!writeCap} required
                validators={[util.validator.EMAIL_VALIDATOR]} />
            </FormGroup>
            <FormGroup label={T('Phone')}>
              <BBStringArrayField bean={contact} field={'phone'} disable={!writeCap} />
            </FormGroup>
            <FormGroup label={T('Mobile')}>
              <BBStringArrayField bean={contact} field={'mobile'} disable={!writeCap} />
            </FormGroup>
            <FormGroup label={T('Fax')}>
              <BBStringArrayField bean={contact} field={'fax'} disable={!writeCap} />
            </FormGroup>
            <FormGroup label={('Website')}>
              <BBStringArrayField bean={contact} field={'website'} disable={!writeCap} />
            </FormGroup>
          </widget.component.VSplitPane>
        </widget.component.VSplit>
      </Form>
    );
    return html;
  }
}

interface UIContactEditorProps extends WEntityEditorProps {
  loginId: string
}
class UIContactEditor extends WEntityEditor<UIContactEditorProps> {

  render() {
    let { appContext, pageContext, observer, loginId, onPostCommit } = this.props;
    const writeCap = this.hasWriteCapability();
    let html = (
      <HSplit>
        <UIContactForm key={`list-contact-${util.common.IDTracker.next()}`}
          appContext={appContext} pageContext={pageContext} observer={observer}
          readOnly={!writeCap} />
        <WToolbar readOnly={!writeCap}>
          <WButtonEntityCommit
            appContext={appContext} pageContext={pageContext}
            observer={observer} label={T(`Contact For {{loginId}}`, { loginId: loginId })}
            commitURL={AccountRestURL.contact.save(loginId)} onPostCommit={onPostCommit} />
        </WToolbar>
      </HSplit>
    )
    return html;
  }
}

interface UIContactListProps extends WGridEntityListProps {
  loginId: string
}
class ContactList extends VGridEntityList<UIContactListProps> {

  onDefaultSelect(dRecord: DisplayRecord) {
    let { appContext, pageContext, loginId } = this.props;
    let popupPageCtx = new app.PageContext().withPopup();

    let onPostCommit = (_entity: any, _uiEditor?: WComponent) => {
      this.forceUpdate();
      popupPageCtx.onBack();
    }
    let html = (
      <UIContactEditor appContext={appContext} pageContext={pageContext}
        observer={new BeanObserver(dRecord.record)}
        onPostCommit={onPostCommit} loginId={loginId} />
    );
    widget.layout.showDialog("Contact", 'md', html, popupPageCtx.getDialogContext());
  }

  onDeleteAction() {
    let { appContext, plugin, onModifyBean, loginId } = this.props;
    const successCB = (response: server.rest.RestResponse) => {
      plugin.getListModel().removeSelectedDisplayRecords();
      if (onModifyBean) onModifyBean(plugin.getListModel().getRecords(), ModifyBeanActions.DELETE);
      else this.forceUpdate();

      appContext.addOSNotification("success", T(`Delete Contacts Success`));
    }
    let contactIds = plugin.getListModel().getSelectedRecordIds();
    appContext.serverDELETE(AccountRestURL.contact.delete(loginId), contactIds, successCB);
  }

  onRenderObList(records: any) {
    if (!records) return '';
    let result: String = '';
    records.forEach((record: any) => {
      result += record + ', ';
    });
    if (result.endsWith(', ')) {
      result = result.slice(0, -2);
    }
    return result;
  }

  createVGridConfig() {
    let writeCap = this.hasWriteCapability();
    let config: VGridConfig = {
      record: {
        fields: [
          ...VGridConfigTool.FIELD_SELECTOR(this.needSelector()),
          VGridConfigTool.FIELD_INDEX(),
          VGridConfigTool.FIELD_ON_SELECT('label', T('Label'), 150),

          { name: 'address', label: T('Address'), width: 250 },
          { name: 'district', label: T('District'), width: 250 },
          { name: 'countryLabel', label: T('Country'), width: 200 },
          { name: 'stateLabel', label: T('State'), width: 200 },
          { name: 'cityLabel', label: T('City'), width: 250 },
          {
            name: "email", label: T("Email"), width: 200,
            customRender: function (ctx: VGridContext, _field: FieldConfig, dRecord: DisplayRecord) {
              let uiContactList = ctx.uiRoot as ContactList;
              return uiContactList.onRenderObList(dRecord.record.email);
            },
          },
          {
            name: "mobile", label: T("Mobile"), width: 200,
            customRender: function (ctx: VGridContext, _field: FieldConfig, dRecord: DisplayRecord) {
              let uiContactList = ctx.uiRoot as ContactList;
              return uiContactList.onRenderObList(dRecord.record.mobile);
            },
          },
          {
            name: "phone", label: T("Phone"), width: 200,
            customRender: function (ctx: VGridContext, _field: FieldConfig, dRecord: DisplayRecord) {
              let uiContactList = ctx.uiRoot as ContactList;
              return uiContactList.onRenderObList(dRecord.record.phone);
            },
          },
          {
            name: "website", label: T("Website"), width: 200,
            customRender: function (ctx: VGridContext, _field: FieldConfig, dRecord: DisplayRecord) {
              let uiContactList = ctx.uiRoot as ContactList;
              return uiContactList.onRenderObList(dRecord.record.website);
            },
          }

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
}

interface UIContactListEditorProps extends WCommittableEntityListProps {
  loginId: string
}
export class UIContactListEditor extends WCommittableEntityList<UIContactListEditorProps> {
  plugin: VGridEntityListPlugin;

  constructor(props: UIContactListEditorProps) {
    super(props);
    this.plugin = new VGridEntityListPlugin([]);
    this.loadData();
    this.markLoading(true);
  }

  loadData() {
    let { appContext, loginId } = this.props;
    const callBack = (response: server.rest.RestResponse) => {
      this.plugin = new VGridEntityListPlugin(response.data);
      this.markLoading(false);
      this.forceUpdate();
    }
    appContext.serverGET(AccountRestURL.contact.findByLoginId(loginId), {}, callBack);
  }

  onNewContact() {
    let { appContext, pageContext, loginId } = this.props;
    let popupPageCtx = new app.PageContext().withPopup();
    let onPostCommit = (_entity: any, _uiEditor?: WComponent) => {
      this.plugin.getRecords().push(_entity);
      this.forceUpdate();
      popupPageCtx.onBack();
    }
    let html = (
      <UIContactEditor appContext={appContext} pageContext={pageContext}
        observer={new BeanObserver({})}
        onPostCommit={onPostCommit} loginId={loginId} />
    );
    widget.layout.showDialog("Contact", 'md', html, popupPageCtx.getDialogContext());
  }

  render() {
    if (this.isLoading()) return this.renderLoading();
    const { appContext, pageContext, loginId } = this.props;
    const writeCap = this.hasWriteCapability();
    let html = (
      <div className='flex-vbox'>
        <ContactList key={`list-editor-${util.common.IDTracker.next()}`}
          appContext={appContext} pageContext={pageContext} loginId={loginId}
          plugin={this.plugin} readOnly={!writeCap} />
        <WToolbar readOnly={!writeCap}>
          <WButtonEntityWrite appContext={appContext} pageContext={pageContext}
            label={T("New Contact")} icon={widget.fa.fas.faPlus} onClick={() => this.onNewContact()} />
        </WToolbar>
      </div>
    );
    return html;
  }
}
