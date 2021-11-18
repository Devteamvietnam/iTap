import React from "react";
import { app, server, util, widget } from "components";

import { WEntity } from 'core/widget/entity'
import { BeanObserver, ModifyBeanActions } from 'core/entity'
import { VGridConfigTool, VGridEntityList, VGridEntityListPlugin, WGridEntityListProps } from "core/widget/vgrid";

import {
  WCommittableEntityList, WCommittableEntityListProps,
  WToolbar, WEntityEditorProps, WEntityEditor, WButtonEntityCommit, WComponent, WButtonEntityWrite,
} from "core/widget";

import { T, AccountRestURL } from "./Dependency";
import { Certificate } from "./model";

import VGridConfig = widget.grid.VGridConfig;
import DisplayRecord = widget.grid.model.DisplayRecord;
import BBOptionAutoComplete = widget.input.BBOptionAutoComplete;
const {
  BBStringField, BBDateTimeField, FormContainer, Row, FormGroupCol
} = widget.input;

const { HSplit } = widget.component;

export class UIUserEducationForm extends WEntity {

  render() {
    let { observer, appContext, pageContext } = this.props;
    let bean = observer.getMutableBean();
    let writeCap = this.hasWriteCapability();
    let html = (
      <FormContainer fluid>
        <Row>
          <FormGroupCol span={6} label={T('Label')}>
            <BBStringField bean={bean} field={"label"} disable={!writeCap}
              placeholder={T('Education Label')} required={true} />
          </FormGroupCol>
        </Row>
        <Row>
          <FormGroupCol span={6} label={T('From Date')}>
            <BBDateTimeField
              bean={bean} field={"fromDate"} dateFormat={"DD/MM/YYYY"} timeFormat={false} disable={!writeCap}
            />
          </FormGroupCol>
          <FormGroupCol span={6} label={T('To Date')}>
            <BBDateTimeField
              bean={bean} field={"toDate"} dateFormat={"DD/MM/YYYY"} timeFormat={false} disable={!writeCap} />
          </FormGroupCol>
        </Row>
        <Row>
          <FormGroupCol span={6} label={T('Certificate')}>
            <BBOptionAutoComplete
              bean={bean} field={'certificate'} allowUserInput={true}
              options={[Certificate.ASSOCIATE, Certificate.BACHELOR, Certificate.ENGINEER, Certificate.MASTER, Certificate.DOCTOR_PHILOSOPHY]} />
          </FormGroupCol>
        </Row>
      </FormContainer>
    );
    return html;
  }
}

interface UserEducationProps extends WGridEntityListProps {
  loginId: string
}

class UserEducationList extends VGridEntityList<UserEducationProps> {

  createVGridConfig() {
    let writeCap = this.hasWriteCapability();
    let config: VGridConfig = {
      record: {
        fields: [
          ...VGridConfigTool.FIELD_SELECTOR(this.needSelector()),
          VGridConfigTool.FIELD_INDEX(),
          VGridConfigTool.FIELD_ON_SELECT('label', T('Label'), 150),

          { name: 'schoolOrInstituteLabel', label: T('School(Institute) Label'), width: 300 },
          { name: 'major', label: T('Major'), width: 300 },
          { name: 'certificate', label: T('Certificate'), width: 200 },
          { name: 'language', label: T('Language'), width: 200 },
          { name: 'fromDate', label: T('From Date'), format: util.text.formater.compactDateTime },
          { name: 'toDate', label: T('To Date'), format: util.text.formater.compactDateTime },
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
      <UserEducationEditor appContext={appContext} pageContext={pageContext}
        observer={new BeanObserver(dRecord.record)}
        onPostCommit={onPostCommit} loginId={loginId} />
    );
    widget.layout.showDialog("Education", 'md', html, popupPageCtx.getDialogContext());
  }

  onDeleteAction() {
    let { appContext, plugin, onModifyBean, loginId } = this.props;
    const successCB = (result: server.rest.RestResponse) => {
      plugin.getListModel().removeSelectedDisplayRecords();
      if (onModifyBean) onModifyBean(plugin.getListModel().getRecords(), ModifyBeanActions.DELETE);
      else this.forceUpdate();

      appContext.addOSNotification("success", T(`Delete User Education Success`));
    }
    let eudcationIds = plugin.getListModel().getSelectedRecordIds();
    appContext.serverDELETE(AccountRestURL.education.delete(loginId), eudcationIds, successCB);
  }
}

interface UserEducationEditorProps extends WEntityEditorProps {
  loginId: string
}
class UserEducationEditor extends WEntityEditor<UserEducationEditorProps> {

  render() {
    let { appContext, pageContext, observer, loginId, onPostCommit } = this.props;
    const writeCap = this.hasWriteCapability();
    let html = (
      <HSplit>
        <UIUserEducationForm key={`list-education-${util.common.IDTracker.next()}`}
          appContext={appContext} pageContext={pageContext} observer={observer}
          readOnly={!writeCap} />
        <WToolbar readOnly={!writeCap}>
          <WButtonEntityCommit
            appContext={appContext} pageContext={pageContext}
            observer={observer} label={T(`Education For {{loginId}}`, { loginId: loginId })}
            commitURL={AccountRestURL.education.save(loginId)} onPostCommit={onPostCommit} />
        </WToolbar>
      </HSplit>
    )
    return html;
  }
}

interface UIUserEducationListEditorProps extends WCommittableEntityListProps {
  loginId: string
}
export class UIUserEducationListEditor extends WCommittableEntityList<UIUserEducationListEditorProps> {
  plugin: VGridEntityListPlugin;

  constructor(props: UIUserEducationListEditorProps) {
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
    appContext.serverGET(AccountRestURL.education.findByLoginId(loginId), {}, callBack);
  }

  onNewEducation() {
    let { appContext, pageContext, loginId } = this.props;
    let popupPageCtx = new app.PageContext().withPopup();
    let onPostCommit = (_entity: any, _uiEditor?: WComponent) => {
      this.plugin.getRecords().push(_entity);
      this.forceUpdate();
      popupPageCtx.onBack();
    }
    let html = (
      <UserEducationEditor appContext={appContext} pageContext={pageContext}
        observer={new BeanObserver({})}
        onPostCommit={onPostCommit} loginId={loginId} />
    );
    widget.layout.showDialog("Education", 'md', html, popupPageCtx.getDialogContext());
  }

  render() {
    if (this.isLoading()) return this.renderLoading();
    const { appContext, pageContext, loginId } = this.props;
    const writeCap = this.hasWriteCapability();
    return (
      <div className='flex-vbox'>
        <UserEducationList key={`list-editor-${util.common.IDTracker.next()}`}
          plugin={this.plugin} appContext={appContext} pageContext={pageContext} readOnly={!writeCap}
          loginId={loginId}
        />
        <WToolbar readOnly={!writeCap}>
          <WButtonEntityWrite appContext={appContext} pageContext={pageContext}
            label={T("New Education")} icon={widget.fa.fas.faPlus} onClick={() => this.onNewEducation()} />
        </WToolbar>
      </div>
    );
  }
}
