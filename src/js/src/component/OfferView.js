import React, { Component } from 'react';
import { getOfferById , deleteOfferById , updateOfferById , auth, addOfferVisit , addOfferVisitAuth, getOfferImagesById } from '../client';


class OfferView extends Component {
  
  constructor(props) {
    super(props);

    this.state = {
      isFetching: true,
      id: this.props.match.params.id,
      title: '',
      creator: '',
      description: '',
      price: '',
      creationDate: '',
      exists: true,
      loggedIn: false,
      loggedName: '',
      editingTitle: false,
      editingDescription: false,
      editingPrice: false,
      titleUpdate: '',
      descriptionUpdate: '',
      priceUpdate: '',
      errorMessage: '',
      visits: '',

      imageList: []
    }

    this.deleteOffer = this.deleteOffer.bind(this);
    this.updateOffer = this.updateOffer.bind(this);
    this.updateOfferTitle = this.updateOfferTitle.bind(this);
    this.updateOfferDescription = this.updateOfferDescription.bind(this);
    this.updateOfferPrice = this.updateOfferPrice.bind(this);

    this.handleTitleChange = this.handleTitleChange.bind(this);
    this.handleDescriptionChange = this.handleDescriptionChange.bind(this);
    this.handlePriceChange = this.handlePriceChange.bind(this);
    this.validatePrice = this.validatePrice.bind(this);
  }

  componentDidMount() {
    this.setState({
      isFetching: true
    });
    if (localStorage.getItem('token') != null) {
      auth(localStorage.getItem('token-type') + ' ' + localStorage.getItem('token'))
        .then(res => res.json())
        .then(json => {
          this.setState({
            loggedIn: true,
            loggedName: json.username
          });
        })
        .catch(error => {
          this.setState({
            loggedIn: false
          });
        });
    }
    this.getOfferDetails();
    this.getOfferImages();
  }

  getOfferDetails() {
    getOfferById(this.state.id)
      .then(res => {
        if (!res.ok) {
          if (res.status === 404) {
            res.json().then(json => {
              this.setState({
                exists: false,
                isFetching: false
              })
            });
          } else {
            this.setState({
              exists: false,
              isFetching: false
            })
          }
          return;
        }

        res.json().then(json => {
          this.setState({
            title: json.title,
            creator: json.creator,
            description: json.description,
            price: json.price,
            creationDate: json.creationDate,
            exists: true
          });


          if (this.state.loggedIn) {
            addOfferVisitAuth(this.state.id, localStorage.getItem('token'), localStorage.getItem('token-type'))
              .then(res => {
                if (!res.ok) {
                  return;
                }
    
                res.json().then(json => {
                  this.setState({
                    visits: json.visits,
                    isFetching: false
                  });
                })
              });
          } else {
            addOfferVisit(this.state.id)
            .then(res => {
              if (!res.ok) {
                return;
              }
    
              res.json().then(json => {
                this.setState({
                  visits: json.visits,
                  isFetching: false
                });
              })
            });
          }
        })
      });
  }

  deleteOffer() {
    let isOwner = this.state.loggedIn && (this.state.creator === this.state.loggedName);
    let token = localStorage.getItem('token');
    let tokenType = localStorage.getItem('token-type');

    if (!isOwner) {
      this.setState({
        errorMessage: 'Please log in'
      });
      return;
    }

    deleteOfferById(this.state.id, token, tokenType)
      .then(res => {
        if (!res.ok) {
          res.json().then(json => {
            this.setState({
              errorMessage: json.message
            });
          });
          return;
        }

        res.json().then(json => {
          window.location.href = '/';
        })
      });
  }

  getOfferImages() {
    getOfferImagesById(this.state.id)
      .then(res => {
        if (!res.ok) {
          return;
        }

        res.json().then(json => {
          let images = [];
          
          for (let i in json) {
            images.push({
              order: i,
              file: json[i].file,
              fileType: json[i].fileType
            });
          }

          this.setState({
            imageList: images
          });

        })
      });
  }

  handleTitleChange(event) {
    this.setState({
      titleUpdate: event.target.value
    });
  }

  handleDescriptionChange(event) {
    this.setState({
      descriptionUpdate: event.target.value
    });
  }

  handlePriceChange(event) {
    this.setState({
      priceUpdate: event.target.value
    });
  }

  updateOfferTitle() {
    let data = {
      title: this.state.titleUpdate,
      description: null,
      price: null
    };

    this.updateOffer(data)
      .then(res => {
        if (!res.ok) {
          res.json().then(json => {
            this.setState({
              errorMessage: json.message
            })
          });
          return;
        }

        res.json().then(json => {
          this.setState({
            editingTitle: false,
            title: json.title,
            titleUpdate: '',
            errorMessage: 'title updated'
          })
        })
      });
  }

  updateOfferDescription() {
    let data = {
      title: null,
      description: this.state.descriptionUpdate,
      price: null
    };

    this.updateOffer(data)
      .then(res => {
        if (!res.ok) {
          res.json().then(json => {
            this.setState({
              errorMessage: json.message
            })
          });
          return;
        }

        res.json().then(json => {
          this.setState({
            editingDescription: false,
            description: json.description,
            descriptionUpdate: '',
            errorMessage: 'description updated'
          })
        })
      });
  }

  validatePrice() {
    let price = this.state.priceUpdate;
    let regexp =  new RegExp("^[0-9]+(.[0-9]{1,2})?$");
    return regexp.test(price);
  }

  updateOfferPrice() {
    if (!this.validatePrice()) {
      this.setState({
        errorMessage: 'Invalid price input'
      });
      return;
    }

    let data = {
      title: null,
      description: null,
      price: this.state.priceUpdate
    };

    this.updateOffer(data)
      .then(res => {
        if (!res.ok) {
          res.json().then(json => {
            this.setState({
              errorMessage: json.message
            })
          });
          return;
        }

        res.json().then(json => {
          this.setState({
            editingPrice: false,
            price: json.price,
            priceUpdate: '',
            errorMessage: 'price updated'
          })
        })
      });
  }

  updateOffer(data) {
    return updateOfferById(
      this.state.id, 
      localStorage.getItem('token'), 
      localStorage.getItem('token-type'), 
      data
    );
  }

  render() {
    
    if (this.state.isFetching) {
      return (
        <div>
          <p>loading</p>
        </div>
      );
    }

    if (!this.state.exists) {
      return (
        <div>
          <p>this offer does not exist</p>
        </div>
      );
    }

    let isOwner = this.state.loggedIn && (this.state.creator === this.state.loggedName);

    let editingTitle = this.state.editingTitle;
    let editingDescription = this.state.editingDescription;
    let editingPrice = this.state.editingPrice;

    let titleDetails = 
      <div>
        <p>title: {this.state.title}</p> 
      </div>

    if (isOwner) {
      if (!editingTitle) {
        titleDetails = 
          <div>
            <p>title: {this.state.title}</p> 
            <button onClick={() => this.setState({editingTitle: true})}>edit</button>
          </div>
      } else {
        titleDetails = 
          <div>
            <div>
              <label>title:
                <input placeholder={this.state.title} type="text" value={this.state.titleUpdate} onChange={this.handleTitleChange}>
                </input>
              </label>
            </div>
            <button onClick={() => {this.updateOfferTitle()}}>submit</button>
            <button onClick={() => this.setState({editingTitle: false, errorMessage: ''})} >cancel</button>
        </div>
      }
    }

    let descriptionDetails = 
      <div>
        <p>description: {this.state.description}</p> 
      </div>

    if (isOwner) {
      if (!editingDescription) {
        descriptionDetails = 
          <div>
            <p>description: {this.state.description}</p> 
            <button onClick={() => this.setState({editingDescription: true})}>edit</button>
          </div>
      } else {
        descriptionDetails = 
          <div>
            <div>
              <label>description:
                <input placeholder={this.state.description} type="text" value={this.state.descriptionUpdate} onChange={this.handleDescriptionChange}>
                </input>
              </label>
            </div>
            <button onClick={() => {this.updateOfferDescription()}}>submit</button>
            <button onClick={() => this.setState({editingDescription: false, errorMessage: ''})} >cancel</button>
        </div>
      }
    }

    let priceDetails = 
      <div>
        <p>price: {this.state.price}</p> 
      </div>

    if (isOwner) {
      if (!editingPrice) {
        priceDetails = 
          <div>
            <p>price: {this.state.price}</p> 
            <button onClick={() => this.setState({editingPrice: true})}>edit</button>
          </div>
      } else {
        priceDetails = 
          <div>
            <div>
              <label>price:
                <input placeholder={this.state.price} type="text" value={this.state.priceUpdate} onChange={this.handlePriceChange}>
                </input>
              </label>
            </div>
            <button onClick={() => {this.updateOfferPrice()}}>submit</button>
            <button onClick={() => this.setState({editingPrice: false, errorMessage: ''})} >cancel</button>
        </div>
      }
    }


    let offerDetails = 
      <div>
        <div>
          <p>id: {this.state.id}</p>
        </div>
        
        <div>
          {titleDetails}
        </div>

        <p>creator: {this.state.creator}</p>
                
        <div>
          {descriptionDetails}
        </div>
        
        <div>
          {priceDetails}
        </div>

        <p>creationDate: {this.state.creationDate} </p>
      </div>

    let errorMessage = 
      <div>
        <span>{this.state.errorMessage}</span>
      </div>
    
    let visitsDetails = 
      <div>
        <p>visits = {this.state.visits}</p>
      </div>

    let imageContainer = 
      <div>
        <img src="/no_image_tmp.png" alt="no img found"></img>
      </div>

    if (this.state.imageList.length) {
      imageContainer =
        <div>
          {this.state.imageList.map(image => 
            <div key={image.order}>
              <img src={'data:' + image.fileType + ';base64,' + image.file} alt="no img found"></img>
            </div>
          )}
        </div>
    }

    return (
      <div>
        {imageContainer}
        {offerDetails}
        {isOwner && 
          <div>
            <button onClick={this.deleteOffer} >delete</button>
          </div>
        }
        {errorMessage}
        {visitsDetails}
      </div>
    )
  }
}

export default OfferView;