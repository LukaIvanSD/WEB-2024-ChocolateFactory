<template>
    <div class="commentSection">
      <div class="header">
      <h2 style="margin-bottom: 10px">Comment section</h2>
      <div class="buttons" v-if="isUserManagerOfFactory">
      <button class="allButton" @click="ShowAllComments()">All</button>
      <button class="approveButton" @click="ShowApprovedComments()">Approved</button>
      <button class="rejectButton" @click="ShowDeclinedComments()">Declined</button>
      <button class="pendingButton" @click="ShowPendingComments()">Pending</button>
      </div>
      </div>
      <div v-for="comment in comments" :key="comment.id" class="comment-card">
        <div class="profile-icon">
            <i class="fa fa-user" aria-hidden="true"></i>
        </div>
        <div class="comment-details">
          <div class="user-info">
            <div class="status">
            <span>{{ comment.creatorName }}</span>
            <span :class="{
                  'status-rejected': comment.status === 'Declined',
                  'status-approved': comment.status === 'Approved',
                  'status-pending': comment.status === 'InProcess'}">
            {{ comment.status }}
          </span>
          <div class="functions" v-if="comment.status==='InProcess'">
          <button class="approveButton" @click="ApproveComment(comment.id)">Approve</button>
          <button class="rejectButton" @click="DeclineComment(comment.id)">Decline</button>
          </div>            
        </div>
            <span class="comment-date">{{ formatDate(comment.dateCreated) }}</span>
          </div>
          <p>{{ comment.text }}</p>
          <div class="comment-rating">
            <span>Rating: {{ comment.rating }}</span>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import axios from 'axios';
  import { useRoute } from 'vue-router';
  
  const isUserManagerOfFactory=ref(false);
  const route = useRoute();
  const factoryId = route.params.id;
  const comments = ref([]);
  const AllComments = ref([]);
  
  onMounted(() => {
    axios.get(`http://localhost:8080/WebShopAppREST/rest/comments/${factoryId}`, { withCredentials: true })
      .then(response => {
        comments.value = response.data.sort((a, b) =>new Date(a.dateCreated)- new Date(b.dateCreated) );
        AllComments.value = comments.value;
      })
      .catch(error => {
        alert(error);
      });
      axios.post('http://localhost:8080/WebShopAppREST/rest/managers/rights/?factoryId='+factoryId,axios.defaults.withCredentials=true)
      .then(response=>{
        isUserManagerOfFactory.value=response.data;
      })
      .catch(error=>{
      })
  });
  
  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('en-US', options);
  };
  function ShowAllComments()
  {
    comments.value = AllComments.value;
    ScrollWindow();
  }
  function ShowApprovedComments()
  {
    comments.value = AllComments.value.filter(comment => comment.status === "Approved");
    ScrollWindow();
  }
  function ShowDeclinedComments()
  {
    comments.value = AllComments.value.filter(comment => comment.status === "Declined");
    ScrollWindow();
  }
  function ShowPendingComments()
  {
    comments.value = AllComments.value.filter(comment => comment.status === "InProcess");
    ScrollWindow();
  }
  function ScrollWindow()
  {
    setTimeout(() => {
    const firstCommentDetails = document.querySelector('.commentSection');
    if (firstCommentDetails) {
      firstCommentDetails.scrollIntoView({ behavior: 'smooth' });
      setTimeout(() => {
        const scrollPosition = window.scrollY + window.innerHeight;
        const pageHeight = document.documentElement.scrollHeight;
        if (scrollPosition < pageHeight - 50) {
          window.scrollBy({ top: -100, behavior: 'smooth' });
        }
      }, 500);
    }
  }, 100);
  }
  function ApproveComment(commentId)
  {
    console.log(commentId);
    axios.put(`http://localhost:8080/WebShopAppREST/rest/comments/approve/${commentId}`, {}, { withCredentials: true })
    .then(response => {
      comments.value = comments.value.map(comment => {
        if (comment.id === commentId) {
          comment.status = "Approved";
        }
        return comment;
      });
    })
    .catch(error => {
      alert(error);
    });
  }
  function DeclineComment(commentId)
  {
    axios.put(`http://localhost:8080/WebShopAppREST/rest/comments/decline/${commentId}`, {}, { withCredentials: true })
    .then(response => {
      comments.value = comments.value.map(comment => {
        if (comment.id === commentId) {
          comment.status = "Declined";
        }
        return comment;
      });
    })
    .catch(error => {
      alert(error);
    });
  }
  </script>
  
  <style scoped>
  .commentSection {
    width: 95vh;
    margin: 0 auto;
    display: flex;
    flex-direction: column;
    margin-bottom: 20px;
  }
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
  }
  .buttons {
    display: flex;
    gap: 10px;
  }
  .comment-card {
    display: flex;
    align-items: flex-start;
    border: 1px solid #ccc;
    border-radius: 8px;
    padding: 15px;
    width:95vh;
    margin-bottom: 15px;
    color: #4b2e20;
    background-color: #f5f5f5;
    transition: transform 0.2s ease, background-color 0.7s ease;
  }
  .comment-card:hover {
    background-color: #e0e0e0;
    transform: scale(1.02);
  }
  
  .profile-icon {
    margin-right: 15px;
    color: #007bff; /* Boja ikonice */
  }
  .approveButton
  {
    margin-top: 10px;
    background-color: #238d3c;
  color: white;
  border: none;
  width: 92px;
  border-radius: 5px;
  padding: 10px 15px;
  cursor: pointer;
  }
  .approveButton:hover {
    background-color: #186d2c;
  }
  .rejectButton{
    margin-top: 10px;
    background-color: #a41906;
  color: white;
  border: none;
  width: 92px;
  border-radius: 5px;
  padding: 10px 15px;
  cursor: pointer;
  }
  .rejectButton:hover {
  background-color: #ff0000;
}

.allButton{
  margin-top: 10px;
  background-color: #007bff;
    color: white;
    border: none;
    width: 92px;
    border-radius: 5px;
    padding: 10px 15px;
    cursor: pointer;
  }
  .allButton:hover {
    background-color: #0056b3;
  }
  .functions
  {
    display: flex;
    gap: 20px;
  }
  .functions button
  {
    margin-top:0;
  }

  .pendingButton{
    margin-top: 10px;
    background-color: #aca910;
    color: white;
    border: none;
    width: 92px;
    border-radius: 5px;
    padding: 10px 15px;
    cursor: pointer;
  }
  .pendingButton:hover {
    background-color: #927001;
  }
  .comment-details {
    flex: 1;
  }
  
  .user-info {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 5px;
  }
  
  .comment-date {
    color: #4b2e20;
    font-size: 0.8em;
  }
  
  .comment-rating {
    margin-top: 10px;
    color: #007bff;
  }
  .status
    {
        display: flex;
        align-items: center;
        gap: 20px;

    }
  .status-rejected
    {
      font-size: large;
      font-weight: 500;
        color:#a41906;
    }
    .status-approved
    {
      font-size: large;
      font-weight: 500;
        color:#238d3c;
    }
    .status-pending
    {
      font-size: large;
      font-weight: 500;
        color:#ffae00;
    }
  </style>
  