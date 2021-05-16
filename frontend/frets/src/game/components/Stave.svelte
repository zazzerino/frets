<script lang="ts">
  import { onMount } from 'svelte';
  import { makeVexObjects, drawNote } from './vexflow-utils';

  let container: HTMLElement;

  const width = 200;
  const height = 130;

  onMount(() => {
    const vexObjs = makeVexObjects(container, width, height);
    const { context, stave } = vexObjs;

    stave.draw();
  });
</script>

<div bind:this={container}/>
