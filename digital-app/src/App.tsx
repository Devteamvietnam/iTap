import React, { Component } from 'react';
import './App.scss';
//es6 class syntax
export class App extends Component {
  constructor(props: any) {
    super(props);
  }

  render() {
    return (
     <div>
        <header className="header" > 
        </header>
      <div className="App">
          <p>
            Digital <code>VFX</code> Project
          </p>
      </div>
    </div>
    );
 }
}
