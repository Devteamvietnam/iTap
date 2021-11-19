import React from 'react';
import logo from './logo.svg';
import './App.scss';

export class App extends React.Component<any, any> {
  constructor(props: any) {
    super(props);
    this.state = {
      name: "Digital"
    };
    
  }
  handleChangeName(event: any) {
    this.setState({ name: event.target.value });
  }
  render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <input type="text" value={this.state.name} onChange={this.handleChangeName.bind(this)} />
          <p>
            {this.state.name} <code>VFX</code> Project
          </p>
        </header>
      </div>
    );
  }
}
