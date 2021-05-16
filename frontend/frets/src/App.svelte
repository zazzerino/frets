<script lang="ts">
  import { page } from './stores';
  import Navbar from './Navbar.svelte';
</script>

<main class="text-center flex flex-col h-screen">
  <Navbar/>
  <div class="flex-grow">
    <svelte:component this={$page}/>
  </div>
</main>
