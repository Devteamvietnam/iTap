import React from 'react';
import { app, widget, server } from 'components';

import {
  ComplexBeanObserver, WToolbar, WButtonNewMembership, WButtonDeleteMembership, WButtonEntityNew
} from 'core/widget';
import { VGridComponent } from 'core/widget/vgrid';
import { AccountType } from 'module/account';

import {
  HRRestURL, T
} from "module/company/hr/Dependency";

import { UILoadableEmployeeAccountInfo } from 'module/company/hr/UIEmployee';
import { UIEmployeeListPlugin, UIEmployeeList } from 'module/company/hr/UIEmployeeList';
import { UINewEmployeeEditor } from 'module/company/hr/UINewEmployee';

export class UIEmployeeListPageControl extends VGridComponent {
  onNewEmployee() {
    let { context, appContext, pageContext } = this.props;
    let popupPageCtx = new app.PageContext().withPopup();
    let onPostCreate = (employee: any) => {
      popupPageCtx.onClose();
      let html = (
        <UILoadableEmployeeAccountInfo appContext={appContext} pageContext={pageContext} loginId={employee.loginId} />
      );
      pageContext.onAdd('new-employee-detail', T(`Employee {{loginId}}`, { loginId: employee.loginId }), html);
    }
    let selectedDepartment = context.getAttribute('currentDepartment');

    if (!selectedDepartment) {
      appContext.addOSNotification("danger", T(`Department is not selected!`));
    } else {
      let departmentIds = [selectedDepartment.id];
      let newEmployeeObserver = new ComplexBeanObserver({
        account: { accountType: AccountType.USER },
        departmentIds: departmentIds
      });

      let html = (
        <UINewEmployeeEditor appContext={appContext} pageContext={popupPageCtx}
          observer={newEmployeeObserver} onPostCommit={onPostCreate} />
      );
      widget.layout.showDialog(T('Add New Employee'), 'md', html, popupPageCtx.getDialogContext());
    }
  }

  onImportSuccess = () => {
    let { context } = this.props;
    let uiEmployeeList = context.uiRoot as UIEmployeeList;
    uiEmployeeList.reloadData();
  }

  onAddMembership() {
    let { context, appContext } = this.props;
    let uiEmployeeList = context.uiRoot as UIEmployeeList;
    let excludeRecords = uiEmployeeList.props.plugin.getRecords();
    let popupPageCtx = new app.PageContext().withPopup();
    let html = (
      <div className='flex-vbox' style={{ height: 600 }}>
        <UIEmployeeList
          appContext={appContext} pageContext={popupPageCtx} readOnly={true} type='selector'
          plugin={new UIEmployeeListPlugin().withExcludeRecords(excludeRecords)}
          onSelect={(_appCtx, pageCtx, account) => this.onMultiSelect(pageCtx, [account])}
          onMultiSelect={(_appCtx, pageCtx, accounts) => this.onMultiSelect(pageCtx, accounts)} />
      </div>
    )
    widget.layout.showDialog(T('Add Account Membership'), 'md', html, popupPageCtx.getDialogContext());
  }

  onDeleteMembership() {
    const { context, appContext } = this.props;
    let uiEmployeeList = context.uiRoot as UIEmployeeList;
    let plugin = uiEmployeeList.props.plugin;
    let selectedDepartment = context.getAttribute('currentDepartment');
    const selectEmployees = plugin.getListModel().getSelectedRecords()
    let employeeIds: Array<any> = [];
    for (let i = 0; i < selectEmployees.length; i++) {
      const employee = selectEmployees[i];
      employeeIds.push(employee.id);
    }
    if (employeeIds.length === 0) return;
    const successCB = (result: server.rest.RestResponse) => {
      plugin.getListModel().removeSelectedDisplayRecords();
      context.getVGrid().forceUpdateView();
      appContext.addOSNotification("success", T(`Delete Replations Success`));
    }
    appContext.serverDELETE(HRRestURL.department.relation(selectedDepartment.id), employeeIds, successCB);
  }

  onMultiSelect = (pageCtx: app.PageContext, employees: Array<any>) => {
    const { context, appContext } = this.props;
    let selectedDepartment = context.getAttribute('currentDepartment');
    let employeeIds = new Array<string>();
    for (let i = 0; i < employees.length; i++) {
      employeeIds.push(employees[i].id);
      pageCtx.onClose();
    }
    const successCB = (result: server.rest.RestResponse) => {
      appContext.addOSNotification("success", T(`Add Replations Success`));
      let uiEmployeeList = context.uiRoot as UIEmployeeList;
      uiEmployeeList.reloadData();
      uiEmployeeList.forceUpdate();
    }
    appContext.serverPUT(HRRestURL.department.relation(selectedDepartment.id), employeeIds, successCB);

  }

  render() {
    let { context, appContext, pageContext, readOnly } = this.props;
    let selectedDepartment = context.getAttribute('currentDepartment');
    const isSelectGroup = !selectedDepartment ? true : false;

    return (
      <WToolbar readOnly={readOnly}>
        <WButtonNewMembership
          appContext={appContext} pageContext={pageContext} hide={readOnly || isSelectGroup}
          label={T('Add Membership')}
          onClick={() => this.onAddMembership()} />
        <WButtonDeleteMembership
          appContext={appContext} pageContext={pageContext} hide={readOnly || isSelectGroup}
          label={T('Delete Membership')}
          onClick={() => this.onDeleteMembership()} />
        <WButtonEntityNew
          appContext={appContext} pageContext={pageContext} readOnly={readOnly}
          label={T('New Employee')} onClick={() => this.onNewEmployee()} />
      </WToolbar>
    )
  }
}