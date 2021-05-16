<script lang="ts">
  import { FretboardDiagram } from 'fretboard-diagram'
  import { onMount } from 'svelte';

  let container: HTMLElement;
  const id = 'fretboard-container'

  onMount(() => {
    const div = document.createElement('div');
    div.id = id;
    container.appendChild(div);

    new FretboardDiagram({
      id,
      drawDotOnHover: true,
    });
  });
</script>

<div bind:this={container}/>
