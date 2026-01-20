<script lang="ts">
import { useAlerts } from "$lib/alerts";
import AlertTitle from "./ui/alert/alert-title.svelte";
import Alert from "./ui/alert/alert.svelte";
import InfoIcon from "@lucide/svelte/icons/info";
import AlertCircleIcon from "@lucide/svelte/icons/alert-circle";

let { alerts } = useAlerts();
</script>

<div class="flex flex-col gap-2 p-2">
  {#each $alerts as alert}
    <Alert variant={alert.level == 'error' ? 'destructive' : 'default'}>
      {#if alert.level == 'info'}
        <InfoIcon />
      {:else if alert.level == 'error'}
        <AlertCircleIcon />
      {/if}
      <AlertTitle>{alert.title}</AlertTitle>
    </Alert>
  {/each}
</div>
