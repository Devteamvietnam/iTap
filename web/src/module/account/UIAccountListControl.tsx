import React from 'react';
import { app, widget, server } from 'components';

import { ExplorerActions, ComplexBeanObserver } from 'core/entity';
import {
  WToolbar, WButtonDeleteMembership, WButtonNewMembership, WButtonEntityNew
} from 'core/widget';
import {
  ExplorerConfig, VGridExplorer, VGridComponentProps, VGridComponent
} from 'core/widget/vgrid';

import { AccountRestURL, T } from './Dependency'
import { AccountType } from './model';
import { UIAccountList, UIAccountListPlugin } from './UIAccountList';
import { UIAccountGroupEditor } from './UIAccountGroup';
import { UILoadableAccountInfo } from './UIAccountInfo';
import { UINewAccountEditor } from './UINewAccount';

import TreeNode = widget.list.TreeNode;
import TreeModel = widget.list.TreeModel;

class AccountGroupTreeModel extends TreeModel {
  appContext: app.AppContext;

  constructor(appContext: app.AppContext, showRoot: boolean = true) {
    super(showRoot);
    this.appContext = appContext;
    let root = new widget.list.TreeNode(null, '', '', T('All Account Groups'), null, false);
    this.setRoot(root);
  }

  loadChildren(node: TreeNode, postLoadCallback?: (node: TreeNode) => void): any {
    let callbackHandler = (result: any) => {
      let children = result.data;
      if (children.length) {
        for (let i = 0; i < children.length; i++) {
          let childGroup = children[i];
          this.addChild(node, childGroup.path, childGroup.label, childGroup);
        }
      }
      node.setLoadedChildren();
      if (postLoadCallback) postLoadCallback(node);
    };
    let params = { parentPath: node.name };
    this.appContext.serverGET(AccountRestURL.group.findChildren, params, callbackHandler);
  }
}

interface UIAccountExplorerProps extends VGridComponentProps {
  plugin: UIAccountListPlugin;
}
export class UIAccountExplorer extends VGridExplorer<UIAccountExplorerProps> {
  createTreeModel() {
    return new AccountGroupTreeModel(this.props.appContext);
  }

  createConfig() {
    let explorerConfig: ExplorerConfig = {
      actions: [ExplorerActions.ADD, ExplorerActions.EDIT, ExplorerActions.DEL]
    }
    return this.createTreeConfig(explorerConfig);
  }

  onSelectNode(node: TreeNode) {
    let { context } = this.props;
    let uiAccountList = context.uiRoot as UIAccountList;
    uiAccountList.filterByGroup(node.userData);
    this.forceUpdate();
  }

  onEdit(node: TreeNode) {
    let { appContext } = this.props;
    let accountGroup = node.userData;
    let accountGroupObserver = new ComplexBeanObserver(accountGroup);
    if (accountGroup == null) {
      appContext.addOSNotification('danger', T("Cannot Edit the root"));
    } else {
      let popupPageCtx = new app.PageContext().withPopup();
      let onPostCommit = (group: any) => {
        popupPageCtx.onClose();
        node.label = group.label;
        node.userData = group;
        this.forceUpdate();
      }
      let html = (
        <UIAccountGroupEditor
          appContext={appContext} pageContext={popupPageCtx} observer={accountGroupObserver}
          onPostCommit={onPostCommit} />
      )
      widget.layout.showDialog(T("Edit Account Group"), "md", html, popupPageCtx.getDialogContext());
    }
  }

  onAdd(node: TreeNode) {
    let { appContext } = this.props;
    let parentPath = node.userData?.path;
    let parentId = node.userData?.id;
    let accountGroup = { parentPath: parentPath, parentId: parentId }
    let observer = new ComplexBeanObserver(accountGroup);
    let popupPageCtx = new app.PageContext().withPopup();

    let onPostCommit = (group: any) => {
      popupPageCtx.onClose();
      node.addChild(group.name, group.label, group, false);
      this.forceUpdate();
    }
    let html = (
      <UIAccountGroupEditor
        appContext={appContext} pageContext={popupPageCtx} observer={observer} onPostCommit={onPostCommit} />
    );
    widget.layout.showDialog(T("Add New Account Group"), "md", html, popupPageCtx.getDialogContext());
  }

  onDel(node: TreeNode) {
    let { appContext } = this.props;
    let group = node.userData;
    let callback = (_response: server.rest.RestResponse) => {
      appContext.addOSNotification('success', T('Delete Account Group Success!'));
      this.model.removeNode(node);
      this.forceUpdate();
    }
    appContext.serverDELETE(AccountRestURL.group.delete(group.id), {}, callback)
  }
}

export class UIAccountListPageControl extends VGridComponent {
  onNewAccount() {
    let { context, appContext, pageContext } = this.props;
    let popupPageCtx = new app.PageContext().withPopup();
    let onPostCreate = (newAccountModel: any) => {
      const account = newAccountModel.account;
      popupPageCtx.onClose();
      let html = (
        <UILoadableAccountInfo appContext={appContext} pageContext={pageContext}
          loginId={account.loginId} />
      );
      pageContext.onAdd('account-detail', T(`Account {{loginId}}`, { loginId: account.loginId }), html);
    }
    let selectedGroup = context.getAttribute('currentGroup');
    let accountGroupPaths = selectedGroup ? [selectedGroup.path] : '';
    let observer = new ComplexBeanObserver({
      account: { accountType: AccountType.USER },
      accountGroupPaths: accountGroupPaths
    });
    let html = (
      <UINewAccountEditor appContext={appContext} pageContext={popupPageCtx} observer={observer}
        commitURL={AccountRestURL.account.create} onPostCommit={onPostCreate} />
    );
    widget.layout.showDialog(T("Add New Account"), 'md', html, popupPageCtx.getDialogContext());
  }

  onImportSuccess = () => {
    let { context } = this.props;
    let uiAccountList = context.uiRoot as UIAccountList;
    uiAccountList.reloadData();
  }

  onAddMembership() {
    let { context, appContext } = this.props;
    let uiAccountList = context.uiRoot as UIAccountList;
    let excludeRecords = uiAccountList.props.plugin.getRecords();
    let popupPageCtx = new app.PageContext().withPopup();
    let html = (
      <div className='flex-vbox' style={{ height: 600 }}>
        <UIAccountList
          appContext={appContext} pageContext={popupPageCtx} readOnly={true} type='selector'
          plugin={new UIAccountListPlugin().withExcludeRecords(excludeRecords)}
          onSelect={(_appCtx, pageCtx, account) => this.onMultiSelect(pageCtx, [account])}
          onMultiSelect={(_appCtx, pageCtx, accounts) => this.onMultiSelect(pageCtx, accounts)} />
      </div>
    )
    widget.layout.showDialog(T('Add Account Membership'), 'md', html, popupPageCtx.getDialogContext());
  }

  onDeleteMembership() {
    const { context, appContext } = this.props;
    let uiAccountList = context.uiRoot as UIAccountList;
    let plugin = uiAccountList.props.plugin;
    let selectedGroup = context.getAttribute('currentGroup');
    const selectedAccounts = plugin.getListModel().getSelectedRecords()
    let accountLoginIds = new Array<string>();
    for (let i = 0; i < selectedAccounts.length; i++) {
      const account = selectedAccounts[i];
      accountLoginIds.push(account.loginId);
    }
    if (accountLoginIds.length === 0) return;
    const successCB = (result: server.rest.RestResponse) => {
      plugin.getListModel().removeSelectedDisplayRecords();
      context.getVGrid().forceUpdateView();
      appContext.addOSNotification("success", T(`Delete Memberships Success`));
    }
    appContext.serverDELETE(AccountRestURL.group.membership(selectedGroup.id), accountLoginIds, successCB);
  }

  onMultiSelect = (pageCtx: app.PageContext, accounts: Array<any>) => {
    const { context, appContext } = this.props;
    let currentGroup = context.getAttribute('currentGroup');
    let accountLoginIds = new Array<string>();
    for (let i = 0; i < accounts.length; i++) {
      const account = accounts[i];
      accountLoginIds.push(account.loginId);
      pageCtx.onClose();
    }
    const successCB = (result: server.rest.RestResponse) => {
      appContext.addOSNotification("success", T(`Add Memberships Success`));
      let uiAccountList = context.uiRoot as UIAccountList;
      uiAccountList.reloadData();
      context.getVGrid().forceUpdateView();
    }
    appContext.serverPUT(AccountRestURL.group.membership(currentGroup.id), accountLoginIds, successCB);
  }

  render() {
    let { context, appContext, pageContext, readOnly } = this.props;
    let selectedGroup = context.getAttribute('currentGroup');
    const isSelectGroup = !selectedGroup ? true : false;

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
          label={T('New Account')} onClick={() => this.onNewAccount()} />
      </WToolbar>
    )
  }
}
