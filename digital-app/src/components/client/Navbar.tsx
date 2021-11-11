import React, { Component } from 'react';

export class Navbar extends Component {
    constructor(props: any) {
        super(props);
      }

    render() {
        return (
            <nav className="navbar navbar-expand-lg navbar-light bg-light">
                <div className="container-fluid">
                    <a className="navbar-brand" href="#">B-Digital</a>
                    <button className="navbar-toggler" type="button" >
                    <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                        <li className="nav-item">
                        <a className="nav-link active" aria-current="page" href="">Home</a>
                        </li>
                        <li className="nav-item">
                        <a className="nav-link active" aria-current="page" href="">Showreel</a>
                        </li>
                        <li className="nav-item">
                        <a className="nav-link active" aria-current="page" href="">About</a>
                        </li>
                        <li className="nav-item">
                        <a className="nav-link active" aria-current="page" href="">Contact</a>
                        </li>
                    </ul>
                </div>
                </div>
            </nav>
        )
    }
}