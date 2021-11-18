import React, { Component } from "react";
import { widget } from "components";

import {
  WEntityEditorProps, WToolbar, WButtonEntityCommit, WButtonEntityReset, WEntityEditor
} from "core/widget";

interface UITreeGroupEditorProps extends WEntityEditorProps {
  putRestUrl: string;
}
/**@deprecated */
export class UITreeGroupEditor extends WEntityEditor<UITreeGroupEditorProps> {
  render() {
    let { appContext, pageContext, observer, putRestUrl, readOnly, onPostCommit } = this.props;
    let { FormGroup, BBStringField } = widget.input;
    let bean = observer.getMutableBean();
    let html = (
      <div className='flex-vbox'>
        <form className="form">
          <FormGroup>
            <label>Group Name</label>
            <BBStringField bean={bean} field={"name"} disable={!observer.isNewBean()} />
          </FormGroup>
          <FormGroup>
            <label>Group Label</label>
            <BBStringField bean={bean} field={"label"} />
          </FormGroup>
        </form>
        <WToolbar>
          <WButtonEntityCommit
            appContext={appContext} pageContext={pageContext} readOnly={readOnly} observer={observer}
            label={`Group ${bean.name}`} commitURL={putRestUrl}
            onPostCommit={(entity) => this.onPostCommit(entity)} />
          <WButtonEntityReset
            appContext={appContext} pageContext={pageContext} readOnly={readOnly} observer={observer} />
        </WToolbar>
      </div>
    );
    return html;
  }
}
