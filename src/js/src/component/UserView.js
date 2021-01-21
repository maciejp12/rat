import React, { Component } from 'react';
import OfferList from './OfferList';


class UserView extends Component {

  constructor(props) {
    super(props);
  }

  componentDidMount() {
    
  }
  
  render() {
    return (
      <div>
        <p>username is {this.props.match.params.username}</p>
        <OfferList username={this.props.match.params.username} />
      </div>
    );
  }
}

export default UserView;