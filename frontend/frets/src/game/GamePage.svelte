<script lang="ts">
  import { game } from '../stores';
  import CreateGameButton from './components/CreateGameButton.svelte';
</script>

<h1 class="text-lg font-bold mb-4">
  Game
</h1>

{#if $game}
  <p>{JSON.stringify($game)}</p>
  {:else}
    <CreateGameButton/>
{/if}
