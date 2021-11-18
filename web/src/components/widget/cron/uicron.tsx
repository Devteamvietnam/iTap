import React from "react";
import {Button, PopoverHeader, PopoverBody, UncontrolledPopover} from 'reactstrap';

import type {CronExpressionModel} from "./model"
import {getCronExpression, newCronExpressionModel, getCronExpressionModel} from "./model"
import UISecondEditor from "./UISecondEditor"
import UIMinuteEditor from "./UIMinuteEditor"
import UIHourEditor from "./UIHourEditor"
import UIMonthEditor from "./UIMonthEditor"
import UIDayEditor from "./UIDayEditor"

import "./stylesheet.scss" ;

type UICronEditorProps = {
  model: CronExpressionModel, disable?: boolean,
  onSelectExpression: (exp: string) => any,
  onClose: () => void
}
type UICronEditorState = {type: 'second' | 'minute' | 'hour'| 'month' | 'day'}
class UICronEditor extends React.Component<UICronEditorProps, UICronEditorState> {

  constructor(props: UICronEditorProps) {
    super(props);
    this.state = {type: 'second'};
  }

  onChangeType(type: 'second' | 'minute' | 'hour'| 'month' | 'day') {
    this.setState({type: type})
  }

  onUpdateExpression() {
    this.forceUpdate();
  }

  render() {
    let {model, onSelectExpression, onClose, disable} = this.props;
    let {type} = this.state;

    let uiTime = null;

    if (type === 'second') {
      uiTime = ( <UISecondEditor model={model} onUpdateExpression={() => this.onUpdateExpression()}/> )
    } else if (type === 'minute') {
      uiTime = ( <UIMinuteEditor model={model} onUpdateExpression={() => this.onUpdateExpression()}/> )
    } else if (type === 'hour') {
      uiTime = ( <UIHourEditor model={model} onUpdateExpression={() => this.onUpdateExpression()}/> )
    } else if (type === 'day') {
      uiTime = ( <UIDayEditor model={model} onUpdateExpression={() => this.onUpdateExpression()}/> )
    } else if (type === 'month') {
      uiTime = ( <UIMonthEditor model={model} onUpdateExpression={() => this.onUpdateExpression()}/> )
    }

    let dayLabel = model.dayOfMonth === '?' ? 'Day Of Week' : 'Day Of Month';
    let day = model.dayOfMonth === '?' ? model.dayOfWeek : model.dayOfMonth;
    let btnSelect;
    if(!disable) {
      btnSelect = (
        <Button disabled={disable} outline onClick={() => onSelectExpression(getCronExpression(model))}>
          Select
        </Button>
      )
    }
    let html = (
      <div className='ui-cron-editor'>
        <div className='header d-flex'>
          <div>
            <Button outline onClick={() => this.onChangeType('second')}>Second</Button>
            <label>{model.second}</label>
          </div>
          <div>
            <Button outline onClick={() => this.onChangeType('minute')}>Minute</Button>
            <label>{model.minute}</label>
          </div>
          <div>
            <Button outline onClick={() => this.onChangeType('hour')}>Hour</Button>
            <label>{model.hour}</label>
          </div>
          <div>
            <Button outline onClick={() => this.onChangeType('day')}>{dayLabel}</Button>
            <label>{day}</label>
          </div>
          <div>
            <Button outline onClick={() => this.onChangeType('month')}>Month</Button>
            <label>{model.month}</label>
          </div>
        </div>
        <div className='ui-time-selector'>{uiTime}</div>
        <div className='d-flex flex-row-reverse'>
          <Button className="ml-1" outline onClick={() => onClose()}>Close</Button>
          {btnSelect}
        </div>
      </div>
    )
    return html;
  }
}

type WCronSelectorProps = {
  id: string, name: string, value: any,
  onInputChange?: (bean: any, field: string, oldVal: any, newVal: any) => void,
  onFocus?: () => void
};
type WCronSelectorState = {value: string}
class WCronSelector extends React.Component<WCronSelectorProps, WCronSelectorState> {

  constructor(props: WCronSelectorProps) {
    super(props);
    this.state = {value: props.value};
  }

  componentWillReceiveProps(nextProps: WCronSelectorProps) {
    this.setState({value: nextProps.value});
  }

  onChange = (_e: any) => { }

  onFocus = (evt: any) => {
    let {onFocus} = this.props;
    evt.target.select();
    if(onFocus) onFocus();
  }

  onFocusLost = (_evt: any) => { }

  render() {
    let {id, name} = this.props;
    let {value} = this.state;
    let classes = 'form-control';
    return (
      <input id={id} className={classes} type={'text'}  name={name} value={value} readOnly={true}
        onChange={this.onChange} onFocus={this.onFocus} onBlur={this.onFocusLost}/>
    );
  }
}

type BBCronSelectorFieldProps = {
  bean: any, field: string, disable?: boolean,
  onInputChange?: (bean: any, field: string, oldVal: any, newVal: any) => void
};
type BBCronSelectorFieldState = {openDialog: boolean}
export class BBCronSelectorField extends React.Component<BBCronSelectorFieldProps, BBCronSelectorFieldState> {

  constructor(props: BBCronSelectorFieldProps) {
    super(props);
    let {bean, field} = props;
    if(!bean[field] || bean[field] === '') {
      bean[field] = getCronExpression(newCronExpressionModel);
    }
    this.state = {openDialog: false};
  }

  componentWillReceiveProps(nextProps: BBCronSelectorFieldProps) {
    let {bean, field} = nextProps;
    if(!bean[field] || bean[field] === '') {
      bean[field] = getCronExpression(newCronExpressionModel);
    }
  }

  onFocus = () => {
    if(this.state.openDialog) return;
    this.setState({openDialog: true});
  }

  onClose = () => {
    this.setState({ openDialog: false });
  }

  onInputChange(oldVal: any, newVal: any) {
    const { bean, field, onInputChange } = this.props;
    bean[field] = newVal;
    if(onInputChange) onInputChange(bean, field, oldVal, newVal);
  }

  onSelectExpression(exp: string) {
    this.onInputChange('', exp);
    this.setState({ openDialog: false });
  }

  render() {
    let { bean, field } = this.props;
    let html = (
      <div>
        <WCronSelector id={'cron-editor'} name={field} value={bean[field]} onFocus={this.onFocus}
          onInputChange={(oldVal, newVal) => this.onInputChange(oldVal, newVal)}/>
        {this.renderDialog()}
      </div>
    )
    return html;
  }

  renderDialog() {
    let {openDialog} = this.state;
    let {bean, field, disable} = this.props;
    let html;
    if (openDialog) {
      html = (
        <UncontrolledPopover trigger='legacy' placement={'auto'} target={'cron-editor'}>
          <PopoverHeader>Cron Editor</PopoverHeader>
          <PopoverBody>
            <UICronEditor model={getCronExpressionModel(bean[field])} disable={disable}
              onSelectExpression={(exp) => this.onSelectExpression(exp)} onClose={() => this.onClose()}/>
          </PopoverBody>
        </UncontrolledPopover>
      )
    }
    return html;
  }
}
