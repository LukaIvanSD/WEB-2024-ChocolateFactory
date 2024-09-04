<template>
    <div class="modal-overlay">
      <div class="modal">
        <h1 class="header">Add Chocolate</h1>
        <form @submit="ConfirmAdd($event)">
          <div>
            <label for="category">Category:</label>
            <input type="text" id="category" v-model="chocolate.category" required>
          </div>
          <div>
            <label for="description">Description:</label>
            <input type="text" id="description" v-model="chocolate.description" required>
          </div>
          <div>
            <label for="imagePath">Image Path:</label>
            <input type="text" id="imagePath" v-model="chocolate.imagePath" required>
          </div>
          <div>
            <label for="name">Name:</label>
            <input type="text" id="name" v-model="chocolate.name" required>
          </div>
          <div>
            <label for="type">Type:</label>
            <input type="text" id="type" v-model="chocolate.type" required>
          </div>
          <div>
            <label for="weight">Weight:</label>
            <input type="number" id="weight" @focusout="SetFocus('weight')" @focusin="ResetFocus('weight')" min="1" v-model="chocolate.weight" required>
            <label class="error" v-if="focusStates.weight===true && chocolate.weight===0" v>Weight cannot be 0</label>
            <label class="error" v-if="focusStates.weight===true && chocolate.weight<0" >Weight must be positive</label>
          </div>
          <div>
            <label for="price">Price:</label>
            <input type="number" id="price" @focusout="SetFocus('price')" @focusin="ResetFocus('price')" min="1" v-model="chocolatePrice" required>
            <label class="error" v-if="focusStates.price===true && chocolatePrice===0" >Price cannot be 0</label>
            <label class="error" v-if="focusStates.price===true && chocolatePrice<0">Price must be positive number</label>
          </div>
          <button type="submit" class="submit">Confirm</button>
          <button type="button" class="cancel" @click="closeAddModal">Cancel</button>
        </form>
      </div>
    </div>
  </template>
<script setup>

import { ref, onMounted, defineEmits, defineProps } from 'vue';
import { useToast } from 'vue-toastification';
const emits=defineEmits(['addChocolate','closeAddModal'])
const chocolate = ref({
  category: '',
  description: '',
  imagePath: '',
  name: '',
  type: '',
  weight: 0,
});
const focusStates=ref( {
  weight: false,
  price: false,
    });
const toast=useToast();
const chocolatePrice = ref(0);

  function ConfirmAdd(event){
      event.preventDefault();
      emits('addChocolate',chocolate.value,chocolatePrice.value);
      GenerateToastNotification();
      closeAddModal();
  }
  function GenerateToastNotification(){
      toast.success('Chocolate added successfully');
  }

  function SetFocus(field)  {
      focusStates.value[field] = true;
  }


  function ResetFocus(field) {
      focusStates.value[field] = false;
  }

    function closeAddModal(){
        emits('closeAddModal');
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
.error {
    color: #FF4C4C;
    font-size: 15px;
  }
.modal {
  background-color: #f5f5f5;
  padding: 20px;
  color: #333;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  text-align: left;
  max-width: 400px;
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

.modal input {
  padding: 0.5rem;
  border: 1px solid #797979;
  border-radius: 0.25rem;
  background-color: #555555;
  color: white;
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