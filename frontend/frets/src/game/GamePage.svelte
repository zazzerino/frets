<script lang="ts">
  import { game } from '../stores';
</script>

<h1 class="text-lg font-bold mb-4">
  Game
</h1>

{#if $game}
  <p>{JSON.stringify($game)}</p>
{/if}
