<script lang="ts">
import EllipsisIcon from "@lucide/svelte/icons/ellipsis";
import { Button } from "$lib/components/ui/button/index.js";
import * as DropdownMenu from "$lib/components/ui/dropdown-menu/index.js";
// import { switchLocation } from "$lib/location";
import type { Training } from "$lib/service/trainingService";
import { location, switchLocation } from "$lib/location";

let { training }: { training: Training } = $props();

// TODO: add button for detail view where you can edit the entry
</script>

<div></div>

<DropdownMenu.Root>
  <DropdownMenu.Trigger>
    {#snippet child({ props })}
      <Button
        {...props}
        variant="outline"
        size="icon"
        class="relative size-8 p-0"
      >
        <span class="sr-only">Menu öffnen</span>
        <EllipsisIcon />
      </Button>
    {/snippet}
  </DropdownMenu.Trigger>
  <DropdownMenu.Content>
    <DropdownMenu.Group>
      <DropdownMenu.Label>Aktionen</DropdownMenu.Label>
    </DropdownMenu.Group>
    {#if $location.page != "athlete-details"}
      <DropdownMenu.Item
        onclick={() => switchLocation({ page: "athlete-details", athleteId: training.athleteId })}
      >
        Zum Athleten gehen
      </DropdownMenu.Item>
    {/if}
    {#if $location.page != "sport-details"}
      <DropdownMenu.Item
        onclick={() => switchLocation({ page: "sport-details", sportId: training.sportId })}
      >
        Zur Sportart gehen
      </DropdownMenu.Item>
    {/if}
    <DropdownMenu.Separator />
    <DropdownMenu.Item
      onclick={() => navigator.clipboard.writeText(training.id.toString())}
    >
      Id kopieren
    </DropdownMenu.Item>
  </DropdownMenu.Content>
</DropdownMenu.Root>
