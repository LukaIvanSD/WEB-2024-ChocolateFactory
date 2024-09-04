import './assets/main.css';

import { createApp } from 'vue';
import { ref } from 'vue';
import App from './App.vue';
import router from './router';
import axios from 'axios';
import Toast, { POSITION } from 'vue-toastification';
import 'vue-toastification/dist/index.css';
import store from './store';

const app = createApp(App);

const options = {
  position: POSITION.BOTTOM_RIGHT,
  /*timeout: 20000,
  closeOnClick: true,
  pauseOnFocusLoss: true,
  pauseOnHover: true,
  draggable: true,
  draggablePercent: 0.6,*/
};

app.use(router);
app.use(Toast, options);
app.use(store);

const user = ref("");

function checkSession() {
  axios.get('http://localhost:8080/WebShopAppREST/rest/currentUser', { withCredentials: true })
    .then(response => {
      if (response.data !== "") {
        user.value = response.data;
      } else if (user.value !== ""){
        user.value="";
        alert("You session has expired. Redirecting to main page...");
        router.push({ name: 'MainPage' });
      }
    })
    .catch(() => {
      router.push({ name: 'MainPage' });
    });
}

setInterval(checkSession, 0.1 * 60 * 1000);

app.mount('#app');
