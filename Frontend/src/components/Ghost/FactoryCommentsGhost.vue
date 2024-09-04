<template>
    <div class="commentSection"> 
    <div>
      <h2 style="margin-bottom: 10px">Comment Section</h2>
      <div v-for="comment in comments" :key="comment.id" class="comment-card">
        <div class="profile-icon">
            <i class="fa fa-user" aria-hidden="true"></i>
        </div>
        <div class="comment-details">
          <div class="user-info">
            <span>{{ comment.creatorName }}</span>
            <span class="comment-date">{{ formatDate(comment.dateCreated) }}</span>
          </div>
          <p>{{ comment.text }}</p>
          <div class="comment-rating">
            <span>Rating: {{ comment.rating }}</span>
          </div>
        </div>
      </div>
    </div>
    </div >
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import axios from 'axios';
  import { useRoute } from 'vue-router';
  
  
  const route = useRoute();
  const factoryId = route.params.id;
  const comments = ref([]);
  
  onMounted(() => {
    axios.get(`http://localhost:8080/WebShopAppREST/rest/comments/${factoryId}`, { withCredentials: true })
      .then(response => {
        comments.value = response.data;
      })
      .catch(error => {
        alert(error);
      });
  });
  
  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('en-US', options);
  };
  </script>
  
  <style scoped>
    .commentSection {
    width: 95vh;
    margin: 0 auto;
    display: flex;
    flex-direction: column;
    margin-bottom: 20px;
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
    transition: transform ease 0.2s, background-color ease 0.7s;
  }
  .comment-card:hover {
    transform: scale(1.02);
    background-color: #e0e0e0;
  }
  
  .profile-icon {
    margin-right: 15px;
    color: #007bff; /* Boja ikonice */
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
  </style>
  