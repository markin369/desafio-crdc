<!-- ErrorDetails.vue -->

<template>
    <div>
      <div @click="toggleAccordion" class="cursor-pointer text-blue-500">
        {{ isOpen ? 'Ocultar Detalhes' : 'Mostrar Detalhes' }}
      </div>
  
      <div v-show="isOpen" class="mt-4">
        <!-- Conteúdo dos detalhes da mensagem de erro aqui -->
        <ul>
          <li v-for="(error, index) in errors" :key="index" class="text-red-500">
            {{ error }}
          </li>
        </ul>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        isOpen: false,
      };
    },
    props: {
      errors: {
        type: Array,
        required: true,
      },
    },
    methods: {
      toggleAccordion() {
        this.isOpen = !this.isOpen;
      },
    },
  };
  </script>
  
  <style scoped>
  /* Estilos específicos do componente aqui, utilizando classes do Tailwind CSS */
  </style>