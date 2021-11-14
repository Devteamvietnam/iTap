import React from 'react';
import { widget, server, util } from 'components';

import {
  ComplexBeanObserver, VGridEntityListEditorPlugin
} from 'core/entity';
import { WComplexEntity, WComplexEntityProps, WButtonEntityCommit } from './WEntity';
import {
  WComponent, WComponentProps, WToolbar
} from '../WLayout';

import { CustomFieldDefinitions } from './UICustomFieldDefinitions'
import { UICustomFieldListEditor } from './UICustomFieldList'
import { T } from '../Dependency';

import { CustomFieldURL } from './RestURL';

import ListRecordMap = util.ListRecordMap;
const { TabPane, Tab } = widget.layout;
const { Form, FormGroup, BBStringField } = widget.input;
interface UICustomFieldsFormProps extends WComponentProps {
  plugin: VGridEntityListEditorPlugin;
  customFieldDefinitions: CustomFieldDefinitions;
}
export class UICustomFieldsForm extends WComponent<UICustomFieldsFormProps> {
  renderGroupFields(groupName: string, fields: Array<any>) {
    let { customFieldDefinitions } = this.props;
    let uiFields = [];
    for (let field of fields) {
      let fieldDef = customFieldDefinitions.getFieldDefinition(field.name);
      let uiField = (
        <FormGroup label={fieldDef.label}>
          <BBStringField bean={field} field='value' />
        </FormGroup>
      );
      uiFields.push(uiField);
    }
    let html = (
      <Form className='py-2'>
        <h6 className='border-bottom mb-2'>{groupName}</h6>
        {uiFields}
      </Form>
    );
    return html;
  }

  render() {
    let { plugin } = this.props;
    let fields = plugin.getMutableArray();
    let groupFieldMap = new ListRecordMap().addAllRecords('groupName', fields);
    let uiFieldGroups = [];
    for (let name of groupFieldMap.getListNames()) {
      let groupFields = groupFieldMap.getList(name);
      uiFieldGroups.push(this.renderGroupFields(name, groupFields));
    }
    return (<div className='flex-vbox'>{uiFieldGroups}</div>);
  }
}

interface UICustomFieldsEditorProps extends WComplexEntityProps {
  entity: string;
  type: string;
  entityId: number;
}
export class UICustomFieldsEditor extends WComplexEntity<UICustomFieldsEditorProps> {
  plugin: VGridEntityListEditorPlugin;
  customFieldDefinitions: CustomFieldDefinitions;

  constructor(props: UICustomFieldsEditorProps) {
    super(props);
    let { observer } = this.props;
    this.plugin = observer.createVGridEntityListEditorPlugin('fields', []);
    let entityCustomFields = observer.getMutableBean();
    this.customFieldDefinitions = new CustomFieldDefinitions(entityCustomFields.definitions);
    if (this.plugin.getModel().getRecords().length == 0) {
      this.plugin.getModel().addRecords(this.customFieldDefinitions.createFields());
    }
  }

  render() {
    let { appContext, pageContext, observer, entity, type, entityId } = this.props;
    return (
      <div className='flex-vbox'>
        <TabPane>
          <Tab name='form' label={T('Form')} active>
            <UICustomFieldsForm appContext={appContext} pageContext={pageContext}
              plugin={this.plugin} customFieldDefinitions={this.customFieldDefinitions} />
          </Tab>
          <Tab name='list' label={T('List')}>
            <UICustomFieldListEditor
              appContext={appContext} pageContext={pageContext} plugin={this.plugin}
              dialogEditor={true} editorTitle={T('Custom Fields')} />
          </Tab>
        </TabPane>
        <WToolbar>
          <WButtonEntityCommit
            appContext={appContext} pageContext={pageContext} observer={observer}
            label={"Save Custom Fields"} onPostCommit={() => pageContext.onBack()}
            commitURL={CustomFieldURL.customFields.save(entity, type, entityId)} />
        </WToolbar>
      </div>
    );
  }
}
export interface UILoadableEntityCustomFieldsProps extends WComponentProps {
  entity: string;
  type: string;
  entityId?: number;
}
export class UILoadableEntityCustomFields extends WComponent<UILoadableEntityCustomFieldsProps> {
  noCustomFields = false;
  entityCustomFieldsObserver: ComplexBeanObserver | null = null;

  constructor(props: UILoadableEntityCustomFieldsProps) {
    super(props);
    let { appContext, entityId, entity, type } = props;
    if (!entityId) return;
    this.markLoading(true);

    let callBack = (response: server.rest.RestResponse) => {
      let customFields = response.data;
      if (customFields) {
        this.entityCustomFieldsObserver = new ComplexBeanObserver(customFields);
      } else {
        this.noCustomFields = true;
      }
      this.markLoading(false)
      this.forceUpdate();
    }
    appContext.serverGET(CustomFieldURL.customFields.load(entity, type, entityId), {}, callBack);
  }

  render() {
    let { entityId } = this.props;
    if (!entityId) {
      return (<div>You need to create the entity first</div>);
    }

    if (this.noCustomFields) {
      return (<div>No Custom Field Definistions is available. You need to configure the Custom Field Definitions.</div>);
    }

    if (!this.entityCustomFieldsObserver) return this.renderLoading();

    let { appContext, pageContext, readOnly, entity, type } = this.props;
    let customFieldDefinitions = this.entityCustomFieldsObserver.getMutableBean().definitions;
    if (!customFieldDefinitions) {
      return (<div>The custom field definitions are not defined!</div>);
    }
    return (
      <UICustomFieldsEditor
        appContext={appContext} pageContext={pageContext} readOnly={readOnly}
        observer={this.entityCustomFieldsObserver} entity={entity} type={type} entityId={entityId} />
    );
  }
}