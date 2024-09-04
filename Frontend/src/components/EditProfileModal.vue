<template>
  <div class="modal-overlay">
  <div class="container" >
    <img src="../../src/assets/close.png" class="close-button" @click="close()">
    <div class="form-wrapper">
      <form class="user-form" @submit.prevent="updatePersonalInfo">
        <h2>Personal Info</h2>
        <div class="form-group">
          <label for="firstName">First Name</label>
          <input type="text" id="firstName" v-model="userInfo.firstName" required>
        </div>
        
        <div class="form-group">
          <label for="lastName">Last Name</label>
          <input type="text" id="lastName" v-model="userInfo.lastName" required>
        </div>
        
        <div class="form-group">
          <label for="sex">Sex</label>
          <select id="sex" v-model="userInfo.sex" required>
            <option value="Male">Male</option>
            <option value="Female">Female</option>
          </select>
        </div>
        
        <div class="form-group">
          <label for="dateOfBirth">Date of Birth</label>
          <input type="date" id="dateOfBirth" v-model="userInfo.dateOfBirth" required>
        </div>
        <button type="submit" class="update-button" :disabled="!isPersonalInfoValid">Update Information</button>
      </form>

      <form class="user-form" @submit.prevent="updatePassword">
        <h2>Password</h2>
        <div class="form-group">
          <label for="oldPassword">Username</label>
          <input readonly="true" type="text" id="username" v-model="currentUser.username">
        </div>
        <div class="form-group">
          <label for="oldPassword">Old Password</label>
          <input type="password" id="oldPassword" v-model="passwords.oldPassword" required>
        </div>
        
        <div class="form-group">
          <label for="newPassword">New Password</label>
          <input type="password" id="newPassword" v-model="passwords.newPassword" required>
        </div>
        
        <div class="form-group">
          <label for="confirmNewPassword">Confirm New Password</label>
          <input type="password" id="confirmNewPassword" v-model="passwords.confirmNewPassword" required>
        </div>
        
        <button type="submit" class="update-button" :disabled="!isFormValid">Change Password</button>
      </form>
    </div>
  </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import {defineProps,defineEmits} from 'vue'
import {useToast} from 'vue-toastification';

const toast=useToast();
const router = useRouter();
axios.defaults.withCredentials = true;
const emits=defineEmits(["closeModal"]);
const currentUser = ref([]);

const userInfo = ref({
  firstName: '',
  lastName: '',
  sex: '',
  dateOfBirth: '',
  password: ''
});

const sendUserInfo = ref({
  firstName: '',
  lastName: '',
  sex: '',
  dateOfBirth: '',
  password: ''
});


const passwords = ref({
  oldPassword: '',
  newPassword: '',
  confirmNewPassword: ''
});

const isPersonalInfoValid = computed(() => {
  return userInfo.value.firstName != "" && userInfo.value.lastName != ""
    && (userInfo.value.dateOfBirth != currentUser.value.dateOfBirth || 
    userInfo.value.sex != currentUser.value.sex ||
    userInfo.value.lastName != currentUser.value.lastName ||
      userInfo.value.firstName != currentUser.value.firstName);
});

const isFormValid = computed(() => {
  return passwords.value.oldPassword && passwords.value.newPassword && passwords.value.oldPassword === currentUser.value.password
          && passwords.value.confirmNewPassword && passwords.value.newPassword === passwords.value.confirmNewPassword;
});

onMounted(() => {
  load();
});

function load(){
  axios.get('http://localhost:8080/WebShopAppREST/rest/currentUser')
    .then(response => {
      currentUser.value = response.data;
      userInfo.value.firstName = currentUser.value.firstName;
      userInfo.value.lastName = currentUser.value.lastName;
      userInfo.value.sex = currentUser.value.sex;
      userInfo.value.dateOfBirth = currentUser.value.dateOfBirth;
      passwords.value.oldPassword = "";
      passwords.value.newPassword = "";
      passwords.value.confirmNewPassword = "";
      console.log(currentUser.value);
      console.log(userInfo.value)
    })
    .catch(error => {
      console.log(error);
    });
}


function logOut() {
  axios.post('http://localhost:8080/WebShopAppREST/rest/logout')
    .then(response => {
      router.push({ name: 'ghost' });
    })
    .catch(error => {
      console.log(error);
    });
}

function PurchaseHistory() {
  router.push({ name: 'customerPurchases' });
}

function updatePersonalInfo() {
    axios.post('http://localhost:8080/WebShopAppREST/rest/profile', userInfo.value)
      .then(response => {
        toast.success('User info updated successfully!');
        close();
      })
      .catch(error => {
        console.log(error);
        alert('An error occurred while updating user information.');
      });
}

function updatePassword() {
  if(isFormValid){
    sendUserInfo.value.password = passwords.value.confirmNewPassword;
    sendUserInfo.value.sex = userInfo.value.sex;
    sendUserInfo.value.dateOfBirth = userInfo.value.dateOfBirth;
  axios.post('http://localhost:8080/WebShopAppREST/rest/profile', sendUserInfo.value)
      .then(response => {
        toast.success('Password updated successfully!');
        close();
      })
      .catch(error => {
        console.log(error);
        alert('An error occurred while updating user information.');
      });
  }
}

function close(){
  emits('closeModal');
  load();
}

</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}
.container {
  max-width: 1200px;
  margin: 50px auto;
  color: #4b2e20;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  align-items: center;
}

.close-button {
  position: fixed;
  top: calc(50% - 240px);  
  left:calc(50% +  180px);
  width: 30px;
  height: 30px;
  cursor: pointer;
}

.form-wrapper {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

h1, h2 {
  text-align: center;
  margin-bottom: 20px;
}

.logout-button {
  padding: 10px 20px;
  background-color: #dc3545;
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

.user-form {
  display: flex;
  flex-direction: column;
  width: 45%;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input, select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ced4da;
  border-radius: 4px;
  box-sizing: border-box;
}

.update-button {
  padding: 10px 20px;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 20px;
}

.update-button:hover, .logout-button:hover, .purchaseHistory-button:hover {
  opacity: 0.8;
}

.update-button:disabled {
  background-color: #6c757d;
  cursor: not-allowed;
}
</style>
