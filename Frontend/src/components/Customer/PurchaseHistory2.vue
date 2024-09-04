<template>
  <div class="purchases-div">
    <div class="title">
      <h1>Purchase History</h1>
    </div>
    <div class="sort">
      <select @change="sort($event)" class="sort-select">
          <option value="">Sort by</option>
          <option value="dateASC">Date ASC</option>
          <option value="dateDESC">Date DESC</option>
          <option value="priceASC">Price ASC</option>
          <option value="priceDESC">Price DESC</option>
          <option value="A-Z">Factory A-Z</option>
          <option value="Z-A">Factory Z-A</option>
        </select>
      </div>
    <div class="main">  
    <div class="header">
      <div class="search-div">
        <div class="filters">
          <label>Filters:</label>
          <div class="filter-options">
            <div class="filter-section">
              <label>Factory Name:</label>
              <input type="text" class="nameInput" v-model="filters.factoryName">
            </div>
            <div class="date">
              <label>Purchase Range:</label>
              <div class="dateInputs">
              <input type="date" v-model="filters.purchaseRangeFrom">
              <label>-</label>
              <input type="date" v-model="filters.purchaseRangeTo">
              </div>
            </div>
            <div class="price">
              <label>Price:</label>
              <div class="priceInputs">
              <input type="number" class="no-arrows" v-model="filters.priceFrom" step="0.01">
              <label>-</label>
              <input type="number" class="no-arrows" v-model="filters.priceTo" step="0.01">
              </div>
            </div>
            <div class="buttons">
            <form @submit="searchPurchases($event)" class="search-form">
              <input class="search-button" type="submit" value="Search">
            </form>
            <button @click="ClearFilters()" class="clearFilters">ClearFilters</button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="purchase-history">
      <div v-for="purchase in purchases" :key="purchase.purchase.id" class="purchase">
        <div class="purchase-header">
          <div>
        <h2>Purchase ID: {{ purchase.purchase.id }}</h2>
        <p><strong>Factory:</strong> {{ purchase.factoryName }}</p>
        <p :class="statusClass(purchase.purchase.purchaseStatus)">
          <strong>Status:</strong> {{ purchase.purchase.purchaseStatus }}
        </p>
        <p><strong>Purchase Date:</strong> {{ formatDate(purchase.purchase.purchaseDate) }}</p>
        <p><strong>Total Price:</strong> ${{ purchase.purchase.price.toFixed(2) }}</p>
          </div>
          <div>
            <button class="comment-button" v-if="purchase.purchase.purchaseStatus==='Approved' && purchase.comment===undefined" @click="AddComment(purchase)">Add Comment</button>
            <button class="comment-button" v-if="purchase.purchase.purchaseStatus==='Rejected'" @click="ShowReason(purchase)">Show reason</button>
            <button class="comment-button" v-if="purchase.comment!==undefined" @click="ShowComment(purchase)">Show Comment</button>
            </div>
        </div>
        <div class="items">
          <h3>Items:</h3>
          <div v-for="item in purchase.items" :key="item.chocolate.id" class="item">
            <img :src="item.chocolate.imagePath" :alt="item.chocolate.name" class="item-image"/>
            <div class="item-details">
              <div><strong>Name:</strong> {{ item.chocolate.name }}</div>
              <div><strong>Description:</strong> {{ item.chocolate.description }}</div>
              <div><strong>Category:</strong> {{ item.chocolate.category }}</div>
              <div><strong>Type:</strong> {{ item.chocolate.type }}</div>
              <div><strong>Weight:</strong> {{ item.chocolate.weight }}g</div>
              <div><strong>Price:</strong> ${{ item.price.toFixed(2) }}</div>
              <div><strong>Quantity:</strong> {{ item.quantity }}</div>
            </div>
          </div>
            <button class="cancel-button" v-if="purchase.purchase.purchaseStatus === 'Inprocess'" @click="cancelPurchase(purchase.purchase.id)">Cancel Purchase</button>
        </div>
      </div>
    </div>
    </div>
  </div>
  <AddCommentModal v-if="addComment" :factoryName="selectedFactory.factoryName" @addComment="saveComment" @closeAddModal="closeAddModal()"/>
  <ShowCommentModal v-if="showComment" :factory="selectedFactory" :comment="selectedFactory.comment" @closeModal="closeShowModal()"/>
  <ShowReasonModal v-if="showReason"  :reason="rejectionReason" @closeModal="closeReasonModal()"/>
</template>

<script setup>
import axios from 'axios';
import { ref, watch, defineProps, onMounted } from 'vue';
import AddCommentModal from './AddCommentModal.vue';
import ShowCommentModal from '../CommentModal.vue';
import { useToast } from 'vue-toastification';
import ShowReasonModal from './ReasonModal.vue';
axios.defaults.withCredentials = true;

onMounted(() => {
    axios.get('http://localhost:8080/WebShopAppREST/rest/purchases/')
    .then(response => {
      console.log(response.data)
      purchases.value = response.data;
      hasSearched = true;
    })
    .catch(error => {
      alert(error);
    });

})

const rejectionReason = ref('');
const showReason = ref(false);
const toast = useToast();
const purchases = ref([])
const addComment = ref(false);
const showComment = ref(false);
const search = ref('');
const filters = ref({
  purchaseRangeFrom: null,
  purchaseRangeTo: null,
  priceFrom: 0,
  priceTo: 0,
  factoryName: ''
});
const selectedFactory = ref({});
let hasSearched = false;

function searchPurchases(event) {
  event.preventDefault();
  Search();
}
function Search() {
  const params = {
    factoryName: filters.value.factoryName,
    purchaseRangeFrom: filters.value.purchaseRangeFrom,
    purchaseRangeTo: filters.value.purchaseRangeTo,
    priceFrom: filters.value.priceFrom,
    priceTo: filters.value.priceTo
  };
  axios.post('http://localhost:8080/WebShopAppREST/rest/purchases/searchPurchases', params)
    .then(response => {
      purchases.value = response.data;
      hasSearched = true;
    })
    .catch(error => {
      alert(error);
    });
    ScrollToBeginning();
  }

function sort(event) {
  const selectedValue = event.target.value;
  if (selectedValue === "dateASC") {
    purchases.value.sort((a, b) => new Date(a.purchase.purchaseDate) - new Date(b.purchase.purchaseDate));
  } else if (selectedValue === "dateDESC") {
    purchases.value.sort((a, b) => new Date(b.purchase.purchaseDate) - new Date(a.purchase.purchaseDate));
  } else if (selectedValue === "priceASC") {
    purchases.value.sort((a, b) => a.purchase.price - b.purchase.price);
  } else if (selectedValue === "priceDESC") {
    purchases.value.sort((a, b) => b.purchase.price - a.purchase.price);
  }else if (selectedValue === "A-Z") {
    purchases.value.sort((a, b) => a.factoryName.localeCompare(b.factoryName));
  }
  else if (selectedValue === "Z-A") {
    purchases.value.sort((a, b) => b.factoryName.localeCompare(a.factoryName));
  }
}
function ScrollToBeginning(){
   if (window.scrollY !== 0) {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    }
}
const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('en-GB', options);
};
const statusClass = (status) => {
  switch (status) {
    case 'Inprocess':
      return 'status-inprocess';
    case 'Approved':
      return 'status-approved';
    case 'Rejected':
      return 'status-rejected';
    case 'Cancelled':
      return 'status-cancelled';
    default:
      return '';
  }
}

function ClearFilters(){
  filters.value.purchaseRangeFrom = null;
  filters.value.purchaseRangeTo = null;
  filters.value.priceFrom = 0;
  filters.value.priceTo = 0;
  filters.value.factoryName = '';
  Search();
}
function AddComment(purchase){
  addComment.value = true;
  selectedFactory.value = purchase;
}
function ShowComment(purchase){
  showComment.value = true;
  selectedFactory.value = purchase;
}
function ShowReason(purchase){
  axios.get(`http://localhost:8080/WebShopAppREST/rest/comments/rejectionReason/${purchase.purchase.id}`)
    .then(response => {
      rejectionReason.value = response.data;
    })
    .catch(error => {
    });
  showReason.value = true;

}
function closeAddModal() {
  addComment.value = false;
}
function closeReasonModal(){
  showReason.value = false;
}
function saveComment(data){
  const comment = {
    comment: data.comment,
    rating: data.rating,
    purchaseId:selectedFactory.value.purchase.id,
    factoryId: selectedFactory.value.factoryId
  };
  axios.defaults.withCredentials=true;
  axios.post('http://localhost:8080/WebShopAppREST/rest/comments/', comment)
    .then(response => {
      purchases.value.forEach(purchase => {
        if (purchase.purchase.id === selectedFactory.value.purchase.id) {
          purchase.comment = response.data;
        }
      });
      toast.success('Comment sent for approval');
    })
    .catch(error => {
      toast.error('Error sending comment');
    });
}

function closeShowModal(){
  showComment.value = false;
  axios.get('http://localhost:8080/WebShopAppREST/rest/purchases/')
    .then(response => {
      console.log(response.data)
      purchases.value = response.data;
      hasSearched = true;
    })
    .catch(error => {
      alert(error);
    });
}

function cancelPurchase(purchaseId) {
  axios.patch(`http://localhost:8080/WebShopAppREST/rest/purchases/cancel/${purchaseId}`)
    .then(response => {
      const index = purchases.value.findIndex(purchase => purchase.purchase.id === purchaseId);
      if (index !== -1) {
        purchases.value[index].purchase.purchaseStatus = 'Cancelled';
        toast.success('Purchase cancelled successfully');
      }
    })
    .catch(error => {
      toast.error('Error cancelling purchase');
    });
}



</script>

<style scoped>
.price{
  display: flex;
  flex-direction:column;
  gap: 2px;

}
.date{
  display: flex;
  flex-direction:column;
  gap: 2px;
}
.dateInputs{
  display: flex;
  flex-direction: row;
  gap:10px;
}
.title{
  display: flex;
  justify-content: center;

}
.dateInputs input{
  padding: 5px;
  background-color: #f5f5f5;
  border: 1px solid black;
  color: #4b2e20;
  width: 110px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
.priceInputs{
  display: flex;
  flex-direction: row;
  gap:10px;
}
.clearFilters{
  margin-top: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  padding:5px 10px;
  cursor: pointer;
  width: 100px;
}
.clearFilters:hover{
  background-color: #0056b3;
}
.no-arrows::-webkit-outer-spin-button,
.no-arrows::-webkit-inner-spin-button {
-webkit-appearance: none;
margin: 0;
}
.priceInputs input{
  text-align: center;
  padding: 5px;
  background-color: #f5f5f5;
  border: 1px solid black;
  color: #4b2e20;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  width: 50px;
}
.buttons{
  display: flex;
  flex-direction: row;
  gap: 20px;

}
.purchase-header{
  display: flex;
  justify-content: space-between;
  flex-direction: row;
}
.nameInput{
  padding: 5px;
  background-color: #f5f5f5;
  border: 1px solid black;
  color: #4b2e20;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
.sort{
  display: flex;
  justify-content: end;
  margin-bottom: 30px;

}
.purchases-div {
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;
}
.header {
  min-width: 350px;
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;
}
.main
{
  display: flex;
  flex-direction: row;
  gap: 20px;
}

.search-div {
  position:fixed;
  padding: 10px 60px 10px 10px;
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  color: #4b2e20;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.search-form {
  display: flex;
  align-items: center;
}

.filters {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.filter-options {
  display: flex;
  flex-direction: column;
  gap: 12px;
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

.purchase-history {
  display: flex;
  flex-direction: column;
  gap: 20px;
  flex: 1;

}

.purchase {
  color: #4b2e20;
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 15px;
  margin-bottom: 20px;
  background-color: #f9f9f9;
  transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
}

.purchase:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.items {
  margin-top: 15px;
}

.item {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 10px;
  margin-bottom: 10px;
  background-color: #fff;
  display: flex;
  align-items: flex-start;
  transition: background-color 0.2s ease-in-out;
}

.item:hover {
  background-color: #f0f0f0;
}

.item-image {
  width: 50px;
  height: 50px;
  margin-right: 15px;
  border-radius: 5px;
  object-fit: cover;
}

.item-details {
  display: flex;
  flex-direction: column;
}

.status-inprocess {
  color: orange;
  font-weight: bold;
}

.status-approved {
  color: green;
  font-weight: bold;
}

.status-rejected {
  color: red;
  font-weight: bold;
}

.status-cancelled {
  color: gray;
  font-weight: bold;
}

h2 {
  font-size: 1.5em;
  margin-bottom: 10px;
}

h3 {
  margin-top: 0;
}

p {
  margin: 5px 0;
}

strong {
  font-weight: bold;
}
.search-button{
  margin-top: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  padding:5px 10px;
  cursor: pointer;
  width: 100px;
}
.search-button:hover{
  background-color: #0056b3;
}
.comment-button{
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  padding:10px 10px;
  cursor: pointer;
  width: 120px;
}
.comment-button:hover{
  background-color: #0056b3;
}


.cancel-button {
  margin-left: 820px;
  padding: 10px 20px;
  font-size: 16px;
  background-color: #dc3545;
  color: white;
  border: none;
  width: 200px;
  border-radius: 4px;
  cursor: pointer;
}

</style>
