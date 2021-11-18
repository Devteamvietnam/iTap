import React from 'react';
import { app, server, util, widget } from 'components';

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

const { HSplit } = widget.component;

const {
  BBStringField, BBDateTimeField, BBSelectField, FormContainer, Row, ColFormGroup, BBTextField
} = widget.input;

export class UIUserRelationForm extends WEntity {
  render() {
    let { observer, appContext, pageContext } = this.props;
    let writeCap = this.hasWriteCapability();
    let bean = observer.getMutableBean();
    let errorCollector = observer.getErrorCollector();
    const genders = ['Male', 'Female', 'Other'];
    const genderLabels = [T('Male'), T('Female'), T('Other')];
    let html = (
      <FormContainer fluid>
        <Row>
          <ColFormGroup span={6} label={T('Label')}>
            <BBStringField bean={bean} field={"label"} disable={!writeCap} required errorCollector={errorCollector} />
          </ColFormGroup>
        </Row>
        <Row>
          <ColFormGroup span={6} label={T('First Name')}>
            <BBStringField bean={bean} field={"firstName"} disable={!writeCap} required errorCollector={errorCollector} />
          </ColFormGroup>
          <ColFormGroup span={6} label={T('Last Name')}>
            <BBStringField bean={bean} field={"lastName"} disable={!writeCap} required errorCollector={errorCollector} />
          </ColFormGroup>
        </Row>
        <Row>
          <ColFormGroup span={6} label={T('Full Name')}>
            <BBStringField bean={bean} field={"fullName"} disable={!writeCap} required errorCollector={errorCollector} />
          </ColFormGroup>
          <ColFormGroup span={6} label={T('Contact')}>
            <BBStringField bean={bean} field={"contact"} disable={!writeCap} required errorCollector={errorCollector} />
          </ColFormGroup>
        </Row>
        <Row>
          <ColFormGroup span={6} label={T('Gender')}>
            <BBSelectField bean={bean} field={"gender"} disable={!writeCap} options={genders} optionLabels={genderLabels} />
          </ColFormGroup>
          <ColFormGroup span={6} label={T('Birthday')}>
            <BBDateTimeField bean={bean} field={"birthday"} disable={!writeCap} dateFormat={"DD/MM/YYYY"} timeFormat={false} />
          </ColFormGroup>
        </Row>
        <Row>
          <ColFormGroup span={12} label={T('Note')}>
            <BBTextField bean={bean} field={'note'} style={{ height: '20em' }} disable={!writeCap} />
          </ColFormGroup>
        </Row>
      </FormContainer>
    );
    return html;
  }
}

interface UIUserRelationListProps extends WGridEntityListProps {
  loginId: string
}

class UserRelationList extends VGridEntityList<UIUserRelationListProps> {

  createVGridConfig() {
    let writeCap = this.hasWriteCapability();
    let config: VGridConfig = {
      record: {
        fields: [
          ...VGridConfigTool.FIELD_SELECTOR(this.needSelector()),
          VGridConfigTool.FIELD_INDEX(),
          VGridConfigTool.FIELD_ON_SELECT('label', T('Label'), 150),

          { name: 'firstName', label: T('First Name') },
          { name: 'lastName', label: T('Last Name') },
          { name: 'fullName', label: T('Full Name') },
          { name: 'contact', label: T('Contact') },
          { name: 'relationLabel', label: T('Relation') },
          { name: 'gender', label: T('Gender') },
          { name: 'occupation', label: T('Occupation') },
          { name: 'birthday', label: T('Birthday '), format: util.text.formater.compactDateTime },
          { name: 'note', label: T('Note') },
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
      <UIUserRelationEditor appContext={appContext} pageContext={pageContext}
        observer={new BeanObserver(dRecord.record)}
        onPostCommit={onPostCommit} loginId={loginId} />
    );
    widget.layout.showDialog("Relation", 'md', html, popupPageCtx.getDialogContext());
  }

  onDeleteAction() {
    let { appContext, plugin, onModifyBean, loginId } = this.props;
    const successCB = (response: server.rest.RestResponse) => {
      plugin.getListModel().removeSelectedDisplayRecords();
      if (onModifyBean) onModifyBean(plugin.getListModel().getRecords(), ModifyBeanActions.DELETE);
      else this.forceUpdate();

      appContext.addOSNotification("success", T(`Delete Relation Success`));
    }
    let relationIds = plugin.getListModel().getSelectedRecordIds();
    appContext.serverDELETE(AccountRestURL.relation.delete(loginId), relationIds, successCB);
  }
}

interface UIUserRelationEditorProps extends WEntityEditorProps {
  loginId: string
}

class UIUserRelationEditor extends WEntityEditor<UIUserRelationEditorProps> {

  render() {
    let { appContext, pageContext, observer, loginId, onPostCommit } = this.props;
    const writeCap = this.hasWriteCapability();
    let html = (
      <HSplit>
        <UIUserRelationForm key={`list-relation-${util.common.IDTracker.next()}`}
          appContext={appContext} pageContext={pageContext} observer={observer}
          readOnly={!writeCap} />
        <WToolbar readOnly={!writeCap}>
          <WButtonEntityCommit
            appContext={appContext} pageContext={pageContext}
            observer={observer} label={T(`Relation For {{loginId}}`, { loginId: loginId })}
            commitURL={AccountRestURL.relation.save(loginId)} onPostCommit={onPostCommit} />
        </WToolbar>
      </HSplit>
    )
    return html;
  }
}
interface UIUserRelationListEditorProps extends WCommittableEntityListProps {
  loginId: string
}
export class UIUserRelationListEditor extends WCommittableEntityList<UIUserRelationListEditorProps> {
  plugin: VGridEntityListPlugin;

  constructor(props: UIUserRelationListEditorProps) {
    super(props);
    this.plugin = new VGridEntityListPlugin([])
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
    appContext.serverGET(AccountRestURL.relation.findByLoginId(this.props.loginId), null, callBack);
  }

  onNewUserRelation() {
    let { appContext, pageContext, loginId } = this.props;
    let popupPageCtx = pageContext.createPopupPageContext();
    let onPostCommit = (_entity: any, _uiEditor?: WComponent) => {
      this.plugin.getRecords().push(_entity);
      this.forceUpdate();
      popupPageCtx.onBack();
    }
    let html = (
      <UIUserRelationEditor appContext={appContext} pageContext={pageContext}
        observer={new BeanObserver({})}
        onPostCommit={onPostCommit} loginId={loginId} />
    );
    widget.layout.showDialog("Relation", 'md', html, popupPageCtx.getDialogContext());
  }

  render() {
    if (this.isLoading()) return this.renderLoading();
    let { appContext, pageContext, loginId } = this.props;
    const writeCap = this.hasWriteCapability();
    return (
      <div className='flex-vbox'>
        <UserRelationList key={`list-editor-${util.common.IDTracker.next()}`}
          plugin={this.plugin} appContext={appContext} pageContext={pageContext} loginId={loginId}
          readOnly={!writeCap} />
        <WToolbar readOnly={!writeCap}>
          <WButtonEntityWrite appContext={appContext} pageContext={pageContext}
            label={T("New Relation")} icon={widget.fa.fas.faPlus} onClick={() => this.onNewUserRelation()} />
        </WToolbar>
      </div>
    );
  }
}
