<template>
    <div class=registration>
      <div>
      <h1 class="header">Registration</h1>
      <form class="register-component" @submit.prevent="RegisterUser()">
        <div>
          <label for="username">Username:</label>
          <input type="text" id="username" v-model="user.username" @focusout="SetFocus('username')" @focusin="ResetFocus('username')" required>
          <label v-if="focusStates.username && user.username===''" class="error">Username not valid</label>
          <label class="error" v-if="usernameExists" >Username already exists</label>
        </div>
        <div>
          <label for="password">Password:</label>
          <input type="password" id="password" @focusout="SetFocus('password')" @focusin="ResetFocus('password')" v-model="user.password" required>
          <label  v-if="focusStates.password && user.password===''" class="error" >Password not valid</label>
        </div>
        <div>
          <label for="passwordAgain">Password:</label>
          <input type="password" id="passwordAgain" @focusout="SetFocus('passwordAgain')" @focusin="ResetFocus('passwordAgain')" v-model="passwordAgain" required>
          <label class="error" v-if="focusStates.passwordAgain && user.password!==passwordAgain">Passwords do not match</label>
        </div>
        <div>
          <label for="name">Name:</label>
          <input type="text"  @focusout="SetFocus('name')" @focusin="ResetFocus('name')" id="name" v-model="user.firstName" required>
          <label v-if="focusStates.name && user.firstName===''" class="error" >Name not valid</label>
        </div>
        <div>
          <label for="surname">Surname:</label>
          <input type="text"  @focusout="SetFocus('surname')" @focusin="ResetFocus('surname')" id="surname" v-model="user.lastName" required>
          <label class="error" v-if="focusStates.surname && user.lastName===''" >Surname not valid</label>
        </div>
        <div>
          <label for="sex">Sex:</label>
          <select id="sex" v-model="user.sex">
            <option value="Male">Male</option>
            <option value="Female">Female</option>
          </select>
        </div>
        <div>
          <label for="birth">Date of birth:</label>
          <input type="date"  @focusout="SetFocus('dateOfBirth')" @focusin="ResetFocus('dateOfBirth')" id="birth" v-model="user.dateOfBirth" required>
          <label class="error" v-if="focusStates.dateOfBirth && user.dateOfBirth===''" >Date of birth not valid</label>
        </div>
        <button class="submit" type="submit">Register</button>
  
        <br>
  
        <p id="success">{{ success }}</p>
        <p id="error">{{ errorMessage }}</p>
      </form>
      </div>
    </div>
  </template>
  
  <script setup>
  import axios from 'axios';
  import { ref, defineEmits, defineProps } from 'vue';
  import { useRouter } from 'vue-router';
  import { useToast } from 'vue-toastification';
  import { reactive } from 'vue';
  axios.defaults.withCredentials = true;

  const router=useRouter();
  const toast=useToast();
  const emit = defineEmits(['userSubmitted']);
const props = defineProps({
  preventBackend: {
    type: Boolean,
    default: false
  }
});

const focusStates=ref( {
      username: false,
      email: false,
      password: false,
      passwordAgain: false,
      name: false,
      surname:false,
      dateOfBirth: false
    });
  const user = ref({
    username: '',
    password: '',
    firstName: '',
    lastName: '',
    sex: 'Male',
    dateOfBirth: ''
  });
  var usernameExists = ref(false);
  var passwordAgain = ref('');
  const success = ref('');
  const errorMessage = ref('');


  function SetFocus(field) {
    focusStates.value[field] = true;
  }
  function ResetFocus(field) {
    focusStates.value[field] = false;
  }
  function RegisterUser() {

    if (props.preventBackend) {
      axios.post('http://localhost:8080/WebShopAppREST/rest/register/checkUsername',user.value.username).then(response => {
        if(response.data==true)
        {
          emit('userSubmitted', user.value);
          success.value = 'User data submitted!';
          errorMessage.value = '';
        }
        else
        usernameExists.value=true;
      })
  } else {
    axios.post('http://localhost:8080/WebShopAppREST/rest/register/', user.value)
      .then(response => {
        // Handle successful response
        success.value = 'Registration successful!';
        errorMessage.value = '';
        usernameExists.value=false;
        GenerateToastNotification();
        router.back(); 
      })
      .catch(error => {
        success.value = '';
        if(error.response.data=="Username already exists"){
          usernameExists.value=true;
        }
        else{
          usernameExists.value=false;
        }
        console.log(error.response.data);
        errorMessage.value = 'Form not valid!';
        console.error(error);
      });
  }
  }
  function GenerateToastNotification() {
    const loggedUser = reactive(JSON.parse(localStorage.getItem('user')) || {});
    if(loggedUser.userType===undefined)
    toast.success('Registration successful!');
    else if(loggedUser.userType==='Manager')
    {
      toast.success('Worker successfully added!');
    }
  }
  </script>
  
  <style scoped>
  .registration{
    position:absolute;
    top:50px;
    left: 50%;
    transform: translateX(-50%); 
  }
  .register-component {
    display: flex;
    flex-direction: column;
    gap: 1rem;
    margin: 0 auto;
    width: 400px;
  }
  
  .register-component label {
    font-weight: bold;
    display: block;
  }
  
  .register-component input, .register-component select {
    padding: 0.5rem;
    border: 1px solid #797979;
    border-radius: 0.25rem;
    background-color: #555555;
    color: white;
    display: block;
    width: 100%;
  }
  
  .register-component>.submit {
    padding: 0.5rem;
    border: none;
    border-radius: 0.25rem;
    background-color: #007bff;
    color: white;
    cursor: pointer;
  }
  
  .register-component>.submit:hover {
    background-color: #0056b3;
  }
  .error {
    color: #FF4C4C;
    font-size: 15px;
  }
  p {
    display: block;
  }
  .header {
    text-align: center;
  }
  </style>