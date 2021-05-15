<script lang="ts">
  import { game } from '../stores';
  import CreateGameButton from './components/CreateGameButton.svelte';
  import WaitingRoom from './components/WaitingRoom.svelte';
</script>

<h2>Game</h2>

{#if $game == null}
  <CreateGameButton/>
{:else if $game?.state === 'INIT'}
  <WaitingRoom game={$game}/>
{/if}
