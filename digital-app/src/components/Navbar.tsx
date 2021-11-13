import React, { Component } from 'react';
import AvatarEditor from 'react-avatar-editor'

export class Navbar extends Component {
    constructor(props: any) {
        super(props);
      }

    render() {
        return (
            <nav className="navbar navbar-expand-lg navbar-light bg-light">
                <div className="container-fluid">
                    <a className="navbar-brand" href="#">Digital</a>
                    <button className="navbar-toggler" type="button" >
                    <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                        <li className="nav-item">
                        <a className="nav-link active" aria-current="page" href="">Home</a>
                        </li>
                        <li className="nav-item">
                        <a className="nav-link" target="_blank" href="https://github.com/ddthien-coder/vion">Project</a>
                        </li>
                    </ul>
                    <div className="d-flex">
                        <AvatarEditor
                            image="https://avatars.githubusercontent.com/u/55986641?s=400"
                            width={30}
                            height={20}
                            border={5}
                            color={[255, 255, 255, 0.6]}
                        />
                    </div>
                </div>
                </div>
            </nav>
        )
    }
}