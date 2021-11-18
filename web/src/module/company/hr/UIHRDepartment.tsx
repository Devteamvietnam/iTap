import React from "react";
import { util, widget } from "components";

import {
  WEntityEditor, WToolbar, WButtonEntityCommit, WButtonEntityReset, UUIDTool
} from "core/widget";

import { T, HRRestURL } from "module/company/hr/Dependency";

const { toFileName } = util.text;
const {
  FormContainer, ColFormGroup, Row, BBStringField, BBTextField, BBCallableStringField
} = widget.input;

export class UIHRDepartmentEditor extends WEntityEditor {

  onGenerateName(label: string) {
    const { observer } = this.props;
    if (!observer.isNewBean()) return;
    let name = UUIDTool.generateWithAnyTokens(
      T("You need to enter the label"),
      [toFileName(label)],
      true
    );
    observer.getMutableBean().name = name;
    this.forceUpdate();
  }

  render() {
    let { appContext, pageContext, observer, onPostCommit, readOnly } = this.props;
    let bean = observer.getMutableBean();
    let writeCap = this.hasWriteCapability();

    return (
      <div className='flex-vbox'>
        <FormContainer fluid>
          <Row>
            <ColFormGroup span={12} label={T('Label')}>
              <BBStringField bean={bean} field={'label'} disable={!writeCap}
                onInputChange={(bean, field, oldVal, newVal) => this.onGenerateName(newVal)} />
            </ColFormGroup>
            <ColFormGroup span={12} label={T('Name')}>
              <BBCallableStringField bean={bean} field={'name'} disable={!observer.isNewBean() || !writeCap}
                onCall={() => this.onGenerateName(bean.label)} required />
            </ColFormGroup>
          </Row>
          <Row>
            <ColFormGroup span={12} label={T('Parent Path')}>
              <BBStringField bean={bean} disable={true} field={"parentPath"} />
            </ColFormGroup>
          </Row>
          <Row>
            <ColFormGroup span={12} label={T('Description')}>
              <BBTextField bean={bean} disable={readOnly} field={"description"} />
            </ColFormGroup>
          </Row>
        </FormContainer>
        <WToolbar>
          <WButtonEntityCommit
            appContext={appContext} pageContext={pageContext} readOnly={readOnly} observer={observer}
            label={T(`HR Department {{label}}`, { label: bean.label })} hide={readOnly}
            commitURL={HRRestURL.department.save}
            onPostCommit={onPostCommit} />
          <WButtonEntityReset
            appContext={appContext} pageContext={pageContext} readOnly={readOnly} observer={observer}
            onPostRollback={this.onPostRollback} hide={readOnly} />
        </WToolbar>
      </div>
    );
  }
}
