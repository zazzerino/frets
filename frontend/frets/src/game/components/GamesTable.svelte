<script lang="ts">
  import type { Game } from '../game';
  import { formatState } from '../game';
  import { playerCount, formatTimestamp } from '../game';
  import { games, user } from '../../stores';
  import { sendJoinGame } from '../../socket/message';

  export function handleClick(game: Game) {
    const userId = $user.id;

    if (game.state === 'INIT' && !game.playerIds.includes(userId)) {
      sendJoinGame(game.id, userId);
    }
  }
</script>

<table class="m-auto shadow-md rounded">
  <thead>
    <tr>
      <th class="bg-blue-100 text-left py-2 px-2">Id</th>
      <th class="bg-blue-100 text-left py-2 px-2">Created At</th>
      <th class="bg-blue-100 text-left py-2 px-2">State</th>
      <th class="bg-blue-100 text-left py-2 px-2">Players</th>
    </tr>
  </thead>
  <tbody>
    {#if $games}
      {#each $games as game}
        <tr 
          class="hover:bg-green-100 cursor-pointer"
          on:click={() => handleClick(game)}
        >
          <td title="Click to join game" class="border px-2">
            {game.id}
          </td>
          <td title="Click to join game" class="border px-2 text-left">
            {formatTimestamp(game.createdAt)}
          </td>
          <td title="Click to join game" class="border px-2 text-left capitalize">
            {formatState(game.state)}
          </td>
          <td title="Click to join game" class="border px-2">
            {playerCount(game)}
          </td>
        </tr>
      {/each}
    {/if}
  </tbody>
</table>
