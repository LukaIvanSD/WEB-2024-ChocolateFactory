<template>
  <div class="chocolate-slider">
    <h2 style="margin-bottom: 10px">Chocolates</h2>
    <div class="card-container">
      <div class="card" v-for="item in chocolates" :key="item.id">
        <div class="card-content">
          <div class="card-img-wrapper">
            <img :src="item.chocolate.imagePath" alt="Chocolate Image" class="card-img">
          </div>
          <div class="card-details">
            <div class="title">{{ item.chocolate.name }}</div>
            <div class="info">
              <div><strong>Type:</strong> {{ item.chocolate.type }}</div>
              <div><strong>Weight:</strong> {{ item.chocolate.weight }}</div>
              <div><strong>Category:</strong> {{ item.chocolate.category }}</div>
            </div>
            <div class="description">
          <p>{{ item.chocolate.description }}</p>
        </div>
          </div>
          <div class="right-details">
          <div><strong>Price:</strong> {{ item.chocolatePrice.price }}</div>
          <div class="purchase">
            <div style="margin-top: 10px">
            <button :disabled="!item.canGoBack" @click="decreaseQuantity(item)"  class="plus-minus-button" :class="{'disabled':!item.canGoBack}">-</button>
          <input class="purchaseQuantity no-arrows" type="number" @input="CheckQuantity(item)" v-model="item.selectedQuantity" min="0">
          <button :disabled="!item.canGoForward" @click="increaseQuantity(item)" class="plus-minus-button" :class="{'disabled':!item.canGoForward}">+</button>
            </div>
          <button @click="SaveToCart(item)" :disabled="item.selectedQuantity === 0" :class="{'disabled-cart': item.selectedQuantity===0}" class="purchase-button">Add to cart</button>
            </div>
            <label class="errorInfo" :class="{'error':!item.canGoForward}">Selected maximum quantity</label>
        </div>
        </div>
      </div>
    </div>
  </div>
</template>



<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import { useToast } from 'vue-toastification';
const toast = useToast();
const route=useRoute();
const factoryId=route.params.id;
const chocolates = ref([]);
var cartArticles = JSON.parse(sessionStorage.getItem('cartArticles')) || [];
onMounted(async () => {
  try {
    const response = await axios.get(`http://localhost:8080/WebShopAppREST/rest/chocolates/${factoryId}`, { withCredentials: true });
    chocolates.value = response.data;
    for (let i = 0; i < chocolates.value.length; i++) {
      chocolates.value[i].selectedQuantity = 0;
      chocolates.value[i].canGoBack=false;
      chocolates.value[i].canGoForward=true;
      //Set forward button to false if quantity is max is in cart already
      cartArticles.forEach(cartArticle => {
        if (cartArticle.chocolateItemId === chocolates.value[i].chocolateItem.id) {
          if(cartArticle.quantity>=chocolates.value[i].chocolateItem.quantity)
            chocolates.value[i].canGoForward=false;
        }
      });
    }
    console.log(chocolates.value);

  } catch (error) {
    alert(error);
  }
});
function SaveToCart(item){
    toast.success('Chocolate added to cart');
    let cartArticle;
    cartArticle = cartArticles.find(cartArticle => cartArticle.chocolateItemId === item.chocolateItem.id);
    if(cartArticle!==undefined)
      cartArticle.quantity += item.selectedQuantity;
    else
    {
      cartArticle={
        chocolateItemId: item.chocolateItem.id,
        quantity: item.selectedQuantity};
      cartArticles.push(cartArticle);
    }
    sessionStorage.setItem('cartArticles', JSON.stringify(cartArticles));
    window.dispatchEvent(new Event('storageArticles'));
    ResetForm(item);
}
  function ResetForm(item){
    item.selectedQuantity = 0;
    item.canGoBack=false;
  }
  function increaseQuantity(item){
    item.selectedQuantity++;
    CheckQuantity(item);
  }
  function decreaseQuantity(item){
    item.selectedQuantity--;
    if(item.selectedQuantity===0)
      item.canGoBack=false;
    item.canGoForward=true;
  }
  function CheckQuantity(item){
    let cartArticle = cartArticles.find(cartArticle => cartArticle.chocolateItemId === item.chocolateItem.id);
    let numOfItemsInCart = cartArticle ? cartArticle.quantity : 0;
    const wantedQuantity = item.selectedQuantity + numOfItemsInCart;
    if(wantedQuantity>=item.chocolateItem.quantity)
    {
      item.selectedQuantity=item.chocolateItem.quantity-numOfItemsInCart;
      item.canGoForward=false;
    }
    if(item.selectedQuantity===0)
      item.canGoBack=false;
    else
    item.canGoBack=true;
  }

</script>

<style scoped>
.chocolate-slider {
width: 95vh;
margin: 0 auto;
margin-bottom: 20px;
}

.card-container {
display: flex;
flex-wrap: wrap;
gap: 20px;
}
.purchase-button {
margin-top: 10px;
background-color: #007bff;
color: white;
border: none;
width:102px;
margin-left: 10px;
border-radius: 5px;
padding: 10px 15px;
cursor: pointer;
}
.plus-minus-button {
background-color: #007bff;
color: white;
border: none;
border-radius: 5px;
padding:5px 10px;
cursor: pointer;
}

.card {
width: 95vh;
border: 1px solid #ccc;
border-radius: 10px;
box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
overflow: hidden;
display:flex;
flex-direction: column;
background-color: #f5f5f5;
transition: transform 0.2s ease, background-color 0.7s ease;
}
.card:hover {
background-color: #e0e0e0;
transform: scale(1.02);
}

.card-content {
  padding: 20px;
display: flex;
align-items: Top;
}

.card-img-wrapper {
margin-right: 20px; 
}

.card-img {
height: 200px;
width:340px;
object-fit:fill;

}
.disabled-cart{
background-color: #ccc;

}
.purhcase{
display: flex;
align-items: center;
flex-direction: column;
}
.card-details {
flex: 1; 
text-align: left;
}

.title {
  color: #333;
font-size: 18px;
font-weight: bold; 
margin-bottom: 5px; 
}

.info {
margin-top: 10px; 
color: #4b2e20;
}
.errorInfo{
color: red;
visibility: collapse;
}
.error{
visibility: visible;
}
.right-details {
  color: #4b2e20;
text-align: right;
}

.description {
margin-top: 15px; 
color: #4b2e20;
}
.no-arrows::-webkit-outer-spin-button,
.no-arrows::-webkit-inner-spin-button {
-webkit-appearance: none;
margin: 0;
}

.purchaseQuantity{
width: 40px;
height: 30px;
margin: 0 5px;
text-align: center;

}
.disabled{
background-color: #ccc;
cursor: not-allowed;
}
</style>



