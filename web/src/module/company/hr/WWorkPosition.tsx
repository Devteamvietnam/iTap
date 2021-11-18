import React from "react";
import { widget } from 'components'

import {
  BeanObserver, EntityAutoCompletePlugin, BBEntityAutoComplete
} from 'core/widget';

import { T, HRWorkRestURL } from 'module/company/hr/Dependency';
import { UIWorkPositionFormEditor } from "module/company/hr/UIWorkPosition";
import { UIWorkPossitionList, UIWorkPositionListPlugin } from "module/company/hr/UIWorkPossitionList";

import BBAutoComplete = widget.input.BBAutoComplete2;

export class WorkPositionAutoCompletePlugin extends EntityAutoCompletePlugin {

  filter(pattern: string, onChangeCallback: (selOptions: any[]) => void) {
    let searchParams: widget.sql.SqlSearchParams = {
      "filters": [...widget.sql.createSearchFilter(pattern)],
      maxReturn: 100
    };
    this.doSearch(HRWorkRestURL.position.search, searchParams, onChangeCallback);
  }

  onShowMoreInfo(ui: BBAutoComplete, bean: any) {
    let callback = (result: any) => {
      let workPosition = result.data;
      let { disable } = ui.props;
      let observer: BeanObserver = new BeanObserver(workPosition);
      let uiContent = (
        <UIWorkPositionFormEditor appContext={this.appContext} pageContext={this.pageContext} observer={observer} readOnly={disable} />
      );
      ui.dialogShow(T('Work Position Info'), 'lg', uiContent);
    };
    let code = this.getRefLinkValue(ui, bean);
    this.doShowMoreInfo(ui, bean, HRWorkRestURL.position.load(code), callback);
  }

  onCustomSelect(ui: BBAutoComplete) {
    let onSelect = (workPosition: any) => {
      this.replaceWithSelect(ui, workPosition, workPosition.code);
      ui.onPostSelect(workPosition.code);
      ui.dialogClose();
    }
    let uiContent = (
      <div className='flex-vbox' style={{ height: 600 }}>
        <UIWorkPossitionList
          type={'selector'} plugin={new UIWorkPositionListPlugin()}
          appContext={this.appContext} pageContext={this.pageContext} readOnly={true}
          onSelect={(_appContext, _pageContext, selWorkPosition) => onSelect(selWorkPosition)} />
      </div>
    );
    ui.dialogShow(T('Select Work Position'), 'lg', uiContent);
  }

  onCreateNew(ui: BBAutoComplete) {
    let onPostCreate = (workPosition: any) => {
      this.closePopupPageContext();
      this.replaceWithSelect(ui, workPosition, workPosition.code);
    }
    let popupPageCtx = this.newPopupPageContext();
    let observer = new BeanObserver({});
    let uiContent = (
      <UIWorkPositionFormEditor appContext={this.appContext} pageContext={popupPageCtx}
        observer={observer} onPostCommit={onPostCreate} />);
    widget.layout.showDialog(T('Create New Work Position'), 'md', uiContent, popupPageCtx.getDialogContext());
  }
}

export class BBWorkPositionAutoComplete extends BBEntityAutoComplete {
  onPostSelect = (option: any, val: any) => {
    let { onPostSelect } = this.props;
    if (onPostSelect) onPostSelect(option, val);
    else this.forceUpdate();
  }

  render() {
    let { appContext, pageContext, disable, style, bean, field, useSelectBean, required } = this.props;
    let allowCreateNew = !disable && appContext.hasUserAdminCapability();
    let html = (
      <BBAutoComplete
        required={required}
        style={style}
        plugin={new WorkPositionAutoCompletePlugin(appContext, pageContext).withAllowCreateNew(allowCreateNew)}
        bean={bean} field={field} useSelectBean={useSelectBean}
        searchField={'code'} searchDescField={'label'} disable={disable}
        onPostSelect={this.onPostSelect} />
    );
    return html;
  }
}