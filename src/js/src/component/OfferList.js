import React, { Component } from 'react';
import { getAllOffers } from '../client';
import { Link } from 'react-router-dom';

import '../App.css';


class OfferList extends Component {

  constructor(props) {
    super(props);
    this.state = {
      offers: [],
      isFetching: false
    }
  }

  componentDidMount() {
    this.loadOffers();
  }

  loadOffers = () => {
    this.setState({
      isFetching: true
    });
    getAllOffers()
      .then(res => res.json()
        .then(offers => {
          this.setState({
            offers,
            isFetching: false
          });
        }))
      .catch(error => {
        console.log(error);
        this.setState({
          isFetching: false
        });
      });
  }

  render() {
    const { offers, isFetching } = this.state;

    if (isFetching) {
      return (
        <p>loading</p>
      );
    }

    const tableCss = {
      width: '90%', 
      border: '2px solid black', 
      borderCollapse: 'collapse', 
      margin: 'auto'
    };

    const offersList = offers.length > 0 ?
      <table style={tableCss} >
        <tbody>
          <tr>
            <th>Title</th>
            <th>Creator</th>
            <th>Creation Date</th>
            <th>Description</th>
            <th>Price</th>
          </tr>
          {offers.map(offer => <tr key={offer.id}> 
                                <td>
                                  <Link to={`/offer/${offer.id}`}>
                                    {offer.title}
                                  </Link>
                                </td>
                                <td>{offer.creator}</td>
                                <td>{offer.creationDate}</td>
                                <td>{offer.description}</td>
                                <td>{offer.price}$</td>
                              </tr>)}
        </tbody>
      </table>
      :
      <p>no offers</p>

      return (
        <div>
          {offersList}
        </div>
      )
  }
}

export default OfferList;
