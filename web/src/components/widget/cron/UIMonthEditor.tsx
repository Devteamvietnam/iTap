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

type UIMonthEditorProps = {
  model: CronExpressionModel,
  onUpdateExpression: () => void
}
type UIMonthEditorState = { }
export default class UIMonthEditor extends React.Component<UIMonthEditorProps, UIMonthEditorState> {

  render() {
    let {model, onUpdateExpression} = this.props;
    let startAtOpts = [
      new TimeUnitValue("Jan", 1), new TimeUnitValue("Feb", 2), new TimeUnitValue("Mar", 3),
      new TimeUnitValue("Apr", 4), new TimeUnitValue("May", 5), new TimeUnitValue("Jun", 6),
      new TimeUnitValue("Jul", 7), new TimeUnitValue("Aug", 8), new TimeUnitValue("Sep", 9),
      new TimeUnitValue("Oct", 10), new TimeUnitValue("Nov", 11), new TimeUnitValue("Dec", 12)
    ];
    let everyOpts = [];

    for (let i = 1; i <= 12; i++) {
      everyOpts.push(i);
    }

    let label = 'month';
    let property = 'month';

    let everyTime = new EveryTimeUnitModel(label, startAtOpts, everyOpts);
    let everyTimeBetween = new EveryTimeUnitBetweenModel(label, 1, 12);
    let specificTime = new SpecificTimeUnitModel(label, 1, 12, model.month);

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
