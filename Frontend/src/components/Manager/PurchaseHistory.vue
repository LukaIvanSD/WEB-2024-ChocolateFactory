<template>
   <div class="purchases-div">
    <div class="title">
      <h1>Factory Purchase History</h1>
    </div>
    <div class="sort">
      <select @change="sort($event)" class="sort-select">
          <option value="">Sort by</option>
          <option value="dateASC">Date ASC</option>
          <option value="dateDESC">Date DESC</option>
          <option value="priceASC">Price ASC</option>
          <option value="priceDESC">Price DESC</option>
        </select>
      </div>
    <div class="main">  
      <div class="header">
      <div class="search-div">
        <div class="filters">
          <label>Filters:</label>
          <div class="filter-options">
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
      <div class="buttons">
          <button class="showAll" @click="ShowAll()">Show All</button>
            <button class="approved" @click="ShowApproved()">Approved</button>
            <button class="rejected"  @click="ShowRejected()">Rejected</button>
            <button class="inProcess"  @click="ShowInProcess()">Inprocess</button>
        </div>
      <div v-for="purchase in purchases" :key="purchase.purchase.id" class="purchase">
        <div class="purchase-header">
          <div>
        <h2>Purchase ID: {{ purchase.purchase.id }}</h2>
        <p>
          <strong>Buyer: </strong> 
          <span class="buyer-name" @mouseover="showTooltip(purchase.buyer)" @mouseleave="hideTooltip">
            {{ purchase.buyer.firstName }} {{ purchase.buyer.lastName }}
          </span>
          <div v-if="tooltipVisible && currentBuyer === purchase.buyer" class="tooltip">
            <p><strong>Username:</strong> {{ purchase.buyer.username }}</p>
            <p><strong>Sex:</strong> {{ purchase.buyer.sex }}</p>
            <p><strong>Date of Birth:</strong> {{ formatDate(purchase.buyer.dateOfBirth) }}</p>
          </div>
        </p>
        <p :class="statusClass(purchase.purchase.purchaseStatus)">
          <strong>Status:</strong> {{ purchase.purchase.purchaseStatus }}
        </p>
        <p><strong>Purchase Date:</strong> {{ formatDate(purchase.purchase.purchaseDate) }}</p>
        <p><strong>Total Price:</strong> ${{ purchase.purchase.price.toFixed(2) }}</p>
          </div>
          <div class="buttons arc">
              <button class="comment-button" v-if="purchase.comment!==undefined" @click="ShowComment(purchase)">Show Comment</button>
              <button class="approved" v-if="purchase.purchase.purchaseStatus==='Inprocess'" @click="ApprovePurchase(purchase)">Approve</button>
              <button class="rejected" v-if="purchase.purchase.purchaseStatus==='Inprocess'" @click="ShowRejectModal(purchase)">Reject</button>
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
        </div>
      </div>
    </div>
    </div>
  </div>
  <RejectPurchaseModal v-if="rejectModal" :buyer="selectedFactory.buyer" @declinePurchase="DeclinePurchase" @closeModal="closeRejectModal()"/>
  <ShowCommentModal v-if="showComment" :factory="selectedFactory" @declineComment="declineComment" @approveComment="approveComment" :comment="selectedFactory.comment" @closeModal="closeShowModal()"/>
</template>

<script setup>
import axios from 'axios';
import { ref, onMounted } from 'vue';
import ShowCommentModal from '../CommentModal.vue';
import { useToast } from 'vue-toastification';
import RejectPurchaseModal from './RejectPurchaseModal.vue';
axios.defaults.withCredentials = true;


const rejectModal= ref(false);
const toast = useToast();
const selectedFactory = ref(null);
const showComment = ref(false);
const purchases = ref([]);
const AllPurchases= ref([]);
const tooltipVisible = ref(false);
const currentBuyer = ref(null);

onMounted(() => {
    axios.get('http://localhost:8080/WebShopAppREST/rest/purchases/allFactoryPurchases')
    .then(response => {
      purchases.value = response.data;
      AllPurchases.value = response.data;
      console.log(response.data);
    })
    .catch(error => {
      alert(error);
    });
});

const filters = ref({
  purchaseRangeFrom: null,
  purchaseRangeTo: null,
  priceFrom: 0,
  priceTo: 0
});

function searchPurchases(event) {
  event.preventDefault();
  Search();
}

function Search() {
  const purchaseRangeFrom = filters.value.purchaseRangeFrom ? new Date(filters.value.purchaseRangeFrom) : null;
  const purchaseRangeTo = filters.value.purchaseRangeTo ? new Date(filters.value.purchaseRangeTo) : null;

  if (purchaseRangeFrom) {
    purchaseRangeFrom.setHours(0, 1, 0, 0); // Set to 00:01
  }

  if (purchaseRangeTo) {
    purchaseRangeTo.setHours(23, 59, 0, 0); // Set to 23:59
  }

  const priceFrom = filters.value.priceFrom;
  const priceTo = filters.value.priceTo;

  purchases.value = AllPurchases.value.filter(purchase => {
    const purchaseDate = new Date(purchase.purchase.purchaseDate);
    const purchasePrice = purchase.purchase.price;

    console.log(purchaseRangeFrom);
    console.log(purchaseDate);
    console.log(purchaseRangeTo);

    const isDateInRange = (!purchaseRangeFrom || purchaseDate >= purchaseRangeFrom) &&
                          (!purchaseRangeTo || purchaseDate <= purchaseRangeTo);

    const isPriceInRange = (!priceFrom || purchasePrice >= priceFrom) &&
                           (!priceTo || purchasePrice <= priceTo);

    return isDateInRange && isPriceInRange;
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
  }
}

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
};

const showTooltip = (buyer) => {
  currentBuyer.value = buyer;
  tooltipVisible.value = true;
};

const hideTooltip = () => {
  tooltipVisible.value = false;
  currentBuyer.value = null;
};

function logOut() {
  axios.post('http://localhost:8080/WebShopAppREST/rest/logout').then(response => {
    router.push({ name: 'ghost' });
  })
  .catch(error => {
    console.log(error);
  });
}
const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('en-GB', options);
};
function ScrollToBeginning(){
   if (window.scrollY !== 0) {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    }
}
function closeShowModal(){
  showComment.value = false;
}
function ShowComment(purchase){
  showComment.value = true;
  selectedFactory.value = purchase;
}
function PurchaseHistory() {
  router.push({ name: 'customerProfile' });
}
function ClearFilters(){
  filters.value.purchaseRangeFrom = null;
  filters.value.purchaseRangeTo = null;
  filters.value.priceFrom = 0;
  filters.value.priceTo = 0;
  Search();
}
function approveComment(){


  axios.put(`http://localhost:8080/WebShopAppREST/rest/comments/approve/${selectedFactory.value.comment.id}`, {}, { withCredentials: true })
    .then(response => {
      selectedFactory.value.comment.status = "Approved";
      toast.success('Comment approved');
    })
    .catch(error => {
      toast.error('Comment failed to approve');
    });
}
function declineComment(){

  axios.put(`http://localhost:8080/WebShopAppREST/rest/comments/decline/${selectedFactory.value.comment.id}`, {}, { withCredentials: true })
    .then(response => {
      selectedFactory.value.comment.status = "Rejected";
      toast.success('Comment declined');
    })
    .catch(error => {
      toast.error('Comment failed to decline');
    });
}
function ShowAll(){
  purchases.value = AllPurchases.value;
}
function ShowApproved(){
  purchases.value = AllPurchases.value.filter(purchase => purchase.purchase.purchaseStatus === "Approved");
}
function ShowRejected(){
  purchases.value = AllPurchases.value.filter(purchase => purchase.purchase.purchaseStatus === "Rejected");
}
function ShowInProcess(){
  purchases.value = AllPurchases.value.filter(purchase => purchase.purchase.purchaseStatus === "Inprocess");
}
function ShowRejectModal(purchase){
  selectedFactory.value = purchase;
  rejectModal.value = true;
}
function ApprovePurchase(purchase){
  console.log(purchase.purchase.id);
  axios.defaults.withCredentials = true;
  axios.patch('http://localhost:8080/WebShopAppREST/rest/purchases/approve/?id='+purchase.purchase.id)
    .then(response => {
      purchase.purchase.purchaseStatus = "Approved";
      toast.success('Purchase approved');
    })
    .catch(error => {
      toast.error('Purchase failed to approve');
    });

}
function DeclinePurchase(data){
  axios.defaults.withCredentials = true;
  const params = {
    reason: data.value,
    purchaseId: selectedFactory.value.purchase.id
  };
  axios.patch('http://localhost:8080/WebShopAppREST/rest/purchases/reject/',params)
    .then(response => {
      selectedFactory.value.purchase.purchaseStatus = "Rejected";
      toast.success('Purchase rejected');
    })
    .catch(error => {
      toast.error('Purchase failed to reject');
    });

}
function closeRejectModal(){
  rejectModal.value = false;
}

</script>

<style scoped>
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
  margin-top:65px;
}
.main
{
  display: flex;
  flex-direction: row;
  gap: 20px;
}
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
.arc{
  height: 35px; 
}
.arc button{
  margin-top: 0;
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
.purchase-header{
  display: flex;
  justify-content: space-between;
  flex-direction: row;
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

.buyer-name {
  position: relative;
  display: inline-block;
  cursor: pointer;
}

.tooltip {
  position: absolute;
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 10;
  width: 200px;
  top: 20px;
  left: 150px;
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
.tooltip p {
  margin: 5px 0;
}
.approved{
  margin-top: 10px;
    background-color: #238d3c;
  color: white;
  border: none;
  width: 92px;
  border-radius: 5px;
  padding: 10px 15px;
  cursor: pointer;
}
.approved:hover{
  background-color: #186d2c;
}
.rejected{
  margin-top: 10px;
    background-color: #a41906;
  color: white;
  border: none;
  width: 92px;
  border-radius: 5px;
  padding: 10px 15px;
  cursor: pointer;
}
.rejected:hover{
  background-color: #ff0000;
}
.inProcess{
  margin-top: 10px;
    background-color: #aca910;
    color: white;
    border: none;
    width: 92px;
    border-radius: 5px;
    padding: 10px 15px;
    cursor: pointer;
}
.inProcess:hover{
  background-color: #927001;
}
.showAll{
  margin-top: 10px;
  background-color: #007bff;
    color: white;
    border: none;
    width: 92px;
    border-radius: 5px;
    padding: 10px 15px;
    cursor: pointer;
}
.showAll:hover{
  background-color: #0056b3;
}

</style>
