import React from 'react';
import { widget } from 'components';

import {
  WEntityEditor, WToolbar, WButtonEntityCommit, WButtonEntityReset
} from 'core/widget';

import { AccountRestURL, T } from './Dependency';
const { Form, FormGroup, BBStringField } = widget.input;

export class UIAccountGroupEditor extends WEntityEditor {

  render() {
    let { appContext, pageContext, observer } = this.props;
    let writeCap = this.hasWriteCapability();
    let accountGroup = observer.getMutableBean();
    return (
      <div className='flex-vbox'>
        <Form>
          <FormGroup label={T('Label')}>
            <BBStringField bean={accountGroup} field={'label'} required disable={!writeCap} />
          </FormGroup>
          <FormGroup label={T('Name')}>
            <BBStringField bean={accountGroup} field={'name'} required disable={!observer.isNewBean() || !writeCap} />
          </FormGroup>
          <FormGroup label={T('Path')}>
            <BBStringField bean={accountGroup} field={'path'} required disable={!observer.isNewBean() || !writeCap} />
          </FormGroup>
          <FormGroup label={T('Parent Path')}>
            <BBStringField bean={accountGroup} field={'parentPath'} required
              disable={observer.getMutableBean().parentPath || !writeCap} />
          </FormGroup>
          <FormGroup label={T('Description')}>
            <BBStringField bean={accountGroup} field={'description'} required disable={!writeCap} />
          </FormGroup>
        </Form>
        <WToolbar>
          <WButtonEntityCommit
            appContext={appContext} pageContext={pageContext} readOnly={!writeCap} observer={observer}
            label={T(`Account Group {{label}}`, { label: accountGroup.label })}
            commitURL={AccountRestURL.group.createChild(accountGroup.parentId)}
            onPostCommit={(entity) => this.onPostCommit(entity)} />
          <WButtonEntityReset
            appContext={appContext} pageContext={pageContext} readOnly={!writeCap} observer={observer}
            onPostRollback={(entity) => this.onPostRollback(entity)} />
        </WToolbar>
      </div>
    )
  }
}