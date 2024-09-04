<template>
  <div>
    <button class="purchaseHistory-button" @click="PurchaseHistory">All Factory Purchases</button>
    <button class="logout-button" @click="logOut">LogOut</button>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
axios.defaults.withCredentials = true;


function logOut(){
  axios.post('http://localhost:8080/WebShopAppREST/rest/logout').then(response => {
    router.push({ name: 'ghost' });
  })
  .catch(error => {
    console.log(error);
  });
}


function PurchaseHistory(){
    router.push({ name: 'customerPurchases' });
}

</script>

<style scoped>
.logout-button {
  padding: 10px 20px;
  background-color: #007BFF;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  position: absolute;
  top: 30px;
  right: 30px;
}

.purchaseHistory-button {
  padding: 10px 20px;
  background-color: green;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  position: absolute;
  top: 30px;
  right: 140px;
}
</style>
