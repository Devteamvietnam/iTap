import React, { Component } from 'react';

import { app, sample } from 'components'

import { BaseAppRegistry } from 'core/app/api';

export default class UIApplication extends Component<app.OSProps> {
  appContext: app.AppContext;

  constructor(props: any) {
    super(props);
    const { osContext } = props;
    this.appContext = new app.AppContext(this, osContext);
  }

  render() {
    let html = (
      <div className='ui-application d-flex h-100'>
        <sample.UISample appContext={this.appContext} />
      </div>
    );
    return html;
  }
}

export class AppRegistry extends BaseAppRegistry {
  module: string = 'sample';
  name: string = 'components';
  label: string = 'React Js Lib Sample';
  description: string = 'React Js Lib Sample';
  requiredAppCapability = app.ADMIN;

  createUI(ctx: app.OSContext) {
    return (<UIApplication osContext={ctx} />);
  };
}