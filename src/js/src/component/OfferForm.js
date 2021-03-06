import React, { Component } from 'react';
import PropTypes from 'prop-types';

class OfferForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      title: '',
      description: '',
      errorMessage: '',
      price: '',
      image: ''
    }

    this.setErrorMessage = this.setErrorMessage.bind(this);
    this.validatePrice = this.validatePrice.bind(this);
    this.handleTitleChange = this.handleTitleChange.bind(this);
    this.handleDescriptionChange = this.handleDescriptionChange.bind(this);
    this.handlePriceChange = this.handlePriceChange.bind(this);
    this.handleImageChange = this.handleImageChange.bind(this);
  }

  setErrorMessage(message) {
    this.setState({
      errorMessage: message
    });
  }

  validatePrice() {
    let price = this.state.price;
    let regexp =  new RegExp("^[0-9]+(.[0-9]{1,2})?$");
    return regexp.test(price);
  }

  handleTitleChange(event) {
    this.setState({
      title: event.target.value
    });
  }

  handleDescriptionChange(event) {
    this.setState({
      description: event.target.value
    });
  }

  handlePriceChange(event) {
    this.setState({
      price: event.target.value.replace(',', '.')
    });
  }

  handleImageChange(event) {
    this.setState({
      image: event.target.files[0]
    });
  }

  render() {
    return (
      <form onSubmit={e=> this.props.handleAddOffer(e, this.state)}>
        
        <label>
          title
          <input type="text" value={this.state.title} onChange={this.handleTitleChange} />
        </label>
        
        <label>
          description
          <input type="text" value={this.state.description} onChange={this.handleDescriptionChange} />
        </label>

        <label>
          price($)
          <input type="text" value={this.state.price} onChange={this.handlePriceChange} />
        </label>

        <label>
          image
          <input type="file" onChange={this.handleImageChange} />
        </label>
        
        <input type="submit" value="add" />

        <span>{this.state.errorMessage}</span>
      </form>
    );
  }
}

export default OfferForm;

OfferForm.propTypes = {
  handleAddOffer: PropTypes.func.isRequired
};