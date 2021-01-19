import React, { Component } from 'react';
import PropTypes from 'prop-types';

class LoginForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: '',
      password: '',
      errorMessage: ''
    }

    this.setErrorMessage = this.setErrorMessage.bind(this);
    this.handleUsernameChange = this.handleUsernameChange.bind(this);
    this.handlePasswordChange = this.handlePasswordChange.bind(this);
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

  handlePasswordChange(event) {
    this.setState({
      password: event.target.value
    });
  }

  render() {
    return (
      <form onSubmit={e => this.props.handleLogin(e, this.state)}>
        <label>
          username
          <input id="username" name="username" value={this.state.username} onChange={this.handleUsernameChange} />
        </label>
        <label>
          password
          <input type="password" name="password" value={this.state.password} onChange={this.handlePasswordChange} />
        </label>
        <input type="submit" value="login" />
        <span>{this.state.errorMessage}</span>
      </form>
    );
  }
}

export default LoginForm;

LoginForm.propTypes = {
  handleLogin: PropTypes.func.isRequired
};
