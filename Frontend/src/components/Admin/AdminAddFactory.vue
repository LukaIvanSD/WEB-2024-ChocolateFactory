<template>
  <div class="container">
    <h1 v-if="!IsRegistering" class="header">Register factory</h1>
    <form  @submit.prevent="RegisterFactory()">
      <div class="register-component">
      <div class="form-content" v-if="!IsRegistering">
        <div>
          <label for="name">Name:</label>
          <input type="text" id="name" v-model="factory.name" @focusout="SetFocus('name')" @focusin="ResetFocus('name')" required>
          <label v-if="focusStates.name && factory.name===''" class="error">Name not valid</label>
        </div>
        <div>
          <label for="openFrom">Open time:</label>
          <input type="time" id="openFrom" name="time" v-model="factory.openFrom" @focusout="SetFocus('openFrom')" @focusin="ResetFocus('openFrom')" required>
          <label v-if="focusStates.openFrom && factory.openFrom===''" class="error">Open time not valid</label>
        </div>
        <div>
          <label for="openTo">Close time:</label>
          <input type="time" id="openTo" name="time" v-model="factory.openTo" @focusout="SetFocus('openTo')" @focusin="ResetFocus('openTo')" required>
          <label v-if="focusStates.openTo && factory.openTo===''" class="error">Close time not valid</label>
        </div>
        <div>
          <label for="logo">Logo Url:</label>
          <input type="text" id="logo" v-model="factory.logoPath" @focusout="SetFocus('logo')" @focusin="ResetFocus('logo')" required>
          <label v-if="focusStates.logo && factory.logoPath===''" class="error">Logo url not valid</label>
        </div>
      </div>
      <div class="manager-content">
        <div v-if="HasAvailableManagers">
        <label>Available Managers</label>
        <div class="form-content">
        <div v-for="manager in managers" v-bind:key="manager.id">
          <div  :class="{'is-clicked': manager.user.id === id}" @click="ChangeColor(manager)" class="managerList">
            <label>{{ manager.user.firstName }} {{ manager.user.lastName }}</label>
            <label>{{ manager.user.sex }}</label>
          </div>
        </div>
        </div>
        </div>
        <div class="button" v-if="!HasAvailableManagers">
          <button :class="{'form-button': !IsRegistering, 'button-on-form': IsRegistering}" v-if="!HasRegisteredManager" type="button" @click="toggleRegisterForm"><!--- OVDE STAVITI JOS USLOV AKO NE ZELIM DA MOZE DA HIDUJE FORMU ISREGISTERING-->
            {{ showRegisterForm ? 'Back' : 'Register New  Manager' }}
          </button>
             <Register class="form" v-if="showRegisterForm" @userSubmitted="handleUserData" :preventBackend="true" @closeForm="toggleRegisterForm" />
          <div v-if="HasRegisteredManager" class="managerList-clicked">
            <label>{{ userData.firstName }} {{ userData.lastName }}</label>
            <label>{{ userData.sex }}</label>
          </div>
        </div>
      </div>
      
      <div class="map-content" v-if="mapVisible">
        <label for="openTo">Location:</label>
        <div class="search-container">
          <input type="text" v-model="searchQuery" placeholder="Search location" required @focusout="SetFocus('location')" @focusin="ResetFocus('location')">
          <button type="button" @click="searchLocation" class="search-button">
            <i class="fa fa-search"></i>
          </button>
        </div>
        <label v-if="focusStates.location && searchQuery===''" class="error">Location not valid</label>
        <label v-if="locationError" class="error">Location doesn't exist</label>
        <div id="map" style="width: 100%; height: 300px; margin-top:20px"></div>
      </div>
      </div>
      <div v-if="!IsRegistering" class="submit-div">
        <button class="submit" type="submit">Register factory</button>
      </div>
    </form>
    <label class="error" style="margin-top:20px;" v-if="errorMessage!=='' && !IsRegistering">{{errorMessage}}</label>

  </div>
</template>

  
<script setup>
import axios from 'axios';
import { ref, onMounted, watch, nextTick } from 'vue';
import Map from 'ol/Map.js';
import OSM from 'ol/source/OSM.js';
import TileLayer from 'ol/layer/Tile.js';
import View from 'ol/View.js';
import { fromLonLat } from 'ol/proj';
import { Feature } from 'ol';
import { Point } from 'ol/geom';
import { Vector as VectorLayer } from 'ol/layer';
import { Vector as VectorSource } from 'ol/source';
import { Style, Icon } from 'ol/style';
import Register from '../Register.vue';
import { useToast } from 'vue-toastification';
import { useRouter } from 'vue-router';

const factory = ref({
  name: '',
  openFrom: '',
  openTo: '',
  logoPath: ''
});
const managers = ref({});
const location = ref({
  longitude: 0,
  latitude: 0,
  city: '',
  address: '',
  postcode: ''
});
const router = useRouter();
const toast = useToast();
var selectedManager;
const id = ref('');
const success = ref('');
const locationError = ref(false);
const errorMessage = ref('');
const searchQuery = ref('');
const HasRegisteredManager = ref(false);
const HasAvailableManagers = ref(true);
const IsRegistering = ref(false);
const focusStates = ref({
  name: false,
  openFrom: false,
  openTo: false,
  logo: false,
  location: false,
  manager: false,
});
const map = ref(null);
const vectorSource = new VectorSource();

const userData = ref(null);
const showRegisterForm = ref(false);

onMounted(() => {



axios.get('http://localhost:8080/WebShopAppREST/rest/managers/free', axios.defaults.withCredentials = true).then(response => {
  if(response.data.length === 0) {
    HasAvailableManagers.value = false;
  }
  managers.value = response.data;
});
initializeMap();
});

function handleUserData(user) {
  userData.value = user;
  toggleRegisterForm();
  HasRegisteredManager.value = true;
}
function toggleRegisterForm() {
  showRegisterForm.value = !showRegisterForm.value;
  IsRegistering.value = !IsRegistering.value;
  mapVisible.value = !mapVisible.value;
}

function ChangeColor(manager) {
  id.value = manager.user.id;
  selectedManager = manager.manager;
}

function SetFocus(field) {
  focusStates.value[field] = true;
}
function ResetFocus(field) {
  focusStates.value[field] = false;
}

const mapVisible = ref(true);
const coordinates = ref([0, 0]);
const zoom = ref(2);
watch(mapVisible, async (newVal) => {
  if (newVal) {
    await nextTick();
    initializeMap();
  }
});

const initializeMap = () => {
  map.value = new Map({
    target: 'map',
    layers: [
      new TileLayer({
        source: new OSM(),
      }),
      new VectorLayer({
        source: vectorSource
      })
    ],
    view: new View({
      center: coordinates.value,
      zoom: zoom.value,
    }),
  });
};


const searchLocation = async () => {
  if (!searchQuery.value) {
    return;
  }
  try {
    axios.defaults.withCredentials = false;
    const response = await axios.get('https://nominatim.openstreetmap.org/search', {
      params: {
        q: searchQuery.value,
        format: 'json',
        addressdetails: 1,
        limit: 1
      }
    });
    if (response.data.length > 0) {
      locationError.value = false;
      const place = response.data[0];
      const lon = parseFloat(place.lon);
      const lat = parseFloat(place.lat);
      location.value.latitude = lat;
      location.value.longitude = lon;
      location.value.city = place.address.city;
      location.value.address = place.address.road;
      location.value.postcode = place.address.postcode;
      coordinates.value = fromLonLat([lon, lat]);
      zoom.value = 16;
      map.value.setView(new View({
        center: coordinates.value,
        zoom: 16,
      }));
      const marker = new Feature({
        geometry: new Point(fromLonLat([lon, lat]))
      });
      marker.setStyle(new Style({
        image: new Icon({
          src: 'https://openlayers.org/en/v4.6.5/examples/data/icon.png',
          anchor: [0.5, 1]
        })
      }));
      vectorSource.clear();
      vectorSource.addFeature(marker);
    } else {
      locationError.value = true;
      ResetLocation();
    }
  } catch (error) {
    console.error(error);
  }
};
function ResetLocation(){
location.value.latitude = 0;
location.value.longitude = 0;
location.value.city = '';
location.value.address = '';
location.value.postcode = '';
coordinates.value = fromLonLat([0, 0]);
zoom.value = 2;
map.value.setView(new View({
  center: coordinates.value,
  zoom: zoom.value,
}));
};

function RegisterFactory() {
  if(selectedManager === undefined && userData.value === null) {
    errorMessage.value= 'Please select or register a manager.';
    return;
  }
  if(selectedManager !== undefined && userData.value !== null) {
    errorMessage.value= 'You cant select a manager and register a new one at the same time.';
    return;
  }
  if(IsLocationValid())
  axios.post('http://localhost:8080/WebShopAppREST/rest/register/factory', {
    factory: factory.value,
    location: location.value,
    manager: selectedManager,
    user: userData.value
  }, axios.defaults.withCredentials = true)
    .then(response => {
      console.log(response.data);
      success.value = 'Factory successfully registered!';
      errorMessage.value = '';
      toast.success('Factory and manager successfully registered!');
      router.back();
    })
    .catch(error => {
      console.log(error.response.data);
      errorMessage.value = error.response.data;
      toast.error('Factory and manager registration failed!');
    });
    
}
function IsLocationValid(){
  if(location.value.postcode===undefined || location.value.postcode==='' || location.value.city===undefined || location.value.city==='' || location.value.address===undefined || location.value.address===''|| location.value.latitude===0 || location.value.longitude===0|| location.value.latitude===undefined || location.value.longitude===undefined){
    errorMessage.value = 'Map couldnt find all necessary information for searched location.';
    return false;
  }
  return true;
}
</script>
  
<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.submit-div {
  display: flex;
  justify-content: center;
}

.register-component {
  display: flex;
  justify-content: space-between;
  gap: 2rem;
  width: 1200px; /* Increase the width */
  max-width: 100%;
}

.form-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.map-content {
  flex: 1;
}
.manager-content {
  flex: 1;
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
.header {
  margin: 1rem 0;
}
.register-component > .submit {
  padding: 0.5rem;
  border: none;
  border-radius: 0.25rem;
  background-color: #007bff;
  color: white;
  cursor: pointer;
}
.managerList{
  display: flex;
  justify-content: space-between;
  padding: 0.5rem;
  border: 1px solid #797979;
  border-radius: 0.25rem;
  background-color: #555555;
  color: white;
  cursor: pointer;

}
.managerList-clicked{
  display: flex;
  flex: 1;
  gap: 1rem;
  margin-top: 1rem;
  justify-content: space-between;
  padding: 0.5rem;
  border: 1px solid #797979;
  border-radius: 0.25rem;
  background-color: #007bff;
  color: white;
  cursor: pointer;

}
.submit:hover {
  background-color: #0056b3;
}
.submit {
  margin-top: 4rem;
  padding: 0.5rem;
  border: none;
  width: 200px;
  border-radius: 0.25rem;
  background-color: #007bff;
  color: white;
  cursor: pointer;
}
.button-on-form{
  position: absolute;
  top: 40px;
  left: calc(50% - 200px);
  margin-top: 0;
  padding: 0.5rem;
  z-index: 1000;
  border: none;
  height: 35px;
  border-radius: 0.25rem;
  background-color: #007bff;
  color: white;
  cursor: pointer;
}
.form-button{
  margin-top: 1.2rem;
  padding: 0.5rem;
  border: none;
  height: 35px;
  border-radius: 0.25rem;
  background-color: #007bff;
  color: white;
  cursor: pointer;

}
.button{
  display: flex;
  justify-content: center;
}
.error {
  color: #FF4C4C;
  font-size: 15px;
  height: 10px;
}

p {
  display: block;
}

.search-container {
  display: flex;
  align-items: center;
}
.form{
  display:flex;
  align-items: flex-center;
    position:absolute;
    top:30px;
    left: 50%;
    transform: translateX(-50%); 
}

.search-button {
  padding: 0.5rem;
  margin:0px 0px 0px 20px;
  border: 1px solid #797979;
  border-radius: 0.25rem;
  background-color: #555555;
  color: white;
  cursor: pointer;
}

.search-button i {
  font-size: 1rem;
}

.search-button:hover {
  background-color: #007bff;
}

.is-clicked {
  background-color: #007bff;
}
</style>
