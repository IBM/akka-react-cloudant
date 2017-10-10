import React, { Component } from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import SidebarMenuRouters from './routers/routers.js';
import getMuiTheme from 'material-ui/styles/getMuiTheme';


import './static/css/App.css';
import './static/css/vendor-styles.css';

import injectTapEventPlugin from 'react-tap-event-plugin';
injectTapEventPlugin();

//import Header from './containers/header.js';


const muiTheme = getMuiTheme({
  palette: {
    primary1Color: '#258df2',
    accent1Color: '#40c741',
  }
});

class App extends Component {

  render() {
    return (
      <div>
        <MuiThemeProvider muiTheme={muiTheme}>
          <div>
            <SidebarMenuRouters />
          </div>
        </MuiThemeProvider>
      </div>

    );
  }
}

export default App;
