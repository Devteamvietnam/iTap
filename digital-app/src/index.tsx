import React from "react";
import ReactDOM from "react-dom";
import { BrowserRouter as Router, Switch, Route, Redirect } from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { App } from './App';
import './index.scss'

function AuthRoute() {
  let router = (
    <Router>
      <Switch>
        <Route exact path="/" component={App}/>
        <Route exact path="/app/admin" render={() => <Redirect to="/app/ws:_restore_" />} />
      </Switch>
    </Router>
  );
  return router;
}

ReactDOM.render(<AuthRoute />, document.getElementById('app'));
