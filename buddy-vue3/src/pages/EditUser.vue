<template>
  dfs
</template>

<script setup lang="ts">
import {useRoute} from 'vue-router'
import {ref} from 'vue'
const route = useRoute();
const editUser = ref({
    editKey: route.query.editKey,
    currentValue: route.query.currentValue,
    editName: route.query.editName,
})

</script>

<style>

</style>