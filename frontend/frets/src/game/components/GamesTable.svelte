<script lang="ts">
  import { playerCount } from '../game';
  import { games } from '../../stores';
  import JoinGameButton from './JoinGameButton.svelte';
</script>

<table>
  <thead>
    <tr>
      <th>Id</th>
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
          <td>{game.state}</td>
          <td>{playerCount(game)}</td>
          <td>
            <JoinGameButton gameId={game.id}/>
          </td>
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
  }
</style>
