<script lang="ts">
  import { user, game, games } from './stores';
</script>

<div class="Footer">
  <p>
    user: {JSON.stringify($user)}
  </p>
  {#if $game}
    <p>
      game: {JSON.stringify($game)}
    </p>
  {/if}
</div>

<style>
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
</style>
