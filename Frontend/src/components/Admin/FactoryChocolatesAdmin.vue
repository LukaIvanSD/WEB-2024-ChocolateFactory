<template>
  <div class="chocolate-slider">
    <h2 style="margin-bottom: 10px">Chocolates</h2>
    <div class="card-container">
      <div class="card" v-for="item in chocolates" :key="item.id">
        <div class="card-content">
          <div class="card-img-wrapper">
            <img :src="item.chocolate.imagePath" alt="Chocolate Image" class="card-img">
          </div>
          <div class="card-details">
            <div class="title">{{ item.chocolate.name }}</div>
            <div class="info">
              <div><strong>Type:</strong> {{ item.chocolate.type }}</div>
              <div><strong>Weight:</strong> {{ item.chocolate.weight }}</div>
              <div><strong>Category:</strong> {{ item.chocolate.category }}</div>
            </div>
            <div class="description">
          <p>{{ item.chocolate.description }}</p>
        </div>
          </div>
          <div class="right-details">
          <div><strong>Price:</strong> {{ item.chocolatePrice.price }}</div>
        </div>
        </div>
      </div>
    </div>
  </div>
</template>



<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const route=useRoute();
const factoryId=route.params.id;
const chocolates = ref([]);
onMounted(async () => {
  try {
    const response = await axios.get(`http://localhost:8080/WebShopAppREST/rest/chocolates/${factoryId}`, { withCredentials: true });
    chocolates.value = response.data;
    for (let i = 0; i < chocolates.value.length; i++) {
      chocolates.value[i].selectedQuantity = 0;
    }
    console.log(chocolates.value);

  } catch (error) {
    alert(error);
  }
});

</script>

<style scoped>
.chocolate-slider {
width: 95vh;
margin: 0 auto;
margin-bottom: 20px;
}

.card-container {
display: flex;
flex-wrap: wrap;
gap: 20px;
}


.card {
width: 95vh;
border: 1px solid #ccc;
border-radius: 10px;
box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
overflow: hidden;
display:flex;
flex-direction: column;
background-color: #f5f5f5;
transition: transform 0.2s ease, background-color 0.7s ease;
}
.card:hover {
background-color: #e0e0e0;
transform: scale(1.02);
}

.card-content {
  padding: 20px;
display: flex;
align-items: Top;
}

.card-img-wrapper {
margin-right: 20px; 
}

.card-img {
height: 200px;
width:340px;
object-fit:fill;

}

.card-details {
flex: 1; 
text-align: left;
}

.title {
  color: #333;
font-size: 18px;
font-weight: bold; 
margin-bottom: 5px; 
}

.info {
margin-top: 10px; 
color: #4b2e20;
}

.right-details {
  color: #4b2e20;
text-align: right;
}

.description {
margin-top: 15px; 
color: #4b2e20;
}
.no-arrows::-webkit-outer-spin-button,
.no-arrows::-webkit-inner-spin-button {
-webkit-appearance: none;
margin: 0;
}
.disabled{
background-color: #ccc;
cursor: not-allowed;
}
</style>



