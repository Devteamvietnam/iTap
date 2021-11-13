import React, { Component } from 'react';
import './App.scss';
import { Navbar } from 'components/Navbar';
//es6 class syntax
export class App extends Component {
  constructor(props: any) {
    super(props);
  }

  render() {
    return (
     <div>
        <header className="header" > 
        <Navbar />
        </header>
      <div className="App">
          <p>
            VION <code>App</code> Project
          </p>
      </div>
    </div>
    );
 }
}
