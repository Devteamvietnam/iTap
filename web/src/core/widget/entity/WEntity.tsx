import React, { ReactFragment } from 'react';

import { app, widget, util, server, reactstrap } from 'components';

import { T } from '../Dependency'
import { IBeanObserver, BeanObserver, ComplexBeanObserver } from 'core/entity';
import { WComponent, WComponentProps } from 'core/widget/WLayout';

import ButtonActionModel = widget.element.ButtonActionModel;

export type EntityOnSelect = (_appCtx: app.AppContext, pageCtx: app.PageContext, entity: any) => void
export type EntityOnMultiSelect = (appCtx: app.AppContext, pageCtx: app.PageContext, entities: Array<any>) => void;
export type EntityOnPreCommit = (entity: any, uiEditor?: WComponent) => void;
export type EntityOnPostCommit = (entity: any, uiEditor?: WComponent) => void;
export type EntityOnPostRollback = (entity: any, uiEditor?: WComponent) => void;
export type EntityOnModify = (bean: any, field: string, oldVal: any, newVal: any) => void

export interface WEntityProps extends WComponentProps {
  observer: BeanObserver;
  onModify?: EntityOnModify
}
export class WEntity<T extends WEntityProps = WEntityProps> extends WComponent<T> { }

export interface WEntityEditorProps extends WEntityProps {
  onPostCommit?: EntityOnPostCommit;
  onPostRollback?: EntityOnPostRollback;
}
export class WEntityEditor<T extends WEntityEditorProps = WEntityEditorProps, S = any> extends WComponent<T, S> {
  onPostCommit = (entity: any) => {
    let { onPostCommit } = this.props;
    if (onPostCommit) {
      onPostCommit(entity, this);
    } else {
      this.forceUpdate();
    }
  }

  onPostRollback = (entity: any) => {
    let { onPostRollback } = this.props;
    if (onPostRollback) {
      onPostRollback(entity);
    } else {
      this.forceUpdate();
    }
  }
}

export interface WComplexEntityProps extends WComponentProps {
  observer: ComplexBeanObserver;
  onModify?: EntityOnModify;
}
export class WComplexEntity<T extends WComplexEntityProps = WComplexEntityProps, S = any> extends WComponent<T, S> {
}

export interface WComplexEntityEditorProps extends WComplexEntityProps {
  observer: ComplexBeanObserver;
  onPostCommit?: EntityOnPostCommit;
  onPostRollback?: EntityOnPostRollback;
}
export class WComplexEntityEditor<T extends WComplexEntityEditorProps = WComplexEntityEditorProps>
  extends WComponent<T> {

  viewId = `view-${util.IDTracker.next()}`;

  nextViewId() {
    this.viewId = `view-${util.IDTracker.next()}`;
  }

  onPostCommit = (entity: any) => {
    let { onPostCommit } = this.props;
    this.nextViewId();
    if (onPostCommit) {
      onPostCommit(entity, this);
    } else {
      this.forceUpdate();
    }
  }

  onPostRollback = (entity: any) => {
    let { onPostRollback } = this.props;
    this.nextViewId();
    if (onPostRollback) {
      onPostRollback(entity, this);
    } else {
      this.forceUpdate();
    }
  }
}

export interface WLoadableEntityProps extends WComponentProps {
  loadUrl: string;
}
export abstract class WLoadableEntity extends WComponent<WLoadableEntityProps> {
  entity: any = null;

  constructor(props: WLoadableEntityProps) {
    super(props);

    let callback = (response: server.rest.RestResponse) => {
      this.entity = response.data;
      this.forceUpdate();
    }
    let { appContext, loadUrl } = props;
    appContext.serverGET(loadUrl, null, callback);
  }

  abstract renderEntity(entity: any): ReactFragment;

  render() {
    if (!this.entity) return this.renderLoading();
    return this.renderEntity(this.entity);
  }
}

export interface WDetailAutoCompleteProps extends WComponentProps {
  bean: any;
  field: string;
  validators?: Array<util.validator.Validator>;
  useSelectBean?: boolean;
  allowEmpty?: boolean;
  onPostSelect?: (selectedBean: any) => void;
}
export class WDetailAutoComplete extends WComponent<WDetailAutoCompleteProps> {
}

interface WButtonEntityWriteProps extends WComponentProps {
  label?: string;
  onClick: () => void;
  color?: 'primary' | 'secondary' | 'link';
  icon?: widget.fa.FAIconDefinition;
  hide?: boolean;
}
export class WButtonEntityWrite extends WComponent<WButtonEntityWriteProps> {
  render() {
    let { onClick, label, icon, color, hide } = this.props;
    if (hide) return (null);
    if (!color) color = 'primary';
    if (!icon) icon = widget.fa.fas.faSpinner;
    const { FAButton } = widget.fa;
    let html = (<FAButton size='sm' icon={icon} color={color} onClick={onClick}> {label}</FAButton>);
    return html;
  }
}

interface WButtonEntityCommitProps extends WComponentProps {
  observer: IBeanObserver;
  btnLabel?: string;
  label: string;
  commitURL: string;
  createURL?: string;
  hide?: boolean;
  onPreCommit?: (observer: IBeanObserver) => void;
  onPostCommit?: (observer: IBeanObserver) => void;
}
export class WButtonEntityCommit extends WComponent<WButtonEntityCommitProps> {
  onCommit() {
    let { appContext, observer, commitURL, createURL, label, onPreCommit, onPostCommit } = this.props;
    let errorCollector = observer.getErrorCollector();
    if (errorCollector.getCount() > 0) {
      widget.layout.showNotification("danger", "Errors", `Have ${errorCollector.count} errors`);
      return;
    }
    let successCB = (result: any) => {
      let entity = result.data;
      appContext.addOSNotification("success", T('Save {{label}} Success', { label: label }));
      observer.replaceWith(entity);
      if (onPostCommit) onPostCommit(entity);
    }
    let failCB = (_result: any) => {
      appContext.addOSNotification('danger', T('Save {{label}} Fail', { label: label }), null, { label: label });
    }
    if (onPreCommit) {
      try {
        onPreCommit(observer);
      } catch (error: any) {
        return;
      }
    }
    let restURL = commitURL;
    if (observer.isNewBean() && createURL) restURL = createURL;

    let entity = observer.commitAndGet();
    appContext.serverPUT(restURL, entity, successCB, failCB);
  }

  render() {
    let { observer, hide, btnLabel } = this.props;
    if (hide) return (null);

    const { FAButton } = widget.fa;
    const { faSave } = widget.fa.far;
    if (!btnLabel) btnLabel = observer.isNewBean() ? T('Create') : T('Save');
    return (
      <FAButton size='sm' icon={faSave} color="primary" onClick={() => this.onCommit()}>{btnLabel}</FAButton>
    );
  }
}

interface WButtonEntityResetProps extends WComponentProps {
  observer: IBeanObserver;
  onPostRollback?: (entity: any) => void;
  color?: 'primary' | 'secondary' | 'link';
  hide?: boolean;
}
export class WButtonEntityReset extends WComponent<WButtonEntityResetProps> {
  onRollBack() {
    let { observer, onPostRollback } = this.props;
    observer.rollback();
    if (onPostRollback) {
      onPostRollback(observer.getMutableBean());
    } else {
      this.forceUpdate();
    }
  }

  render() {
    let { hide, color } = this.props;
    if (hide) return (null);
    if (!color) color = 'primary';
    const { FAButton } = widget.fa;
    const { faSyncAlt } = widget.fa.fas;
    let html = (
      <FAButton size='sm' icon={faSyncAlt} color={color} onClick={() => this.onRollBack()}>
        {T('Reset')}
      </FAButton>
    );
    return html;
  }
}

interface WButtonEntityReadProps extends WComponentProps {
  label?: string;
  onClick: () => void;
  color?: 'primary' | 'secondary' | 'link';
  icon?: widget.fa.FAIconDefinition;
  hide?: boolean;
}
export class WButtonEntityRead extends WComponent<WButtonEntityReadProps> {
  render() {
    let { onClick, label, icon, color, hide } = this.props;
    if (hide) return (null);
    if (!color) color = 'primary';
    const { FAButton } = widget.fa;
    let html = (
      <FAButton size='sm' icon={icon} color={color} onClick={onClick}> {label}</FAButton>
    );
    return html;
  }
}

export class WButtonNewMembership extends WComponent<WButtonEntityWriteProps> {
  render() {
    return <WButtonEntityWrite {...this.props} icon={widget.fa.fas.faUsers} />
  }
}

export class WButtonDeleteMembership extends WComponent<WButtonEntityWriteProps> {
  render() {
    return <WButtonEntityWrite {...this.props} icon={widget.fa.fas.faMinus} />
  }
}
export class WButtonEntityNew extends WComponent<WButtonEntityWriteProps> {
  render() {
    return <WButtonEntityWrite {...this.props} icon={widget.fa.fas.faPlus} />
  }
}

export class WButtonEntityClone extends WComponent<WButtonEntityWriteProps> {
  render() {
    let { label } = this.props;
    if (!label) label = T('Clone');
    return <WButtonEntityWrite {...this.props} label={label} icon={widget.fa.fas.faClone} />
  }
}

export class WButtonEntityPrint extends WComponent<WButtonEntityWriteProps> {
  render() {
    let { label } = this.props;
    if (!label) label = T('Print');
    return <WButtonEntityRead {...this.props} label={label} icon={widget.fa.fas.faPrint} />
  }
}

export class WButtonEntityEmail extends WComponent<WButtonEntityWriteProps> {
  render() {
    let { label } = this.props;
    if (!label) label = T('Email');
    return <WButtonEntityRead {...this.props} label={label} icon={widget.fa.fas.faPrint} />
  }
}

export class WButtonEntityGoTo extends WComponent<WButtonEntityWriteProps> {
  render() {
    return <WButtonEntityRead {...this.props} icon={widget.fa.fas.faArrowCircleRight} />
  }
}
interface WButtonEntityStorageProps extends WButtonEntityCommitProps {
  states: Array<'ACTIVE' | 'ARCHIVED' | 'DEPRECATED' | 'JUNK'>;
  hide?: boolean;
}
export class WButtonEntityStorage extends WComponent<WButtonEntityStorageProps> {
  onChangeState(newState: any) {
    let { appContext, observer, commitURL, label, onPostCommit } = this.props;
    let entity = observer.getMutableBean();
    let successCB = (_result: server.rest.RestResponse) => {
      entity['entityState'] = newState;
      appContext.addOSNotification("success", T('Change Storage State {{label}} Success', { label: label }));
      if (onPostCommit) onPostCommit(entity);
    }
    let failCB = (result: any) => {
      appContext.addOSNotification('danger', T('Change Storage State {{label}} Fail', { label: label }), null, { label: label });
    }
    let changeStorageReq = { entityIds: [entity.id], newStorageState: newState };
    appContext.serverPUT(commitURL, changeStorageReq, successCB, failCB);
  }

  render() {
    let { observer, states, hide: disable } = this.props;
    let entity = observer.getMutableBean();
    let currState = entity.storageState;
    if (disable) {
      return <reactstrap.Button color='primary' disabled>{currState}</reactstrap.Button>
    };
    let { DropdownActionButton } = widget.element;
    let thisUI = this;
    let actionItems: Array<ButtonActionModel> = [];
    for (let state of states) {
      if (currState == state) continue;

      actionItems.push(
        {
          name: state, label: state, size: 'sm',
          onSelect(item: ButtonActionModel, context?: any) { thisUI.onChangeState(state); }
        }
      );
    }
    return (<DropdownActionButton items={actionItems} label={currState} color={'primary'} />);
  }
}