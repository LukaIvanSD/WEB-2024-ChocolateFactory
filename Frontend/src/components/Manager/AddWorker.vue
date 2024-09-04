<template>
        <div v-if="canAdd">
      <button class="addWorker-button" value="AddWorker" @click="AddWorker">Add worker</button>
      </div>
</template>
<script setup>
import { useRouter,useRoute } from 'vue-router';
import{ref,onMounted}   from 'vue';
import axios from 'axios';
const route=useRoute();
const router = useRouter();
const factoryId = route.params.id;
axios.defaults.withCredentials = true;
const canAdd=ref(false);
onMounted(()=>{
    axios.post('http://localhost:8080/WebShopAppREST/rest/managers/rights/?factoryId='+factoryId,axios.defaults.withCredentials=true)
    .then(response=>{
      canAdd.value=response.data;
    })
    .catch(error=>{
      alert(error);
    })
});
function AddWorker(){
  router.push({name:'registerUser'});
}
</script>
<style scoped>

.addWorker-button {
  padding: 10px 20px;
  background-color: #007BFF;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

</style>