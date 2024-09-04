<script setup>
import FactoryDetails from '@/components/FactoryDetails.vue';
import FactoryComments from '@/components/Customer/FactoryCommentsCustomer.vue';
import FactoryChocolates from '@/components/Customer/FactoryChocolatesCustomer.vue';
import StatusBar from '@/components/StatusBar.vue';
</script>

<template>
  <main>
    <StatusBar />
    <FactoryDetails />
    <FactoryChocolates />
    <FactoryComments />
  </main>
</template>