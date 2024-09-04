<script setup>
import FactoryDetails from '@/components/FactoryDetails.vue';
import FactoryComments from '@/components/Manager/FactoryCommentsManager.vue';
import FactoryChocolates from '@/components/Manager/FactoryChocolatesManager.vue';
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