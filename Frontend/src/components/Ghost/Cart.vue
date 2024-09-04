<template>
  <div class="cart-container" v-if="!showLogin">
    <div class="left-section">
      <h1 v-if="cart.shipments.length === 0">Korpa je prazna</h1>
      <p v-if="cart.shipments.length > 0">Ovde se nalaze proizvodi koje želiš da kupiš.</p>
      <p v-if="cart.shipments.length > 1" >Odabrao/la si proizvode od više prodavaca ili sa više adresa slanja.</p>
      <p v-if="cart.shipments.length > 1">Grupisali smo tvoju porudžbinu u nekoliko paketa, u zavisnosti od prodavca.</p>
      <div v-for="(shipment, index) in cart.shipments" :key="shipment.factory.id" class="shipment">
        <h2>Pošiljka {{ index + 1 }}</h2>
        <div v-for="article in shipment.cartArticles" :key="article.cartArticle.id" class="cart-article">
          <img :src="article.chocolate.chocolate.imagePath" alt="Product Image" class="product-image" />
          <div class="article-details">
            <h3>{{ article.chocolate.chocolate.name }}</h3>
            <p>Boja proizvoda: {{ article.chocolate.chocolate.type }}</p>
            <p>Težina: {{ article.chocolate.chocolate.weight }}g</p>
            <p>Opis: {{ article.chocolate.chocolate.description }}</p>
            <p>Cena: ${{ article.chocolate.chocolatePrice.price }}</p>
            <div class="quantity-controls">
              <button @click="decreaseQuantity(article.cartArticle)">-</button>
              <span>{{ article.cartArticle.quantity }}</span>
              <button @click="increaseQuantity(article.cartArticle)">+</button>
              <p style="color: red;">{{ article.validationMessage }}</p>
            </div>
            <button @click="removeItem(article.chocolate.chocolateItem.id)" class="remove-button">Ukloni proizvod</button>
            <p>Ukupna cena: ${{ article.totalPrice.toFixed(2) }}</p>
          </div>
        </div>
        <hr>
        <h3>Ukupna cena pošiljke: ${{ shipment.totalPrice.toFixed(2) }}</h3>
      </div>
    </div>
    <div class="right-section">
      <h2>Pregled porudžbine ({{ cart.numberOfArticles }})</h2>
      <p>Ukupna cena: ${{ calculateTotalPrice().toFixed(2) }}</p>
      <p>Trošak dostave: $0</p>
      <p>Akcijska ušteda: ${{ calculateTotalPrice().toFixed(2) - discountPrice}}</p>
      <h3>Ukupno: ${{ discountPrice }}</h3><br>
      <button class="purchase-button" @click="makePurchase()">Confirm purchase</button>
    </div>
  </div>
  <Login v-if="showLogin" :showCart="true" @closeLogin="closeLogin"/>
</template>

<script setup>
import axios from 'axios';
import { ref, onMounted } from 'vue';
import Login from '../Login.vue';
import {useRouter} from 'vue-router';

axios.defaults.withCredentials = true;

const cart = ref({
  shipments: [],
  numberOfArticles: 0,
});

const router=useRouter();
const showLogin=ref(false);
const discountPrice = ref([]);
const discount = ref([])

onMounted(() => {
  loadCart();
});

function loadCart() {
  console.log(sessionStorage.getItem('cartArticles'));
  const jsonString = JSON.parse(sessionStorage.getItem('cartArticles')) || [];
    axios.post('http://localhost:8080/WebShopAppREST/rest/cart/getGhostCart',jsonString)
    .then(response => {
      cart.value = response.data;
      console.log(cart.value);
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

  findAndUpdateCartArticle(cartArticle,cartArticle.quantity + 1);
}

function findAndUpdateCartArticle(cartArticle,wantedQuantity) {
  for (const shipment of cart.value.shipments) {
    const article = shipment.cartArticles.find(a => a.chocolate.chocolateItem.id === cartArticle.chocolateItemId);
    if(article)
    {
      if( wantedQuantity>article.chocolate.chocolateItem.quantity)
      {
        article.validationMessage = `Not enough product in stock.`;
        break;
      }
      else
      {
        article.validationMessage = ``;
        article.cartArticle.quantity = wantedQuantity;
        article.totalPrice = article.cartArticle.quantity * article.chocolate.chocolatePrice.price;
        shipment.totalPrice = shipment.cartArticles.reduce((total, article) => total + article.totalPrice, 0);
        UpdateSessionStorage();
      }
    }

  }
}
function UpdateSessionStorage(){
  const cartArticles = [];
  for (const shipment of cart.value.shipments) {
    for (const article of shipment.cartArticles) {
      const data={
        chocolateItemId: article.chocolate.chocolateItem.id,
        quantity: article.cartArticle.quantity
      }
      cartArticles.push(data);
    }
  }
  sessionStorage.setItem('cartArticles', JSON.stringify(cartArticles));
  window.dispatchEvent(new Event('storageArticles'));
}

function decreaseQuantity(cartArticle) {
  if(cartArticle.quantity - 1>0)
  findAndUpdateCartArticle(cartArticle,cartArticle.quantity - 1);
}

function removeItem(chocolateItemId) {

    const cartArticles = JSON.parse(sessionStorage.getItem('cartArticles'));
    const updatedCartArticles = cartArticles.filter(article => article.chocolateItemId !== chocolateItemId);
    sessionStorage.setItem('cartArticles', JSON.stringify(updatedCartArticles));
    window.dispatchEvent(new Event('storageArticles'));
    loadCart();

}

function makePurchase(){
  showLogin.value=true;
}
function closeLogin(){
  showLogin.value=false;
  router.go();

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
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f9f9f9;
  color: #4b2e20;
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
