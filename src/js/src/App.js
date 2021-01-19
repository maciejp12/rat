import React, { Component } from 'react';
import { getAllOffers , registerUser, login , auth , addOffer } from './client';
import OfferForm from './component/OfferForm';
import RegisterForm from './component/RegisterForm';
import LoginForm from './component/LoginForm';
import { BrowserRouter as Router, Link, Route } from 'react-router-dom';
import './App.css';


class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loggedIn: localStorage.getItem('token') ? true : false,
      username: '',
      isFetching: false,
      offers: [],
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
          this.handleLogout();
        });
    }

    this.loadOffers();
  }

  handleRegister = (e, data) => {
    e.preventDefault();
    if (!this.registerForm.current.checkConfirmPassword()) {
      this.registerForm.current.setErrorMessage('Passwords doesnt match');
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
        this.loginForm.current.setErrorMessage('Invalid login or password');
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
    
    const { offers, isFetching } = this.state;

    if (isFetching) {
      return (
        <div>
          <p>loading</p>
        </div>
      );
    }

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

    const tableCss = {width: '90%', border: '2px solid black', borderCollapse: 'collapse', margin: 'auto'};

    const offersList = this.state.offers.length > 0 ?
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
                                <td>{offer.title}</td>
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
            <div>
              {offersList}
            </div>
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
        </div>
      </Router>
    );
  }
  
}

export default App;
