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

type UISecondEditorProps = {
  model: CronExpressionModel,
  onUpdateExpression: () => void
}
type UISecondEditorState = { }
export default class UISecondEditor extends React.Component<UISecondEditorProps, UISecondEditorState> {

  render() {
    let {model, onUpdateExpression} = this.props;
    let startAtOpts = [];
    let everyOpts = [];
    for (let i = 0; i < 60; i++) {
      startAtOpts.push(new TimeUnitValue(i.toString(), i));
      everyOpts.push(i + 1);
    }
    let label = 'second';
    let property = 'second';

    let everyTime = new EveryTimeUnitModel(label, startAtOpts, everyOpts);
    let everyTimeBetween = new EveryTimeUnitBetweenModel(label, 0, 59);
    let specificTime = new SpecificTimeUnitModel(label, 0, 59, model.second);

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
