<script lang="ts">
  import { games } from './stores';
</script>

<table>
  <thead>
    <tr>
      <th>Id</th>
      <!-- <th>Players</th> -->
      <th>State</th>
    </tr>
  </thead>
  <tbody>
    {#if $games}
      {#each $games as game}
        <tr>
          <td>{game.id}</td>
          <!-- <td>{game.playerCount}</td> -->
          <td>{game.state}</td>
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
