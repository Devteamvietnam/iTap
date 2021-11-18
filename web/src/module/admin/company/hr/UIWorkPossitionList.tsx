import React from 'react';
import { widget } from 'components'

import { VGridConfigTool, VGridEntityList } from 'core/widget/vgrid';
import { ComplexBeanObserver } from 'core/widget';

import {
  T, StorageState, HRDepartmentExplorerPlugin, HRWorkRestURL
} from 'module/company/hr/Dependency';
import { UIHRDepartmentWorkPossitonExplorer } from 'module/company/hr/UIHRDepartmentExplorer';
import { UIPositionListPageControl } from 'module/company/hr/UIWorkPossitionListControl';
import { UIWorkPositionFormEditor } from 'module/company/hr/UIWorkPosition';

import DisplayRecord = widget.grid.model.DisplayRecord;
import VGridConfig = widget.grid.VGridConfig;
import VGridContext = widget.grid.VGridContext;

export class UIWorkPositionListPlugin extends HRDepartmentExplorerPlugin {
  constructor() {
    super([]);

    this.searchParams = {
      "params": this.createDepartmentOptionalParams(),
      "filters": [
        ...widget.sql.createSearchFilter()
      ],
      "optionFilters": [
        widget.sql.createStorageStateFilter([StorageState.ACTIVE, StorageState.ARCHIVED])
      ],
      "rangeFilters": [
        ...widget.sql.createModifiedTimeFilter()
      ],
      "orderBy": {
        fields: ["code", "modifiedTime"],
        fieldLabels: ["Code", "Modified Time"],
        selectFields: [],
        sort: "DESC"
      },
      "maxReturn": 1000
    }
  }

  loadData(uiList: VGridEntityList<any>) {
    this.serverSearch(uiList, HRWorkRestURL.position.search);
  }
}

export class UIWorkPossitionList extends VGridEntityList {
  createVGridConfig() {
    let { plugin, readOnly, onMultiSelect, type } = this.props;
    readOnly = readOnly ? true : false;
    let allowSelector = !readOnly || onMultiSelect ? true : false;
    let addDbSearchFilter = plugin.searchParams ? true : false;

    let config: VGridConfig = {
      record: {
        fields: [
          ...VGridConfigTool.FIELD_SELECTOR(allowSelector),
          VGridConfigTool.FIELD_INDEX(),
          VGridConfigTool.FIELD_ON_SELECT('label', T('Label'), 250),

          { name: 'code', label: T('Code'), width: 250 },
          { name: 'basicSalary', label: T('Basic Salary'), width: 200 },
          { name: 'performanceSalary', label: T('Performance Salary'), width: 200 },
          ...VGridConfigTool.FIELD_ENTITY
        ]
      },
      toolbar: {
        actions: [
          ...VGridConfigTool.TOOLBAR_CHANGE_STORAGE_STATES(
            HRWorkRestURL.position.saveState, [StorageState.ACTIVE, StorageState.INACTIVE], readOnly
          )
        ],
        filters: VGridConfigTool.TOOLBAR_FILTERS(addDbSearchFilter),
      },
      control: {
        label: T('Departments'),
        width: 200,
        render: (ctx: VGridContext) => {
          let uiWorkPossitionList = ctx.uiRoot as UIWorkPossitionList;
          let { appContext, pageContext, plugin } = uiWorkPossitionList.props;
          let pluginImpl = plugin as UIWorkPositionListPlugin;
          return (
            <UIHRDepartmentWorkPossitonExplorer appContext={appContext} pageContext={pageContext}
              plugin={pluginImpl} context={ctx} />
          );
        }
      },
      footer: {
        page: {
          hide: type !== 'page',
          render: (ctx: VGridContext) => {
            let uiWorkPossitionList = ctx.uiRoot as UIWorkPossitionList;
            let { appContext, pageContext } = uiWorkPossitionList.props;
            const hasWriteCap = this.hasWriteCapability();
            return (
              <UIPositionListPageControl appContext={appContext} pageContext={pageContext} context={ctx}
                readOnly={!hasWriteCap} />
            );
          }
        },
        selector: VGridConfigTool.FOOTER_MULTI_SELECT(T("Select Work Posstion"), type)
      },
      view: {
        currentViewName: 'table',
        availables: {
          table: {
            viewMode: 'table'
          }
        },
      }
    }
    return config;
  }

  onDefaultSelect(dRecord: DisplayRecord) {
    let selectedWorkPosition = dRecord.record;
    let { appContext, pageContext, readOnly } = this.props;
    let successCB = (result: any) => {
      let workPosition = result.data;
      let observer = new ComplexBeanObserver(workPosition);
      let html = (
        <UIWorkPositionFormEditor
          appContext={appContext} pageContext={pageContext} observer={observer} readOnly={readOnly} />
      );
      pageContext.onAdd('work-position-detail', T(`Work Position : ${workPosition.code}`), html);
    }
    appContext.serverGET(HRWorkRestURL.position.load(selectedWorkPosition.code), {}, successCB);
  }

  filterByDepartment(department: any) {
    let { plugin } = this.props;
    let context = this.getVGridContext();
    context.withAttr('currentDepartment', department);
    let departmentName = department ? department.name : null;
    (plugin as UIWorkPositionListPlugin).onSelectDepartment(departmentName);
    this.reloadData();
  }
}