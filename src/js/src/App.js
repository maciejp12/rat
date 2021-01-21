import React, { Component } from 'react';
import { registerUser, login , auth , addOffer } from './client';
import OfferForm from './component/OfferForm';
import RegisterForm from './component/RegisterForm';
import LoginForm from './component/LoginForm';
import OfferList from './component/OfferList';
import OfferView from './component/OfferView';
import UserView from './component/UserView';
import { BrowserRouter as Router, Link, Route } from 'react-router-dom';
import './App.css';


class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loggedIn: localStorage.getItem('token') ? true : false,
      username: '',
    }

    this.loginForm = React.createRef();
    this.registerForm = React.createRef();
    this.offerForm = React.createRef();
  }

  componentDidMount() {
    if (this.state.loggedIn) {
      auth(localStorage.getItem('token-type') + ' ' + localStorage.getItem('token'))
        .then(res => res.json())
        .then(json => {
          this.setState({
            username: json.username
          });
        })
        .catch(error => {
          this.setState({
            loggedIn: false
          })
          this.handleLogout();
        });
    }

  }

  handleRegister = (e, data) => {
    e.preventDefault();
    if (!this.registerForm.current.validateInput()) {
      return;
    }
    registerUser(
      data.username,
      data.email,
      data.password
    )
    .then(res => {
      if (!res.ok) {
        res.json().then(json => {
          console.log(json);
          this.registerForm.current.setErrorMessage(json.message);
        });
        return;
      }

      res.json().then(json => {
        console.log(json);
        this.registerForm.current.setErrorMessage('');
        window.location.href = '/login';
      })
    })
    .catch(error => {
      this.registerForm.current.setErrorMessage('Connection error');
    });
  }

  handleLogin = (e, data) => {
    e.preventDefault();
  
    login(data.username, data.password)
    .then(res => {
      if (!res.ok) {
        res.json().then(json => {
          this.loginForm.current.setErrorMessage('Invalid login or password');
        });
        return;
      }

      res.json().then(json => {
        localStorage.setItem('token', json.token);
        localStorage.setItem('token-type', json.type);
        this.setState({
          loggedIn: true,
          username: json.username
        });
        this.loginForm.current.setErrorMessage('');
        window.location.href = '/';
      })
    });
  }

  handleLogout = () => {
    this.setState({
      loggedIn: false,
      username: ''
    });
    localStorage.removeItem('token');
    localStorage.removeItem('token-type');
  }

  handleAddOffer = (e, data) => {
    e.preventDefault();
    if (!this.offerForm.current.validatePrice()) {
      this.offerForm.current.setErrorMessage('invalid price');
      return;
    }
    addOffer(data, localStorage.getItem('token'), localStorage.getItem('token-type'))
      .then(res => {
        if (!res.ok) {
          if (res.status === 401) {
            this.offerForm.current.setErrorMessage('please log in first');
            return;
          }
          res.json().then(json => {
            this.offerForm.current.setErrorMessage(json.message);
          })
          return;
        }

        res.json().then(json => {
          console.log(json);
          let offers = this.state.offers.concat(json);
          this.setState({
            offers
          });
          this.offerForm.current.setErrorMessage('');
          window.location.href = '/';
        })
      })
      
  }

  render() {

    const userNav = this.state.loggedIn ?
      <div>
        welcome {this.state.username}
        <button onClick={this.handleLogout} >logout</button>
        
        <Link to="/newoffer">
          <p>add offer</p>
        </Link>

      </div>
      : 
      <div>
        <p>please log in</p>
        
        <Link to="/login">
          <p>login</p>
        </Link>
        
        <Link to="/register">
          <p>register</p>
        </Link>

      </div>;
    
    
    const navbar = 
      <nav>
        {userNav}
      </nav>;

    return (
      <Router>
        <div>
          <Link to="/">
            <h1>Rat</h1>
          </Link>
        </div>
        <div>
          {navbar}
          
          <Route exact path="/">
            <h1>Offer list</h1>
            <OfferList />
          </Route>
          
          <Route path="/register">
            <div>
              <RegisterForm ref={this.registerForm} handleRegister={this.handleRegister} />
            </div>
          </Route>

          <Route path="/login">
            <div>
              <LoginForm ref={this.loginForm} handleLogin={this.handleLogin} />
            </div>
          </Route>
          
          <Route path="/newoffer">
            <div>
              <OfferForm ref={this.offerForm} handleAddOffer={this.handleAddOffer} />
            </div>
          </Route>

          <Route exact path="/offer/:id" component={OfferView} />
        
          <Route exact path="/user/:username" component={UserView} />

        </div>
      </Router>
    );
  }
  
}

export default App;
