<script setup>
    import { ref, reactive, onMounted, computed } from 'vue'
    import axios from 'axios';

    let reports = reactive({
        data: []
    });

    const setError = ref(null);
    const setSuccess = ref(null);
    const fetchURL = import.meta.env.VITE_VUE_APP_FETCH_URL;

    const fetchTransactions = async () => {
        try {
            const response = await axios.get(fetchURL);
            reports.data = response.data;
            if(response.data.status === 'error')
                setError.value = response.data.message;
            else
                setSuccess.value = response.data.message;
        } catch (error) {
            setError.value = error.response.data.message;
            setSuccess.value = null;
        } finally {
            
        }
    };

    onMounted(() => {
        fetchTransactions();
    });

    const formatDate = (dateString) => {
        const options = { 
            year: 'numeric', 
            month: 'numeric', 
            day: 'numeric', 
            timeZone: 'UTC', // Ajuste conforme necessário
        };

        const formattedDate = new Intl.DateTimeFormat('pt-BR', options).format(new Date(dateString));

        return formattedDate;
    };


    const formatCurrency = (value) => {
    const formattedValue = new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    }).format(parseFloat(value));

    return formattedValue;
  };
   
</script>

<template>
    
    <button className="bg-gray-200 hover:bg-gray-300 text-gray-600 py-2 px-4 rounded-lg mb-4" @click="fetchTransactions">Atualizar Transações</button>
    <div className="p-4">
        <h2 className="text-2xl font-semibold mb-4">Transações</h2>
        <p v-if="reports.data.length==0" className="mb-4 text-gray-500 text-center">Sem transações disponíveis.</p>
        <ul className="bg-white shadow-md rounded-md p-4">
            <div>
                <li v-for="(report, index) in reports.data" :key="index" className="mb-4 p-4 border-b border-gray-300 flex flex-col">
                    <div className="flex justify-between items-center mb-2">
                    <div className="text-xl font-semibold"> {{report.razaoSocial}} </div>
                        <div :class="{
                        'text-green-500 font-semibold' : parseFloat(report.total) >= 0,
                        'text-red-500 font-semibold' : parseFloat(report.total) <= 0 
                        }" >
                            Total: {{formatCurrency(parseFloat(report.total))}}
                        </div>
                    </div>
                    <table className="table-auto w-full">
                        <thead>
                        <tr>
                            <th className="px-4 py-2">Razão Social</th>
                            <th className="px-4 py-2">Identificador da Empresa</th>
                            <th className="px-4 py-2">Tipo de Transação</th>
                            <th className="px-4 py-2">Conta Origem</th>
                            <th className="px-4 py-2">Conta Destino</th>
                            <th className="px-4 py-2">Valor</th>

                        </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(transacao, key) in report.transacoes" :key={key} :class="{ 'bg-gray-100': index % 2 === 0 }">
                                <td className="px-4 py-2">{{transacao.razaoSocial}}</td>
                                <td className="px-4 py-2">{{transacao.identificadorEmpresa}}</td>
                                <td className="px-4 py-2">{{transacao.tipoTransacao}}</td>
                                <td className="px-4 py-2">{{transacao.contaOrigem}}</td>
                                <td className="px-4 py-2">{{transacao.contaDestino}}</td>
                                <td className="px-4 py-2">{{formatCurrency(transacao.valorTransacao)}}</td>
                            </tr>
                        
                        </tbody>
                    </table>  
               </li>
                
            </div>
        </ul>
    </div>
</template>