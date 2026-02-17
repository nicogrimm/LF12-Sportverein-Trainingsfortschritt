<script lang="ts">
import EllipsisIcon from "@lucide/svelte/icons/ellipsis";
import { Button } from "$lib/components/ui/button/index.js";
import * as DropdownMenu from "$lib/components/ui/dropdown-menu/index.js";
import type { Training } from "$lib/service/trainingService";
import { switchLocation } from "$lib/location";

let { training, variant }: { training: Training; variant: "sport" | "athlet" } =
  $props();

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
    <DropdownMenu.Item
      onclick={() => switchLocation({ page: "training-details", athleteId: training.athleteId, trainingId: training.id })}
    >
      Zur Detailansicht gehen
    </DropdownMenu.Item>
    {#if variant === "sport"}
      <DropdownMenu.Item
        onclick={() => switchLocation({ page: "athlete-details", athleteId: training.athleteId })}
      >
        Zum Athleten gehen
      </DropdownMenu.Item>
    {/if}
    {#if variant === "athlet"}
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
    {#if variant === "sport"}
      <DropdownMenu.Item
        onclick={() => navigator.clipboard.writeText(training.athleteId.toString())}
      >
        Athleten-Id kopieren
      </DropdownMenu.Item>
    {/if}
    {#if variant === "athlet"}
      <DropdownMenu.Item
        onclick={() => navigator.clipboard.writeText(training.sportId.toString())}
      >
        Sportart-Id kopieren
      </DropdownMenu.Item>
    {/if}
  </DropdownMenu.Content>
</DropdownMenu.Root>
