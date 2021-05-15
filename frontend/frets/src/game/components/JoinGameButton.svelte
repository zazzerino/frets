<script lang="ts">
  import { sendJoinGame } from '../../socket/message';
  import { user } from '../../stores';
  export let gameId: number;
</script>

<button on:click={() => sendJoinGame(gameId, $user.id)}>
  Join Game
</button>
