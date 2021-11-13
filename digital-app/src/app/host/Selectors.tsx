import  { Component } from 'react';
import { i18n, app, widget, React } from 'components'

import { HostAppContext } from './HostAppContext'

// const T = i18n.getT([]);

interface WebosProps { webosContext: HostAppContext; }

export class LanguageSelector extends Component<WebosProps> {
  onInputChange = (_bean: any, _field: string, _oldVal: any, newVal: any) => {
    let { webosContext } = this.props;
    let osContext = webosContext.osContext;
    i18n.changeLanguage(newVal);
    osContext.broadcast(new app.OSEvent('Banner', 'webos:change:language', {}, osContext));
  }

  render() {
    let { BBRadioInputField } = widget.input;
    let opts = ['en', 'vn'];
    let optLabels = ['English', 'Vietnamese'];
    let bean = { select: i18n.getLanguage() };

    let html = (
      <div className='p-1'>
        <h6 className='border-bottom'>Languages</h6>
        <BBRadioInputField style={{ display: 'block', padding: '5px 10px' }}
          bean={bean} field={'select'} options={opts} optionLabels={optLabels} onInputChange={this.onInputChange} />
      </div>
    );
    return html;
  }
}