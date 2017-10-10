import React, {Component} from 'react';
import {Route } from 'react-router-dom';

import Home from '../pages/home.js';
import Fixtures from '../pages/fixtures.js';
import Results from '../pages/results.js';
import TeamStanding from '../pages/teamstanding.js';

class RoutesComponent extends Component {
  render() {
    return (
      <div>      
        <Route exact path="/" component={Home}/>
        <Route exact path="/fixtures" component={Fixtures}/>
        <Route exact path="/results" component={Results}/>
        <Route exact path="/teamstanding" component={TeamStanding}/>          
      </div>
    );
  }
}

export default RoutesComponent;
