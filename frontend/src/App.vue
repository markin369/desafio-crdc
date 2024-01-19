<script setup>
import UploadFileCNAB from './components/UploadFileCNAB.vue';
import TransacoesReports from './components/TransacoesReports.vue';

</script>

<template>
  <div className="p-4">
      <UploadFileCNAB />    
      <TransacoesReports />
    </div>
  
</template>

<style scoped>
.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter;
  transition: filter 300ms;
}
.logo:hover {
  filter: drop-shadow(0 0 2em #646cffaa);
}
.logo.vue:hover {
  filter: drop-shadow(0 0 2em #42b883aa);
}
</style>
