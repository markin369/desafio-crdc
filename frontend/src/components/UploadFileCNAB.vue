<script setup>
    import { ref, computed } from 'vue'
    import axios from 'axios';
    import ErrorDetails from './ErrorDetails.vue';
    
    const file = ref(null);
    const setError = ref(null);
    const erros = ref(null);
    const setSuccess = ref(null);

    const uploadURL = import.meta.env.VITE_VUE_APP_UPLOAD_URL;

    const handleFileChange = (event) => {
      const selectedFile = event.target.files[0];
        setError.value = null;
        setSuccess.value = null;
        if (selectedFile) {
            // Atualize a referência reativa com o novo arquivo
            file.value = selectedFile;
        }
    };

    const uploadFile = async () => {
    try {
      const formData = new FormData();
      formData.append('file', file.value);

      const response = await axios.post(uploadURL, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      if(response.data.status === 'error')
        setError.value = response.data.message;
      else
        setSuccess.value = response.data.message;

    } catch (error) {
      setError.value = error.response.data.message;
      erros.value = error.response.data.errors;
      setSuccess.value = null;
    }
  };

    // Obtenha o nome do arquivo
    const fileName = computed(() => (file.value ? file.value.name : 'Nenhum arquivo selecionado'));
</script>

<template>

    <h1 className="text-2xl font-semibold mb-4">Importação de CNAB</h1>
    <div className="mb-8">
        <div className="flex items-center space-x-4">
            <label className="text-gray-600">
            <span className="bg-blue-500 hover:bg-blue-600 cursor-pointer text-white py-2 px-4 rounded-lg">
                Choose File
            </span>
            <input
                type="file"
                ref="fileInput" 
                @change="handleFileChange"
                className="hidden"
                accept=".txt"
            />
            </label>
            <button
            className="bg-blue-700 hover:bg-blue-800 text-white py-2 px-4 rounded-lg"
            @click="uploadFile"
            >
            Upload File
            </button>
        </div>
        <div v-if="file">
            <p className="text-green-500 mt-2">{{ fileName }}</p>
        </div>
        <div v-if="setError">
          <p className="text-red-500 mt-2">{{setError}}</p>
          <error-details :errors="erros" />
        </div>
        <div v-if="setSuccess">
          <p className="text-green-500 mt-2">{{setSuccess}}</p>
        </div>
    </div>
    

</template>