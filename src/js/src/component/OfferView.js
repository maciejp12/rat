import React, { Component } from 'react';


class OfferView extends Component {
  
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <p>id is {this.props.match.params.id}</p>
    );
  }
}

export default OfferView;