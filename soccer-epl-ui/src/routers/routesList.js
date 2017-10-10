import React, {Component} from 'react';
import {connect} from 'react-redux';
import {List, ListItem} from 'material-ui/List';
import {NavLink} from 'react-router-dom'

class RoutesList extends Component {
  render() {
    // Parent list style
    const style = {
      padding: '16px 16px 16px 55px',
      fontSize: '14px',
    }
        
    return (
      <List className="nav-menu">
        <ListItem
          primaryText="Dashboard"
          innerDivStyle={style}
          leftIcon={<i className="material-icons">dashboard</i>}
          containerElement={<NavLink to="/" exact />}
        />      
        <ListItem
          primaryText="Fixtures"
          innerDivStyle={style}
          leftIcon={<i className="material-icons">keyboard_arrow_right</i>}
          containerElement={<NavLink to="/fixtures" exact />}
        />      
        <ListItem
          primaryText="Results"
          innerDivStyle={style}
          leftIcon={<i className="material-icons">keyboard_arrow_right</i>}
          containerElement={<NavLink to="/results" exact />}
        />      
        <ListItem
          primaryText="Team Standing"
          innerDivStyle={style}
          leftIcon={<i className="material-icons">keyboard_arrow_right</i>}
          containerElement={<NavLink to="/teamstanding" exact />}
        />      
      </List>
    );
  }
}

function mapStateToProps(state) {
  return {
    apiData: state.apiData
  }
}

export default connect(mapStateToProps)(RoutesList);
