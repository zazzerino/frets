<script lang="ts">
  import { page } from './stores';
  import Navbar from './Navbar.svelte';
  import Footer from './Footer.svelte';
</script>

<main class="m-4 text-center flex flex-col">
  <Navbar/>
  <div class="flex-grow">
    <svelte:component this={$page}/>
  </div>
  <Footer/>
</main>
