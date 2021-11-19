import React from 'react';
import logo from './logo.svg';
import './App.scss';
export default class App extends React.Component {
  render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <p>
            Digital <code>VFX</code> Project
          </p>
        </header>
      </div>
    );
  }
}
