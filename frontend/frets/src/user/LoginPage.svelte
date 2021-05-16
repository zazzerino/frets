<script lang="ts">
  import { sendLogin } from '../socket/message';

  let username: string;

  function onClick() {
    if (username) {
      sendLogin(username);
    }
  }
</script>

<h1 class="text-lg font-bold mb-4">Login</h1>

<div class="flex flex-col ">
  <input class="m-auto mb-4 text-center shadow rounded border-2 border-indigo-200" 
    id="name-input" 
    placeholder="Enter your name..." 
    bind:value={username}
    on:keypress={key => key.code === 'Enter' && onClick()}
  />

  <button
    class="bg-blue-500 hover:bg-blue-400 rounded px-4 py-2 m-auto text-white"
    on:click={onClick}
  >
    Login
  </button>
</div>
