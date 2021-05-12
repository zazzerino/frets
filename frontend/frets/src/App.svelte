<script lang="ts">
	import { page } from './stores';
	import Navbar from './Navbar.svelte';
	import Footer from './Footer.svelte';
</script>

<main>
	<Navbar/>
	<svelte:component this={$page}/>
	<Footer/>
</main>

<style>
	main {
		text-align: center;
		padding: 1em;
		max-width: 240px;
		margin: 0 auto;
	}

	@media (min-width: 640px) {
		main {
			max-width: none;
		}
	}
</style>