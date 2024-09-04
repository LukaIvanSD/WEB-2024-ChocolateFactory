<template>
  <div class="chocolate-slider">
    <div class="header">
    <h2>Chocolates</h2>
    </div>
    <div class="card-container">
      <div class="card" v-for="item in chocolates" :key="item.id">
        <div class="card-content">
          <div class="card-img-wrapper">
            <img :src="item.chocolate.imagePath" alt="Chocolate Image" class="card-img">
          </div>
          <div class="card-details">
            <div class="title">
              <div class="status">
              <span class="title">{{ item.chocolate.name }}</span>
              <span v-if="isUserWorkerInFactory===true" class="notAvailable" :class="{'available':item.chocolateItem.availability==='Available'}">{{ item.chocolateItem.availability }}</span>
              </div>
            </div>
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
          <div v-if="isUserWorkerInFactory===true"><strong>Quantity:</strong> {{ item.chocolateItem.quantity }}</div>
          <div class="functions" v-if="isUserWorkerInFactory===true">
          <button v-if="!item.chocolateItem.isEditing" class="editQuantity-button" @click="ShowEditQuantityForm(item)">Edit quantity</button>
          <div v-if="item.chocolateItem.isEditing" class="editSection" style="margin-top: 10px">
            <button @click="decreaseQuantity(item)" class="plus-minus-button">-</button>
          <input class="purchaseQuantity no-arrows" type="number" v-model="item.chocolateItem.selectedQuantity" min="0">
          <button @click="increaseQuantity(item)" class="plus-minus-button">+</button>
            </div>
            <div v-if="item.chocolateItem.isEditing">
                <button  class="save-button" @click="EditChocolate(item)">Save</button>
                <button class="cancel-button" @click="CancelEdit(item)">Cancel</button>
            </div>
            </div>
        </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref,onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import { useToast } from 'vue-toastification'
const toast=useToast();
const route=useRoute();
const factoryId=route.params.id;
const chocolates=ref([]);
const isUserWorkerInFactory=ref(false);
const quantity=ref(0);
  onMounted(()=>{
    axios.get('http://localhost:8080/WebShopAppREST/rest/chocolates/'+factoryId,axios.defaults.withCredentials=true)
    .then(response=>{
      chocolates.value=response.data;
      for (let i = 0; i < chocolates.value.length; i++) {
            chocolates.value[i].chocolateItem.selectedQuantity = chocolates.value[i].chocolateItem.quantity;
            chocolates.value[i].chocolateItem.isEditing=false;
          }
      console.log(chocolates.value);
    })
    .catch(error=>{
    alert(error);
    })
    axios.post('http://localhost:8080/WebShopAppREST/rest/workers/rights/?factoryId='+factoryId,axios.defaults.withCredentials=true)
          .then(response=>{
            isUserWorkerInFactory.value=response.data;
          })
          .catch(error=>{
            alert(error);
          })
  })


function EditChocolate(item){
  axios.defaults.withCredentials = true;
  if(item.chocolateItem.quantity===item.chocolateItem.selectedQuantity){
    item.chocolateItem.isEditing=false;
    return;
  }
  axios.patch('http://localhost:8080/WebShopAppREST/rest/chocolateItems/', {
  chocolateItem:item.chocolateItem,
  quantity: item.chocolateItem.selectedQuantity
})
.then(response => {
  item.chocolateItem.quantity =  item.chocolateItem.selectedQuantity;
  item.chocolateItem.isEditing=false;
  if(item.chocolateItem.selectedQuantity===0)
    item.chocolateItem.availability='NotAvailable';
  else
    item.chocolateItem.availability='Available';
  toast.success('Quantity updated successfully!');
})
.catch(error => {
  alert(error);
  toast.error('Quantity update failed!');
});

}

function CancelEdit(item){
  item.chocolateItem.isEditing=false;
  item.chocolateItem.selectedQuantity=item.chocolateItem.quantity;
}

function ShowEditQuantityForm(item){
  item.chocolateItem.isEditing=true;
  }
 function increaseQuantity(item){
   item.chocolateItem.selectedQuantity++;
 }
  function decreaseQuantity(item){
    if( item.chocolateItem.selectedQuantity>0){
      item.chocolateItem.selectedQuantity--;
    }
  }

</script>
<style scoped>
.chocolate-slider {
  width: 95vh;
  margin: 0 auto;
  margin-bottom: 20px;
}

.status {
  display: flex;
  gap: 20px;
}

.card-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.editQuantity-button {
  margin-top: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  width: 110px;
  margin-left: 10px;
  border-radius: 5px;
  padding: 10px 15px;
  cursor: pointer;
}

.editQuantity-button:hover {
  background-color: #0056b3;
}

.header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  align-items: center;
}

.card {
  width: 95vh;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
  transition: transform 0.2s ease, background-color 0.6s ease;
}
.save-button {
  margin-top: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  width: 62px;
  padding: 10px;
  border-radius: 5px;
  cursor: pointer;
  margin-right: 15px;
}
.cancel-button {
  margin-top: 10px;
  background-color: #a41906;
  color: white;
  border: none;
  width: 62px;
  padding: 10px;
  border-radius: 5px;
  cursor: pointer;
}

.save-button:hover {
  background-color: #0056b3;
}
.cancel-button:hover {
  background-color: #ff0000;
}
.card:hover {
  background-color: #e0e0e0;
  transform: scale(1.02);
}

.card-content {
  padding: 20px;
  display: flex;
  align-items: top;
}
.plus-minus-button {
background-color: #007bff;
color: white;
border: none;
border-radius: 5px;
font-size: 20px;
padding:5px 10px;
cursor: pointer;
}
.purchaseQuantity{
width: 55px;
height: 33px;
font-size: 16px;
margin: 0 5px;
border: 2px solid #007bff;
text-align: center;

}
.editSection{
display: flex;
align-items: center;
justify-content: space-evenly;
}
.no-arrows::-webkit-outer-spin-button,
.no-arrows::-webkit-inner-spin-button {
-webkit-appearance: none;
margin: 0;
}

.card-img-wrapper {
  margin-right: 20px;
}

.card-img {
  height: 200px;
  width: 340px;
  object-fit: fill;
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

.notAvailable {
  color: red;
}

.available {
  color: green;
}

.right-details {
  color: #4b2e20;
  text-align: right;
}

.description {
  margin-top: 15px;
  color: #4b2e20;
}


</style>
