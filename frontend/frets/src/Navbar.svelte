<script lang="ts">
  import { user } from './stores';
</script>

<nav class="mb-2">
  <ul class="flex bg-blue-100 rounded">
    <li class="ml-5 hover:text-blue-500">
      <a href="/">Home</a>
    </li>
    <li class="ml-5 hover:text-blue-500">
      <a href="/login">Login</a>
    </li>
    <li class="ml-5 hover:text-blue-500">
      <a href="/game">Game</a>
    </li>
    <li class="flex-grow text-right mx-5">
      <p>logged in as: 
        <span class="text-green-600">{$user.name}</span>
        <span>({ $user.id })</span>
      </p>
    </li>
  </ul>
</nav>
