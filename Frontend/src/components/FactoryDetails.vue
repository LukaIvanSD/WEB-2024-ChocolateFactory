<template>
  <div class="factory-card">
    <div class="basic-info">
    <img :src="factory.logoPath" alt="Factory Logo" class="factory-logo"/>
    <div class="details-wrapper">
      <div class="factory-details">
        <h2>{{ factory.name }}</h2>
        <p><strong>Opens at:</strong> {{ factory.openFrom }}</p>
        <p><strong>Closes at:</strong> {{ factory.openTo }}</p>
        <p><strong>Status:</strong> <span :class="statusClass">{{ factory.status }}</span></p>
        <p><strong>Rating:</strong> {{ factory.rating }}</p>
      </div>
      <div class="location-details">
        <h2>Address:</h2>
        <p>{{ location.address }}</p>
        <p>{{ location.city }}  {{ location.postcode }}</p>
        <p>{{ location.latitude }} {{ location.longitude }}</p>
        <button class="map-button" @click="ShowMap()">
          <i style="margin-right: 10px;" class="fa fa-map-marker" aria-hidden="true"></i> View on Map
        </button>
      </div>
      <div class="removeFactory" v-if="user.userType==='Administrator'">
                <button class="removeFactoryButton" @click="RemoveFactory()">Remove factory</button>
      </div>
    </div>
    </div>
    <Map v-if="showMap" :location="location"></Map>
  </div>
</template>
  
  <script setup>
  import { onMounted, ref, computed } from 'vue';
  import { useRoute } from 'vue-router';
  import axios from 'axios';
  import Map from '@/components/Map.vue';
  import { useToast } from 'vue-toastification';
  import { reactive } from 'vue';
import router from '@/router';
import { useRouter } from 'vue-router';
const Router=useRouter();
  const route = useRoute();
  const factory = ref({});
  const toast = useToast();
  const location = ref({});
  const factoryId = route.params.id;
  const showMap = ref(false);
  const user = reactive(JSON.parse(localStorage.getItem('user')) || {});
  const statusClass = computed(() => {
    return factory.value.status === 'Open' ? 'status-open' : 'status-closed';
  });
  
  onMounted(() => {
    axios.get('http://localhost:8080/WebShopAppREST/rest/factories/show/' + factoryId)
      .then(response => {
        const data = response.data;
        factory.value = data.factory;
        location.value = data.location;
      })
      .catch(error => {
        alert(error);
      });
  });
  function ShowMap() {
    showMap.value = !showMap.value;
  }
  function RemoveFactory() {
    axios.defaults.withCredentials = true;
    axios.delete('http://localhost:8080/WebShopAppREST/rest/factories/' + factoryId)
      .then(response => {
        toast.success('Factory removed successfully');
        Router.push({ name: 'MainPage' });
      })
      .catch(error => {
        alert(error);
      });
  }
  </script>
  
  <style scoped>
  .removeFactoryButton{
    min-width: 120px;
    padding: 10px 10px;
    background-color: #a41906;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}
.removeFactoryButton:hover{
    background-color: #d32f2f;

}
  .factory-card {
    display: flex;
    flex-direction: column;
    align-items: flex-start; 
    background-color: #f5f5f5;
    border: 1px solid #ddd;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    padding:20px;
    width: 95vh;
    margin: 20px auto;
  }
  .basic-info
  {
    width: 100%;
    display: flex;
    justify-content: space-evenly;

  }
  .factory-logo {
    max-width: 300px;
    max-height: 200px;
    margin-right: 50px; /* Razmak između slike i teksta */
  }
  
  .details-wrapper {
    display: flex;
    justify-content: space-between;
    width: 100%;
  }
  
  .factory-details, .location-details {
    text-align: left;
    width: 45%;
  }
  
  .factory-details h2 {
    margin-bottom: 10px;
    color: #4b2e20;
  }
  .location-details h2 {
    margin-bottom: 10px;
    color: #4b2e20;
  }
  
  .factory-details p, .location-details p {
    margin: 5px 0;
    color: #333;
  }
  
  .factory-details p strong, .location-details p strong {
    color: #4b2e20;
  }
  
  .status-open {
    color: green;
  }
  
  .status-closed {
    color: red;
  }
  .map-button {
  margin-top: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 10px 15px;
  cursor: pointer;
  display: flex;
  align-items: center;
}
  </style>
  