import React from "react";
import { Route, Redirect } from "react-router-dom";
import { widget } from "components";

import { session } from "core/app/session";

type PrivateRouteProps = { loginPath: string, path: any, component: any }
type PrivateRouteState = { tryAutoSignin: boolean }
export class PrivateRoute extends React.Component<PrivateRouteProps, PrivateRouteState> {
  state = { tryAutoSignin: false }

  tryAutoSigninRender() {
    let successCallback = (_accountACL: any) => {
      this.forceUpdate();
    };
    let failCallback = () => {
      this.setState({ tryAutoSignin: true })
    };
    session.autoSignin(successCallback, failCallback);
    return (
      <div className='mx-auto' style={{ marginTop: 100, fontWeight: 'bold', fontSize: '3rem' }}>
        Trying Auto Signin....
      </div>
    );
  }

  render() {
    if (session.authenticated) {
      let { component: Component, ...rest } = this.props;
      let render = (props: any) => {
        return (<Component {...props} />);
      };
      return (<Route {...rest} render={render} />);
    }

    let { tryAutoSignin } = this.state;
    if (!tryAutoSignin) {
      return this.tryAutoSigninRender();
    }

    let { loginPath } = this.props;
    let { component: Component, ...rest } = this.props;
    let render = (props: any) => {
      if (session.authenticated) return (<Component {...props} />);
      else return (<Redirect to={{ pathname: loginPath, state: { from: props.location } }} />);
    };
    return (<Route {...rest} render={render} />);
  }
}

type UILoginProps = { location?: any }
type UILoginState = { redirectToReferrer: any }
export class UILogin extends React.Component<UILoginProps, UILoginState> {
  state = { redirectToReferrer: false };

  onLogin(loginModel: any) {
    let successCallback = (_accountACL: any) => {
      this.setState({ redirectToReferrer: true });
    };
    session.signin(loginModel, successCallback);
  };

  onLoginByShortcutKey(winput: widget.input.WInput, _evt: any, keyCode: number, currInput: any, model: any) {
    if (keyCode && keyCode === 13) {
      winput.updateValue(currInput);
      this.onLogin(model);
    }
  }

  render() {
    const { from } = this.props.location.state || { from: { pathname: "/" } };
    const { redirectToReferrer } = this.state;

    if (redirectToReferrer) return <Redirect to={from} />;

    let model = { loginId: '', password: '', validFor: 7 };

    const { FormContainer, ColFormGroup, Row } = widget.input;
    const { BBStringField, BBRadioInputField, BBPasswordField } = widget.input;
    const { FAButton, FAIcon, fas } = widget.fa;
    let validForOpts = [1, 7, 30];
    let validForOptLabels = ['1 Day', '7 Days', '30 Days'];
    let html = (
      <div style={{ height: '100%' }}>
        <div className='ui-login mx-auto' style={{ marginTop: 100, width: 500 }}>
          <div className="ui-card">
            <div className="header">
              <h4>Login</h4>
              <FAIcon size='2x' icon={fas.faSignOutAlt} />
            </div>
            <FormContainer fluid className='py-2 px-5'>
              <Row>
                <ColFormGroup span={12} className='mt-2 mb-1'>
                  <label>Login Id</label>
                  <BBStringField
                    bean={model} field={'loginId'}
                    onKeyDown={(winput: widget.input.WInput, e: any, keyCode: number, currInput: any) => this.onLoginByShortcutKey(winput, e, keyCode, currInput, model)} />
                </ColFormGroup>
              </Row>

              <Row>
                <ColFormGroup span={12} className='mt-2 mb-1'>
                  <label>Password</label>
                  <BBPasswordField bean={model} field={'password'}
                    onKeyDown={
                      (winput: widget.input.WInput, e: any, keyCode: number, currInput: any) => {
                        this.onLoginByShortcutKey(winput, e, keyCode, currInput, model);
                      }
                    }
                  />
                </ColFormGroup>
              </Row>

              <Row>
                <ColFormGroup span={12} className='mt-2 mb-1 flex-hbox-grow-0 justify-content-between'>
                  <div className='flex-hbox-grow-0'>
                    <label className='text-nowrap mr-2'>Valid For:</label>
                    <BBRadioInputField
                      bean={model} field={'validFor'} options={validForOpts} optionLabels={validForOptLabels} />
                  </div>
                  {/* <Link to=''>Forgot Password</Link> */}
                </ColFormGroup>
              </Row>
            </FormContainer>

            <div className='footer'>
              <FAButton size='md' color='primary' icon={fas.faSignInAlt} onClick={() => this.onLogin(model)}>Submit</FAButton>
            </div>
          </div>
        </div>
        <div className='d-flex justify-content-end mt-1' style={{ opacity: 0.2 }}>Build: 1.0.0</div>
      </div>
    );
    return html;
  }
}