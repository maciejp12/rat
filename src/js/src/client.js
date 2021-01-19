const offerUrl = 'http://127.0.0.1:8080/api/offer';
const userUrl = 'http://127.0.0.1:8080/api/user';
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

export const registerUser = (username, email, password) => {
  return fetch(userUrl, {
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    method: 'POST',
    body: JSON.stringify({
      username: username,
      email: email,
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
