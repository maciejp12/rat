const offerUrl = 'http://127.0.0.1:8080/api/offer';
const userOfferUrl = 'http://127.0.0.1:8080/api/offer/creator';
const userUrl = 'http://127.0.0.1:8080/api/user';
const userDetailsUrl = 'http://127.0.0.1:8080/api/user/username';
const loginUrl = 'http://127.0.0.1:8080/api/user/login';
const authUrl = 'http://127.0.0.1:8080/api/user/auth';


export const getAllOffers = () => {
  return fetch(offerUrl, {
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    method: 'GET'
  });
}

export const getOfferById = (id) => {
  return fetch(offerUrl + '/' + id, {
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    method: 'GET'
  });
}

export const getUserOffers = (username) => {
  return fetch(userOfferUrl + '/' + username, {
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    method: 'GET'
  });
}

export const getUserDetails = (username) => {
  return fetch(userDetailsUrl + '/' + username, {
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    method: 'GET'
  });
}

export const addOffer = (data, token, type) => {
  return fetch(offerUrl, {
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': type + ' ' + token
    },
    method: 'POST',
    body: JSON.stringify({
      title: data.title,
      description: data.description,
      price: data.price
    })
  });
}

export const registerUser = (username, email, phoneNumber, password) => {
  return fetch(userUrl, {
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    method: 'POST',
    body: JSON.stringify({
      username: username,
      email: email,
      phoneNumber: phoneNumber,
      password: password
    }) 
  })
}

export const login = (username, password) => {
  return fetch(loginUrl, {
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    method: 'POST',
    body: JSON.stringify({
      username: username,
      password: password
    }) 
  })
}

export const auth = (token) => {
  return fetch(authUrl, {
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': token
    },
    method: 'GET',
  });
}
