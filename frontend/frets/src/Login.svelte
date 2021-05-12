<script lang="ts">
  import { sendLogin } from './socket/message';

  let username: string;

  function onClick() {
    if (username) {
      sendLogin(username);
    }
  }
</script>

<h2>Login</h2>

<label for="name-input">Username</label>
<input id="name-input" bind:value={username} />

<button on:click={onClick}>
  Login
</button>
