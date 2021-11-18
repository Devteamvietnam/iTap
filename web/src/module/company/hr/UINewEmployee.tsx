import React from 'react'
import { widget, app } from 'components'

import {
  WComplexEntityEditor, ComplexBeanObserver
} from 'core/widget';

import {
  UINewAccountEditor, UIConvertAccountEditor, UINewAccountEditorPlugin
} from "module/account";
import { T, HRRestURL } from 'module/company/hr/Dependency'

export class UINewEmployeeAccountEditorPlugin extends UINewAccountEditorPlugin {
  createAdditionalTabs(_appCtx: app.AppContext, _pageCtx: app.PageContext, _observer: ComplexBeanObserver) {
    const { Tab } = widget.layout;
    let tabs = [
      <Tab className='p-2' name='test' label='Test'>
        This is a test
      </Tab>
    ];
    return tabs;
  }
}
export class UINewEmployeeEditor extends WComplexEntityEditor {
  render() {
    let { appContext, pageContext, observer, onPostCommit } = this.props;
    let plugin = new UINewEmployeeAccountEditorPlugin();
    const { TabPane, Tab } = widget.layout;
    let html = (
      <TabPane>
        <Tab className='p-2' name='new' label={T('New')} active>
          <UINewAccountEditor
            plugin={plugin} appContext={appContext} pageContext={pageContext}
            label={T('New Employee')} observer={observer}
            commitURL={HRRestURL.employee.create}
            onPostCommit={onPostCommit} />
        </Tab>
        <Tab className='p-2' name='convert' label={T('Convert')}>
          <UIConvertAccountEditor
            plugin={plugin} appContext={appContext} pageContext={pageContext}
            label={T('Convert An Account To Employee')} observer={observer}
            commitURL={HRRestURL.employee.create}
            onPostCommit={onPostCommit} />
        </Tab>
      </TabPane>
    );
    return html;
  }
}