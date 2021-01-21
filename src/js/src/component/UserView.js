import React, { Component } from 'react';
import OfferList from './OfferList';
import { getUserDetails } from '../client';

class UserView extends Component {

  constructor(props) {
    super(props);
    this.state = {
      username: this.props.match.params.username
    }
  }

  componentDidMount() {
    this.loadUserDetails();
  }
  
  loadUserDetails() {
    getUserDetails(this.state.username)
    .then(res => {
      if (!res.ok) {
        res.json().then(json => {
          console.log('errlog');
          console.log(json);
        });
        return;
      }

      res.json().then(json => {
        this.setState({
          email: json.email,
          registerDate: json.registerDate
        });
      })
    });
  }

  render() {
    return (
      <div>
        <p>username: {this.state.username}</p>
        <p>email: {this.state.email}</p>
        <p>joined {this.state.registerDate}</p>
        <OfferList username={this.props.match.params.username} />
      </div>
    );
  }
}

export default UserView;