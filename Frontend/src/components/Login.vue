<template>
    <div class="login">
        <h1>Login</h1>
        <div class="registerSection">
        <p>Dont have an account?</p>
        <p @click="ShowRegistrationForm()" class="register">Register.</p>
        </div>
        <form class="login-component" @submit.prevent="login">
            <div>
                <label for="username">Username:</label>
                <input type="text" id="username" v-model="username" required>
            </div>
            <div>
                <label for="password">Password:</label>
                <input type="password" id="password" v-model="password" required>
            </div>
            <button class="submit" type="submit">Login</button>

            <br>

            <p id="success">{{ success }}</p>
            <p id="error">{{ errorMessage }}</p>
        </form>
    </div>
</template>

<script setup>
import axios from 'axios';
import { ref } from 'vue';
import{useRouter} from 'vue-router';
import { useToast } from 'vue-toastification';
import{defineProps,defineEmits} from 'vue';
const props=defineProps({
    showCart:Boolean,
    default: false
});
const emits=defineEmits(['closeLogin']);
const toast=useToast();
const username = ref('');
const password = ref('');
const router=useRouter();
const errorMessage = ref('');
const success = ref('');
axios.defaults.withCredentials = true;
const login = () => {
    axios.post('http://localhost:8080/WebShopAppREST/rest/login', { username: username.value, password: password.value })
    .then(response => {
        toast.success('Log in successful!');
        console.log(response.data);
            saveCart();
            sessionStorage.removeItem('cartArticles');
            LoadMainPage();
            if(props.showCart)
                emits('closeLogin');
        
    })
    .catch(error => {
        if (error.response && error.response.status === 403) {
            const responseMessage = error.response.data;
            const dateMatch = responseMessage.match(/(\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2})/);
            if (dateMatch) {
                const blockedDate = dateMatch[0];
                errorMessage.value = `${formatBlockedDate(blockedDate)}`;
            } else {
                errorMessage.value = 'User is blocked';
            }
            return;
        }
        success.value = '';
        errorMessage.value = 'Pogresno korisnicko ime ili lozinka.';
        console.error(error);
    });
}
function saveCart() {
    const jsonString = JSON.parse(sessionStorage.getItem('cartArticles'))|| [];
    console.log(jsonString);
    axios.post('http://localhost:8080/WebShopAppREST/rest/cart/save',jsonString)
    .then(response => {
      console.log(response.data);
    })
    .catch(error => {
    });
}
function formatBlockedDate(blockedDate) {
    const date = new Date(blockedDate);
    const options = {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric',
        hour12: true
    };
    const formattedDate = date.toLocaleString('en-US', options);
    return `Your account has been blocked at ${formattedDate}`;

}
function LoadMainPage(){
    router.push({name:'MainPage'});

}
function ShowRegistrationForm(){
    router.push({name:'registerUser'});
}

</script>

<style scoped>
.login
{    
    display: flex;
    flex-direction: column;
    margin:0 auto;
    width: 400px;
}
.login-component {
    display: flex;
    flex-direction: column;
    gap: 1rem;
    margin: 0 auto;
    width: 400px;
}

.login-component label {
    font-weight: bold;
    display: block;
}

.login-component input {
    padding: 0.5rem;
    border: 1px solid #797979;
    border-radius: 0.25rem;
    background-color: #555555;
    color: white;
    display: block;
    width: 100%;
}

.login-component>.submit {
    padding: 0.5rem;
    border: none;
    border-radius: 0.25rem;
    background-color: #007bff;
    color: white;
    cursor: pointer;
}

.login-component>.submit:hover {
    background-color: #0056b3;
}

p {
margin: 0;
}
.registerSection{
    display: flex;
    flex-direction: row;
    gap: 5px;
    margin-bottom: 10px;
}
.register{
    color:#007bff;
    cursor: pointer;
}
.register:hover{
    color: #0056b3;
}
h1{
    margin:0;

}
/* Add your component-specific styles here */
</style>