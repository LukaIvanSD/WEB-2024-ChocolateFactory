<template>
  <div v-if="isLoaded" class="profile">
    <div class="profile-header">
      <img src="../assets/malePlaceholder.png" class="profile-image" v-show="customer.user.sex === 'Male'">
      <img src="../assets/femalePlaceholder.jpg" class="profile-image" v-show="customer.user.sex === 'Female' ">
      <div class="user-info">
        <h1>{{ customer.user.firstName }} {{ customer.user.lastName }}</h1>
        <p style="font-weight: bold;">Username: {{ customer.user.username }}</p>
        <p style="font-weight: bold;">Gender: {{ customer.user.sex }}</p>
        <p style="font-weight: bold;">Date of Birth: {{ formatDate(customer.user.dateOfBirth) }}</p>
        <p v-if="customer.user.userType==='Manager'"  style="font-weight: bold;">Manager of: {{ customer.factoryName }}</p>
        <p v-if="customer.user.userType==='Worker'"  style="font-weight: bold;">Working in: {{ customer.factoryName }}</p>
        <p v-if="customer.user.userType==='Customer'"  style="font-weight: bold;" :style="{ color: getFontColor(customer.customerType.type) }">Badge: {{ customer.customerType.type }}</p>
      </div>
    </div>    
    <button class="edit-info-button" @click="isFormOpen = true">Edit Info</button>
    <div class="progress-section" v-if="customer.user.userType==='Customer'" >
      <h2 style="text-align: center; font-weight: bold;">Customer Progress</h2><br>
      <div class="badges-container">
        <div class="badge-wrapper">
          <img :src="currentBadge" class="badge-image1">
          <span class="tooltip-text" :style="{ color: getFontColor(customer.customerType.type) }" >Current badge: {{ customer.customerType.type }} <br> Discount: {{ getDiscount(customer.customerType.type) }}%</span>
        </div>
        <div class="progress-bar">
          <div class="progress" :style="{ width: progressPercentage + '%' }"></div>       
        </div>
        <div class="badge-wrapper">
          <img :src="nextBadge" class="badge-image2">
          <span class="tooltip-text" :style="{ color: getFontColor(nextLevel) }">Next badge: {{ nextLevel }} <br> Discount: {{ getDiscount(nextLevel) }}%</span>
        </div>
      </div>
      <div class="points-text">
        <p style="font-weight: bold;">{{ customer.customer.bonusPoints }} / {{ pointsForNextLevel }} points</p>
        <p> ({{ pointsToNextLevel }} points to {{ nextLevel }})</p>
      </div>
    </div>
  </div>
  <EditProfileModal v-show="isFormOpen"  @closeModal="load()"></EditProfileModal>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import EditProfileModal from '../components/EditProfileModal.vue';

axios.defaults.withCredentials = true;

const customer = ref([]);
const isLoaded = ref(false);
const isFormOpen = ref(false);

onMounted(() => {
  load();
});

function load() {
  axios.get('http://localhost:8080/WebShopAppREST/rest/userProfile')
    .then(response => {
      customer.value = response.data
      isLoaded.value = true;
      isFormOpen.value = false;
      console.log("gas")
      console.log(customer.value)
    })
    .catch(error => {
      console.log(error);
      isLoading.value = false;
    });
}



const nextLevel = computed(() => {
  if(customer.value.user.userType==='Customer')
  switch (customer.value.customerType.type) {
    case 'Basic':
      return 'Bronze';
    case 'Bronze':
      return 'Silver';
    case 'Silver':
      return 'Gold';
    default:
      return 'Highest Level';
  }
});

const currentBadge = computed(() => {
  if(customer.value.user.userType==='Customer')
  switch (customer.value.customerType.type) {
    case 'Basic':
      return "../../src/assets/basic.png";
    case 'Bronze':
      return "../../src/assets/bronze.jpg";
    case 'Silver':
      return "../../src/assets/silver.webp";
    default:
      return "../../src/assets/gold.png";
  }
});

const nextBadge = computed(() => {
  if(customer.value.user.userType==='Customer')
  switch (customer.value.customerType.type) {
    case 'Basic':
      return '../../src/assets/bronze.jpg';
    case 'Bronze':
      return "../../src/assets/silver.webp";
    case 'Silver':
      return "../../src/assets/gold.png";
    default:
      return "../../src/assets/gold.png";
  }
});

const pointsForNextLevel = computed(() => {
  if(customer.value.user.userType==='Customer')
  switch (customer.value.customerType.type) {
    case 'Basic':
      return 1000;
    case 'Bronze':
      return 2800;
    case 'Silver':
      return 5000;
    case 'Gold':
      return 5000;
  }
});

const pointsToNextLevel = computed(() => {
  if(customer.value.user.userType==='Customer')
  return pointsForNextLevel.value - customer.value.customer.bonusPoints;
});

const progressPercentage = computed(() => {
  if(customer.value.user.userType==='Customer')
  return (customer.value.customer.bonusPoints / pointsForNextLevel.value) * 100;
});

function getDiscount(type) {
  if(customer.value.user.userType==='Customer')
  switch (type) {
    case 'Basic':
      return 0;
    case 'Bronze':
      return 5;
    case 'Silver':
      return 12;
    case 'Gold':
      return 20;
    default:
      return 0;
  }
}


function getFontColor(type) {
  if(customer.value.user.userType==='Customer')
  switch (type) {
    case 'Basic':
      return 'gray';
    case 'Bronze':
      return 'brown';
    case 'Silver':
      return 'silver';
    case 'Gold':
      return 'gold';
    default:
      return 'black';
  }
}
const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('en-GB', options);
};

</script>


<style scoped>
.profile{
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 50px;

}
.profile-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
}

.profile-header {
  display: flex;
  align-items: center;
}

.profile-image {
  width: 100px;
  height: 100px;
  border-radius: 80%;
  margin-right: 20px;
}

.user-info {
  flex: 1;
}

.progress-section {
  margin-top: 20px;
  width: 500px;
}

.progress-bar {
  width: 100%;
  background-color: #f3f3f3;
  border-radius: 5px;
  overflow: hidden;
  height: 20px;
  margin-bottom: 10px;
}

.progress {
  height: 100%;
  background-color: #4caf50;
  transition: width 0.3s;
}

.badges-container {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.badge-wrapper {
  position: relative;
  display: inline-block;
}

.badge-image1, .badge-image2 {
  width: 50px;
  height: 50px;
  margin-bottom: 10px;
  border-radius: 50%;
}

.tooltip-text {
  visibility: hidden;
  width: 120px;
  background-color: black;
  color: #fff;
  text-align: center;
  border-radius: 6px;
  padding: 5px;
  position: absolute;
  z-index: 1;
  bottom: 125%; /* Position the tooltip above the badge */
  left: 50%;
  margin-left: -60px; /* Center the tooltip */
  opacity: 0;
  transition: opacity 0.3s;
}

.badge-wrapper:hover .tooltip-text {
  visibility: visible;
  opacity: 1;
}

.points-text {
  text-align: center;
  margin-top: -30px;
  font-weight: bold;
}

.edit-info-button {
  display: block;
  margin: 20px auto;
  padding: 10px 20px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
  transition: background-color 0.3s;
}

.edit-info-button:hover {
  background-color: #45a049;
}

</style>


