import { createRouter, createWebHistory } from 'vue-router'
import { reactive } from 'vue';
import Login from '@/views/Login.vue'
import FactoryDetailsGhost from '@/views/GhostView/FactoryDetails.vue'
import FactoryDetailsAdmin from '@/views/AdminView/FactoryDetailsAdmin.vue'
import FactoryDetailsCustomer from '@/views/CustomerView/FactoryDetailsCustomer.vue'
import Cart from '@/views/CustomerView/CartView.vue'
import GhostCart from '@/views/GhostView/CartView.vue'
import Profile from '@/views/Profile.vue'
import PurchaseHistory from '@/views/CustomerView/PurchaseHistoryView.vue'
import FactoryPurchaseHistory from '@/views/ManagerView/PurchaseHistoryView.vue'
import FactoryDetailsManager from '@/views/ManagerView/FactoryDetailsManager.vue'
import FactoryDetailsWorker from '@/views/WorkerView/FactoryDetailsWorker.vue'
import UserList from '@/views/AdminView/UserListView.vue'
import Registration from '@/views/Registration.vue'
import AddFactory from '@/views/AdminView/AdminAddFactory.vue'
import MainPage from '@/views/MainPage.vue'
import axios from 'axios'

const user = reactive(JSON.parse(localStorage.getItem('user')) || {});

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'MainPage',
      component: MainPage,
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/factory/:id',
      name: 'factoryDetailsGhost',
      component: FactoryDetailsGhost,
      beforeEnter: (to, from, next) => {
        user.value = JSON.parse(localStorage.getItem('user'));
        if (user.value.userType === 'Customer') {
          next({ name: 'factoryDetailsCustomer', params: { id: to.params.id } });
        } else if (user.value.userType === 'Worker') {
          next({ name: 'factoryDetailsWorker', params: { id: to.params.id } });
        } else if (user.value.userType === 'Manager') {
          next({ name: 'factoryDetailsManager', params: { id: to.params.id } });
        } else if (user.value.userType === 'Administrator') {
          next({ name: 'factoryDetailsAdmin', params: { id: to.params.id } });
        } else {
          next();
        }
      }
    },
    {
      path: '/factory/:id/admin',
      name: 'factoryDetailsAdmin',
      component: FactoryDetailsAdmin
    },
    {
      path: '/factory/:id/customer',
      name: 'factoryDetailsCustomer',
      component: FactoryDetailsCustomer
    },
    {
      path: '/purchases',
      name: 'customerPurchases',
      component: PurchaseHistory
    },
    {
      path: '/cart',
      name: 'customerCart',
      component: Cart
    },
    {
      path: '/ghostCart',
      name: 'ghostCart',
      component: GhostCart,
      beforeEnter: (to, from, next) => {
        user.value = JSON.parse(localStorage.getItem('user'));
        console.log(user.value);
        if (user.value.userType === undefined) {
          next();
        } else if(user.value.userType === 'Customer'){
          next({ name: 'customerCart' });
        }
        else {
          next({ name: 'MainPage' });
        }
      }
    },
    {
      path: '/factoryPurchases',
      name: 'factoryPurchases',
      component: FactoryPurchaseHistory
    },
    {
      path: '/users',
      name: 'usersList',
      component: UserList
    },
    {
      path: '/factory/:id/manager',
      name: 'factoryDetailsManager',
      component: FactoryDetailsManager
    },
    {
      path:'/profile',
      name:'profile',
      component: Profile
    },
    {
      path: '/factory/:id/worker',
      name: 'factoryDetailsWorker',
      component: FactoryDetailsWorker
    },
    {
      path: '/registration',
      name: 'registerUser',
      component: Registration
    },
    {
      path:'/addFactory',
      name:'addFactory',
      component: AddFactory
    }
  ]
});
router.beforeEach((to, from, next) => {
  axios.get('http://localhost:8080/WebShopAppREST/rest/currentUser', { withCredentials: true })
    .then(response => {
      user.value = response.data;
      localStorage.setItem('user', JSON.stringify(response.data));
      if(user.value.userType===undefined && !((to.name.includes('Ghost'))||(to.name.includes('MainPage')|| to.name.includes('login')||to.name.includes('Login') || to.name.includes('ghostCart')||to.name.includes('registerUser'))))
        next({ name: 'MainPage' });
      next();
    })
    .catch(() => {
      });
});
export default router;
