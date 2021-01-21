import React, { Component } from 'react';
import OfferList from './OfferList';
import { getUserDetails } from '../client';

class UserView extends Component {

  constructor(props) {
    super(props);
    this.state = {
      isFetching: false,
      userExists: true,
      errorMessage: '',
      username: this.props.match.params.username,
      email: '',
      phoneNumber: '',
      registerDate: ''
    }
  }

  componentDidMount() {
    this.loadUserDetails();
  }
  
  loadUserDetails() {
    this.setState({
      isFetching: true
    });

    getUserDetails(this.state.username)
    .then(res => {
      if (!res.ok) {
        res.json().then(json => {
          this.setState({
            userExists: false,
            errorMessage: json.message,
            isFetching: false
          })
        });
        return;
      }

      res.json().then(json => {
        this.setState({
          userExists: true,
          email: json.email,
          phoneNumber: json.phoneNumber,
          registerDate: json.registerDate,
          isFetching: false
        });
      })
    });
  }

  render() {
    const userData = <div>
        <p>username: {this.state.username}</p>
        <p>email: {this.state.email}</p>
        <p>phone number: {this.state.phoneNumber}</p>
        <p>joined {this.state.registerDate}</p>
        <OfferList username={this.props.match.params.username} />
      </div>;
    
    const noUser = 
      <div>
        <p>user {this.state.username} does not exist</p>
      </div>;
    
    const fetching =
      <div>
        <p>loading</p>
      </div>

    let userDetails = noUser;

    if (this.state.userExists) {
      userDetails = userData;
    }

    if (this.state.isFetching) {
      userDetails = fetching;
    }

    return (
      <div>
        {userDetails}
      </div>
    );
  }
}

export default UserView;