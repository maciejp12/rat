import React, { Component } from 'react';
import PropTypes from 'prop-types';
import '../Form.css';


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
      <form className="form-container" onSubmit={e => this.props.handleLogin(e, this.state)}>
        <label className="form-label">
          username
          <input className="form-input-text" id="username" name="username" value={this.state.username} onChange={this.handleUsernameChange} />
        </label>
        <br className="form-br"></br>
        <label className="form-label">
          password
          <input className="form-input-text" type="password" name="password" value={this.state.password} onChange={this.handlePasswordChange} />
        </label>
        <br className="form-br"></br>
        <input className="form-input-but" type="submit" value="login" />
        <br className="form-br"></br>
        <span>{this.state.errorMessage}</span>
      </form>
    );
  }
}

export default LoginForm;

LoginForm.propTypes = {
  handleLogin: PropTypes.func.isRequired
};
