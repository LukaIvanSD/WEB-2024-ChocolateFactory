<template>
  <div class="mainPage">
  <div class="sortMain">
    <div class="sort">
    <select @change="sort($event)" class="sort-select" :class="{'invisible':factories.length===0}">
        <option value="">Sort by</option>
        <option value="nameASC">Name ASC</option>
        <option value="nameDESC">Name DESC</option>
        <option value="ratingASC">Rating ASC</option>
        <option value="ratingDESC">Rating DESC</option>
        <option value="locationASC">Location ASC</option>
        <option value="locationDESC">Location DESC</option>
      </select>
    </div>
  <div class="main">
    <div class="search-div">
      <div class="filters">
        <div class="filter-options">
          <div class="filter-section">
            <label class="mainText">Types:</label>
            <div v-for="(filter, index) in filters.types" :key="index" class="filter-checkbox">
              <label>
                <input type="checkbox" v-model="selectedType" :value="filter">
                {{ filter }}
              </label>
            </div>
          </div>
          <div class="filter-section">
            <label class="mainText">Categories:</label>
            <div v-for="(filter, index) in filters.categories" :key="index" class="filter-checkbox">
              <label>
                <input type="checkbox" v-model="selectedCategory" :value="filter">
                {{ filter }}
              </label>
            </div>
          </div>
          <div class="filter-section">
            <label>
              <input type="checkbox" v-model="onlyOpen">
              Show only open
            </label>
          </div>
        </div>
      </div>
    </div>
  <div class="factories-container">
    <div class="factories-grid">
      <div v-for="factory in factories" :key="factory.id" class="factory-card" @dblclick="ShowFactoryDetails(factory)">
        <img :src="factory.logoPath" alt="Logo" class="factory-logo">
        <div class="factory-details">
          <h3>{{ factory.name }}</h3>
          <p>{{ factory.address }}</p>
          <p>Rating: {{ factory.rating }}</p>
          <p>City: {{ factory.city }}</p>
          <p>Post code: {{ factory.postCode }}</p>
        </div>
      </div>
    </div>
  </div>
    </div>
    </div>
  </div>
</template>

<script setup>
import axios from 'axios';
import { onMounted, computed,ref, watch,watchEffect} from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
const store=useStore();
const searchQuery = computed(() => store.getters.searchQuery);
const factories = ref([]);
const filters = ref([]);
const selectedType = ref([]);
const selectedCategory = ref([]);
const onlyOpen = ref(false);
let hasSearched = false;
const router = useRouter();

function ShowFactoryDetails(factory) {
  router.push({ name: 'factoryDetailsGhost', params: { id: factory.id } });
}

function filter() {
  const filterData = {
    types: selectedType.value,
    categories: selectedCategory.value,
    onlyOpen: onlyOpen.value
  };
  if (hasSearched) {
    filterData.factories = factories.value;
  }
  axios.post('http://localhost:8080/WebShopAppREST/rest/factories/filter', filterData)
    .then(response => {
      console.log(filterData);
      factories.value = response.data;
    })
    .catch(error => {
      alert(error);
    });
}

const SearchFactories = (query) => {
  axios.post(`http://localhost:8080/WebShopAppREST/rest/factories/search?parameter=${query}`)
    .then(response => {
      factories.value = response.data;
      hasSearched = query !== '';
    })
    .catch(error => {
      alert(error);
    });
};

watchEffect(() => {
  if (searchQuery.value && searchQuery.value!=='') {
    SearchFactories(searchQuery.value);
  } else {
    showAllFactories();
    hasSearched = false;
  }
});

function showAllFactories(){
  axios.get('http://localhost:8080/WebShopAppREST/rest/factories/sort')
    .then(response => {
      factories.value = response.data;
      console.log("GAs");
      console.log(factories.value);
    })
    .catch(error => {
      alert(error);
    });
}
function InitializeFilters(){
  axios.get('http://localhost:8080/WebShopAppREST/rest/chocolates/filters')
    .then(response => {
      filters.value = response.data;
      console.log(filters.value);
    })
    .catch(error => {
      alert(error);
    });
}
onMounted(() => {
  if (searchQuery.value && searchQuery.value!=='') {
    SearchFactories(searchQuery.value);
  } else {
    showAllFactories();
  }
  InitializeFilters();
});

watch(selectedType, filter);
watch(selectedCategory, filter);
watch(onlyOpen, filter);

function sort(event) {
  const selectedValue = event.target.value;
  if (selectedValue === "nameASC") {
    factories.value.sort((a, b) => a.name.localeCompare(b.name));
  } else if (selectedValue === "nameDESC") {
    factories.value.sort((a, b) => b.name.localeCompare(a.name));
  } else if (selectedValue === "ratingASC") {
    factories.value.sort((a, b) => a.rating - b.rating);
  } else if (selectedValue === "ratingDESC") {
    factories.value.sort((a, b) => b.rating - a.rating);
  } else if (selectedValue === "locationASC") {
    factories.value.sort((a, b) => a.city.localeCompare(b.city));
  } else if (selectedValue === "locationDESC") {
    factories.value.sort((a, b) => b.city.localeCompare(a.city));
  }
}
</script>

<style scoped>
.mainPage{
  display: flex;
}
.main {
  display: flex;
  flex-direction: row;
  gap: 140px;
}
.sortMain{
  display: flex;
  flex-direction: column;
  align-items: end;
  gap:30px;
}

.search-div {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.filters{
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 10px 60px 10px 10px;
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  color: #4b2e20;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
.filter-options {
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.search-form {
  display: flex;
  align-items: center;
  gap: 10px;
}


.filter-section {
  display: flex;
  flex-direction: column;
}

.search-label {
    width: 300px;
}

.filter-checkbox {
  margin-bottom: 5px;
}

.sort-select {
    width: 200px;
  padding: 5px;
}
.mainText{
  margin-bottom: 5px;
  font-size: 20px;
  font-weight: bold;

}
.factories-container {
  display: flex;
  justify-content: center;
  color: black;
}

.factories-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* Ensures 4 columns initially */
  gap: 20px;
  width: 100%;
}

.factory-card {
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 16px;
  transition: transform 0.2s;
  width: 250px; /* Ensure each card has a fixed width */
}

.factory-card:hover {
  transform: scale(1.05);
}
.invisible{
  visibility: hidden;
}
.factory-logo {
  width: 100%;
  height: 140px;
  border-bottom: 1px solid #ddd;
  margin-bottom: 10px;
  object-fit:fill;
}

.factory-details {
  text-align: center;
}
</style>
