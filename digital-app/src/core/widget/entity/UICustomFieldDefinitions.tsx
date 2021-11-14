import React from 'react';
import { widget, util, server } from 'components';
import { ComplexBeanObserver, VGridEntityListEditorPlugin, } from 'core/entity';

import { WComponent, WComponentProps, WToolbar } from '../WLayout';

import { WComplexEntity, WButtonEntityCommit } from './WEntity';

import { UICustomFieldListEditor } from './UICustomFieldList'
import { T } from '../Dependency';

import ListRecordMap = util.ListRecordMap;
import { CustomFieldURL } from './RestURL';
import { WComplexEntityProps } from './WEntity';
const { TabPane, Tab } = widget.layout;
const { Form, FormGroup, BBStringField } = widget.input;

export interface ICustomField {
  groupName: string;
  name: string;
  type: string;
  value: any;
}

interface ICustomFieldDefinition extends ICustomField {
  label: string;
  description: string;
  required: boolean;
}
export class CustomFieldDefinitions {
  definitions: any;
  fields: Array<ICustomFieldDefinition>;
  fieldMap: Record<string, ICustomFieldDefinition> = {};

  constructor(definitions: any) {
    this.definitions = definitions;
    this.fields = definitions.fields;

    for (let fieldDef of this.fields) {
      this.fieldMap[fieldDef.name] = fieldDef;
    }
  }

  getFieldDefinition(name: string) {
    let fieldDef = this.fieldMap[name];
    if (!fieldDef) throw new Error(`No field ${name}`);
    return fieldDef;
  }

  createFields() {
    let holder: Array<ICustomField> = [];
    for (let fieldDef of this.fields) {
      let field: ICustomField = {
        groupName: fieldDef.groupName, name: fieldDef.name, type: fieldDef.type, value: fieldDef.value
      };
      holder.push(field);
    }
    return holder;
  }
}

interface UICustomFieldDefinitionsFormProps extends WComponentProps {
  plugin: VGridEntityListEditorPlugin;
}
export class UICustomFieldDefinitionsForm extends WComponent<UICustomFieldDefinitionsFormProps> {
  renderGroupFields(groupName: string, fields: Array<any>) {
    let uiFields = [];
    for (let field of fields) {
      let uiField = (
        <FormGroup label={field.label}>
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

interface UICustomFieldDefinitionsEditorProps extends WComplexEntityProps {
  entity: string;
  type: string;
}
export class UICustomFieldDefinitionsEditor extends WComplexEntity<UICustomFieldDefinitionsEditorProps> {

  render() {
    let { appContext, pageContext, observer, readOnly, entity, type } = this.props;
    let plugin = observer.createVGridEntityListEditorPlugin('fields', []);

    return (
      <div className='flex-vbox'>
        <TabPane>
          <Tab name='list' label={T('List')} active>
            <UICustomFieldListEditor
              appContext={appContext} pageContext={pageContext} plugin={plugin} definition={true}
              dialogEditor={true} editorTitle={T('Custom Field')} />
          </Tab>
          <Tab name='template' label={T(' Template')}>
            Todo
          </Tab >
          <Tab name='form-preview' label={T('Form Preview')}>
            <UICustomFieldDefinitionsForm appContext={appContext} pageContext={pageContext} plugin={plugin} />
          </Tab>
        </TabPane>
        <WToolbar>
          <WButtonEntityCommit
            appContext={appContext} pageContext={pageContext} readOnly={readOnly} observer={observer}
            label={"Definitions"} commitURL={CustomFieldURL.definitions.save(entity, type)}
            onPostCommit={() => pageContext.onClose()} />
        </WToolbar>
      </div>
    );
  }
}

export interface UILoadableCustomFieldDefinitionsProps extends WComponentProps {
  entity: string;
  type: string;
}
export class UILoadableCustomFieldDefinitions extends WComponent<UILoadableCustomFieldDefinitionsProps> {
  definitions: any;

  constructor(props: UILoadableCustomFieldDefinitionsProps) {
    super(props);
    this.markLoading(true);

    let { appContext, entity, type } = props;
    let callBack = (response: server.rest.RestResponse) => {
      this.definitions = response.data;
      if (!this.definitions) {
        this.definitions = {
          type: type,
          fields: []
        }
      }
      this.markLoading(false)
      this.forceUpdate();
    }
    appContext.serverGET(CustomFieldURL.definitions.load(entity, type), {}, callBack);
  }

  render() {
    if (this.isLoading()) return this.renderLoading();
    let { appContext, pageContext, readOnly, entity, type } = this.props;
    if (!this.definitions) return null;
    const customFieldDefinitionsObserver = new ComplexBeanObserver(this.definitions);
    return (
      <UICustomFieldDefinitionsEditor
        appContext={appContext} pageContext={pageContext} readOnly={readOnly}
        observer={customFieldDefinitionsObserver} entity={entity} type={type} />
    );
  }
}
