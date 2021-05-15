<script lang="ts">
  import { user, game } from './stores';
</script>

<div class="fixed bottom-0">
  <p class="text-sm break-words">
    user: {JSON.stringify($user)}
  </p>
  {#if $game}
    <p class="text-sm break-words">
      game: {JSON.stringify($game)}
    </p>
  {/if}
</div>

<!-- <style>
  .Footer {
    position: absolute;
    bottom: 0;
    left: 50%;
    transform: translate(-50%, 0%);
    background-color: #eee;
    font-size: 0.8rem;
  }
  p {
    word-wrap: break-word;
  }
</style> -->
