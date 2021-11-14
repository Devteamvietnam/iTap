import React from 'react';
import { widget } from 'components';

import {
  WEntity
} from 'core/widget';
import {
  VGridEntityListEditor, VGridEntityListEditorProps, VGridConfigTool
} from 'core/widget/vgrid';

import { T } from '../Dependency';

import VGridConfig = widget.grid.VGridConfig;
import { WEntityProps } from './WEntity';

const {
  FormContainer, ColFormGroup, BBStringField, BBTextField, BBSelectField, BBNumberField, BBStringArrayField, BBDateTimeField } = widget.input;

interface UICustomFieldProps extends WEntityProps {
  definition?: boolean;
}

export enum TypeOpts {
  STRING = 'String',
  STRING_ARRAY = 'StringArray',
  INTEGER = 'Integer',
  LONG = 'Long',
  FLOAT = 'Float',
  DOUBLE = 'Double',
  BOOLEAN = 'Boolean',
  DATE = 'Date',
}
class UICustomField extends WEntity<UICustomFieldProps> {

  onTypeChange(_newVal: string) {
    this.forceUpdate();
  }

  isNewBean() {
    let { observer } = this.props;
    let bean = observer.getMutableBean();
    if (bean.id) return false;
    return true;
  }

  renderValueField(typeOpts: string) {
    let { observer } = this.props;
    let bean = observer.getMutableBean();
    let writeCap = this.hasWriteCapability();
    let html = null;
    if (typeOpts === TypeOpts.DOUBLE || typeOpts === TypeOpts.FLOAT ||
      typeOpts === TypeOpts.LONG || typeOpts === TypeOpts.INTEGER) {
      html = (
        <BBNumberField bean={bean} field={'value'} disable={!writeCap} />
      )
    } else if (typeOpts === TypeOpts.STRING) {
      html = (
        <BBStringField bean={bean} field={'value'} disable={!writeCap} />
      )
    } else if (typeOpts === TypeOpts.STRING_ARRAY) {
      html = (
        <BBStringArrayField bean={bean} field={'value'} disable={!writeCap} />
      )
    } else if (typeOpts === TypeOpts.DATE) {
      html = (
        <BBDateTimeField bean={bean} field={'value'} dateFormat={"DD/MM/YYYY"} timeFormat={true} disable={!writeCap} />
      )
    } else {
      html = (
        <BBStringField bean={bean} field={'value'} disable={!writeCap} />
      )
    }
    return html;
  }

  render() {
    let { observer, definition } = this.props;
    let bean = observer.getMutableBean();
    let writeCap = this.hasWriteCapability();
    let typeOpts = [
      TypeOpts.STRING, TypeOpts.STRING_ARRAY, TypeOpts.INTEGER, TypeOpts.LONG,
      TypeOpts.FLOAT, TypeOpts.DOUBLE, TypeOpts.BOOLEAN, TypeOpts.DATE
    ];
    return (
      <FormContainer fluid>
        <ColFormGroup span={12} label={T('Group')}>
          <BBStringField bean={bean} field={'groupName'} required disable={!writeCap || !this.isNewBean()} />
        </ColFormGroup>
        <ColFormGroup span={12} label={T('Name')}>
          <BBStringField bean={bean} field={'name'} required disable={!writeCap || !this.isNewBean()} />
        </ColFormGroup>
        {definition && (
          <ColFormGroup span={12} label={T('Label')}>
            <BBStringField bean={bean} field={'label'} required disable={!writeCap} />
          </ColFormGroup>
        )}
        <ColFormGroup span={12} label={T('Type')}>
          <BBSelectField
            bean={bean} field={'type'} options={typeOpts} disable={!writeCap || !this.isNewBean()}
            onInputChange={(_bean: any, _field: string, _oldVal: any, newVal: any) => this.onTypeChange(newVal)} />
        </ColFormGroup>
        <ColFormGroup span={12} label={T('Value')}>
          {this.renderValueField(bean.type)}
        </ColFormGroup>
        {definition && (
          <ColFormGroup span={12} label={T('Description')}>
            <BBTextField bean={bean} field={'description'} style={{ height: '10em' }} disable={!writeCap} />
          </ColFormGroup>
        )}
      </FormContainer>
    );
  }
}
interface UICustomFieldListEditorProps extends VGridEntityListEditorProps {
  definition?: boolean;
}
export class UICustomFieldListEditor extends VGridEntityListEditor<UICustomFieldListEditorProps> {
  renderBeanEditor() {
    let { appContext, pageContext, definition } = this.props;
    let observer = this.createBeanObserver();
    return (
      <UICustomField appContext={appContext} pageContext={pageContext} observer={observer} definition={definition} />
    );
  }

  createVGridConfig(): VGridConfig {
    let writeCap = this.hasWriteCapability();
    const { definition } = this.props;
    let config: VGridConfig = {
      record: {
        fields: [
          ...VGridConfigTool.FIELD_SELECTOR(this.needSelector()),
          VGridConfigTool.FIELD_INDEX(),
          VGridConfigTool.FIELD_ON_SELECT('name', T('Name'), 200),
          { name: 'groupName', label: T('Group') },
          { name: 'type', label: T('Type'), state: { visible: false } },
          { name: 'value', label: T('Value'), width: 200 },
        ]
      },
      toolbar: {
        actions: [

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
    }
    if (definition) {
      config.record.fields = [
        ...VGridConfigTool.FIELD_SELECTOR(this.needSelector()),
        VGridConfigTool.FIELD_INDEX(),
        VGridConfigTool.FIELD_ON_SELECT('label', T('Label'), 200),
        { name: 'groupName', label: T('Group') },
        { name: 'name', label: T('Name') },
        { name: 'type', label: T('Type'), state: { visible: false } },
        { name: 'value', label: T('Value'), width: 200 },
        { name: 'description', label: T('Description'), width: 300 }
      ];

      config.toolbar.actions = [
        ...VGridConfigTool.TOOLBAR_ON_ADD(!writeCap, "Add"),
        ...VGridConfigTool.TOOLBAR_ON_DELETE(!writeCap, "Del")
      ]
    }
    return config;
  }
}
