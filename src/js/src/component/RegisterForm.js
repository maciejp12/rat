import React, { Component } from 'react';
import PropTypes from 'prop-types';
import '../Form.css';


class RegisterForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: '',
      email: '',
      phoneNumber: '',
      password: '',
      confirmPassword: '',
      errorMessage: ''
    }

    this.setErrorMessage = this.setErrorMessage.bind(this);
    this.handleUsernameChange = this.handleUsernameChange.bind(this);
    this.handleEmailChange = this.handleEmailChange.bind(this);
    this.handlePhoneNumberChange = this.handlePhoneNumberChange.bind(this);
    this.handlePasswordChange = this.handlePasswordChange.bind(this);
    this.handleConfirmPasswordChange = this.handleConfirmPasswordChange.bind(this);
    this.validateInput = this.validateInput.bind(this);
  }

  setErrorMessage(message) {
    this.setState({
      errorMessage: message
    });
  }

  handleUsernameChange(event) {
    this.setState({
      username: event.target.value
    });
  }

  handleEmailChange(event) {
    this.setState({
      email: event.target.value
    });
  }

  handlePhoneNumberChange(event) {
    this.setState({
      phoneNumber: event.target.value
    });
  }

  handlePasswordChange(event) {
    this.setState({
      password: event.target.value
    });
  }

  handleConfirmPasswordChange(event) {
    this.setState({
      confirmPassword: event.target.value
    });
  }

  validateInput() {
    if (!this.checkConfirmPassword()) {
      return false;
    }
    return true;
  }

  checkConfirmPassword() {
    let valid = this.state.password === this.state.confirmPassword;
    if (!valid) {
      this.setState({
        errorMessage: 'passwords not matching'
      });
    }
    return valid;
  }


  render() {
    return (
      <form className="form-container" onSubmit={e => this.props.handleRegister(e, this.state)}>
        <label className="form-label">
          username 
          <input className="form-input-text" type="text" value={this.state.username} onChange={this.handleUsernameChange} />
        </label>
        
        <br className="form-br"></br>
        
        <label className="form-label">
          email 
          <input className="form-input-text" type="text" value={this.state.email} onChange={this.handleEmailChange} />
        </label>
        
        <br className="form-br"></br>
        
        <label className="form-label">
          phone number 
          <input className="form-input-text" type="text" value={this.state.phoneNumber} onChange={this.handlePhoneNumberChange} />
        </label>
        
        <br className="form-br"></br>
        
        <label className="form-label">
          password 
          <input className="form-input-text" type="password" value={this.state.password} onChange={this.handlePasswordChange} />
        </label>
        
        <br className="form-br"></br>
        
        <label className="form-label">
          confirm password 
          <input className="form-input-text" type="password" value={this.state.confirmPassword}
            onChange={this.handleConfirmPasswordChange} 
          />
        </label>
        
        <br className="form-br"></br>
        
        <input className="form-input-but" type="submit" value="register" />
        
        <br className="form-br"></br>
        
        <span>{this.state.errorMessage}</span>
      </form>
    );
  }
}

export default RegisterForm;

RegisterForm.propTypes = {
  handleRegister: PropTypes.func.isRequired
};
