const offerUrl = 'http://127.0.0.1:8080/api/offer';
const offerImageUrl = 'http://127.0.0.1:8080/api/offer/image';
const offerDeleteUrl = 'http://127.0.0.1:8080/api/offer/delete';
const offerUpdateUrl = 'http://127.0.0.1:8080/api/offer/update';
const userOfferUrl = 'http://127.0.0.1:8080/api/offer/creator';
const offerVisitsUrl = 'http://127.0.0.1:8080/api/offer/visits';
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

export const deleteOfferById = (id, token, type) => {
  return fetch(offerDeleteUrl + '/' + id, {
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': type + ' ' + token
    },
    method: 'DELETE'
  });
}

export const updateOfferById = (id, token, type, data) => {
  return fetch(offerUpdateUrl + '/' + id, {
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': type + ' ' + token
    },
    method: 'PATCH',
    body: JSON.stringify({
      title: data.title,
      description: data.description,
      price: data.price
    })
  });
}

export const addOfferVisitAuth = (id, token, type) => {
  return fetch(offerVisitsUrl + '/' + id, {
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': type + ' ' + token
    },
    method: 'POST'
  });
}

export const addOfferVisit = (id) => {
  return fetch(offerVisitsUrl + '/' + id, {
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
    },
    method: 'POST'
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


export const uploadOfferImage = (id, file, token, type) => {
  const reader = new FileReader();
  let fileType = file.type;

  reader.onloadend = () => {
    const base64String = reader.result
      .replace("data:", "")
      .replace(/^.+,/, "");

    fetch(offerImageUrl + '/' + id, {
        headers: {
          'Authorization': type + ' ' + token,
          'Content-Type': 'application/json'
        },
        method: 'POST',
        body: JSON.stringify({
          file: base64String,
          filetype: fileType
        })
    })
  }

  reader.readAsDataURL(file);
}


export const getOfferImagesById = (id) => {
  return fetch(offerImageUrl + '/' + id, {
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    method: 'GET'
  })
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
