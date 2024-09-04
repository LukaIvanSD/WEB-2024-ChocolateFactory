<template>
  <div class="modal-overlay">
    <div class="modal">
      <h1 class="header">Edit Chocolate</h1>
      <form @submit="ConfirmEdit">
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
          <input type="number" id="weight" v-model="chocolate.weight" min="1" required>
        </div>
        <div>
          <label for="price">Price:</label>
          <input type="number" id="price" v-model="chocolatePrice" min="1" required>
        </div>
        <button type="submit" class="submit">Confirm</button>
        <button type="button" class="cancel" @click="closeEditModal">Cancel</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, defineEmits, defineProps } from 'vue';
import { useToast } from 'vue-toastification';
const emit = defineEmits(['editChocolate', 'closeEditModal']);
const props = defineProps({
  receivedObject: {
    type: Object,
    default: () => null
  }
});

const chocolate = ref({
  category: '',
  description: '',
  imagePath: '',
  name: '',
  type: '',
  weight: 0,
});
const toast=useToast();
const chocolatePrice = ref(0);

onMounted(() => {
  if (props.receivedObject) {
    chocolate.value = { ...props.receivedObject.chocolate };
    chocolatePrice.value = props.receivedObject.chocolatePrice.price;
  }
});

function ConfirmEdit(event) {
  event.preventDefault();
  if(!IsEdited(chocolate,chocolatePrice.value,props.receivedObject))
  {
    emit('editChocolate',chocolate,chocolatePrice.value, props.receivedObject);
    GenerateToastNotification();
  }
  closeEditModal();

}
function IsEdited(chocolate,price,oldObject)
{
return chocolate.value.category===oldObject.chocolate.category &&
   chocolate.value.description===oldObject.chocolate.description &&
    chocolate.value.imagePath===oldObject.chocolate.imagePath &&
     chocolate.value.name===oldObject.chocolate.name &&
      chocolate.value.type===oldObject.chocolate.type &&
       chocolate.value.weight===oldObject.chocolate.weight &&
        price===oldObject.chocolatePrice.price;
}
function GenerateToastNotification() {
  toast.success('Chocolate edited successfully');
}

function closeEditModal() {
  emit('closeEditModal');
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
