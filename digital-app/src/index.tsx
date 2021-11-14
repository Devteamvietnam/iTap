import { app, React, ReactDOM } from "components";
import { BrowserRouter as Router, Switch, Route, Redirect } from "react-router-dom";
import * as apps from "app";
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { App } from './client/Home';
import './stylesheet.scss'

import { PrivateRoute, UILogin } from "app/auth/uiauth";
import UIHostApp from "app/host/UIHostApp";

import AdminAppRegistry = apps.admin.AdminAppRegistry;
import AppRegistryGroup  = app.host.AppRegistryGroup;
const AppRegistryManager = app.host.AppRegistryManager;

function AuthRoute() {
  let router = (
    <Router>
      <Switch>
        <Route exact path="/" component={App}/>
        <Route path="/admin/login/app" component={UILogin} />
        <Route exact path="/app/admin" render={() => <Redirect to="/app/ws:_restore_" />} />
        <PrivateRoute loginPath='admin/login/app' path="/app/ws:name/:app*" component={UIHostApp} />
      </Switch>
    </Router>
  );
  return router;
}

ReactDOM.render(<AuthRoute />, document.getElementById('app'));
