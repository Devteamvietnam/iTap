import React from "react";
import type {SimpleTimeUnitEditorProps} from "./UITimeUnitEditor";

import {Form, BBSelectField} from "components/widget/input"
import {
  ITimeUnitEditor, SimpleTimeUnitEditor,
  EveryUnitEditor, EveryNUnitEditor,
  SpecificNUnitEditor
} from "./UITimeUnitEditor"

import type {CronExpressionModel} from "./model"
import { EveryTimeUnitModel, TimeUnitValue, SpecificTimeUnitModel, } from "./model"

type UIDayEditorProps = {
  model: CronExpressionModel,
  onUpdateExpression: () => void
}
type UIDayEditorState = {uiSelected: 'dayOfMonth' | 'dayOfWeek'}
export default class UIDayEditor extends React.Component<UIDayEditorProps, UIDayEditorState> {

  constructor(props: UIDayEditorProps) {
    super(props);
    if ('?' === props.model.dayOfMonth) {
      this.state = {uiSelected: 'dayOfWeek'};
    } else {
      this.state = {uiSelected: 'dayOfMonth'};
    }
  }

  onChangeScreenType(type: 'dayOfMonth' | 'dayOfWeek') {
    let {model, onUpdateExpression} = this.props;
    if (type === 'dayOfMonth') {
      model.dayOfWeek = '?'
      model.dayOfMonth = '*'
    } else {
      model.dayOfMonth = '?'
      model.dayOfWeek = '*'
    }
    this.setState({uiSelected: type});
    onUpdateExpression();
  }

  render() {
    let {model, onUpdateExpression} = this.props;
    let {uiSelected} = this.state;
    let uiScreen;
    if (uiSelected === 'dayOfMonth') {
      uiScreen = (<UIDayOfMonthEditor model={model} onUpdateExpression={onUpdateExpression} />)
    } else if (uiSelected === 'dayOfWeek') {
      uiScreen = (<UIDayOfWeekEditor model={model} onUpdateExpression={onUpdateExpression} />)
    }
    let html = (
      <div>
        <div className='d-flex flex-row-reverse'>
          <div className='mx-1 d-flex align-items-center'>
            <input checked={uiSelected === 'dayOfWeek'} type={'radio'} name={'day'}
              onChange={() => this.onChangeScreenType('dayOfWeek')} />
            <span>Day Of Week</span>
          </div>
          <div className='mx-1 d-flex align-items-center'>
            <input checked={uiSelected === 'dayOfMonth'} type={'radio'} name={'day'}
              onChange={() => this.onChangeScreenType('dayOfMonth')} />
            <span>Day Of Month</span>
          </div>
        </div>
        {uiScreen}
      </div>
    )
    return html;
  }
}

type UIDayOfMonthEditorProps = {
  model: CronExpressionModel,
  onUpdateExpression: () => void
}
type UIDayOfMonthEditorState = { }
class UIDayOfMonthEditor extends React.Component<UIDayOfMonthEditorProps, UIDayOfMonthEditorState> {

  render() {
    let {model, onUpdateExpression} = this.props;
    let startAtOpts = [];
    let everyOpts = [];
    for (let i = 1; i <= 31; i++) {
      startAtOpts.push(new TimeUnitValue(i.toString(), i));
      everyOpts.push(i);
    }
    let label = 'day';
    let property = 'dayOfMonth';

    let everyTime = new EveryTimeUnitModel(label, startAtOpts, everyOpts);
    let specificTime = new SpecificTimeUnitModel(label, 1, 31, model.dayOfMonth);

    let html = (
      <div>
        <EveryUnitEditor label={label} model={model} property={property} onUpdateExpression={onUpdateExpression}/>
        <EveryNUnitEditor model={model} property={property} onUpdateExpression={onUpdateExpression}
          timeUnit={everyTime}/>
        <SpecificNUnitEditor model={model} property={property} onUpdateExpression={onUpdateExpression}
          timeUnit={specificTime}/>
        <LastDayOfMonthUnitEditor model={model} property={property} onUpdateExpression={onUpdateExpression} />
        <LastWeekDayOfMonthUnitEditor model={model} property={property} onUpdateExpression={onUpdateExpression} />
        <DayBeforeEndOfMonthUnitEditor model={model} property={property} onUpdateExpression={onUpdateExpression} />
        <NearestWeekDayOfMonthUnitEditor model={model} property={property} onUpdateExpression={onUpdateExpression} />
      </div>
    )
    return html;
  }
}

type UIDayOfWeekEditorProps = {
  model: CronExpressionModel,
  onUpdateExpression: () => void
}
type UIDayOfWeekEditorState = { }
class UIDayOfWeekEditor extends React.Component<UIDayOfWeekEditorProps, UIDayOfWeekEditorState> {

  render() {
    let {model, onUpdateExpression} = this.props;
    let startAtOpts = [
      new TimeUnitValue("Sun", 1), new TimeUnitValue("Mon", 2), new TimeUnitValue("Tue", 3),
      new TimeUnitValue("Wed", 4), new TimeUnitValue("Thu", 5), new TimeUnitValue("Fri", 6),
      new TimeUnitValue("Sat", 7)
    ];

    let everyOpts = [];

    for (let i = 1; i <= 7; i++) {
      everyOpts.push(i);
    }

    let label = 'day';
    let property = 'dayOfWeek';

    let everyTime = new EveryTimeUnitModel(label, startAtOpts, everyOpts);
    let specificTime = new SpecificTimeUnitModel('week', 1, 7, model.dayOfWeek);

    let html = (
      <div>
        <EveryUnitEditor label={label} model={model} property={property} onUpdateExpression={onUpdateExpression}/>
        <EveryNUnitEditor model={model} property={property} onUpdateExpression={onUpdateExpression}
          timeUnit={everyTime}/>
        <SpecificNUnitEditor model={model} property={property} onUpdateExpression={onUpdateExpression}
          timeUnit={specificTime}/>
        <LastDayOfWeekOfMonthEditor model={model} property={property} onUpdateExpression={onUpdateExpression}/>
        <EveryDayOfWeekOfMonthEditor model={model} property={property} onUpdateExpression={onUpdateExpression}/>
      </div>
    )
    return html;
  }
}

class LastDayOfMonthUnitEditor extends SimpleTimeUnitEditor {
  constructor(props: SimpleTimeUnitEditorProps) {
    super(props);
    this.label = `On the last day of the month`;
  }

  onUpdateExpression() {
    let {model, property, onUpdateExpression} = this.props;
    model[property] = 'L';
    onUpdateExpression();
  }

  canEditExpression() {
    let {model, property} = this.props;
    return model[property] === 'L';
  }
}

class LastWeekDayOfMonthUnitEditor extends SimpleTimeUnitEditor {
  constructor(props: SimpleTimeUnitEditorProps) {
    super(props);
    this.label = `On the last weekday of the month`;
  }

  onUpdateExpression() {
    let {model, property, onUpdateExpression} = this.props;
    model[property] = 'LW';
    onUpdateExpression();
  }

  canEditExpression() {
    let {model, property} = this.props;
    return model[property] === 'LW';
  }
}

type DayBeforeEndOfMonthUnitEditorProps = {
  model: CronExpressionModel|any;
  property: string;
  onUpdateExpression: () => void
}
type DayBeforeEndOfMonthUnitEditorState = { }
class DayBeforeEndOfMonthUnitEditor
      extends React.Component<DayBeforeEndOfMonthUnitEditorProps, DayBeforeEndOfMonthUnitEditorState> implements ITimeUnitEditor {

  timeOptions: Array<string>;
  labelTimeOptions: Array<number>;

  constructor(props: DayBeforeEndOfMonthUnitEditorProps) {
    super(props);
    this.timeOptions = [];
    this.labelTimeOptions = [];
    for (let i = 1; i <= 31; i++) {
      this.timeOptions.push(`L-${i}`);
      this.labelTimeOptions.push(i);
    }
  }

  onSelect() {
    let {model, property} = this.props;
    model[property] = this.timeOptions[0];
    this.onUpdateExpression();
  }

  onUpdateExpression() {
    let {onUpdateExpression} = this.props;
    onUpdateExpression();
  }

  canEditExpression() {
    let {model, property} = this.props;
    return model[property].startsWith('L-');
  }

  render() {
    let {model, property} = this.props;
    let isEditable = this.canEditExpression();
    let html = (
      <Form className='d-flex align-items-center my-2'>
        <input checked={isEditable} type={'radio'} name={property} value={model[property]}
          onChange={() => this.onSelect()} />
        <span>{'Day(s) before the end of the month'}</span>
        <BBSelectField bean={model} field={property}  disable={!isEditable}
          options={this.timeOptions} optionLabels={this.labelTimeOptions}
          onInputChange={(_bean, _field, _oldVal, _newVal) => this.onUpdateExpression()}/>
        <span>{'days before the end of the month'}</span>
      </Form>
    );
    return html;
  }
}

type NearestWeekDayOfMonthUnitEditorProps = {
  model: CronExpressionModel|any;
  property: string;
  onUpdateExpression: () => void;
}
type NearestWeekDayOfMonthUnitEditorState = { }
class NearestWeekDayOfMonthUnitEditor
      extends React.Component<NearestWeekDayOfMonthUnitEditorProps, NearestWeekDayOfMonthUnitEditorState>
      implements ITimeUnitEditor {

  timeOptions: Array<string>;
  labelTimeOptions: Array<number>;

  constructor(props: NearestWeekDayOfMonthUnitEditorProps) {
    super(props);
    this.timeOptions = [];
    this.labelTimeOptions = [];
    for (let i = 1; i <= 31; i++) {
      this.timeOptions.push(`${i}W`);
      this.labelTimeOptions.push(i);
    }
  }

  onSelect() {
    let {model, property} = this.props;
    model[property] = this.timeOptions[0];
    this.onUpdateExpression();
  }

  onUpdateExpression() {
    let {onUpdateExpression} = this.props;
    onUpdateExpression();
  }

  canEditExpression() {
    let {model, property} = this.props;
    return model[property].endsWith('W');
  }

  render() {
    let {model, property} = this.props;
    let isEditable = this.canEditExpression();
    let html = (
      <Form className='d-flex align-items-center my-2'>
        <input checked={isEditable} type={'radio'} name={property} value={model[property]}
          onChange={() => this.onSelect()} />
        <span>{'Nearest weekday (Monday to Friday) to the'} </span>
        <BBSelectField bean={model} field={property}  disable={!isEditable}
          options={this.timeOptions} optionLabels={this.labelTimeOptions}
          onInputChange={(_bean, _field, _oldVal, _newVal) => this.onUpdateExpression()}/>
        <span> {'of the month'}</span>
      </Form>
    );
    return html;
  }
}

type LastDayOfWeekOfMonthEditorProps = {
  model: CronExpressionModel|any;
  property: string;
  onUpdateExpression: () => void;
}
type LastDayOfWeekOfMonthEditorState = { }
class LastDayOfWeekOfMonthEditor 
  extends React.Component <LastDayOfWeekOfMonthEditorProps, LastDayOfWeekOfMonthEditorState> implements ITimeUnitEditor {

  timeOptions: Array<string>;
  labelTimeOptions: Array<string>;

  constructor(props: LastDayOfWeekOfMonthEditorProps) {
    super(props);
    let dayValues = [
      new TimeUnitValue("Sun", 1), new TimeUnitValue("Mon", 2), new TimeUnitValue("Tue", 3),
      new TimeUnitValue("Wed", 4), new TimeUnitValue("Thu", 5), new TimeUnitValue("Fri", 6),
      new TimeUnitValue("Sat", 7)
    ];

    this.timeOptions = [];
    this.labelTimeOptions = [];
    for (let i = 0; i < dayValues.length; i++) {
      this.timeOptions.push(`${dayValues[i].value}L`);
      this.labelTimeOptions.push(dayValues[i].label);
    }
  }

  onSelect() {
    let {model, property} = this.props;
    model[property] = this.timeOptions[0];
    this.onUpdateExpression();
  }

  onUpdateExpression() {
    let {onUpdateExpression} = this.props;
    onUpdateExpression();
  }

  canEditExpression() {
    let {model, property} = this.props;
    return model[property].endsWith('L');
  }

  render() {
    let {model, property} = this.props;
    let isEditable = this.canEditExpression();
    let html = (
      <Form className='my-2'>
        <div className='d-flex align-items-center'>
          <input checked={isEditable} type={'radio'} name={property} value={model[property]}
          onChange={() => this.onSelect()} />
          <span>{'On the last day of week of the month'}</span>
        </div>
        <div className='d-flex align-items-center ml-3'>
          <span>{'On the last'}</span>
          <BBSelectField bean={model} field={property}  disable={!isEditable}
           options={this.timeOptions} optionLabels={this.labelTimeOptions}
           onInputChange={(_bean, _field, _oldVal, _newVal) => this.onUpdateExpression()}/>
          <span>{'of the month'}</span>
        </div>
      </Form>
    );
    return html;
  }
}

type EveryDayOfWeekOfMonthEditorProps = {
  model: CronExpressionModel|any;
  property: string;
  onUpdateExpression: () => void;
}
type EveryDayOfWeekOfMonthEditorState = { }
class EveryDayOfWeekOfMonthEditor extends React.Component<EveryDayOfWeekOfMonthEditorProps, EveryDayOfWeekOfMonthEditorState> {

  ordinalOptions: Array<number>;
  ordinalLabelOptions: Array<string>;

  dayOptions: Array<number>;
  dayLabelOptions: Array<string>;

  valueSelected: any;

  constructor(props: EveryDayOfWeekOfMonthEditorProps) {
    super(props);
    this.ordinalOptions = [];
    this.ordinalLabelOptions = [];
    let ordinalValues = [
      new TimeUnitValue("1st", 1), new TimeUnitValue("2nd", 2), new TimeUnitValue("3rd", 3),
      new TimeUnitValue("4th", 4), new TimeUnitValue("5th", 5)
    ];
    for (let i = 0; i < ordinalValues.length; i++) {
      this.ordinalOptions.push(ordinalValues[i].value);
      this.ordinalLabelOptions.push(ordinalValues[i].label);
    }

    let dayValues = [
      new TimeUnitValue("Sun", 1), new TimeUnitValue("Mon", 2), new TimeUnitValue("Tue", 3),
      new TimeUnitValue("Wed", 4), new TimeUnitValue("Thu", 5), new TimeUnitValue("Fri", 6),
      new TimeUnitValue("Sat", 7)
    ];

    this.dayOptions = [];
    this.dayLabelOptions = [];
    for (let i = 0; i < dayValues.length; i++) {
      this.dayOptions.push(dayValues[i].value);
      this.dayLabelOptions.push(dayValues[i].label);
    }

    this.valueSelected = {ordinal: this.ordinalOptions[0], day: this.dayOptions[0]}
  }

  onSelect() {
    let {model, property} = this.props;
    model[property] =  `${this.valueSelected.ordinal}#${this.valueSelected.day}`
    this.onUpdateExpression();
  }

  onUpdateExpression() {
    let {onUpdateExpression} = this.props;
    onUpdateExpression();
  }

  canEditExpression() {
    let {model, property} = this.props;
    return model[property].includes('#');
  }

  render() {
    let {model, property} = this.props;
    let isEditable = this.canEditExpression();

    let html = (
      <Form className='my-2'>
        <div className='d-flex align-items-center'>
          <input checked={isEditable} type={'radio'} name={property} value={model[property]}
          onChange={() => this.onSelect()} />
          <span>{'Every a day of week of the month'}</span>
        </div>
        <div className='d-flex ml-3'>
          <span> {'On the'} </span>
          <BBSelectField bean={this.valueSelected} field={'ordinal'}  disable={!isEditable}
           options={this.ordinalOptions} optionLabels={this.ordinalLabelOptions}
           onInputChange={(_bean, _field, _oldVal, _newVal) => this.onSelect()}/>
          <BBSelectField bean={this.valueSelected} field={'day'}  disable={!isEditable}
            options={this.dayOptions} optionLabels={this.dayLabelOptions}
            onInputChange={(_bean, _field, _oldVal, _newVal) => this.onSelect()}/>
          <span> {'of the month'}</span>
        </div>
      </Form>
    );
    return html;
  }
}
