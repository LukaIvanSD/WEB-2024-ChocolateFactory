<script setup>
import FactoryDetails from '@/components/FactoryDetails.vue';
import FactoryComments from '@/components/Ghost/FactoryCommentsGhost.vue';
import FactoryChocolates from '@/components/Ghost/FactoryChocolatesGhost.vue';
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