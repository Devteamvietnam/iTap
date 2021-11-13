import { Component } from 'react';

import { storage } from 'components/widget/storage';
import { IDTracker } from 'components/util/common';
import { Rest, SuccessCallback, FailCallback } from 'components/server/rest';
import { Breadcumbs, ContentFactory, DialogContext, showDialog } from 'components/widget/layout'
import { NotificationMessage } from 'components/widget/util'
import { READ, WRITE, MODERATOR, ADMIN } from './permission';
import { IAppRegistry } from './AppRegistry';

export class ServerContext {
  hostname: string;
  rest: Rest;

  constructor(hostname: string, serverUrl: string, restUrl: string) {
    this.hostname = hostname;
    this.rest = new Rest(serverUrl, restUrl)
  }

  getRestClient() { return this.rest; }

  createServerURL(path: string, params: any = null) {
    return this.rest.createUrl(path, params);
  }
}

export class OSEvent {
  source: string;
  name: string;
  data: any;
  osContext: null | OSContext;

  constructor(source: string, name: string, data: any, osContext: null | OSContext) {
    this.source = source;
    this.name = name;
    this.data = data;
    this.osContext = osContext;
  }

  isTarget(pattern: string) {
    let regrex = new RegExp(pattern);
    return regrex.test(this.name);
  }
}

export class OSContext {
  appName: string = 'webos';
  appLabel: string = 'WebOS';
  serverCtx: ServerContext;
  servers: { [hostname: string]: ServerContext; } = {}
  event: null | OSEvent = null;
  uiOS: Component;

  constructor(uiOS: Component, ctx: ServerContext) {
    this.uiOS = uiOS;
    this.serverCtx = ctx;
  }

  getServerContext() { return this.serverCtx; }

  getEvent() { return this.event; }

  consumeEvent() {
    let event = this.event;
    this.event = null;
    return event;
  }

  setEvent(event: OSEvent) { this.event = event }

  broadcast(event: OSEvent) {
    this.event = event;
    this.uiOS.forceUpdate();
  }

  getWatchObject() {
    return {
      appName: this.appName, appLabel: this.appName, servers: this.servers, event: this.event
    }
  }
}

export interface OSProps {
  osContext: OSContext
}

export class AppEvent {
  source: string;
  name: string;
  data: any;
  appContext: null | AppContext;

  constructor(source: string, name: string, data: any, appContext: null | AppContext) {
    this.source = source;
    this.name = name;
    this.data = data;
    this.appContext = appContext;
  }
}

export class AppContext {
  appRegistry: IAppRegistry | null = null;
  osContext: OSContext;
  serverContext: ServerContext;
  event: null | AppEvent = null;
  uiApplication: Component;

  constructor(uiApp: Component, osContext: OSContext, serverCtx: null | ServerContext = null) {
    this.osContext = osContext;
    if (serverCtx) {
      this.serverContext = serverCtx;
    } else {
      this.serverContext = osContext.getServerContext();
    }
    this.uiApplication = uiApp;
  }

  getAppRegistry(): any {
    return this.appRegistry;
  }

  setAppRegistry(registry: IAppRegistry) { this.appRegistry = registry; }

  getOSContext() { return this.osContext; }

  getServerContext() { return this.serverContext; }

  getEvent() { return this.event; }

  consumeEvent() {
    let event = this.event;
    this.event = null;
    return event;
  }

  setEvent(event: AppEvent) { this.event = event }

  broadcast(event: AppEvent) {
    this.event = event;
    this.uiApplication.forceUpdate();
  }

  hasUserReadCapability(): boolean {
    if (!this.appRegistry) return false;
    let cap = this.appRegistry.getUserAppCapability();
    return cap.hasCapability(READ);
  }

  hasUserWriteCapability(): boolean {
    if (!this.appRegistry) return false;
    let cap = this.appRegistry.getUserAppCapability();
    return cap.hasCapability(WRITE);
  }

  hasUserModeratorCapability(): boolean {
    if (!this.appRegistry) return false;
    let cap = this.appRegistry.getUserAppCapability();
    return cap.hasCapability(MODERATOR);
  }

  hasUserAdminCapability(): boolean {
    if (!this.appRegistry) return false;
    let cap = this.appRegistry.getUserAppCapability();
    return cap.hasCapability(ADMIN);
  }

  addOSNotification(type: "success" | "info" | "warning" | "danger", label: string, detail?: string | null, cause?: any) {
    let stacktrace = '';
    if (cause) stacktrace = JSON.stringify(cause, null, 2);
    let data: NotificationMessage = { type: type, label: label, detail: detail, stacktrace: stacktrace };
    this.osContext.broadcast(new OSEvent('Application', 'os:notification:message', data, this.osContext));
  }

  serverGET(restUrl: string, params: any, successCB: SuccessCallback, failCB?: FailCallback) {
    let restClient = this.getServerContext().getRestClient();
    restClient.get(restUrl, params, successCB, failCB);
  }

  serverPOST(restUrl: string, params: any, successCB: SuccessCallback, failCB?: FailCallback) {
    let restClient = this.getServerContext().getRestClient();
    restClient.post(restUrl, params, successCB, failCB);
  }

  serverPUT(restUrl: string, params: any, successCB: SuccessCallback, failCB?: FailCallback) {
    let restClient = this.getServerContext().getRestClient();
    restClient.put(restUrl, params, successCB, failCB);
  }

  serverDELETE(restUrl: string, params: any, successCB: SuccessCallback, failCB?: FailCallback) {
    let restClient = this.getServerContext().getRestClient();
    restClient.delete(restUrl, params, successCB, failCB);
  }
}

export class PageContext {
  id = `page-${IDTracker.next()}`;
  dialogContext: null | DialogContext = null;
  breadcumbs: null | Breadcumbs = null;

  constructor(env: null | Breadcumbs | DialogContext = null) {
    if (env) {
      if (env instanceof DialogContext) {
        this.dialogContext = env;
      } else {
        this.breadcumbs = env;
      }
    }
  }

  withPopup() {
    if (this.breadcumbs || this.dialogContext) throw new Error('Breadcumbs or Dialog is already set');
    this.dialogContext = new DialogContext();
    return this;
  }

  clearPageStorage() { storage.pageClear(); }

  createStoreId(name: String) {
    return `${this.id}/${name}`
  }

  withBreadcumbs(breadcumbs: Breadcumbs) {
    //if (this.breadcumbs || this.dialogContext) throw new Error('Breadcumbs or Dialog is already set');
    this.breadcumbs = breadcumbs;
    return this;
  }

  getDialogContext() { return this.dialogContext; }

  /** @deprecated */
  onBack() {
    if (this.dialogContext) {
      this.dialogContext.getDialog().hide();
    } else if (this.breadcumbs) {
      this.breadcumbs.onBack();
    }
  }

  onClose() {
    if (this.dialogContext) {
      this.dialogContext.getDialog().hide();
    } else if (this.breadcumbs) {
      this.breadcumbs.onBack();
    }
  }

  onAdd(name: string, label: string, ui: any) {
    if (this.breadcumbs) {
      this.breadcumbs.push(name, label, ui);
    } else {
      showDialog(label, "md", ui);
    }
  }

  addContent(factory: ContentFactory) {
    if (this.breadcumbs) {
      this.breadcumbs.pushContent(factory);
    } else {
      showDialog(factory.label, "md", factory.createContent());
    }
  }
}

export interface ApplicationProps { appContext: AppContext }
export class UIBaseApplication extends Component<OSProps, {}> {
  state = { osEvent: null };

  constructor(props: any) {
    super(props);
  }
}