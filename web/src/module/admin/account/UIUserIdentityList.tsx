import React from 'react';
import { app, server, util, widget } from 'components';

import { WEntity } from 'core/widget/entity'
import { BeanObserver, ModifyBeanActions } from 'core/entity'
import { VGridConfigTool, VGridEntityList, VGridEntityListPlugin, WGridEntityListProps } from 'core/widget/vgrid';
import {
  WCommittableEntityListProps, WCommittableEntityList,
  WToolbar, WEntityEditorProps, WEntityEditor, WButtonEntityCommit, WComponent, WButtonEntityWrite
} from 'core/widget';

import { T, AccountRestURL } from './Dependency';
import { UserIdentityType } from './model';

import VGridConfig = widget.grid.VGridConfig;
import DisplayRecord = widget.grid.model.DisplayRecord;
import BBOptionAutoComplete = widget.input.BBOptionAutoComplete;

const { HSplit } = widget.component;

const {
  BBStringField, BBDateTimeField, FormContainer, Row, FormGroupCol
} = widget.input;

export class UIUserIdentityForm extends WEntity {

  render() {
    let { observer, readOnly, appContext, pageContext } = this.props;
    let bean = observer.getMutableBean();
    let writeCap = this.hasWriteCapability();
    let errorCollector = observer.getErrorCollector();
    let html = (
      <FormContainer fluid>
        <Row>
          <FormGroupCol span={6} label={T('Label')}>
            <BBStringField bean={bean} field={'label'} required errorCollector={errorCollector} disable={!writeCap} />
          </FormGroupCol>
          <FormGroupCol span={6} label={T('Name')}>
            <BBStringField bean={bean} field={'name'} required errorCollector={errorCollector} disable={!writeCap} />
          </FormGroupCol>
        </Row>
        <Row>
          <FormGroupCol span={6} label={T('Identification No')}>
            <BBStringField bean={bean} field={"identificationNo"} disable={readOnly} />
          </FormGroupCol>
          <FormGroupCol span={6} label={T('Identity Type')}>
            <BBOptionAutoComplete bean={bean} field={'type'} allowUserInput={true}
              options={[UserIdentityType.ID, UserIdentityType.PASSPORT, UserIdentityType.TAX_CODE]} />
          </FormGroupCol>
        </Row>
        <Row>
          <FormGroupCol span={12} label={T('Issue Date')}>
            <BBDateTimeField bean={bean} field={"issueDate"}
              dateFormat={"DD/MM/YYYY"} timeFormat={false} disable={readOnly} />
          </FormGroupCol>
        </Row>
        <Row>
          <FormGroupCol span={6} label={T('Valid From')}>
            <BBDateTimeField bean={bean} field={"validFrom"}
              dateFormat={"DD/MM/YYYY"} timeFormat={false} disable={readOnly} />
          </FormGroupCol>
          <FormGroupCol span={6} label={T('Valid To')}>
            <BBDateTimeField bean={bean} field={"validTo"}
              dateFormat={"DD/MM/YYYY"} timeFormat={false} disable={readOnly} />
          </FormGroupCol>
        </Row>
      </FormContainer>
    );
    return html;
  }
}

interface UIContactListProps extends WGridEntityListProps {
  loginId: string
}

class UserIdentityList extends VGridEntityList<UIContactListProps> {

  createVGridConfig() {
    let writeCap = this.hasWriteCapability();
    let config: VGridConfig = {
      record: {
        fields: [
          ...VGridConfigTool.FIELD_SELECTOR(this.needSelector()),
          VGridConfigTool.FIELD_INDEX(),
          VGridConfigTool.FIELD_ON_SELECT('label', T('Label'), 150),

          { name: 'name', label: T('Name') },
          { name: 'identificationNo', label: T('Identification No') },
          { name: 'type', label: T('Identity Type') },
          { name: 'issuePlace', label: T('Issue Place') },
          { name: 'issueBy', label: T('Issue By') },
          { name: 'issueDate', label: T('Issue Date'), format: util.text.formater.compactDateTime },
          { name: 'validFrom', label: T('Valid From'), format: util.text.formater.compactDateTime },
          { name: 'validTo', label: T('Valid To'), format: util.text.formater.compactDateTime },
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
      <UserIdentityEditor appContext={appContext} pageContext={pageContext}
        observer={new BeanObserver(dRecord.record)}
        onPostCommit={onPostCommit} loginId={loginId} />
    );
    widget.layout.showDialog("Identity", 'md', html, popupPageCtx.getDialogContext());
  }

  onDeleteAction() {
    let { appContext, plugin, onModifyBean, loginId } = this.props;
    const successCB = (response: server.rest.RestResponse) => {
      plugin.getListModel().removeSelectedDisplayRecords();
      if (onModifyBean) onModifyBean(plugin.getListModel().getRecords(), ModifyBeanActions.DELETE);
      else this.forceUpdate();

      appContext.addOSNotification("success", T(`Delete Identity Success`));
    }
    let identityIds = plugin.getListModel().getSelectedRecordIds();
    appContext.serverDELETE(AccountRestURL.identity.delete(loginId), identityIds, successCB);
  }

}

interface UserIdentityProps extends WEntityEditorProps {
  loginId: string
}
class UserIdentityEditor extends WEntityEditor<UserIdentityProps> {

  render() {
    let { appContext, pageContext, observer, loginId, onPostCommit } = this.props;
    const writeCap = this.hasWriteCapability();
    let html = (
      <HSplit>
        <UIUserIdentityForm key={`list-identity-${util.common.IDTracker.next()}`}
          appContext={appContext} pageContext={pageContext} observer={observer}
          readOnly={!writeCap} />
        <WToolbar readOnly={!writeCap}>
          <WButtonEntityCommit
            appContext={appContext} pageContext={pageContext}
            observer={observer} label={T(`Identity For {{loginId}}`, { loginId: loginId })}
            commitURL={AccountRestURL.identity.save(loginId)} onPostCommit={onPostCommit} />
        </WToolbar>
      </HSplit>
    )
    return html;
  }
}

interface UIUserIdentityListEditorProps extends WCommittableEntityListProps {
  loginId: string;
}
export class UIUserIdentityListEditor extends WCommittableEntityList<UIUserIdentityListEditorProps> {
  plugin: VGridEntityListPlugin;

  constructor(props: UIUserIdentityListEditorProps) {
    super(props);
    this.plugin = new VGridEntityListPlugin([]);
    this.markLoading(true);
    this.loadData();
  }

  loadData() {
    let { loginId, appContext } = this.props;
    const callBack = (response: server.rest.RestResponse) => {
      this.plugin = new VGridEntityListPlugin(response.data);
      this.markLoading(false);
      this.forceUpdate();
    }
    appContext.serverGET(AccountRestURL.identity.findByLoginId(loginId), {}, callBack);
  }

  onNewUserIdentity() {
    let { appContext, pageContext, loginId } = this.props;
    let popupPageCtx = pageContext.createPopupPageContext();
    let onPostCommit = (_entity: any, _uiEditor?: WComponent) => {
      this.plugin.getRecords().push(_entity);
      this.forceUpdate();
      popupPageCtx.onBack();
    }
    let html = (
      <UserIdentityEditor appContext={appContext} pageContext={pageContext}
        observer={new BeanObserver({})}
        onPostCommit={onPostCommit} loginId={loginId} />
    );
    widget.layout.showDialog("Identity", 'md', html, popupPageCtx.getDialogContext());
  }

  render() {
    if (this.isLoading()) return this.renderLoading();
    let { appContext, pageContext, loginId } = this.props;
    const writeCap = this.hasWriteCapability();
    let html = (
      <div className='flex-vbox'>
        <UserIdentityList key={`list-editor-${util.common.IDTracker.next()}`}
          plugin={this.plugin} appContext={appContext} pageContext={pageContext} readOnly={!writeCap}
          loginId={loginId}
        />
        <WToolbar readOnly={!writeCap}>
          <WButtonEntityWrite appContext={appContext} pageContext={pageContext}
            label={T("New Identity")} icon={widget.fa.fas.faPlus} onClick={() => this.onNewUserIdentity()} />
        </WToolbar>
      </div>
    );
    return html;
  }
}
