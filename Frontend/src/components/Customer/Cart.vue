<template>
  <div class="cart-container">
    <div class="left-section">
      <h1 v-if="cart.shipments.length === 0">The cart is empty</h1>
      <p v-if="cart.shipments.length > 0">Here are the products you want to buy.</p>
      <p v-if="cart.shipments.length > 1">You have chosen products from multiple sellers or with multiple shipping addresses.</p>
      <p v-if="cart.shipments.length > 1">We have grouped your order into several packages, depending on the seller.</p>
      <div v-for="(shipment, index) in cart.shipments" :key="shipment.factory.id" class="shipment">
        <h2>Shipment {{ index + 1 }}</h2>
        <div v-for="article in shipment.cartArticles" :key="article.cartArticle.id" class="cart-article">
          <img :src="article.chocolate.chocolate.imagePath" alt="Product Image" class="product-image" />
          <div class="article-details">
            <h3>{{ article.chocolate.chocolate.name }}</h3>
            <p>Product color: {{ article.chocolate.chocolate.type }}</p>
            <p>Weight: {{ article.chocolate.chocolate.weight }}g</p>
            <p>Description: {{ article.chocolate.chocolate.description }}</p>
            <p>Price: ${{ article.chocolate.chocolatePrice.price }}</p>
            <div class="quantity-controls">
              <button @click="decreaseQuantity(article.cartArticle)">-</button>
              <span>{{ article.cartArticle.quantity }}</span>
              <button @click="increaseQuantity(article.cartArticle)">+</button>
              <p style="color: red;">{{ article.validationMessage }}</p>
            </div>
            <button @click="removeItem(article.cartArticle.id)" class="remove-button">Remove product</button>
            <p>Total price: ${{ article.totalPrice.toFixed(2) }}</p>
          </div>
        </div>
        <hr>
        <h3>Total shipment price: ${{ shipment.totalPrice.toFixed(2) }}</h3>
      </div>
    </div>
    <div class="right-section">
      <h2>Order summary ({{ cart.numberOfArticles }})</h2>
      <p>Total price: ${{ calculateTotalPrice().toFixed(2) }}</p>
      <p>Shipping cost: $0</p>
      <p>Discount savings: ${{ calculateTotalPrice().toFixed(2) - discountPrice}}</p>
      <h3>Total: ${{ discountPrice }}</h3><br>
      <button class="purchase-button" @click="makePurchase()">Confirm Purchase</button>
      <p v-show="isInvalid" style="color: red;">Not enough items in stock, please change the quantity.</p>
    </div>
  </div>
</template>

<script setup>
import axios from 'axios';
import { ref, onMounted } from 'vue';
import { useToast } from 'vue-toastification';

axios.defaults.withCredentials = true;

const cart = ref({
  shipments: [],
  numberOfArticles: 0,
});

const toast = useToast();
const discountPrice = ref([]);
const discount = ref([]);
const isInvalid = ref(false);

onMounted(() => {
  loadCart();
});

function loadCart() {
    axios.get('http://localhost:8080/WebShopAppREST/rest/cart/getCart')
    .then(response => {
      cart.value = response.data;
      UpdateLocalStorage();
      console.log(cart.value);
    })
    .catch(error => {
      alert(error);
    });
    axios.get('http://localhost:8080/WebShopAppREST/rest/cart/getDiscount')
    .then(response => {
      discount.value = response.data;
    })
    .catch(error => {
      alert(error);
    });
}

function calculateTotalPrice() {
  discountPrice.value = (1.00 - discount.value) * cart.value.shipments.reduce((total, shipment) => total + shipment.totalPrice, 0);
  return cart.value.shipments.reduce((total, shipment) => total + shipment.totalPrice, 0);
}

function increaseQuantity(cartArticle) {
    const cartArticleId = cartArticle.id;
    axios.patch(`http://localhost:8080/WebShopAppREST/rest/cart/increase${cartArticleId}`)
    .then(response => {
      loadCart();
      setTimeout(() => {
                if (response.data === "Not enough product in stock.") {
                    findAndUpdateCartArticle(cartArticleId);
                    isInvalid.value = false;
                }
            }, 200);
    })
    .catch(error => {
      alert(error);
    });
}

function findAndUpdateCartArticle(cartArticleId) {
  for (const shipment of cart.value.shipments) {
    const article = shipment.cartArticles.find(a => a.cartArticle.id === cartArticleId);
    if (article) {
      article.validationMessage = `In stock: ${article.cartArticle.quantity}`;
      break;
    }
  }
}
function UpdateLocalStorage() {
  const cartArticles = JSON.parse(sessionStorage.getItem('cartArticles'));
  let newCartArticle=[];
  for(const shipment of cart.value.shipments){
    for(const article of shipment.cartArticles){
      const articleFound=cartArticles.find(a => a.chocolateItemId === article.chocolate.chocolateItem.id);
      if(articleFound)
      newCartArticle.push(articleFound);
    }
  }
    sessionStorage.setItem('cartArticles', JSON.stringify(newCartArticle));
    window.dispatchEvent(new Event('storageArticles'));
  
}

function decreaseQuantity(cartArticle) {
    const cartArticleId = cartArticle.id;
  if (cartArticle.quantity > 1) {
    axios.patch(`http://localhost:8080/WebShopAppREST/rest/cart/decrease${cartArticleId}`)
    .then(response => {
      loadCart();
      setTimeout(() => {
                if (response.data === "Not enough product in stock.") {
                    findAndUpdateCartArticle(cartArticleId);
                    isInvalid.value = false;
                }
            }, 200);
    })
    .catch(error => {
      alert(error);
    });
  }
}

function removeItem(cartArticleId) {
  axios.delete(`http://localhost:8080/WebShopAppREST/rest/cart/${cartArticleId}`)
    .then(response => {
    loadCart();
    isInvalid.value = false;
    })
    .catch(error => {
      alert(error);
    });
}

function makePurchase(){
    if(cart.value.numberOfArticles > 0){
      axios.post('http://localhost:8080/WebShopAppREST/rest/cart/makePurchase', cart.value)
    .then(response => {
    
    if(response.data === "Purchase is made."){
        const updatedCartArticles = [];
        toast.success('Purchase is made!');
        sessionStorage.setItem('cartArticles', JSON.stringify(updatedCartArticles));
        window.dispatchEvent(new Event('storageArticles'));
        loadCart();
    }
    else{
      toast.error('Not enough items in stock, please change the quantity.');
        cart.value = response.data;
        isInvalid.value = true;
    }
    })
    .catch(error => {
      alert(error);
    });
    }
}

</script>

<style scoped>
.cart-container {
  display: flex;
  justify-content: space-between;
  padding: 20px;
  font-family: Arial, sans-serif;
}

.left-section, .right-section {
  width: 48%;
}

.shipment {
  margin-bottom: 20px;
  color: #4b2e20;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.cart-article {
  display: flex;
  margin-bottom: 10px;
  padding: 10px;
  border-bottom: 1px solid #eee;
}

.product-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  margin-right: 10px;
}

.article-details {
  flex: 1;
}

.quantity-controls {
  display: flex;
  align-items: center;
}

.quantity-controls button {
  width: 30px;
  height: 30px;
  margin: 0 5px;
  background-color: #f0f0f0;
  border: 1px solid #ccc;
  cursor: pointer;
}

.remove-button {
  margin-top: 10px;
  padding: 5px 10px;
  background-color: #ff4d4d;
  color: white;
  border: none;
  cursor: pointer;
}

.purchase-button {
  width: 300px;
  padding: 10px;
  background-color: #ff6f00;
  color: white;
  border: none;
  cursor: pointer;
  font-size: 16px;
}

h2, h3 {
  margin-top: 0;
}

h3 {
  margin-bottom: 5px;
}

p {
  margin: 5px 0;
}
</style>
