import React, { Component } from 'react';
import { app, widget, reactstrap } from 'components';

export interface WComponentProps extends widget.element.StoreableStateComponentProps {
  appContext: app.AppContext;
  pageContext: app.PageContext;
  readOnly?: boolean;
  laf?: string; // look and feel
}
export class WComponent<T extends WComponentProps = WComponentProps, S = any>
  extends widget.element.StoreableStateComponent<T, S> {
  /**@deprecated */
  popupPageContext: app.PageContext | null = null;

  loading: boolean = false;

  isLoading() { return this.loading; }

  markLoading(loading: boolean) {
    this.loading = loading;
    return this;
  }

  protected renderLoading() {
    let html = (
      <div className='flex-vbox'>
        <div className='mx-auto my-auto'>
          <reactstrap.Spinner color="primary" style={{ width: '5rem', height: '5rem' }} />
        </div>
      </div>
    );
    return html;
  }

  protected renderNoDataAvailable() {
    let html = (
      <div className='flex-vbox'>
        <div className='mx-auto mt-3'>
          <reactstrap.Alert color='info' style={{ fontSize: '2em' }}>
            No Data Available
          </reactstrap.Alert>
        </div>
      </div>
    );
    return html;
  }

  loadData(_forceReload: boolean = false) {
    this.forceUpdate();
  }

  reconfigure() {
  }

  hasReadCapability() {
    let { pageContext } = this.props;
    return pageContext.hasUserReadCapability();
  }

  public hasWriteCapability() {
    let { readOnly, pageContext } = this.props;
    if (readOnly === true) return false;
    return pageContext.hasUserWriteCapability();
  }

  hasModeratorCapability() {
    let { readOnly, pageContext } = this.props;
    if (readOnly === true) return false;
    return pageContext.hasUserModeratorCapability();
  }

  hasAdminCapability() {
    let { readOnly, pageContext } = this.props;
    if (readOnly === true) return false;
    return pageContext.hasUserAdminCapability();
  }

  canEditEntity(entity: any) {
    let state = entity.storageState;
    if (state != 'ACTIVE') {
      return this.hasModeratorCapability();
    }
    let editMode = entity.editMode;
    if (editMode == 'LOCKED') {
      return this.hasModeratorCapability();
    }
    return this.hasWriteCapability();
  }

  /**@deprecated */
  newPopupPageContext() {
    this.closePopupPageContext();
    this.popupPageContext = new app.PageContext(new widget.layout.DialogContext());
    return this.popupPageContext;
  }

  /**@deprecated */
  closePopupPageContext() {
    if (this.popupPageContext) {
      this.popupPageContext.onBack();
      this.popupPageContext = null;
    }
  }
}
export interface WToolbarProps {
  readOnly?: boolean;
  laf?: string; // look and feel
}
export class WToolbar extends Component<WToolbarProps> {
  render() {
    let { readOnly, laf, children } = this.props;
    if (readOnly) return <></>;

    if (!laf) laf = 'w-toolbar flex-hbox-grow-0 justify-content-end py-1 my-1';
    let html = (
      <div className={laf}> {children} </div>
    );
    return html;
  }
}