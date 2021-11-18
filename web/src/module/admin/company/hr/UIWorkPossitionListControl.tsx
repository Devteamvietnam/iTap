import React from 'react';

import { ComplexBeanObserver, WToolbar, WButtonEntityNew } from 'core/widget';
import { VGridComponent } from 'core/widget/vgrid';

import { T } from "module/admin/company/hr//Dependency";
import { UIWorkPositionFormEditor } from 'module/admin/company/hr/UIWorkPosition';

export class UIPositionListPageControl extends VGridComponent {
  onNewWorkPosition() {
    let { appContext, pageContext, readOnly } = this.props;
    let workPosition = {};
    let observer = new ComplexBeanObserver(workPosition);
    let html = (
      <UIWorkPositionFormEditor
        appContext={appContext} pageContext={pageContext} observer={observer} readOnly={readOnly} />
    )
    pageContext.onAdd('work-position-detail', T('New Work Position'), html);
  }

  render() {
    let { context, appContext, pageContext, readOnly } = this.props;
    let selectedGroup = context.getAttribute('currentGroup');
    const isSelectGroup = !selectedGroup ? true : false;
    const hasWriteCap = context.uiRoot
    return (
      <WToolbar readOnly={readOnly}>
        <WButtonEntityNew
          appContext={appContext} pageContext={pageContext} readOnly={readOnly}
          label={T('New Work Posstion')} onClick={() => this.onNewWorkPosition()} />
      </WToolbar>
    )
  }
}