<template>
    <div class="modal-overlay">
      <div class="modal">
        <h1 class="header">Rate <span class="factortName">{{ props.factoryName }}</span> factory</h1>
        <form @submit="ConfirmAdd">
          <div>
            <label for="comment">Comment:</label>
            <textarea  id="comment" v-model="comment" required></textarea>
          </div>
          <div >
                <label>Rating:</label>
                <div v-for="rating in [1, 2, 3, 4, 5]" :key="rating" class="rating">
                    <label :for="'rating' + rating">{{ rating }}</label>
                     <input type="radio" :id="'rating' + rating" name="rating" :value="rating" v-model="selectedRating" required>
                </div>
            </div>
          <button type="submit" class="submit">Confirm</button>
          <button type="button" class="cancel" @click="closeAddModal">Cancel</button>
        </form>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, defineEmits,defineProps} from 'vue';
  const emit = defineEmits(['addComment', 'closeEditModal']);
  

  const comment=ref('');
  const selectedRating=ref(0);
  const props = defineProps({
  factoryName: {
    type: String,
    default: ''
  }
});
  
  function ConfirmAdd(event) {
    event.preventDefault();
    emit('addComment', { comment: comment.value, rating: selectedRating.value });
    comment.value = '';  // Reset comment field
    selectedRating.value = null;  // Reset rating
    closeAddModal();
  
  }
  
  function closeAddModal() {
    emit('closeAddModal');
  }
  </script>
  
  <style scoped>
  .rating {
    display: flex;
    align-items: center;
    flex-direction: row;
    gap: 0.5rem;
  }
  .rating input {
    width: 10px;
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
    text-align: left;
    max-width: 500px;
    width: 100%;
  }
  
  .modal form {
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }
  
  .modal label {
    font-weight: bold;
  }
  .factortName {
    color: #007bff;
  }
  .modal textarea {
    padding: 0.5rem;
    font-size: 17px;
    border: 1px solid #797979;
    border-radius: 0.25rem;
    background-color: #555555;
    color: white;
    min-height: 200px;
    width: 100%;
  }
  
  button.submit {
    padding: 0.5rem;
    border: none;
    border-radius: 0.25rem;
    background-color: #007bff;
    color: white;
    cursor: pointer;
  }
  
  button.submit:hover {
    background-color: #0056b3;
  }
  
  button.cancel {
    padding: 0.5rem;
    border: none;
    border-radius: 0.25rem;
    background-color: #dc3545;
    color: white;
    cursor: pointer;
  }
  
  button.cancel:hover {
    background-color: #c82333;
  }
  
  .header {
    text-align: center;
    margin-bottom: 1rem;
  }
  </style>
  