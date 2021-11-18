import React from "react";
import {
  EveryUnitEditor, EveryNUnitEditor,
  SpecificNUnitEditor, EveryUnitBetweenEditor
} from "./UITimeUnitEditor"

import type {CronExpressionModel} from "./model"
import {
  EveryTimeUnitBetweenModel, EveryTimeUnitModel,
  TimeUnitValue, SpecificTimeUnitModel
} from "./model"

type UIHourEditorProps = {
  model: CronExpressionModel,
  onUpdateExpression: () => void
}
type UIHourEditorState = { }
export default class UIHourEditor extends React.Component<UIHourEditorProps, UIHourEditorState> {

  render() {
    let {model, onUpdateExpression} = this.props;
    let startAtOpts = [];
    let everyOpts = [];
    for (let i = 0; i < 24; i++) {
      startAtOpts.push(new TimeUnitValue(i.toString(), i));
      everyOpts.push(i + 1);
    }
    let label = 'hour';
    let property = 'hour';

    let everyTime = new EveryTimeUnitModel(label, startAtOpts, everyOpts);
    let everyTimeBetween = new EveryTimeUnitBetweenModel(label, 0, 23);
    let specificTime = new SpecificTimeUnitModel(label, 0, 23, model.hour);

    let html = (
      <div>
        <EveryUnitEditor label={label} model={model} property={property} onUpdateExpression={onUpdateExpression}/>
        <EveryNUnitEditor model={model} property={property} onUpdateExpression={onUpdateExpression}
          timeUnit={everyTime}/>
        <SpecificNUnitEditor model={model} property={property} onUpdateExpression={onUpdateExpression}
          timeUnit={specificTime}/>
        <EveryUnitBetweenEditor model={model} property={property} onUpdateExpression={onUpdateExpression}
          timeUnit={everyTimeBetween}/>
      </div>
    )
    return html;
  }
}
