<template>
    <div class="modal-overlay">
      <div class="modal">
        <h1 class="header">Worker List</h1>
        <div class="worker-cards">
          <div class="worker-card" v-for="worker in workers" :key="worker.id">
            <h2>{{ worker.firstName }} {{ worker.lastName }}</h2>
            <p>{{ worker.sex }}</p>
            <p>{{ formatDate(worker.dateOfBirth) }}</p>
            <button class="remove-button" @click="RemoveWorker(worker.id)">Remove</button>
          </div>
        </div>
        <button type="button" class="close" @click="closeModal">Close</button>
      </div>
    </div>
  </template>
  
  
  <script setup>
  import { ref, onMounted, defineEmits, defineProps } from 'vue';
  import { useToast } from 'vue-toastification';
  import axios from 'axios';
  const emit = defineEmits(['closeWorkerListModal']);
  const props = defineProps({
    factoryId: {
      type: String,
      default: () => null
    }
  });
  
  const workers = ref({
    id: 0,
    firstName: '',
    lastName: '',
    sex: '',
    dateOfBirth: ''
  });
  const toast=useToast();
  const chocolatePrice = ref(0);
  
  onMounted(() => {
    if (props.factoryId) {
        axios.defaults.withCredentials=true;
        console.log(props.factoryId);
        axios.get('http://localhost:8080/WebShopAppREST/rest/workers/'+props.factoryId)
        .then(response=>{
            console.log(response.data);
          workers.value=response.data;
        })
        .catch(error=>console.log(error));
    }
  });
  function RemoveWorker(workerId) {
    axios.delete('http://localhost:8080/WebShopAppREST/rest/workers/'+workerId)
    .then(response=>{
      GenerateToastNotification();
      workers.value=workers.value.filter(worker=>worker.id!==workerId);
    })
    .catch(error=>console.log(error));
  }
  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('en-GB', options);
};

  function GenerateToastNotification() {
    toast.success('Worker removed successfully');
  }
  
  function closeModal() {
    emit('closeWorkerListModal');
  }
  </script>
  
 <style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal {
  background-color: #f5f5f5;
  padding: 20px;
  color: #333;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  text-align: left;
  max-width: 600px;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.worker-cards {
  min-width: 500px;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 1rem;
}

.worker-card {
  background-color: #ffffff;
  padding: 15px;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  width: calc(50% - 1rem);
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.worker-card h2 {
  margin: 0 0 10px;
  font-size: 1.2rem;
  color: #007bff;
}

.worker-card p {
  margin: 5px 0;
  font-size: 0.9rem;
  color: #555;
}

.close {
  padding: 0.5rem;
  border: none;
  width: 100px;
  border-radius: 0.25rem;
  background-color: #007bff;
  color: white;
  cursor: pointer;
  margin-top: 1rem;
}

.close:hover {
  background-color: #0056b3;
}

.header {
  text-align: center;
  margin-bottom: 1rem;
  font-size: 1.5rem;
}
.remove-button {
  margin-top: 10px;
  background-color: #a41906;
  color: white;
  border: none;
  width: 72px;
  margin-left: 10px;
  border-radius: 5px;
  padding: 5px 5px;
  cursor: pointer;
}
.remove-button:hover {
  background-color: #d32f2f;
}
</style>
  