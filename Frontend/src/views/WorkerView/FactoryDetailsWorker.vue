<script setup>
import FactoryDetails from '@/components/FactoryDetails.vue';
import FactoryComments from '@/components/Worker/FactoryCommentsWorker.vue';
import FactoryChocolates from '@/components/Worker/FactoryChocolatesWorker.vue';
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