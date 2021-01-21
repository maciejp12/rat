import React, { Component } from 'react';
import PropTypes from 'prop-types';

class RegisterForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: '',
      email: '',
      password: '',
      confirmPassword: '',
      errorMessage: ''
    }

    this.setErrorMessage = this.setErrorMessage.bind(this);
    this.handleUsernameChange = this.handleUsernameChange.bind(this);
    this.handleEmailChange = this.handleEmailChange.bind(this);
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
      <form onSubmit={e => this.props.handleRegister(e, this.state)}>
        <label>
          username 
          <input type="text" value={this.state.username} onChange={this.handleUsernameChange} />
        </label>
        <label>
          email 
          <input type="text" value={this.state.email} onChange={this.handleEmailChange} />
        </label>
        <label>
          password 
          <input type="password" value={this.state.password} onChange={this.handlePasswordChange} />
        </label>
        <label>
          confirm password 
          <input type="password" value={this.state.confirmPassword}
            onChange={this.handleConfirmPasswordChange} 
          />
        </label>
        <input type="submit" value="register" />
        <span>{this.state.errorMessage}</span>
      </form>
    );
  }
}

export default RegisterForm;

RegisterForm.propTypes = {
  handleRegister: PropTypes.func.isRequired
};
