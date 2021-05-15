<script lang="ts">
  import { playerCount, formatTimestamp } from '../game';
  import { games } from '../../stores';
  import JoinGameButton from './JoinGameButton.svelte';
</script>

<table>
  <thead>
    <tr>
      <th>Id</th>
      <th>Created At</th>
      <th>State</th>
      <th>Players</th>
      <th>Join</th>
    </tr>
  </thead>
  <tbody>
    {#if $games}
      {#each $games as game}
        <tr>
          <td>{game.id}</td>
          <td>{formatTimestamp(game)}</td>
          <td>{game.state}</td>
          <td>{playerCount(game)}</td>
          {#if game.state === 'INIT'}
            <td>
             <JoinGameButton gameId={game.id}/>
            </td>
          {/if}
        </tr>
      {/each}
    {/if}
  </tbody>
</table>

<style>
  table {
    margin: auto;
    margin-top: 2rem;
    margin-bottom: 2rem;
    border-spacing: 0.5rem;
    max-height: 50px;
    overflow: auto;
  }
</style>
