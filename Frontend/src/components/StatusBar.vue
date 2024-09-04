<template>
    <div class="transparent"></div>
    <div class="statusBar">
        <div class="logoSection" @click="Home()">
            <img class="logo" src="../assets/logo.png" alt="Logo" />
            <h1 >Chocolate factory</h1>
        </div>
        <div class="searchFunctions">
        <div class="searchBar">
            <input type="text" class="searchInput" @keydown.enter="SearchFactories" placeholder="Search..." v-model="searchQuery"/>
            <button class="searchButton" @click="SearchFactories()">
                <img class="searchIcon" src="../assets/search1.png" alt="Search" />
            </button>
        </div>
        <div class="functions">
            <div class="register" v-if="user.userType===undefined">
                <button class="loginRegisterButton" @click="LogIn()">Login / Register</button>
            </div>
            <div class="myFactory" v-if="user.userType==='Manager' || user.userType==='Worker'" @click="MyFactory()">
                <img class="factoryLogo" src="../assets/factory.png" alt="Profile" />
                <p>My factory</p>
            </div>
            <div  v-if="user.userType==='Administrator'">
                <img class="cartLogo" src="../assets/userList.png" alt="Purchases" @click="ShowUsers()"/>
            </div>
            <div  v-if="user.userType==='Customer' || user.userType==='Manager'">
                <img class="cartLogo" src="../assets/purchases.png" alt="Purchases" @click="PurchaseHistory()"/>
            </div>
            <div class="cart" v-if="user.userType==='Customer' || user.userType===undefined">
                <label v-if="user.userType===undefined || user.userType === 'Customer'" class="numOfArticles">{{ numberOfCartAricles }}</label>
                <img class="cartLogo" src="../assets/cart.png" alt="Cart" @click="openCart()" />
            </div>
            <div class="addFactory" v-if="user.userType==='Administrator'">
                <button class="addFactoryButton" @click="AddFactory()">Add factory</button>
            </div>
            <div class="profile" @click="ShowProfile()" v-if="user.userType!==undefined">
                <img class="profileLogo" :src="profile" alt="Profile" />
                <p>{{ user.firstName }}</p>
            </div>
            <div class="logout" v-if="user.userType!==undefined">
                <button class="logoutButton" @click="LogOut()">Logout</button>
            </div>
        </div>
        </div>
    </div>
    </template>
<script setup>
import { reactive } from 'vue';
import { ref } from 'vue';
import axios from 'axios';
import { stringifyQuery, useRouter } from 'vue-router';
import { onMounted,onUnmounted } from 'vue';
import profileMale from '../assets/profileMale.png';
import profileFemale from '../assets/profileFemale.png';
import { useStore } from 'vuex';
import { watch } from 'vue';
import { parse } from 'vue/compiler-sfc';
const store = useStore();
const searchQuery = ref('');
const router = useRouter();
const numberOfCartAricles = ref(0);
const user = reactive(JSON.parse(localStorage.getItem('user')) || {});
const profile = ref("");
    onMounted(() => {
        window.addEventListener('storageArticles', calculateNumOfArticles);
        searchQuery.value = store.state.searchQuery;
        if(user.sex==='Male')
            profile.value = profileMale;
            else
            {
                profile.value = profileFemale;
            }
        if(user.userType === 'Customer'){
            if(sessionStorage.getItem('cartArticles')!==null)
            calculateNumOfArticles();
            else{
                axios.defaults.withCredentials = true;
                axios.get('http://localhost:8080/WebShopAppREST/rest/cart/numberOfCartAricles')
                .then(response => {
                    sessionStorage.setItem('cartArticles', JSON.stringify(response.data));
                    calculateNumOfArticles();
                })
                .catch(error => {
                        console.log(error);
                });
            }
        }
        else if(user.userType===undefined && sessionStorage.getItem('cartArticles')!==null) 
        {
            calculateNumOfArticles();
        }
    });
    onUnmounted(() => {
      window.removeEventListener('storageArticles', calculateNumOfArticles);
    });
    function calculateNumOfArticles(){
        const cartArticles = JSON.parse(sessionStorage.getItem('cartArticles'));
        numberOfCartAricles.value = cartArticles.length;
    }

    function LogOut(){
        axios.defaults.withCredentials = true;
        axios.post('http://localhost:8080/WebShopAppREST/rest/logout').then(response => {
            ClearSearch();
            sessionStorage.removeItem('cartArticles');
            router.go();
            })
        .catch(error => {
                console.log(error);
            });
    }


    function LogIn(){
        ClearSearch();
        router.push('/login');
    }

    function openCart(){
        ClearSearch();
        router.push('/ghostCart');
    }

    function AddFactory(){
        ClearSearch();
        router.push('/addFactory');
    }

    function PurchaseHistory(){
        ClearSearch();
        if(user.userType==='Customer'){
            router.push({ name: 'customerPurchases' });
        }            
        else if(user.userType==='Manager'){
            router.push({ name: 'factoryPurchases' });
        }
    }

    function ShowUsers(){
        ClearSearch();
        router.push({ name: 'usersList' });
    }

    function MyFactory(){
        axios.defaults.withCredentials = true;
        ClearSearch();
        if(user.userType==='Worker')
        axios.get('http://localhost:8080/WebShopAppREST/rest/workers/myFactory')
        .then(response => {
            if(response.data.length!==0 && response.data!==-1)
            {
                console.log(response.data)
                router.push({ name: 'factoryDetailsGhost', params: { id: response.data } }).then(() => {
                    router.go();
                });
            }
        })
        .catch(error => {
            console.log(error);
        });
        else if(user.userType==='Manager')
        axios.get('http://localhost:8080/WebShopAppREST/rest/managers/myFactory')
        .then(response => {
            if(response.data.length!==0 && response.data!==-1)
            {
                console.log("gas");
                router.push({ name: 'factoryDetailsGhost', params: { id: response.data } }).then(() => {
                    router.go();
                });
            }
            else if (response.data===-1)
            {
                alert("You are currently unemployed!");
            }
        })
        .catch(error => {
            console.log(error);
        });
    }
    function SearchFactories() {
        router.push('/')
        store.dispatch('updateSearchQuery', searchQuery.value);
    }
    function Home(){
        ClearSearch();
        router.push('/');
    }
function  ClearSearch(){
    searchQuery.value = '';
    store.dispatch('updateSearchQuery', searchQuery.value);
}
function ShowProfile(){
    ClearSearch();
    router.push('/profile');
}
</script>


<style scoped>
p{
     color: #333;
}
h1{
    color: #333;
}
.statusBar {
    position: fixed; 
    top: 30px;
    right: 10px; /* Dodato za udaljavanje od desne ivice */
    width: calc(100% - 20px); /* Širina ekrana minus 60px (30px levo + 30px desno) */
    height: 70px;
    padding: 10px 20px;
    background-color: #f5f5f5;
    color: white;
    border: none;
    border-radius: 20px;
    z-index: 1000;
    display: flex;
    align-items: center;
    flex-direction: row;
}
.statusBar::before {
    content: '';
    position: absolute;
    top: -30px; /* Udaljenost iznad status bara */
    left: 0;
    width: 100%;
    height: 30px; /* Visina prekrivača */
    background-color: #181818; /* Nasleđuje boju pozadine */
    z-index: 1000; /* Ista visina sloja kao i status bar */
}
.logoSection {
    width: 400px;
    display: flex;
    margin-left: 15px;
    align-items: center;
    flex-direction: row;
    gap: 15px;
    margin-right: 20px;
    cursor: pointer;
}
.logo{
    width: 50px;
    height: 50px;
    border: 1px solid #007BFF;
    border-radius: 15%;
}

.cart {
  position: relative; /* Postavljamo relativno pozicioniranje da bi apsolutno pozicionirani elementi bili u odnosu na njega */
  display: inline-block; /* Da bi se elementi prikazivali jedan pored drugog */
}

.numOfArticles {
  position: absolute; /* Postavljamo apsolutno pozicioniranje */
  top: -4px; /* Pomeramo na vrh */
  right: -4px; /* Pomeramo udesno */
  font-weight:bold; /* Debljina fonta */
  background-color: rgb(158, 4, 4);
  padding-left: 1px;
  border: 1px  #007BFF;
  border-radius: 3px;
  padding-right: 1px;
  color:white ;/* Bela boja teksta */ /* Dodajemo malu razdaljinu od ivica */
  font-size: 10px; /* Veličina fonta */
}
.searchFunctions{
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-direction: row;
    width: 100%;

}

.searchIcon
{
    width: 20px;
    height: 20px;
}
.searchBar{
    display: flex;
    align-items: center;
    flex-direction: row;

}
.searchButton
{
    background-color: #007BFF;
    border: none;
    width: 45px;
    border-radius: 50%;
    cursor: pointer;
    height: 46px;
    margin-left: -30px;
    margin-top: 0.5px;

}
.searchInput{
    height:44px;
    width: 500px;
    font-size: 20px;
    padding-left: 10px;
}
.functions{
    display: flex;
    align-items: center;
    flex-direction: row;
    gap: 20px;
}
.loginRegisterButton{
    padding: 10px 20px;
    background-color: #007BFF;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}
.loginRegisterButton:hover{
    background-color: #0056b3;
}
.logoutButton{
    padding: 10px 20px;
    background-color: #007BFF;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}
.logoutButton:hover{
    background-color: #0056b3;
}
.cartLogo{
    margin-top: 4px;
    width: 28px;
    height: 28px;
}
.profile{
    display: flex;
    align-items: center;
    flex-direction: row;
    gap: 2px;

}
.profileLogo{
    width: 28px;
    height: 28px;
    border-radius: 50%;
}
.addFactoryButton{
    padding: 10px 20px;
    background-color: #007BFF;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}
.addFactoryButton:hover{
    background-color: #0056b3;
}
.myFactory{
    display: flex;
    align-items:center;
    flex-direction: row;
    gap: 2px;
}
.factoryLogo{
    width: 28px;
    height: 28px;
}
</style>