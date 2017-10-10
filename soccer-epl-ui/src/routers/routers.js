/*
 * filename: routers.js
 * mainly responsivle for all routes component
 * change and sidebar routlist menu item
 * */

import React, {Component} from 'react';
import {connect} from 'react-redux';
import Drawer from 'material-ui/Drawer';
import classNames from 'classnames';
import {BrowserRouter as Router} from 'react-router-dom'
import RoutesList from './routesList.js';
import RoutesComponent from './routesComponent.js';
import { Scrollbars } from 'react-custom-scrollbars';
import Footer from '../components/footer.js';

import Header from '../containers/header.js';


class SidebarMenuRouters extends Component {
  constructor () {
    super();
    this.state = { menuOpen: true };
    this.menuCollapseWithResize = this.menuCollapseWithResize.bind(this);
    this.toggleMenu = this.toggleMenu.bind(this);
  }

  // menu collapse when on mobile function
  menuCollapseWithResize (){
    if (window.innerWidth < 991) {
      this.setState({menuOpen: false});
    }
    if (window.innerWidth > 991) {
      this.setState({menuOpen: true});
    }
  }

  // Sidebar collapse when tablet
  componentDidMount () {
    window.addEventListener('resize', this.menuCollapseWithResize);

    if (window.innerWidth < 991) {
      this.setState({menuOpen: false});
    }
  }

  // Sidebar collapse when tablet
  componentWillUnmount() {
    window.removeEventListener('resize', this.menuCollapseWithResize);
  }

  // Sidebar toggle
  toggleMenu() {
    this.setState(prevState => ({
      menuOpen: !prevState.menuOpen
    }));
  }

  render() {

    const headerStyle = {
      background: this.props.colorHeader.color
    }

    const containerStyle = {
      background: this.props.colorSidebar.color
    }

    // Page content class change based on menu toggle
    const pageContent = classNames ({
      'readmin-page-content' : true,
      'menu-open': this.state.menuOpen
    });

    // Sidebar class based on bg color
    const sidebarClass = classNames ({
      'menu-drawer' : true,
      'has-bg': true,
    });

    // header left part with logo and toggle button
    const HeaderLogoWithMenu = () => (
      <div className="an-header" style={headerStyle}>
        <div className="header-left brand">
          Soccer EPL @ IBM
        </div>
      </div>
    );

    return (
      <Router>
        <div>
          {
          /* Added header component here istead of app component */
          }
          <Header />
          <div className="readmin-sidebar">
            <HeaderLogoWithMenu />
            <Drawer open={this.state.menuOpen}
              className={sidebarClass}
              containerClassName="sidebar-initial-color"
              containerStyle={containerStyle}
            >
              <Scrollbars>
                <RoutesList />
              </Scrollbars>
            </Drawer>
          </div>
          <div className={pageContent}>            
            <RoutesComponent />
            <Footer />
          </div>
        </div>
      </Router>
    );
  }
}

function mapStateToProps(state) {
  return {
    colorHeader: state.headerActiveStyle,
    colorSidebar: state.sidebarActiveStyle
  }
}

export default connect(mapStateToProps)(SidebarMenuRouters);
