<script lang="ts">
  import { game } from '../stores';
  import CreateGameButton from './components/CreateGameButton.svelte';
  import Stave from './components/Stave.svelte';
  import Fretboard from './components/Fretboard.svelte';
</script>

<h1 class="text-lg font-bold mb-4">
  Game
</h1>

{#if $game}
  <div class="flex flex-col items-center">
    <Stave/>
    <Fretboard/>
    <p>{JSON.stringify($game)}</p>
  </div>
{:else}
  <CreateGameButton/>
{/if}
