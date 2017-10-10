import React, {Component} from 'react';
import {connect} from 'react-redux';

class Header extends Component {

  render () {
    const style = {
      background: this.props.colorHeaderBanner.color
    }

    return (
      <header style={style} className="an-header">
        <div className="header-right">          
          <div>Hello Soccer Fans!</div>
        </div>
      </header>
    );
  }
}

function mapStateToProps (state) {
  return {
    colorHeaderBanner: state.headerBAnnerActiveStyle
  }
}

export default connect(mapStateToProps)(Header);
