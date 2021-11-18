import React from "react";

import {Form, BBSelectField, BBCheckboxField} from 'components/widget/input';
import type {CronExpressionModel} from "./model"
import {EveryTimeUnitBetweenModel, EveryTimeUnitModel, SpecificTimeUnitModel} from "./model"

export interface ITimeUnitEditor {
  onUpdateExpression(): void;
}

export type SimpleTimeUnitEditorProps = {
  label?: string;
  model: CronExpressionModel|any;
  property: string;
  onUpdateExpression: () => void;
}
type SimpleTimeUnitEditorState = { }
export class SimpleTimeUnitEditor 
  extends React.Component<SimpleTimeUnitEditorProps, SimpleTimeUnitEditorState> implements ITimeUnitEditor {
  label: string;

  constructor(props: SimpleTimeUnitEditorProps) {
    super(props);
    this.label = props.label ? props.label : '';
  }

  onUpdateExpression() { throw Error('Need to be implemented'); }

  canEditExpression() : boolean { throw Error('Need to be implemented'); }

  render() {
    let {property, model} = this.props;
    let isChecked = this.canEditExpression();
    let html = (
      <Form className='d-flex align-items-center my-2'>
        <input checked={isChecked} type={'radio'} name={property}
          value={model[property]} onChange={() => this.onUpdateExpression()} />
        <span>{this.label}</span>
      </Form>
    )
    return html;
  }
}

type EveryUnitBetweenEditorProps = {
  timeUnit: EveryTimeUnitBetweenModel;
  model: CronExpressionModel|any; 
  property: string;
  onUpdateExpression: () => void;
}
type EveryUnitBetweenEditorState = {}
export class EveryUnitBetweenEditor
  extends React.Component<EveryUnitBetweenEditorProps, EveryUnitBetweenEditorState>
  implements ITimeUnitEditor {

  timeOptions: Array<number>;
  timeUnit: EveryTimeUnitBetweenModel;

  constructor(props: EveryUnitBetweenEditorProps) {
    super(props);
    this.timeUnit = props.timeUnit;
    this.timeOptions = [];
    for (let i = this.timeUnit.minRange; i <= this.timeUnit.maxRange; i++) {
      this.timeOptions.push(i);
    }
  }

  onUpdateExpression() {
    let {model, property, onUpdateExpression} = this.props;
    model[property] = this.timeUnit.getUnitExpression();
    onUpdateExpression()
  }

  canEditExpression() {
    let {model, property} = this.props;
    return this.timeUnit.canEditExpression(model[property]);
  }

  render() {
    let {model, property} = this.props;
    let isEditable = this.canEditExpression();
    let label = this.timeUnit.label;
    let html = (
      <Form className='d-flex align-items-center my-2'>
        <input checked={isEditable} type={'radio'} name={property} value={model[property]}
          onChange={() => this.onUpdateExpression()} />
        <span>{`${'Every'} ${label} ${'between'} ${label}`}</span>
        <BBSelectField bean={this.timeUnit} field={'from'} options={this.timeOptions} disable={!isEditable}
          onInputChange={(_bean, _field, _oldVal, _newVal) => this.onUpdateExpression()}/>
        <span> {'and'} </span>
        <BBSelectField bean={this.timeUnit} field={'to'} options={this.timeOptions} disable={!isEditable}
          onInputChange={(_bean, _field, _oldVal, _newVal) => this.onUpdateExpression()}/>
      </Form>
    )
    return html;
  }
}

type SpecificNUnitEditorProps = {
  model: CronExpressionModel|any;
  property: string;
  timeUnit: SpecificTimeUnitModel;
  onUpdateExpression: () => void;
}
type SpecificNUnitEditorState = {}
export class SpecificNUnitEditor 
  extends React.Component<SpecificNUnitEditorProps, SpecificNUnitEditorState> implements ITimeUnitEditor {

  timeUnit: SpecificTimeUnitModel;
  beanOptions: Array<any>;

  constructor(props: SpecificNUnitEditorProps) {
    super(props);
    this.timeUnit = props.timeUnit;

    this.beanOptions = [];
    for (let i = this.timeUnit.minRange; i <= this.timeUnit.maxRange; i++) {
      let checked = false;
      if (this.timeUnit.contains(i)) { checked = true }
      this.beanOptions.push({value: i, checked: checked})
    }
  }

  onSelectFirstOption() {
    this.onUpdateExpression();
  }

  onSelect(checked: boolean, opt: any) {
    if(checked) { this.timeUnit.select(opt.value); }
    else { this.timeUnit.deselect(opt.value); }
    opt.checked = checked;
    //Select at least one if empty
    if(this.timeUnit.isEmpty()) {
      this.beanOptions[0].checked = true;
      this.timeUnit.select(this.beanOptions[0].value);
    }
    this.onUpdateExpression();
  }

  onUpdateExpression() {
    let {model, property, onUpdateExpression} = this.props;
    model[property] = this.timeUnit.getUnitExpression();
    onUpdateExpression()
  }

  canEditExpression() {
    let {model, property} = this.props;
    return this.timeUnit.canEditExpression(model[property]);
  }

  render() {
    let {model, property} = this.props;

    let isEditable = this.canEditExpression();
    let label = this.timeUnit.label;

    let uiCheckbox = [];
    for (let i = 0; i < this.beanOptions.length; i++) {
      let option = this.beanOptions[i];
      uiCheckbox.push(
        <div className='box' key={i}>
          <BBCheckboxField bean={option} field={'checked'} value={option.value}
            label={`${option.value}`} disable={!isEditable}
            onInputChange={(_bean, _field, _oldVal, newVal) => this.onSelect(newVal, option)}/>
        </div>
      )
    }
    let html = (
      <Form className='specific-n-unit-editor'>
        <div className='d-flex align-items-center'>
          <input checked={isEditable} type={'radio'} name={property} value={model[property]}
            onChange={() => this.onSelectFirstOption()} />
          <span>{`${'Specific'} ${label}`}</span>
        </div>
        <div className='select-box ml-3'>
          {uiCheckbox}
        </div>
      </Form>
    )
    return html;
  }
}

type EveryNUnitEditorProps = {
  model: CronExpressionModel|any; 
  property: string;
  timeUnit: EveryTimeUnitModel;
  onUpdateExpression: () => void;
}
type EveryNUnitEditorState = { }
export class EveryNUnitEditor 
  extends React.Component<EveryNUnitEditorProps, EveryNUnitEditorState> implements ITimeUnitEditor {

  timeUnit: EveryTimeUnitModel;

  constructor(props: EveryNUnitEditorProps) {
    super(props);
    this.timeUnit = props.timeUnit;
  }

  onUpdateExpression() {
    let {model, property, onUpdateExpression} = this.props;
    model[property] = this.timeUnit.getUnitExpression();
    onUpdateExpression()
  }

  canEditExpression() {
    let {model, property} = this.props;
    return this.timeUnit.canEditExpression(model[property]);
  }

  render() {
    let {model, property} = this.props;
    let isEditable = this.canEditExpression();
    let label = this.timeUnit.label;

    let startAtOptions = [];
    let startAtLabelOptions = [];

    for (let i = 0; i < this.timeUnit.startAtOpts.length; i++) {
      let startAt = this.timeUnit.startAtOpts[i];
      startAtOptions.push(startAt.value);
      startAtLabelOptions.push(startAt.label);
    }
    let html = (
      <Form className='d-flex align-items-center my-2'>
        <input checked={isEditable} type={'radio'} name={property} value={model[property]}
          onChange={() => this.onUpdateExpression()} />
        <span> {'Every'} </span>
        <BBSelectField bean={this.timeUnit} field={'every'} options={this.timeUnit.everyOpts} disable={!isEditable}
          onInputChange={(_bean, _field, _oldVal, _newVal) => this.onUpdateExpression()}/>
        <span>{`${label} ${'starting at'} ${label}`}</span>
        <BBSelectField bean={this.timeUnit} field={'startAt'} disable={!isEditable}
            options={startAtOptions} optionLabels={startAtLabelOptions}
            onInputChange={(_bean, _field, _oldVal, _newVal) => this.onUpdateExpression()}/>
      </Form>
    )
    return html;
  }
}

export class EveryUnitEditor extends SimpleTimeUnitEditor {
  constructor(props: SimpleTimeUnitEditorProps) {
    super(props);
    this.label = `${'Every'} ${props.label ? props.label: ''}`;
  }

  onUpdateExpression() {
    let {model, property, onUpdateExpression} = this.props;
    model[property] = '*';
    onUpdateExpression();
  }

  canEditExpression() {
    let {model, property} = this.props;
    return model[property] === '*';
  }
}
