import React, { Component } from 'react';
import { getOfferById } from '../client';


class OfferView extends Component {
  
  constructor(props) {
    super(props);
    this.state = {
      isFetching: false,
      id: this.props.match.params.id,
      title: '',
      creator: '',
      description: '',
      price: '',
      creationDate: '',
      exists: true
    }
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

    return (
      <div>
        <p>id: {this.state.id}</p>
        <p>title: {this.state.title}</p>
        <p>creator: {this.state.creator}</p>
        <p>description: {this.state.description}</p>
        <p>price: {this.state.price}</p>
        <p>creationDate: {this.state.creationDate}</p>
      </div>
    );
  }
}

export default OfferView;