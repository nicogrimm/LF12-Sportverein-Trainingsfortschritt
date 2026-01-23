<script lang="ts">
import { alerts } from "$lib/alerts";
import AlertTitle from "./ui/alert/alert-title.svelte";
import AlertDescription from "./ui/alert/alert-description.svelte";
import Alert from "./ui/alert/alert.svelte";
import InfoIcon from "@lucide/svelte/icons/info";
import AlertCircleIcon from "@lucide/svelte/icons/alert-circle";
import ChevronDownIcon from "@lucide/svelte/icons/chevron-down";
import ChevronRightIcon from "@lucide/svelte/icons/chevron-right";
import { Button } from "./ui/button";
import { SvelteSet } from "svelte/reactivity";

let expandedAlerts = $state<SvelteSet<number>>(new SvelteSet());

function toggleExpand(index: number) {
  console.log("toggle " + index);
  console.log(expandedAlerts);
  if (expandedAlerts.has(index)) {
    expandedAlerts.delete(index);
  } else {
    expandedAlerts.add(index);
  }
  expandedAlerts = expandedAlerts;
}
</script>

<div class={`flex flex-col gap-2 ${$alerts.length != 0 ? "p-2" : ""}`}>
  {#each $alerts as alert, index}
    <Alert variant={alert.level == 'error' ? 'destructive' : 'default'}>
      {#if alert.level == 'info'}
        <InfoIcon />
      {:else if alert.level == 'error'}
        <AlertCircleIcon />
      {/if}
      <div class="flex w-full flex-col">
        <div class="flex items-center">
          <AlertTitle>{alert.title}</AlertTitle>
          {#if alert.description}
            <Button
              variant="ghost"
              onclick={() => toggleExpand(index)}
              class="ml-1 h-fit has-[>svg]:p-1"
              aria-label={expandedAlerts.has(index) ? "Details ausblenden" : "Details anzeigen"}
            >
              {#if expandedAlerts.has(index)}
                <ChevronDownIcon class="size-4" />
              {:else}
                <ChevronRightIcon class="size-4" />
              {/if}
            </Button>
          {/if}
        </div>
        {#if alert.description && expandedAlerts.has(index)}
          <AlertDescription>{alert.description}</AlertDescription>
        {/if}
      </div>
    </Alert>
  {/each}
</div>
