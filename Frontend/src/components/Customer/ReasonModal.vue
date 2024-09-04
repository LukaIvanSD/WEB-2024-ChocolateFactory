<template>
    <div class="modal-overlay">
      <div class="modal">
        <div class="titles">
        <h1>Rejection reason</h1>
        </div >
        <div class="comment">
          <div>
            <label for="comment">Reason:</label>
            <textarea  id="comment" v-model="props.reason" disabled></textarea>
          </div>
          <button  type="button" class="submit" @click="closeModal">OK</button>
          </div >
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, defineEmits,defineProps} from 'vue';
  import { useToast } from 'vue-toastification';
  const emit = defineEmits(['closeModal']);

  const props = defineProps({
  reason: {
    reason: String,
    default: null
  }
});
  
  function closeModal() {
    emit('closeModal');
  }


  </script>
  
  <style scoped>
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
  .buttons {
    display: flex;
    flex:1;
    gap: 10rem;
    justify-content: center;
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
  
  .comment {
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
  
  button.approve {
    padding: 0.5rem;
    border: none;
    border-radius: 0.25rem;
    background-color: #28a745;
    color: white;
    cursor: pointer;
  }
  button.approve:hover {
    background-color: #218838;
  }
  button.decline {
    padding: 0.5rem;
    border: none;
    border-radius: 0.25rem;
    background-color: #dc3545;
    color: white;
    cursor: pointer;
  }
  button.decline:hover {
    background-color: #c82333;
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
  }
  .titles{
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 0.5rem;
    margin-bottom: 10px;
  }
  </style>
  