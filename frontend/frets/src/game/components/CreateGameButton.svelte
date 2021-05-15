<script lang="ts">
  import { sendCreateGame } from '../../socket/message';
</script>

<button 
  on:click={sendCreateGame} 
  class="bg-blue-500 hover:bg-blue-400 rounded px-4 py-2 mt-8 text-white"
>
  Create Game
</button>
