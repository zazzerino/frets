<script lang="ts">
  import { sendLogin } from './socket/message';

  let username: string;

  function onClick() {
    if (username) {
      sendLogin(username);
    }
  }
</script>

<h1 class="text-lg font-bold mb-4">Login</h1>

<div class="flex flex-col ">
  <!-- <label class="mb-4" for="name-input">Username</label> -->
  <input class="mb-4 m-auto text-center shadow" 
    id="name-input" 
    placeholder="Enter your name..." 
    bind:value={username}
  />
  
  <button
    class="bg-blue-600 rounded px-4 py-2 m-auto text-white"
    on:click={onClick}
  >
    Login
  </button>
</div>
