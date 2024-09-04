<template>
  <div>
    <div class="tabs">
      <button class="tabButton" v-for="(tab, index) in tabs" :key="index" @click="changeTab(tab)" :class="{ 'active-tab': currentTab === tab }">
        {{ tab }}
      </button>
    </div>

    <div class="customer-type-tabs" v-if="currentTab === 'Customers'">
      <button v-for="(typeTab, index) in customerTypeTabs" :key="index" @click="currentCustomerTypeTab = typeTab" :class="{ 'active-tab': currentCustomerTypeTab === typeTab }">
        {{ typeTab }}
      </button>
    </div>

    <div class="user-list">
      <div class="search-sort-container">
        <div class="search-bar">
          <input type="text" v-model="searchQuery" placeholder="Search..." class="search-input">
        </div>
        
        <div class="sort-options">
          <label for="sortBy">Sort By: </label>
          <select id="sortBy" v-model="sortBy" class="sort-select">
            <option value="firstName_ASC">First Name (A-Z)</option>
            <option value="firstName_DESC">First Name (Z-A)</option>
            <option value="lastName_ASC">Last Name (A-Z)</option>
            <option value="lastName_DESC">Last Name (Z-A)</option>
            <option value="username_ASC">Username (A-Z)</option>
            <option value="username_DESC">Username (Z-A)</option>
            <option v-if="currentTab === 'Customers'" value="bonusPoints_ASC">Bonus Points ASC</option>
            <option v-if="currentTab === 'Customers'" value="bonusPoints_DESC">Bonus Points DESC</option>
          </select>
        </div>
      </div>
      <label style="margin-left: 450px;" v-if="currentTab === 'Customers'">
                <input type="checkbox" v-model="suspiciousOnly">
                Suspicious Users Only
        </label>
      <div v-for="user in sortedFilteredUsers" :key="user.id" class="user-container">
        <div class="user-details">
          <h2>{{ user.firstName }} {{ user.lastName }}</h2>
          <p><strong>Username:</strong> {{ user.username }}</p>
          <p><strong>Sex:</strong> {{ user.sex }}</p>
          <p><strong>Date of Birth:</strong> {{ user.dateOfBirth }}</p>
          <p><strong>User Type:</strong> {{ user.userType }}</p>
          <p v-if="user.userType === 'Customer'"><strong>Customer Type:</strong> {{ getCustomerType(user.customerTypeId) }}</p>
          <p v-if="user.userType === 'Customer' &&  currentTab !== 'Customers'"><strong>Bonus points:</strong> {{ getBonusPoints(user.id) }}</p>
          <p v-if="user.userType === 'Customer' &&  currentTab === 'Customers'"><strong>Bonus points:</strong> {{ user.bonusPoints }}</p>
        </div>
        <div class="user-actions" v-if="user.userType === 'Customer' && currentTab !== 'Customers'">
          <button 
            class="block-button" 
            :disabled="!isSuspiciousAndNotBlocked(user.id)"
            @click="blockUser(user.id)">
            Block
          </button>
          <button 
            class="unblock-button" 
            :disabled="!blockedUsers.includes(user.id)"
            @click="unblockUser(user.id)">
            Unblock
          </button>
        </div>
        <div class="user-actions" v-if="user.userType === 'Customer' && currentTab === 'Customers'">
          <button 
            class="block-button" 
            :disabled="!isSuspiciousAndNotBlocked(user.userId)"
            @click="blockUser(user.userId)">
            Block
          </button>
          <button 
            class="unblock-button" 
            :disabled="!blockedUsers.includes(user.userId)"
            @click="unblockUser(user.userId)">
            Unblock
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import axios from 'axios';
import { ref, onMounted, computed } from 'vue';
import { useToast } from 'vue-toastification';

const allUsers = ref([]);
const allSusCustomers = ref([]);
const allCustomersIncludingSus = ref([]);
const blockedUsers = ref([]);
const suspiciousUsers = ref([]);
const currentTab = ref('All Users');
const currentCustomerTypeTab = ref('All Customers');
const searchQuery = ref('');
const sortBy = ref('firstName_ASC');
const toast = useToast();
const susUsers = ref([]);
const suspiciousOnly = ref(false);

axios.defaults.withCredentials = true;

onMounted(() => {
  load();
});

function changeTab(tab){
  resetSortFilter();
  currentTab.value = tab;
}
function load() {
  axios.get('http://localhost:8080/WebShopAppREST/rest/users')
    .then(response => {
      allUsers.value = response.data;
      console.log("Loaded all users");
    })
    .catch(error => {
      alert(error);
    });
  axios.get('http://localhost:8080/WebShopAppREST/rest/users/allCustomers')
    .then(response => {
      allCustomersIncludingSus.value = response.data;
      console.log("Loaded all customers");
      console.log(allCustomersIncludingSus.value);
    })
    .catch(error => {
      alert(error);
    });
  axios.get('http://localhost:8080/WebShopAppREST/rest/users/suspicious')
    .then(response => {
      suspiciousUsers.value = response.data.map(user => user.id);
      susUsers.value = response.data;
      console.log("Loaded suspicious users");
    })
    .catch(error => {
      alert(error);
    });
  axios.get('http://localhost:8080/WebShopAppREST/rest/blockedUsers')
    .then(response => {
      blockedUsers.value = response.data.map(user => user.userId);
      console.log("Loaded blocked users");
    })
    .catch(error => {
      alert(error);
    });
}

const tabs = ['All Users', 'Managers', 'Workers', 'Customers', 'Administrators'];
const customerTypeTabs = ['All Customers', 'Basic Customers', 'Bronze Customers', 'Silver Customers', 'Gold Customers'];

const filteredUsersByType = (type) => {
  return allUsers.value.filter(user => user.userType === type.slice(0, -1));
};

const filteredCustomersByType = (typeId) => {
  return allCustomers.value.filter(customer => customer.customerTypeId === typeId).map(customer => {
    const user = allUsers.value.find(user => user.id === customer.userId);
    return { ...user, ...customer };
  });
};

const allCustomers = computed(() => {
  if(suspiciousOnly.value)
    return allCustomersIncludingSus.value.filter(customer => susUsers.value.some(susUser => susUser.id === customer.userId));
  else
    return allCustomersIncludingSus.value;
});

const filteredUsers = computed(() => {
  if (currentTab.value === 'All Users') {
    return allUsers.value;
  } else if (currentTab.value === 'Customers') {
    if (currentCustomerTypeTab.value === 'All Customers') {
      return allCustomers.value.map(customer => {
        const user = allUsers.value.find(user => user.id === customer.userId);
        console.log("HIHI")
        console.log({ ...user, ...customer });
        return { ...user, ...customer };
      });
    } else if (currentCustomerTypeTab.value === 'Basic Customers') {
      return filteredCustomersByType(0);
    } else if (currentCustomerTypeTab.value === 'Bronze Customers') {
      return filteredCustomersByType(1);
    } else if (currentCustomerTypeTab.value === 'Silver Customers') {
      return filteredCustomersByType(2);
    } else if (currentCustomerTypeTab.value === 'Gold Customers') {
      return filteredCustomersByType(3);
    }
  } else {
    return filteredUsersByType(currentTab.value);
  }
});

const sortedFilteredUsers = computed(() => {
  const [sortByField, sortOrder] = sortBy.value.split('_');
  if (currentTab.value === 'Customers' && sortByField === 'bonusPoints') {
    return filteredUsers.value.filter(user =>
      user.firstName.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      user.lastName.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      user.username.toLowerCase().includes(searchQuery.value.toLowerCase())
    ).slice().sort((a, b) => {
      const comparison = a.bonusPoints - b.bonusPoints;
      return sortOrder === 'ASC' ? comparison : -comparison;
    });
    
  } else {
    return filteredUsers.value.filter(user =>
      user.firstName.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      user.lastName.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      user.username.toLowerCase().includes(searchQuery.value.toLowerCase())
    ).slice().sort((a, b) => {
      const comparison = a[sortByField].localeCompare(b[sortByField]);
      return sortOrder === 'ASC' ? comparison : -comparison;
    });
  }
});


const isSuspiciousAndNotBlocked = (userId) => {
  return suspiciousUsers.value.includes(userId) && !blockedUsers.value.includes(userId);
};

const blockUser = (userId) => {
  axios.post(`http://localhost:8080/WebShopAppREST/rest/blockedUsers/${userId}`)
    .then(response => {
      toast.success('User blocked successfully');
      blockedUsers.value.push(userId);
      suspiciousUsers.value = suspiciousUsers.value.filter(id => id !== userId);
      load();
    })
    .catch(error => {
      console.log(userId)
      alert('Failed to block user');
    });
};

function resetSortFilter(){
  suspiciousOnly.value = false;
  sortBy.value = 'firstName_ASC';
}

const unblockUser = (userId) => {
  axios.patch(`http://localhost:8080/WebShopAppREST/rest/blockedUsers/${userId}`)
    .then(response => {
      toast.success('User unblocked successfully');
      blockedUsers.value = blockedUsers.value.filter(id => id !== userId);
      load();
    })
    .catch(error => {
      alert('Failed to unblock user');
    });
};

function getBonusPoints(userId) {
  const customer = allCustomers.value.find(customer => customer.userId === userId);
  return customer.bonusPoints;
}

const getCustomerType = (typeId) => {
  switch (typeId) {
    case 0:
      return 'Basic Customer';
    case 1:
      return 'Bronze Customer';
    case 2:
      return 'Silver Customer';
    case 3:
      return 'Gold Customer';
    default:
      return 'Unknown Customer Type';
  }
};
</script>

<style scoped>
.tabs {
  display: flex;
  justify-content: center;
}
.tabButton{
  color:#f5f5f5;
}

.tabs button {
  padding: 10px 20px;
  margin-right: 10px;
  border: none;
  background-color: transparent;
  cursor: pointer;
}

.tabs button:hover {
  background-color: #f0f0f0;
}

.active-tab {
  background-color: orange;
  color: #eb7100;
}

.customer-type-tabs {
  display: flex;
  justify-content: center;
  margin-top: 10px;
}

.customer-type-tabs button {
  padding: 5px 15px;
  margin-right: 10px;
  border: none;
  background-color: transparent;
  cursor: pointer;
}

.customer-type-tabs button:hover {
  background-color: #f0f0f0;
}

.search-sort-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 10px;
}

.search-bar {
  margin-right: 140px;
}

.search-input {
  padding: 8px;
  width: 250px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
}

.sort-options {
  margin-left: 10px;
}

.sort-select {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
}

.user-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 20px;
  align-items: center;
}

.user-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(237, 162, 0, 0.758);
  padding: 16px;
  max-width: 600px;
  width: 100%;
  text-align: left;
  transition: transform 0.2s;
}

.user-container:hover {
  transform: scale(1.02);
}

.user-container h2 {
  margin: 0 0 8px;
  font-size: 1.5em;
  color: #333;
}

.user-container p {
  margin: 4px 0;
  color: #555;
}

.user-container p strong {
  color: #000;
}

.user-actions {
  display: flex;
  flex-direction: column;
}

.block-button,
.unblock-button {
  padding: 8px 12px;
  margin-bottom: 8px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.block-button {
  margin-top: 80px;
  background-color: #36a7f4; /* Blue */
  color: #fff;
}

.unblock-button {
  background-color: orange;
  color: #fff;
}

.block-button:disabled,
.unblock-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.block-button:hover:not(:disabled),
.unblock-button:hover:not(:disabled) {
  opacity: 0.9;
}
</style>
