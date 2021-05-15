<script lang="ts">
  import { sendCreateGame } from '../../socket/message';
</script>

<button 
  on:click={sendCreateGame} 
  class="bg-blue-600 rounded px-4 py-2 mt-4 text-white"
>
  Create Game
</button>
