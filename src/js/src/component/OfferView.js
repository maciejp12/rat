import React, { Component } from 'react';
import { getOfferById , deleteOfferById } from '../client';


class OfferView extends Component {
  
  constructor(props) {
    super(props);

    this.state = {
      isFetching: false,
      id: this.props.id,
      title: '',
      creator: '',
      description: '',
      price: '',
      creationDate: '',
      exists: true,
      loggedIn: this.props.loggedIn,
      loggedName: this.props.loggedName,
      errorMessage: ''
    }

    this.deleteOffer = this.deleteOffer.bind(this);
  }

  componentDidMount() {
    this.setState({
      isFetching: true
    });

    getOfferById(this.state.id)
      .then(res => {
        if (!res.ok) {
          if (res.status === 404) {
            res.json().then(json => {
              this.setState({
                exists: false
              })
            });
          } else {
            this.setState({
              exists: false
            })
          }
          this.setState({
            isFetching: false
          });

          return;
        }

        res.json().then(json => {
          this.setState({
            title: json.title,
            creator: json.creator,
            description: json.description,
            price: json.price,
            creationDate: json.creationDate,
            exists: true
          })
        })
      });
      this.setState({
        isFetching: false
      })
  }

  deleteOffer() {
    let isOwner = this.state.loggedIn && (this.state.creator === this.state.loggedName);
    let token = localStorage.getItem('token');
    let tokenType = localStorage.getItem('token-type');

    if (!isOwner) {
      this.setState({
        errorMessage: 'Please log in'
      });
      return;
    }

    deleteOfferById(this.state.id, token, tokenType)
      .then(res => {
        if (!res.ok) {
          res.json().then(json => {
            this.setState({
              errorMessage: json.message
            });
          });
          return;
        }

        res.json().then(json => {
          window.location.href = '/';
        })
      });
  }

  render() {
    
    if (this.state.isFetching) {
      return (
        <div>
          <p>loading</p>
        </div>
      );
    }

    if (!this.state.exists) {
      return (
        <div>
          <p>this offer does not exist</p>
        </div>
      );
    }

    let isOwner = this.state.loggedIn && (this.state.creator === this.state.loggedName);

    let offerDetails = 
      <div>
        <p>id: {this.state.id}</p>
        <p>title: {this.state.title} {isOwner && <button>edit</button>}</p>
        <p>creator: {this.state.creator}</p>
        <p>description: {this.state.description} {isOwner && <button>edit</button>}</p>
        <p>price: {this.state.price} {isOwner && <button>edit</button>}</p>
        <p>creationDate: {this.state.creationDate} {isOwner && <button>edit</button>}</p>
      </div>

    let errorMessage = 
      <div>
        <span>{this.state.errorMessage}</span>
      </div>
    
    return (
      <div>
        {offerDetails}
        {isOwner && 
          <div>
            <button onClick={this.deleteOffer} >delete</button>
          </div>
        }
        {errorMessage}
      </div>
    )
  }
}

export default OfferView;