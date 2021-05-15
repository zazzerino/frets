<script lang="ts">
  import { sendCreateGame } from '../../socket/message'
</script>

<button on:click={sendCreateGame}>
  Create Game
</button>
