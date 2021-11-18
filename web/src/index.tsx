import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router, Switch, Route, Redirect } from "react-router-dom";

import { PrivateRoute, UILogin } from "./app/webos/auth/uiauth";
import UIWebos from "./app/webos/UIWebos";

import "./stylesheet.scss";

function AuthRoute() {
  let router = (
    <Router>
      <Switch>
        <Route path="/login/app" component={UILogin} />
        <Route exact path="/" render={() => <Redirect to="/app/ws:_restore_" />} />
        <Route exact path="/app" render={() => <Redirect to="/app/ws:_restore_" />} />
        <PrivateRoute loginPath='/login/app' path="/app/ws:name/:app*" component={UIWebos} />
      </Switch>
    </Router>
  );
  return router;
}

ReactDOM.render(<AuthRoute />, document.getElementById('app'));
