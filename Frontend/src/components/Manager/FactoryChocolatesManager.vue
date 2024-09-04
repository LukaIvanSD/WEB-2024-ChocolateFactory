<template>
  <div class="chocolate-slider">
    <div class="header">
    <h2>Chocolates</h2>
    <div class="addButtons">
      <button class="listButton" @click="ShowWorkersListModal()">
        <img class="listLogo" src="../../assets/list.png">
        </button>
      <div class="workersNum"  v-if="isUserManagerOfFactory">
        <img class="workerLogo" src="../../assets/worker1.png">
        <p class="workersNumber">{{ numberOfWorkers }}</p>
      </div>
      <AddWorker />
    <button v-if="isUserManagerOfFactory" @click="ShowAddChocolateModal()" class="add-button">Add chocolate</button>
    </div>
    </div>
    <div class="card-container">
      <div class="card" v-for="item in chocolates" :key="item.id">
        <div class="card-content">
          <div class="card-img-wrapper">
            <img :src="item.chocolate.imagePath" alt="Chocolate Image" class="card-img">
          </div>
          <div class="card-details">
            <div class="title">
              <div class="status">
              <span class="title">{{ item.chocolate.name }}</span>
              <span v-if="isUserManagerOfFactory===true" class="notAvailable" :class="{'available':item.chocolateItem.availability==='Available'}">{{ item.chocolateItem.availability }}</span>
              </div>
            </div>
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
          <div v-if="isUserManagerOfFactory===true"><strong>Quantity:</strong> {{ item.chocolateItem.quantity }}</div>
          <div class="functions" v-if="isUserManagerOfFactory===true">
          <button class="edit-button" @click="ShowEditModal(item)">Edit</button>
          <button class="delete-button" @click="ShowModal(item)">Delete</button>
            </div>
        </div>
        </div>
      </div>
    </div>
  </div>
  <div v-if="modalVisible" class="modal-overlay">
    <div class="modal">
      <h2>Confirm Deletion</h2>
      <p>Are you sure you want to delete</p>
      <p style="margin-bottom: 10px;">{{chocolateToDelete.chocolate.name}} ?</p>
      <button style="margin-right: 30px;" @click="confirmDelete" class="delete-button">Confirm</button>
      <button @click="cancelDelete" class="edit-button">Cancel</button>
    </div>
  </div>
  <WorkerListModal @closeWorkerListModal="closeWorkerListModal" v-if="workerListModalVisible" :factoryId="factoryId"/>
  <EditChocolateModal v-if="modalEditVisible" @editChocolate="EditChocolate" :receivedObject="chocolateToEdit" @closeEditModal="closeEditModal"/>
  <AddChocolateModal v-if="modalAddVisible" @addChocolate="AddChocolate" @closeAddModal="closeAddModal" />
</template>
<script setup>
import { ref,onMounted } from 'vue'
import { useRoute,useRouter } from 'vue-router'
import EditChocolateModal from './EditChocolateModal.vue'
import AddChocolateModal from './AddChocolateModal.vue'
import WorkerListModal from './WorkerListModal.vue'
import AddWorker from '@/components/Manager/AddWorker.vue';
import { useToast } from 'vue-toastification';
import axios from 'axios'
const toast=useToast();
const router=useRouter();
const route=useRoute();
const factoryId=route.params.id;
const chocolates=ref([]);
const chocolate=ref({
  category:'',
  description:'',
  imagePath:'',
  name:'',
  type:'',
  weight:0
});
const workerListModalVisible=ref(false);
const numberOfWorkers=ref(0);
const chocolateToEdit = ref(null);
const modalEditVisible = ref(false);
const modalVisible = ref(false);
const chocolateToDelete = ref(null);
const isUserManagerOfFactory=ref(false);
const price=ref(0);
const modalAddVisible=ref(false);


  onMounted(()=>{
      axios.get('http://localhost:8080/WebShopAppREST/rest/chocolates/'+factoryId,axios.defaults.withCredentials=true)
      .then(response=>{
      chocolates.value=response.data;
      console.log(chocolates.value)

      })
      .catch(error=>{
      })
      axios.post('http://localhost:8080/WebShopAppREST/rest/managers/rights/?factoryId='+factoryId,axios.defaults.withCredentials=true)
      .then(response=>{
        isUserManagerOfFactory.value=response.data;
      })
      .catch(error=>{
      })
      axios.defaults.withCredentials = true;
      axios.get('http://localhost:8080/WebShopAppREST/rest/workers/number', { params: { factoryId: factoryId } })
        .then(response => {
          numberOfWorkers.value = response.data;
        })
        .catch(error => {
        });
      }
  )


  function AddChocolate(chocolate,price){
      axios.defaults.withCredentials = true;

      axios.post(`http://localhost:8080/WebShopAppREST/rest/chocolates/?price=${price}&factoryId=${factoryId}`, chocolate)
        .then(response => {
          chocolates.value.push(response.data);
        })
        .catch(error => {
          alert(error);
        });
  }


  function ShowModal(item){
    chocolateToDelete.value = item;
    modalVisible.value = true;
  }


  const confirmDelete = async () => {
      axios.defaults.withCredentials=true
      axios.delete('http://localhost:8080/WebShopAppREST/rest/chocolates/', {
        data: chocolateToDelete.value.chocolateItem,
        withCredentials: true,
        headers: {
          'Content-Type': 'application/json'
        }
      })
      .then(response=>{
        const index = chocolates.value.findIndex(chocolate => chocolate.chocolate.id === chocolateToDelete.value.chocolate.id);
          if (index !== -1) {
            chocolates.value.splice(index, 1);
            modalVisible.value = false;
            chocolateToDelete.value = null;
            toast.success('Chocolate deleted successfully');
          }
      })
      .catch(error=>{
        alert(error);
      })
    
  
  };


  
  const cancelDelete = () => {
    modalVisible.value = false;
    chocolateToDelete.value = null;
  };
  function closeWorkerListModal(){
    workerListModalVisible.value=false;
    router.go();
  }
  function ShowWorkersListModal(){
    workerListModalVisible.value=true;
  }
  function EditChocolate(editedChocolate,price,ChocolateItem) {
      axios.patch('http://localhost:8080/WebShopAppREST/rest/chocolates/', 
    {
      chocolateEdit: editedChocolate.value, 
      item: ChocolateItem, 
      price: price
    }, 
      {
        withCredentials: true,
        headers: {
          'Content-Type': 'application/json'
        }
      }
    ).then(response => {
      const index = chocolates.value.findIndex(chocolate => chocolate.chocolate.id === ChocolateItem.chocolate.id);
      if (index !== -1) {
        chocolates.value[index].chocolate = editedChocolate.value;
          chocolates.value[index].chocolatePrice.price = price;
      }
      closeEditModal();
    })
    .catch(error => {
      alert(error);
    });
  };
  


  function ShowEditModal(item) {
    chocolateToEdit.value = item;
    modalEditVisible.value = true;
  }

  function closeEditModal() {
    modalEditVisible.value = false;
    chocolateToEdit.value = null;
  }

  function ShowAddChocolateModal(){
    modalAddVisible.value = true;
  }
  function closeAddModal() {
    modalAddVisible.value = false;
  }
</script>
<style scoped>
.chocolate-slider {
  width: 95vh;
  margin: 0 auto;
  margin-bottom: 20px;
}

.status {
  display: flex;
  gap: 20px;
}

.card-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.edit-button {
  margin-top: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  width: 72px;
  margin-left: 10px;
  border-radius: 5px;
  padding: 10px 15px;
  cursor: pointer;
}

.delete-button {
  margin-top: 10px;
  background-color: #a41906;
  color: white;
  border: none;
  width: 72px;
  margin-left: 10px;
  border-radius: 5px;
  padding: 10px 15px;
  cursor: pointer;
}

.delete-button:hover {
  background-color: #ff0000;
}

.edit-button:hover {
  background-color: #0056b3;
}

.header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  align-items: center;
}
.listLogo{
  height: 34px;
}
.add-button {
  margin-top: 7px;
  margin-bottom: 7px;
  background-color: #238d3c;
  color: white;
  border: none;
  width: 122px;
  margin-left: 10px;
  border-radius: 5px;
  padding: 10px 15px;
  cursor: pointer;
}
.listButton{
  background-color: #181818;
  border: none;


  cursor: pointer;
}
.add-button:hover {
  background-color: #186d2c;
}
.addButtons{
  display: flex;
  align-items: center;
  flex-direction: row;
  gap: 20px;
}
.card {
  width: 95vh;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
  transition: transform 0.2s ease, background-color 0.6s ease;
}
.workersNumber{
  font-size: 20px;
}
.card:hover {
  background-color: #e0e0e0;
  transform: scale(1.02);
}

.card-content {
  padding: 20px;
  display: flex;
  align-items: top;
}

.card-img-wrapper {
  margin-right: 20px;
}

.card-img {
  height: 200px;
  width: 340px;
  object-fit: fill;
}

.functions {
  display: flex;
  align-items: center;
  flex-direction: column;
}
.workersNum{
  display: flex;
  align-items: center;
  gap: 5px;

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

.notAvailable {
  color: red;
}

.available {
  color: green;
}

.right-details {
  color: #4b2e20;
  text-align: right;
}

.description {
  margin-top: 15px;
  color: #4b2e20;
}

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
  text-align: center;
  max-width: 400px;
  width: 100%;
}
.workerLogo{
  height: 34px;
}
</style>
