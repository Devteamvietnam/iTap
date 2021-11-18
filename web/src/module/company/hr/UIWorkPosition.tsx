import React from 'react';
import { util, widget } from 'components';

import {
  WButtonEntityCommit, WButtonEntityReset, WToolbar, WEntityEditor,
} from 'core/widget';
import { VGridEntityListPlugin } from 'core/widget/vgrid';

import { HRWorkRestURL, T } from 'module/company/hr/Dependency';

const { FormContainer, FormGroupCol, Row, BBStringField } = widget.input;
const { Tab, TabPane } = widget.layout;

export class UIWorkPositionFormEditor extends WEntityEditor {

  render() {
    let { appContext, pageContext, observer, readOnly } = this.props;
    let writeCap = this.hasWriteCapability();
    let workPosition = observer.getMutableBean();

    let html = (
      <widget.component.VSplit>
        <widget.component.VSplitPane width={'60%'}>
          <FormContainer fluid>
            <Row>
              <FormGroupCol span={6} label={T('Label')}>
                <BBStringField bean={workPosition} field={'label'} disable={!writeCap} />
              </FormGroupCol>
              <FormGroupCol span={6} label={T('Code')}>
                <BBStringField bean={workPosition} field={'code'} disable={!writeCap} />
              </FormGroupCol>
              <FormGroupCol span={6} label={T('Basic Salary')}>
                <BBStringField bean={workPosition} field={'basicSalary'} disable={!writeCap} />
              </FormGroupCol>
              <FormGroupCol span={6} label={T('Performance Salary')}>
                <BBStringField bean={workPosition} field={'performanceSalary'} disable={!writeCap} />
              </FormGroupCol>
            </Row>
          </FormContainer>
          <TabPane>
          <div>Comming soon</div>
          </TabPane>
          <WToolbar>
            <WButtonEntityCommit
              appContext={appContext} pageContext={pageContext} hide={!writeCap} observer={observer}
              label={T('Add/Edit Work Position')}
              commitURL={HRWorkRestURL.position.save} />
            <WButtonEntityReset
              appContext={appContext} pageContext={pageContext} hide={!writeCap} observer={observer}
              onPostRollback={this.onPostRollback} />
          </WToolbar>
        </widget.component.VSplitPane>
        <widget.component.VSplitPane>
         <div>Comming soon</div>
        </widget.component.VSplitPane>
      </widget.component.VSplit>
    );
    return html;
  }
}
