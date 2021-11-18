import React from "react";
import { app, server, util, widget } from "components";

import { WEntity } from 'core/widget/entity'
import { BeanObserver, ModifyBeanActions } from 'core/entity'
import { VGridConfigTool, VGridEntityList, VGridEntityListPlugin, WGridEntityListProps } from 'core/widget/vgrid';

import {
  WCommittableEntityList, WCommittableEntityListProps,
  WToolbar, WEntityEditorProps, WEntityEditor, WButtonEntityCommit, WComponent, WButtonEntityWrite
} from "core/widget";

import { T, AccountRestURL } from "./Dependency";

import VGridConfig = widget.grid.VGridConfig;
import DisplayRecord = widget.grid.model.DisplayRecord;

const { HSplit } = widget.component;
const {
  BBStringField, BBDateTimeField, FormContainer, Row, FormGroupCol, BBTextField
} = widget.input;

export class UIUserWorkForm extends WEntity {
  render() {
    let { observer, readOnly, appContext, pageContext } = this.props;
    let bean = observer.getMutableBean();

    let html = (
      <FormContainer fluid>
        <Row>
          <FormGroupCol span={6} label={T('Label')}>
            <BBStringField bean={bean} field={"label"} disable={readOnly} required />
          </FormGroupCol>
        </Row>
        <Row>
          <FormGroupCol span={6} label={T('From Date')}>
            <BBDateTimeField bean={bean} field={"fromDate"} dateFormat={"DD/MM/YYYY"} timeFormat={false} disable={readOnly} />
          </FormGroupCol>
          <FormGroupCol span={6} label={T('To Date')}>
            <BBDateTimeField bean={bean} field={"toDate"} dateFormat={"DD/MM/YYYY"} timeFormat={false} disable={readOnly} />
          </FormGroupCol>
        </Row>
        <FormGroupCol span={12} label={T('Description')}>
          <BBTextField bean={bean} field={"description"} disable={readOnly} />
        </FormGroupCol>
      </FormContainer>
    );
    return html;
  }
}

interface UserWorkListProps extends WGridEntityListProps {
  loginId: string
}

class UserWorkList extends VGridEntityList<UserWorkListProps> {
  createVGridConfig() {
    let writeCap = this.hasWriteCapability();
    let config: VGridConfig = {
      record: {
        fields: [
          ...VGridConfigTool.FIELD_SELECTOR(this.needSelector()),
          VGridConfigTool.FIELD_INDEX(),
          VGridConfigTool.FIELD_ON_SELECT('label', T('Label'), 150),

          { name: 'organization', label: T('Organization') },
          { name: 'positionLabel', label: T('position') },
          { name: 'fromDate', label: T('From Date'), format: util.text.formater.compactDateTime },
          { name: 'toDate', label: T('To Date'), format: util.text.formater.compactDateTime },
          { name: 'description', label: T('description') },
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
    let popupPageCtx = new app.PageContext().withPopup();

    let onPostCommit = (_entity: any, _uiEditor?: WComponent) => {
      this.forceUpdate();
      popupPageCtx.onBack();
    }
    let html = (
      <UserWorkEditor appContext={appContext} pageContext={pageContext}
        observer={new BeanObserver(dRecord.record)}
        onPostCommit={onPostCommit} loginId={loginId} />
    );
    widget.layout.showDialog("User Work", 'md', html, popupPageCtx.getDialogContext());
  }

  onDeleteAction() {
    let { appContext, plugin, onModifyBean, loginId } = this.props;
    const successCB = (response: server.rest.RestResponse) => {
      plugin.getListModel().removeSelectedDisplayRecords();
      if (onModifyBean) onModifyBean(plugin.getListModel().getRecords(), ModifyBeanActions.DELETE);
      else this.forceUpdate();

      appContext.addOSNotification("success", T(`Delete Contacts Success`));
    }
    let workIds = plugin.getListModel().getSelectedRecordIds();
    appContext.serverDELETE(AccountRestURL.work.delete(loginId), workIds, successCB);
  }

}

interface UserWorkEditorProps extends WEntityEditorProps {
  loginId: string
}

class UserWorkEditor extends WEntityEditor<UserWorkEditorProps> {

  render() {
    let { appContext, pageContext, observer, loginId, onPostCommit } = this.props;
    const writeCap = this.hasWriteCapability();
    let html = (
      <HSplit>
        <UIUserWorkForm key={`list-work-${util.common.IDTracker.next()}`}
          appContext={appContext} pageContext={pageContext} observer={observer}
          readOnly={!this.hasWriteCapability()} />
        <WToolbar readOnly={!writeCap}>
          <WButtonEntityCommit
            appContext={appContext} pageContext={pageContext}
            observer={observer} label={T(`User Work For {{loginId}}`, { loginId: loginId })}
            commitURL={AccountRestURL.work.save(loginId)} onPostCommit={onPostCommit} />
        </WToolbar>
      </HSplit>
    )
    return html;
  }
}

interface UIUserWorkListEditorProps extends WCommittableEntityListProps {
  loginId: string;
}

export class UIUserWorkListEditor extends WCommittableEntityList<UIUserWorkListEditorProps> {
  plugin: VGridEntityListPlugin;

  constructor(props: UIUserWorkListEditorProps) {
    super(props);
    this.plugin = new VGridEntityListPlugin([]);
    this.markLoading(true);
    this.loadData();
  }

  loadData() {
    const callBack = (response: server.rest.RestResponse) => {
      this.plugin = new VGridEntityListPlugin(response.data);
      this.markLoading(false);
      this.forceUpdate();
    }
    const { appContext } = this.props;
    appContext.serverGET(AccountRestURL.work.findByLoginId(this.props.loginId), null, callBack);
  }

  onNewUserWork() {
    let { appContext, pageContext, loginId } = this.props;
    let popupPageCtx = new app.PageContext().withPopup();
    let onPostCommit = (_entity: any, _uiEditor?: WComponent) => {
      this.plugin.getRecords().push(_entity);
      this.forceUpdate();
      popupPageCtx.onBack();
    }
    let html = (
      <UserWorkEditor appContext={appContext} pageContext={pageContext}
        observer={new BeanObserver({})}
        onPostCommit={onPostCommit} loginId={loginId} />
    );
    widget.layout.showDialog("User Work", 'md', html, popupPageCtx.getDialogContext());
  }

  render() {
    if (this.isLoading()) return this.renderLoading();
    const { appContext, pageContext, loginId } = this.props;
    const writeCap = this.hasWriteCapability();
    let html = (
      <div className='flex-vbox'>
        <UserWorkList key={`list-editor-${util.common.IDTracker.next()}`}
          plugin={this.plugin} appContext={appContext} pageContext={pageContext}
          readOnly={!writeCap} loginId={loginId} />
        <WToolbar readOnly={!writeCap}>
          <WButtonEntityWrite appContext={appContext} pageContext={pageContext}
            label={T("New Work")} icon={widget.fa.fas.faPlus} onClick={() => this.onNewUserWork()} />
        </WToolbar>
      </div>
    );
    return html;
  }
}
